package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.R
import com.example.config.AppConfig
import com.example.config.AppModule
import com.example.ui.FemCareViewModel
import com.example.ui.components.PremiumCard
import com.example.ui.components.RealGuideCard
import com.example.ui.components.SoftCareButton
import com.example.ui.components.SoftModuleHeader
import com.example.ui.theme.FemCareColors
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PoppinsFontFamily

@Composable
fun DailyFitnessScreen(
    viewModel: FemCareViewModel
) {
    BackHandler { viewModel.navigateToModule(AppModule.HOME) }

    val seconds by viewModel.timerSeconds.collectAsStateWithLifecycle()
    val running by viewModel.isTimerRunning.collectAsStateWithLifecycle()
    val fitnessSession by viewModel.todayFitness.collectAsStateWithLifecycle()
    val completedCount = fitnessSession?.completedCount ?: 0

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val lavenderAccent = Color(0xFFE8D7FF)
    val successGreen = Color(0xFF4CAF50)

    val exercises = listOf(
        Pair(R.string.ex_1_title, R.string.ex_1_desc),
        Pair(R.string.ex_2_title, R.string.ex_2_desc),
        Pair(R.string.ex_3_title, R.string.ex_3_desc),
        Pair(R.string.ex_4_title, R.string.ex_4_desc)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        SoftModuleHeader(
            illustrationResId = R.drawable.real_fitness,
            title = stringResource(R.string.mod_fitness_title),
            subtitle = stringResource(R.string.mod_fitness_subtitle),
            onBack = { viewModel.navigateToModule(AppModule.HOME) }
        )

        RealGuideCard(
            imageRes = R.drawable.real_fitness,
            title = stringResource(R.string.guide_fitness_title),
            steps = listOf(
                stringResource(R.string.guide_fitness_step1),
                stringResource(R.string.guide_fitness_step2)
            ),
            testTagName = "fitness_real_guide_card"
        )

        PremiumCard(
            modifier = Modifier.testTag("fitness_timer_card")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.fitness_timer_ready,
                            AppConfig.formatDurationSeconds(seconds)
                        ),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = deepPlum
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(lavenderAccent)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.fitness_completed_count, completedCount),
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = deepPlum
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SoftCareButton(
                        text = if (running) stringResource(R.string.fitness_btn_pause) else stringResource(R.string.fitness_btn_start),
                        onClick = { viewModel.toggleFitnessTimer() },
                        icon = if (running) Icons.Default.Pause else Icons.Default.PlayArrow,
                        containerColor = deepPlum,
                        modifier = Modifier.weight(1f),
                        testTagName = "btn_fitness_start_pause"
                    )
                    SoftCareButton(
                        text = stringResource(R.string.fitness_btn_reset),
                        onClick = { viewModel.resetFitnessTimer() },
                        icon = Icons.Default.Refresh,
                        containerColor = softPink,
                        contentColor = deepPlum,
                        modifier = Modifier.weight(1f),
                        testTagName = "btn_fitness_reset"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SoftCareButton(
                    text = stringResource(R.string.fitness_btn_complete),
                    onClick = { viewModel.markFitnessCompleted() },
                    icon = Icons.Default.CheckCircle,
                    containerColor = successGreen,
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "btn_fitness_complete"
                )
            }
        }

        exercises.forEachIndexed { idx, (titleRes, descRes) ->
            PremiumCard(
                modifier = Modifier.testTag("fitness_exercise_card_$idx")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(softPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = stringResource(titleRes),
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(titleRes),
                            style = MaterialTheme.typography.headlineLarge,
                            color = deepPlum
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(descRes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = FemCareColors.textBodyDark
                    )
                }
            }
        }
    }
}

