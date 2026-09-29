package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.R
import com.example.config.AppConfig
import com.example.config.AppModule
import com.example.config.BraFitCalculation
import com.example.config.CyclePrediction
import com.example.config.StoreRegion
import com.example.config.SupportedLanguage
import com.example.data.local.BraFitEntity
import com.example.data.local.FemCareRepository
import com.example.data.local.FitnessSessionEntity
import com.example.data.local.PeriodLogEntity
import com.example.data.local.SavedDoctorEntity
import com.example.data.local.WaterIntakeEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FemCareViewModel(
    private val repository: FemCareRepository
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(SupportedLanguage.ENGLISH)
    val currentLanguage: StateFlow<SupportedLanguage> = _currentLanguage.asStateFlow()

    private val _currentModule = MutableStateFlow(AppModule.HOME)
    val currentModule: StateFlow<AppModule> = _currentModule.asStateFlow()

    // Module 1: Bra Calculator & Location Store Finder
    private val _underbustInput = MutableStateFlow("78")
    val underbustInput: StateFlow<String> = _underbustInput.asStateFlow()

    private val _bustInput = MutableStateFlow("88")
    val bustInput: StateFlow<String> = _bustInput.asStateFlow()

    private val _isCmUnit = MutableStateFlow(true)
    val isCmUnit: StateFlow<Boolean> = _isCmUnit.asStateFlow()

    private val _braCalculationResult = MutableStateFlow<BraFitCalculation?>(
        AppConfig.calculateBraSize(78f, 88f, true)
    )
    val braCalculationResult: StateFlow<BraFitCalculation?> = _braCalculationResult.asStateFlow()

    private val _braInputError = MutableStateFlow(false)
    val braInputError: StateFlow<Boolean> = _braInputError.asStateFlow()

    private val _selectedStoreRegion = MutableStateFlow(StoreRegion.SAUDI_ARABIA)
    val selectedStoreRegion: StateFlow<StoreRegion> = _selectedStoreRegion.asStateFlow()

    val braHistory: StateFlow<List<BraFitEntity>> = repository.braFitHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Module 2: Period Tracker
    val periodLogs: StateFlow<List<PeriodLogEntity>> = repository.periodLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cycleLengthDays = MutableStateFlow(BuildConfig.DEFAULT_CYCLE_LENGTH)
    val cycleLengthDays: StateFlow<Int> = _cycleLengthDays.asStateFlow()

    private val _periodDurationDays = MutableStateFlow(BuildConfig.DEFAULT_PERIOD_DURATION)
    val periodDurationDays: StateFlow<Int> = _periodDurationDays.asStateFlow()

    // Module 4: Daily Fitness Timer
    private val _timerSeconds = MutableStateFlow(180)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    private val todayKey = AppConfig.getTodayKey()

    val todayFitness: StateFlow<FitnessSessionEntity?> = repository.getFitnessSessions(todayKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Module 5: Adult Wellness 18+ Lock
    private val _isAdultUnlocked = MutableStateFlow(false)
    val isAdultUnlocked: StateFlow<Boolean> = _isAdultUnlocked.asStateFlow()

    private val _adultPinInput = MutableStateFlow("1818")
    val adultPinInput: StateFlow<String> = _adultPinInput.asStateFlow()

    private val _adultPinError = MutableStateFlow(false)
    val adultPinError: StateFlow<Boolean> = _adultPinError.asStateFlow()

    // Module 6: Auto Diet & Water Reminder
    val todayWater: StateFlow<WaterIntakeEntity?> = repository.getWaterIntake(todayKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Module 7: Doctor Finder
    private val _doctorRegionFilter = MutableStateFlow("ALL")
    val doctorRegionFilter: StateFlow<String> = _doctorRegionFilter.asStateFlow()

    val savedDoctors: StateFlow<List<SavedDoctorEntity>> = repository.savedDoctors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectLanguage(language: SupportedLanguage) {
        _currentLanguage.value = language
    }

    fun navigateToModule(module: AppModule) {
        _currentModule.value = module
    }

    fun onUnderbustChanged(value: String) {
        _underbustInput.value = value
        _braInputError.value = false
    }

    fun onBustChanged(value: String) {
        _bustInput.value = value
        _braInputError.value = false
    }

    fun setMeasurementUnit(isCm: Boolean) {
        if (_isCmUnit.value != isCm) {
            _isCmUnit.value = isCm
            if (isCm) {
                _underbustInput.value = "78"
                _bustInput.value = "88"
            } else {
                _underbustInput.value = "30"
                _bustInput.value = "36"
            }
        }
    }

    fun calculateAndSaveBraFit() {
        val under = _underbustInput.value.toFloatOrNull()
        val full = _bustInput.value.toFloatOrNull()
        if (under == null || full == null) {
            _braInputError.value = true
            return
        }
        val calc = AppConfig.calculateBraSize(under, full, _isCmUnit.value)
        if (calc == null) {
            _braInputError.value = true
            return
        }
        _braInputError.value = false
        _braCalculationResult.value = calc
        viewModelScope.launch {
            repository.saveBraFit(
                BraFitEntity(
                    underbust = under,
                    bust = full,
                    isCm = _isCmUnit.value,
                    calculatedSize = calc.fullSize,
                    sisterSizes = calc.sisterSizes
                )
            )
        }
    }

    fun selectStoreRegion(region: StoreRegion) {
        _selectedStoreRegion.value = region
    }

    fun detectLiveGpsRegion(context: Context) {
        viewModelScope.launch {
            val detected = AppConfig.detectLiveStoreRegion(context)
            _selectedStoreRegion.value = detected
        }
    }

    fun adjustCycleLength(delta: Int) {
        _cycleLengthDays.value = (_cycleLengthDays.value + delta).coerceIn(21, 40)
    }

    fun adjustPeriodDuration(delta: Int) {
        _periodDurationDays.value = (_periodDurationDays.value + delta).coerceIn(2, 10)
    }

    fun logPeriodStart(daysAgo: Int, symptom: String = "") {
        val timestamp = System.currentTimeMillis() - (daysAgo * 86_400_000L)
        viewModelScope.launch {
            repository.logPeriod(
                PeriodLogEntity(
                    startDateMillis = timestamp,
                    cycleLengthDays = _cycleLengthDays.value,
                    periodDurationDays = _periodDurationDays.value,
                    symptomTag = symptom
                )
            )
        }
    }

    fun getCurrentCyclePrediction(): CyclePrediction {
        val latestStart = periodLogs.value.firstOrNull()?.startDateMillis
            ?: (System.currentTimeMillis() - 14 * 86_400_000L)
        return AppConfig.calculateCyclePrediction(
            lastPeriodStartMillis = latestStart,
            cycleLengthDays = _cycleLengthDays.value,
            periodDurationDays = _periodDurationDays.value
        )
    }

    fun triggerCycleNotification(context: Context) {
        val prediction = getCurrentCyclePrediction()
        val title = context.getString(R.string.period_notif_title)
        val body = context.getString(
            R.string.period_notif_body,
            prediction.daysUntilNextPeriod,
            prediction.nextPeriodFormatted
        )
        AppConfig.sendWellnessNotification(
            context = context,
            notificationId = context.resources.getInteger(R.integer.Notif_id_period),
            title = title,
            body = body
        )
    }

    fun toggleFitnessTimer() {
        if (_isTimerRunning.value) {
            _isTimerRunning.value = false
            timerJob?.cancel()
        } else {
            _isTimerRunning.value = true
            timerJob = viewModelScope.launch {
                while (isActive && _isTimerRunning.value && _timerSeconds.value > 0) {
                    delay(1000L)
                    _timerSeconds.value = (_timerSeconds.value - 1).coerceAtLeast(0)
                    if (_timerSeconds.value == 0) {
                        _isTimerRunning.value = false
                        markFitnessCompleted()
                    }
                }
            }
        }
    }

    fun resetFitnessTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
        _timerSeconds.value = 180
    }

    fun markFitnessCompleted() {
        viewModelScope.launch {
            val current = todayFitness.value?.completedCount ?: 0
            repository.updateFitness(
                FitnessSessionEntity(
                    dateKey = todayKey,
                    completedCount = current + 1
                )
            )
        }
    }

    fun onAdultPinChanged(pin: String) {
        _adultPinInput.value = pin
        _adultPinError.value = false
    }

    fun unlockAdultSection(context: Context) {
        if (AppConfig.verifyAdultPin(_adultPinInput.value, context)) {
            _isAdultUnlocked.value = true
            _adultPinError.value = false
        } else {
            _adultPinError.value = true
        }
    }

    fun lockAdultSection() {
        _isAdultUnlocked.value = false
    }

    fun addWaterGlass(context: Context) {
        viewModelScope.launch {
            val current = todayWater.value ?: WaterIntakeEntity(
                dateKey = todayKey,
                intakeMl = 0,
                goalMl = BuildConfig.DEFAULT_WATER_GOAL_ML,
                autoReminderEnabled = true
            )
            val updatedMl = current.intakeMl + BuildConfig.WATER_GLASS_STEP_ML
            val updated = current.copy(intakeMl = updatedMl)
            repository.updateWaterIntake(updated)
            if (updated.autoReminderEnabled) {
                triggerWaterNotification(context, updatedMl)
            }
        }
    }

    fun resetTodayWater() {
        viewModelScope.launch {
            val current = todayWater.value
            repository.updateWaterIntake(
                WaterIntakeEntity(
                    dateKey = todayKey,
                    intakeMl = 0,
                    goalMl = BuildConfig.DEFAULT_WATER_GOAL_ML,
                    autoReminderEnabled = current?.autoReminderEnabled ?: true
                )
            )
        }
    }

    fun toggleWaterAutoReminder(enabled: Boolean) {
        viewModelScope.launch {
            val current = todayWater.value ?: WaterIntakeEntity(
                dateKey = todayKey,
                intakeMl = 0,
                goalMl = BuildConfig.DEFAULT_WATER_GOAL_ML,
                autoReminderEnabled = enabled
            )
            repository.updateWaterIntake(current.copy(autoReminderEnabled = enabled))
        }
    }

    fun triggerWaterNotification(context: Context, overrideMl: Int? = null) {
        val currentMl = overrideMl ?: (todayWater.value?.intakeMl ?: 0)
        val title = context.getString(R.string.water_notif_title)
        val body = context.getString(R.string.water_notif_body, currentMl)
        AppConfig.sendWellnessNotification(
            context = context,
            notificationId = context.resources.getInteger(R.integer.Notif_id_water),
            title = title,
            body = body
        )
    }

    fun setDoctorFilter(regionCode: String) {
        _doctorRegionFilter.value = regionCode
    }

    fun toggleDoctorBookmark(doctorId: Int) {
        val isSaved = savedDoctors.value.any { it.doctorId == doctorId }
        viewModelScope.launch {
            repository.toggleDoctorBookmark(doctorId, isSaved)
        }
    }

    companion object {
        fun provideFactory(repository: FemCareRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FemCareViewModel(repository) as T
                }
            }
    }
}
