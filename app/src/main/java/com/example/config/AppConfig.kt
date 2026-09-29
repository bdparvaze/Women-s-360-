package com.example.config

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.BuildConfig
import com.example.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.math.roundToInt

enum class AppModule(
    @StringRes val titleRes: Int,
    @StringRes val navLabelRes: Int,
    @StringRes val subtitleRes: Int
) {
    HOME(R.string.app_name, R.string.nav_home, R.string.app_tagline),
    BRA_CALCULATOR(R.string.mod_bra_title, R.string.nav_bra, R.string.mod_bra_subtitle),
    PERIOD_TRACKER(R.string.mod_period_title, R.string.nav_period, R.string.mod_period_subtitle),
    BREAST_CARE(R.string.mod_care_title, R.string.nav_care, R.string.mod_care_subtitle),
    DAILY_FITNESS(R.string.mod_fitness_title, R.string.nav_fitness, R.string.mod_fitness_subtitle),
    ADULT_WELLNESS(R.string.mod_adult_title, R.string.nav_adult, R.string.mod_adult_subtitle),
    DIET_WATER(R.string.mod_diet_title, R.string.nav_diet, R.string.mod_diet_subtitle),
    DOCTOR_FINDER(R.string.mod_doctor_title, R.string.nav_doctors, R.string.mod_doctor_subtitle),
    FIRST_TIME_GUIDE(R.string.mod_first_time_title, R.string.nav_first_time, R.string.mod_first_time_subtitle),
    ADULT_WELLNESS_EDUCATION(R.string.mod_adult_edu_title, R.string.nav_adult_edu, R.string.mod_adult_edu_subtitle)
}

enum class SupportedLanguage(
    @StringRes val codeRes: Int,
    @StringRes val labelRes: Int,
    val isoCode: String,
    val isRtl: Boolean
) {
    BANGLA(R.string.lang_code_bn, R.string.lang_label_bn, "bn", false),
    ENGLISH(R.string.lang_code_en, R.string.lang_label_en, "en", false),
    HINDI(R.string.lang_code_hi, R.string.lang_label_hi, "hi", false),
    URDU(R.string.lang_code_ur, R.string.lang_label_ur, "ur", true),
    ARABIC(R.string.lang_code_ar, R.string.lang_label_ar, "ar", true)
}

enum class StoreRegion(val code: String, @StringRes val labelRes: Int, @StringRes val statusRes: Int) {
    SAUDI_ARABIA(BuildConfig.COUNTRY_CODE_SA, R.string.bra_region_saudi, R.string.bra_location_status_sa),
    BANGLADESH(BuildConfig.COUNTRY_CODE_BD, R.string.bra_region_bangladesh, R.string.bra_location_status_bd),
    GLOBAL("GLOBAL", R.string.bra_region_global, R.string.bra_location_status_global)
}

data class BraFitCalculation(
    val bandSize: Int,
    val cupLetter: String,
    val fullSize: String,
    val sisterSizes: String
)

data class StoreProductLink(
    @StringRes val buttonLabelRes: Int,
    val url: String
)

data class CyclePrediction(
    val daysUntilNextPeriod: Int,
    val nextPeriodFormatted: String,
    val ovulationWindowFormatted: String,
    @StringRes val phaseNameRes: Int,
    val cycleProgressFraction: Float
)

data class DoctorInfo(
    val id: Int,
    @StringRes val nameRes: Int,
    @StringRes val specialtyRes: Int,
    @StringRes val locationRes: Int,
    val regionCode: String,
    val phoneUri: String,
    val geoUri: String
)

object AppConfig {

    private val CUP_PROGRESSION = listOf("AA", "A", "B", "C", "D", "DD/E", "F", "G", "H")
    private const val CM_PER_INCH = 2.54f
    private const val MILLIS_PER_DAY = 86_400_000L