@Composable
fun AdultWellnessScreen(
    viewModel: FemCareViewModel,
    localizedContext: android.content.Context
) {
    BackHandler { viewModel.navigateToModule(AppModule.HOME) }

    val isUnlocked by viewModel.isAdultUnlocked.collectAsStateWithLifecycle()
    val pinInput by viewModel.adultPinInput.collectAsStateWithLifecycle()
    val pinError by viewModel.adultPinError.collectAsStateWithLifecycle()

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val bodyDark = Color(0xFF333333)

    Column(modifier = Modifier.fillMaxWidth()) {
        SoftModuleHeader(
            illustrationResId = R.drawable.real_selfcheck,
            title = stringResource(R.string.mod_adult_title),
            subtitle = stringResource(R.string.mod_adult_subtitle),
            onBack = { viewModel.navigateToModule(AppModule.HOME) }
        )

        RealGuideCard(
            imageRes = R.drawable.real_selfcheck,
            title = stringResource(R.string.guide_adult_title),
            steps = listOf(
                stringResource(R.string.guide_adult_step1),
                stringResource(R.string.guide_adult_step2)
            ),
            testTagName = "wellness_real_guide_card"
        )

        if (!isUnlocked) {
            PremiumCard(
                modifier = Modifier.testTag("adult_lock_gate_card")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(softPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(R.string.adult_lock_heading),
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(R.string.adult_lock_heading),
                            style = MaterialTheme.typography.headlineLarge,
                            color = deepPlum
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.adult_lock_desc),
                        style = MaterialTheme.typography.bodyLarge,
                        color = bodyDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { viewModel.onAdultPinChanged(it) },
                        label = { Text(stringResource(R.string.adult_pin_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = deepPlum,
                            unfocusedBorderColor = softPink,
                            focusedTextColor = bodyDark,
                            unfocusedTextColor = bodyDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_adult_pin")
                    )

                    if (pinError) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.adult_pin_error),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFD94362)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SoftCareButton(
                        text = stringResource(R.string.adult_btn_unlock),
                        onClick = { viewModel.unlockAdultSection(localizedContext) },
                        icon = Icons.Default.LockOpen,
                        containerColor = deepPlum,
                        modifier = Modifier.fillMaxWidth(),
                        testTagName = "btn_unlock_adult"
                    )
                }
            }
        } else {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SoftCareButton(
                    text = stringResource(R.string.adult_btn_lock),
                    onClick = { viewModel.lockAdultSection() },
                    icon = Icons.Default.Lock,
                    containerColor = deepPlum,
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "btn_lock_adult"
                )
            }

            PremiumCard(
                modifier = Modifier.testTag("btn_open_full_18plus_safety_guide"),
                containerColor = softPink,
                onClick = { viewModel.navigateToModule(AppModule.ADULT_WELLNESS_EDUCATION) }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "নিরাপদ ব্যক্তিগত স্বাস্থ্য অভ্যাস - সম্পূর্ণ গাইড (18+)",
                        style = MaterialTheme.typography.headlineLarge,
                        color = deepPlum
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "কী ব্যবহার করা নিরাপদ, কী ক্ষতিকর, মাসিকের সময় যত্ন ও ডাক্তারের পরামর্শ — বিস্তারিত পড়তে এখানে ট্যাপ করুন।",
                        style = MaterialTheme.typography.bodyLarge,
                        color = bodyDark
                    )
                }
            }

            val topics = listOf(
                Pair(R.string.adult_topic1_title, R.string.adult_topic1_body),
                Pair(R.string.adult_topic2_title, R.string.adult_topic2_body),
                Pair(R.string.adult_topic3_title, R.string.adult_topic3_body),
                Pair(R.string.adult_topic4_title, R.string.adult_topic4_body)
            )

            topics.forEachIndexed { index, (titleRes, bodyRes) ->
                PremiumCard(
                    modifier = Modifier.testTag("adult_edu_card_$index")
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(titleRes),
                            style = MaterialTheme.typography.headlineLarge,
                            color = deepPlum
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(bodyRes),
                            style = MaterialTheme.typography.bodyLarge,
                            color = bodyDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DietWaterScreen(
    viewModel: FemCareViewModel,
    localizedContext: android.content.Context
) {
    BackHandler { viewModel.navigateToModule(AppModule.HOME) }

    val waterState by viewModel.todayWater.collectAsStateWithLifecycle()
    val currentMl = waterState?.intakeMl ?: 750
    val goalMl = waterState?.goalMl ?: BuildConfig.DEFAULT_WATER_GOAL_ML
    val autoEnabled = waterState?.autoReminderEnabled ?: true
    val progress = (currentMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f)

    val notifPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        viewModel.triggerWaterNotification(localizedContext, currentMl)
    }

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val successGreen = Color(0xFF4CAF50)
    val bodyDark = Color(0xFF333333)

    Column(modifier = Modifier.fillMaxWidth()) {
        SoftModuleHeader(
            illustrationResId = R.drawable.real_water,
            title = stringResource(R.string.mod_diet_title),
            subtitle = stringResource(R.string.mod_diet_subtitle),
            onBack = { viewModel.navigateToModule(AppModule.HOME) }
        )

        RealGuideCard(
            imageRes = R.drawable.real_water,
            title = stringResource(R.string.guide_water_title),
            steps = listOf(
                stringResource(R.string.guide_water_step1),
                stringResource(R.string.guide_water_step2)
            ),
            testTagName = "water_real_guide_card"
        )

        PremiumCard(
            modifier = Modifier.testTag("water_tracker_card")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(softPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = stringResource(R.string.mod_diet_title),
                            tint = deepPlum,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.water_progress_label, currentMl, goalMl),
                        style = MaterialTheme.typography.headlineLarge,
                        color = deepPlum
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.progress_ring_stroke))
                        .clip(RoundedCornerShape(50)),
                    color = successGreen,
                    trackColor = softPink
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SoftCareButton(
                        text = stringResource(R.string.water_btn_add_glass),
                        onClick = { viewModel.addWaterGlass(localizedContext) },
                        icon = Icons.Default.WaterDrop,
                        containerColor = deepPlum,
                        modifier = Modifier.weight(1.4f),
                        testTagName = "btn_add_water_glass"
                    )
                    SoftCareButton(
                        text = stringResource(R.string.water_btn_reset),
                        onClick = { viewModel.resetTodayWater() },
                        icon = Icons.Default.Refresh,
                        containerColor = softPink,
                        contentColor = deepPlum,
                        modifier = Modifier.weight(1f),
                        testTagName = "btn_reset_water"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.water_auto_reminder_toggle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = bodyDark,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = autoEnabled,
                        onCheckedChange = { viewModel.toggleWaterAutoReminder(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = deepPlum
                        ),
                        modifier = Modifier.testTag("switch_water_auto_reminder")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SoftCareButton(
                    text = stringResource(R.string.water_btn_test_notif),
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.triggerWaterNotification(localizedContext, currentMl)
                        }
                    },
                    icon = Icons.Default.NotificationsActive,
                    containerColor = deepPlum,
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "btn_test_water_notif"
                )
            }
        }

        Text(
            text = stringResource(R.string.diet_plan_heading),
            style = MaterialTheme.typography.headlineLarge,
            color = deepPlum,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        val dietPhases = listOf(
            Pair(R.string.diet_menstrual_title, R.string.diet_menstrual_foods),
            Pair(R.string.diet_follicular_title, R.string.diet_follicular_foods),
            Pair(R.string.diet_ovulatory_title, R.string.diet_ovulatory_foods),
            Pair(R.string.diet_luteal_title, R.string.diet_luteal_foods)
        )

        dietPhases.forEachIndexed { idx, (titleRes, foodRes) ->
            PremiumCard(
                modifier = Modifier.testTag("diet_phase_card_$idx")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(softPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = stringResource(titleRes),
                                tint = deepPlum,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(titleRes),
                            style = MaterialTheme.typography.headlineLarge,
                            color = deepPlum
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(foodRes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = bodyDark
                    )
                }
            }
        }
    }
}

