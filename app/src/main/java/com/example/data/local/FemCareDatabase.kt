package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.BuildConfig
import com.example.R
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "bra_fit_history")
data class BraFitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val underbust: Float,
    val bust: Float,
    val isCm: Boolean,
    val calculatedSize: String,
    val sisterSizes: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "period_logs")
data class PeriodLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startDateMillis: Long,
    val cycleLengthDays: Int = BuildConfig.DEFAULT_CYCLE_LENGTH,
    val periodDurationDays: Int = BuildConfig.DEFAULT_PERIOD_DURATION,
    val symptomTag: String = ""
)

@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey val dateKey: String,
    val intakeMl: Int,
    val goalMl: Int = BuildConfig.DEFAULT_WATER_GOAL_ML,
    val autoReminderEnabled: Boolean = true
)

@Entity(tableName = "fitness_sessions")
data class FitnessSessionEntity(
    @PrimaryKey val dateKey: String,
    val completedCount: Int
)

@Entity(tableName = "saved_doctors")
data class SavedDoctorEntity(
    @PrimaryKey val doctorId: Int,
    val savedAt: Long = System.currentTimeMillis()
)

@Dao
interface FemCareDao {
    @Query("SELECT * FROM bra_fit_history ORDER BY timestamp DESC LIMIT 10")
    fun getBraFitHistory(): Flow<List<BraFitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBraFit(entry: BraFitEntity)

    @Query("SELECT * FROM period_logs ORDER BY startDateMillis DESC")
    fun getPeriodLogs(): Flow<List<PeriodLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriodLog(log: PeriodLogEntity)

    @Query("SELECT * FROM water_intake WHERE dateKey = :dateKey LIMIT 1")
    fun getWaterIntakeForDate(dateKey: String): Flow<WaterIntakeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWaterIntake(entity: WaterIntakeEntity)

    @Query("SELECT * FROM fitness_sessions WHERE dateKey = :dateKey LIMIT 1")
    fun getFitnessForDate(dateKey: String): Flow<FitnessSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFitness(entity: FitnessSessionEntity)

    @Query("SELECT * FROM saved_doctors")
    fun getSavedDoctors(): Flow<List<SavedDoctorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDoctor(entity: SavedDoctorEntity)

    @Query("DELETE FROM saved_doctors WHERE doctorId = :doctorId")
    suspend fun removeSavedDoctor(doctorId: Int)
}

@Database(
    entities = [
        BraFitEntity::class,
        PeriodLogEntity::class,
        WaterIntakeEntity::class,
        FitnessSessionEntity::class,
        SavedDoctorEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FemCareDatabase : RoomDatabase() {
    abstract fun femCareDao(): FemCareDao

    companion object {
        @Volatile
        private var INSTANCE: FemCareDatabase? = null

        fun getInstance(context: Context): FemCareDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FemCareDatabase::class.java,
                    context.getString(R.string.database_name)
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}

class FemCareRepository(private val dao: FemCareDao) {
    val braFitHistory: Flow<List<BraFitEntity>> = dao.getBraFitHistory()
    val periodLogs: Flow<List<PeriodLogEntity>> = dao.getPeriodLogs()
    val savedDoctors: Flow<List<SavedDoctorEntity>> = dao.getSavedDoctors()

    fun getWaterIntake(dateKey: String): Flow<WaterIntakeEntity?> = dao.getWaterIntakeForDate(dateKey)
    fun getFitnessSessions(dateKey: String): Flow<FitnessSessionEntity?> = dao.getFitnessForDate(dateKey)

    suspend fun saveBraFit(entry: BraFitEntity) = dao.insertBraFit(entry)
    suspend fun logPeriod(log: PeriodLogEntity) = dao.insertPeriodLog(log)
    suspend fun updateWaterIntake(entity: WaterIntakeEntity) = dao.upsertWaterIntake(entity)
    suspend fun updateFitness(entity: FitnessSessionEntity) = dao.upsertFitness(entity)

    suspend fun toggleDoctorBookmark(doctorId: Int, currentlySaved: Boolean) {
        if (currentlySaved) {
            dao.removeSavedDoctor(doctorId)
        } else {
            dao.saveDoctor(SavedDoctorEntity(doctorId = doctorId))
        }
    }
}