    fun createLocalizedContext(baseContext: Context, language: SupportedLanguage): Context {
        val locale = Locale(language.isoCode)
        Locale.setDefault(locale)
        val config = Configuration(baseContext.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        val configContext = baseContext.createConfigurationContext(config)
        return object : android.content.ContextWrapper(baseContext) {
            override fun getResources(): android.content.res.Resources = configContext.resources
            override fun getAssets(): android.content.res.AssetManager = configContext.assets
        }
    }

    fun getLayoutDirection(language: SupportedLanguage): LayoutDirection {
        return if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    }

    fun calculateBraSize(underbustInput: Float, bustInput: Float, isCm: Boolean): BraFitCalculation? {
        if (underbustInput <= 0f || bustInput <= 0f || bustInput < underbustInput) return null

        val unit = if (isCm) "cm" else "inch"
        val res = com.example.utils.BraSizeCalculator.calculateBraSize(
            underbustCm = underbustInput.toDouble(),
            bustCm = bustInput.toDouble(),
            unit = unit
        )
        if (res.underbustCm < 50.0 || res.underbustCm > 145.0 || res.bustCm > 180.0) return null

        return BraFitCalculation(
            bandSize = res.bandSize,
            cupLetter = res.cupSize,
            fullSize = res.fullSize,
            sisterSizes = res.sisterSizes.joinToString(", ")
        )
    }

    fun getStoreLinksForRegion(region: StoreRegion, braSize: String): List<StoreProductLink> {
        val encodedSize = Uri.encode(braSize)
        return when (region) {
            StoreRegion.SAUDI_ARABIA -> listOf(
                StoreProductLink(
                    buttonLabelRes = R.string.bra_shop_amazon_sa,
                    url = BuildConfig.AMAZON_SA_SEARCH_URL + encodedSize
                ),
                StoreProductLink(
                    buttonLabelRes = R.string.bra_shop_noon_sa,
                    url = BuildConfig.NOON_SA_SEARCH_URL + encodedSize
                )
            )
            StoreRegion.BANGLADESH -> listOf(
                StoreProductLink(
                    buttonLabelRes = R.string.bra_shop_daraz_bd,
                    url = BuildConfig.DARAZ_BD_SEARCH_URL + encodedSize
                )
            )
            StoreRegion.GLOBAL -> listOf(
                StoreProductLink(
                    buttonLabelRes = R.string.bra_shop_global,
                    url = BuildConfig.GLOBAL_SHOP_SEARCH_URL + encodedSize
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun detectLiveStoreRegion(context: Context): StoreRegion {
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) return StoreRegion.SAUDI_ARABIA

        return try {
            val client = LocationServices.getFusedLocationProviderClient(context)
            val location = suspendCancellableCoroutine { cont ->
                client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .addOnSuccessListener { loc -> cont.resume(loc) }
                    .addOnFailureListener { cont.resume(null) }
            }
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                // Bounding check for Saudi Arabia (Mecca / KSA) & Bangladesh (Dhaka / BD)
                val isSaudiCoordinates = lat in 16.0..32.5 && lon in 34.5..56.0
                val isBangladeshCoordinates = lat in 20.5..26.7 && lon in 88.0..92.8
                if (isSaudiCoordinates) return StoreRegion.SAUDI_ARABIA
                if (isBangladeshCoordinates) return StoreRegion.BANGLADESH

                @Suppress("DEPRECATION")
                val addresses = Geocoder(context, Locale.US).getFromLocation(lat, lon, 1)
                val countryCode = addresses?.firstOrNull()?.countryCode?.uppercase(Locale.US)
                when (countryCode) {
                    BuildConfig.COUNTRY_CODE_SA -> StoreRegion.SAUDI_ARABIA
                    BuildConfig.COUNTRY_CODE_BD -> StoreRegion.BANGLADESH
                    else -> StoreRegion.SAUDI_ARABIA
                }
            } else {
                StoreRegion.SAUDI_ARABIA
            }
        } catch (_: Exception) {
            StoreRegion.SAUDI_ARABIA
        }
    }

    fun calculateCyclePrediction(
        lastPeriodStartMillis: Long,
        cycleLengthDays: Int = BuildConfig.DEFAULT_CYCLE_LENGTH,
        periodDurationDays: Int = BuildConfig.DEFAULT_PERIOD_DURATION,
        nowMillis: Long = System.currentTimeMillis()
    ): CyclePrediction {
        val safeCycle = cycleLengthDays.coerceIn(21, 40)
        val daysElapsed = (((nowMillis - lastPeriodStartMillis) / MILLIS_PER_DAY).toInt()).coerceAtLeast(0)
        val currentCycleDay = (daysElapsed % safeCycle) + 1
        val daysUntilNext = (safeCycle - (daysElapsed % safeCycle)).coerceIn(1, safeCycle)

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val nextPeriodDate = Date(nowMillis + daysUntilNext * MILLIS_PER_DAY)
        val ovulationOffsetDays = (safeCycle - 14) - currentCycleDay
        val ovulationCenterMillis = nowMillis + ovulationOffsetDays * MILLIS_PER_DAY
        val ovulationStart = Date(ovulationCenterMillis - 2 * MILLIS_PER_DAY)
        val ovulationEnd = Date(ovulationCenterMillis + 2 * MILLIS_PER_DAY)

        val phaseRes = when {
            currentCycleDay <= periodDurationDays -> R.string.phase_menstrual
            currentCycleDay in (periodDurationDays + 1)..12 -> R.string.phase_follicular
            currentCycleDay in 13..16 -> R.string.phase_ovulation
            else -> R.string.phase_luteal
        }

        val progress = (currentCycleDay.toFloat() / safeCycle.toFloat()).coerceIn(0.05f, 1f)

        return CyclePrediction(
            daysUntilNextPeriod = daysUntilNext,
            nextPeriodFormatted = dateFormat.format(nextPeriodDate),
            ovulationWindowFormatted = "${dateFormat.format(ovulationStart)} - ${dateFormat.format(ovulationEnd)}",
            phaseNameRes = phaseRes,
            cycleProgressFraction = progress
        )
    }

    fun verifyAdultPin(inputPin: String, context: Context): Boolean {
        val expected = context.getString(R.string.default_adult_pin)
        return inputPin.trim() == expected
    }

    fun formatDurationSeconds(totalSeconds: Int): String {
        val mins = totalSeconds / 60
        val secs = totalSeconds % 60
        return String.format(Locale.US, "%02d:%02d", mins, secs)
    }

    fun getTodayKey(): String {
        val cal = Calendar.getInstance()
        return String.format(
            Locale.US,
            "%04d-%02d-%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getSpecialistDoctors(): List<DoctorInfo> = listOf(
        DoctorInfo(
            id = 1,
            nameRes = R.string.doc_1_name,
            specialtyRes = R.string.doc_1_spec,
            locationRes = R.string.doc_1_loc,
            regionCode = BuildConfig.COUNTRY_CODE_SA,
            phoneUri = "tel:+966125561111",
            geoUri = "geo:21.3891,39.8579?q=Mecca+Hospital+Saudi+Arabia"
        ),
        DoctorInfo(
            id = 2,
            nameRes = R.string.doc_2_name,
            specialtyRes = R.string.doc_2_spec,
            locationRes = R.string.doc_2_loc,
            regionCode = BuildConfig.COUNTRY_CODE_SA,
            phoneUri = "tel:+966112889999",
            geoUri = "geo:21.4225,39.8262?q=Women+Specialist+Mecca+SA"
        ),
        DoctorInfo(
            id = 3,
            nameRes = R.string.doc_3_name,
            specialtyRes = R.string.doc_3_spec,
            locationRes = R.string.doc_3_loc,
            regionCode = BuildConfig.COUNTRY_CODE_BD,
            phoneUri = "tel:+88028144400",
            geoUri = "geo:23.7516,90.3812?q=Square+Hospital+Dhaka+Bangladesh"
        ),
        DoctorInfo(
            id = 4,
            nameRes = R.string.doc_4_name,
            specialtyRes = R.string.doc_4_spec,
            locationRes = R.string.doc_4_loc,
            regionCode = BuildConfig.COUNTRY_CODE_BD,
            phoneUri = "tel:+88029856666",
            geoUri = "geo:23.7925,90.4078?q=Gulshan+Women+Clinic+Dhaka+Bangladesh"
        )
    )

    @SuppressLint("MissingPermission")
    fun sendWellnessNotification(
        context: Context,
        notificationId: Int,
        title: String,
        body: String
    ) {
        val channelId = context.getString(R.string.notification_channel_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun openExternalUri(context: Context, uriString: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }
}