@Composable
fun DoctorFinderScreen(
    viewModel: FemCareViewModel
) {
    BackHandler { viewModel.navigateToModule(AppModule.HOME) }

    val filter by viewModel.doctorRegionFilter.collectAsStateWithLifecycle()
    val savedList by viewModel.savedDoctors.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val lavenderAccent = Color(0xFFE8D7FF)
    val successGreen = Color(0xFF4CAF50)
    val captionGray = Color(0xFF888888)

    val allDoctors = AppConfig.getSpecialistDoctors()
    val filteredDoctors = if (filter == "ALL") {
        allDoctors
    } else {
        allDoctors.filter { it.regionCode == filter }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SoftModuleHeader(
            illustrationResId = R.drawable.real_doctor,
            title = stringResource(R.string.mod_doctor_title),
            subtitle = stringResource(R.string.mod_doctor_subtitle),
            onBack = { viewModel.navigateToModule(AppModule.HOME) }
        )

        RealGuideCard(
            imageRes = R.drawable.real_doctor,
            title = stringResource(R.string.guide_doctor_title),
            steps = listOf(
                stringResource(R.string.guide_doctor_step1),
                stringResource(R.string.guide_doctor_step2)
            ),
            testTagName = "doctor_real_guide_card"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Pair("ALL", R.string.doc_filter_all),
                Pair(BuildConfig.COUNTRY_CODE_SA, R.string.doc_filter_sa),
                Pair(BuildConfig.COUNTRY_CODE_BD, R.string.doc_filter_bd)
            ).forEach { (code, labelRes) ->
                val active = filter == code
                SoftCareButton(
                    text = stringResource(labelRes),
                    onClick = { viewModel.setDoctorFilter(code) },
                    modifier = Modifier.weight(1f),
                    containerColor = if (active) deepPlum else Color.White,
                    contentColor = if (active) Color.White else deepPlum,
                    testTagName = "btn_doc_filter_${code.lowercase()}"
                )
            }
        }

        filteredDoctors.forEach { doc ->
            val isSaved = savedList.any { it.doctorId == doc.id }

            PremiumCard(
                modifier = Modifier.testTag("doctor_card_${doc.id}")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(doc.nameRes),
                                fontFamily = OutfitFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = deepPlum
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stringResource(doc.specialtyRes),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color(0xFF333333)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(lavenderAccent)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = doc.regionCode,
                                fontFamily = OutfitFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = deepPlum
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(doc.locationRes),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = captionGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SoftCareButton(
                            text = stringResource(R.string.doc_btn_call),
                            onClick = { AppConfig.openExternalUri(context, doc.phoneUri) },
                            icon = Icons.Default.Call,
                            containerColor = successGreen,
                            modifier = Modifier.weight(1f),
                            testTagName = "btn_call_doc_${doc.id}"
                        )
                        SoftCareButton(
                            text = stringResource(R.string.doc_btn_map),
                            onClick = { AppConfig.openExternalUri(context, doc.geoUri) },
                            icon = Icons.Default.Map,
                            containerColor = deepPlum,
                            modifier = Modifier.weight(1f),
                            testTagName = "btn_map_doc_${doc.id}"
                        )
                        SoftCareButton(
                            text = "",
                            onClick = { viewModel.toggleDoctorBookmark(doc.id) },
                            icon = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            containerColor = if (isSaved) deepPlum else softPink,
                            contentColor = if (isSaved) Color.White else deepPlum,
                            testTagName = "btn_save_doc_${doc.id}"
                        )
                    }
                }
            }
        }
    }
}
