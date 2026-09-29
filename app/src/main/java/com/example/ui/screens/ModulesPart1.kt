package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.config.AppModule
import com.example.ui.FemCareViewModel
import com.example.ui.components.PremiumCard
import com.example.ui.components.RealGuideCard
import com.example.ui.components.SoftCareButton
import com.example.ui.components.SoftModuleHeader
import com.example.ui.theme.FemCareColors
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    viewModel: FemCareViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAgeVerificationDialog by remember { mutableStateOf(false) }
    var isAgeCheckboxChecked by remember { mutableStateOf(false) }
    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val lavenderAccent = Color(0xFFE8D7FF)
    val bodyDark = Color(0xFF333333)
    val captionGray = Color(0xFF888888)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Search Bar below Top App Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_modules_hint),
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = captionGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_modules_hint),
                    tint = deepPlum
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = deepPlum,
                unfocusedBorderColor = softPink,
                focusedTextColor = bodyDark,
                unfocusedTextColor = bodyDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("home_search_bar")
        )

        // Hero Welcome Banner
        PremiumCard(
            modifier = Modifier.testTag("home_hero_card"),
            containerColor = softPink
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(lavenderAccent)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.section_all_modules),
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.hero_welcome_title),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = deepPlum
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.hero_welcome_subtitle),
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = bodyDark
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.real_underbust),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }

        // Pink Highlighted First Time Beginner Guide Button on HomeScreen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(softPink)
                .border(2.dp, deepPlum, RoundedCornerShape(20.dp))
                .clickable { viewModel.navigateToModule(AppModule.FIRST_TIME_GUIDE) }
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .testTag("btn_home_first_time_guide"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "প্রথমবার? First Time? - এখানে ক্লিক করুন",
                    tint = deepPlum,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "প্রথমবার? First Time? - এখানে ক্লিক করুন",
                    fontFamily = OutfitFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = deepPlum,
                    textAlign = TextAlign.Center
                )
            }
        }

        // "১৮+ Wellness Education" Card with Lock icon that opens Age Verification Dialog
        PremiumCard(
            modifier = Modifier.testTag("card_home_18plus_wellness_edu"),
            onClick = {
                isAgeCheckboxChecked = false
                showAgeVerificationDialog = true
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(softPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "১৮+ Wellness Education",
                            tint = deepPlum,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "১৮+ Wellness Education",
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = deepPlum
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.mod_adult_edu_subtitle),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            color = captionGray
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(lavenderAccent)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "18+",
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = deepPlum
                    )
                }
            }
        }

        if (showAgeVerificationDialog) {
            BasicAlertDialog(
                onDismissRequest = { showAgeVerificationDialog = false }
            ) {
                PremiumCard(
                    modifier = Modifier.testTag("dialog_18plus_age_verification")
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(softPink, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "১৮+ Wellness Education",
                                    tint = deepPlum,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "১৮+ Wellness Education",
                                fontFamily = OutfitFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = deepPlum
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "ডিসক্লেইমার: এটি চিকিৎসা পরামর্শ নয়, শুধুমাত্র সাধারণ স্বাস্থ্য শিক্ষা।",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = captionGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(softPink.copy(alpha = 0.45f))
                                .clickable { isAgeCheckboxChecked = !isAgeCheckboxChecked }
                                .padding(8.dp)
                                .testTag("dialog_checkbox_row_18plus"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isAgeCheckboxChecked,
                                onCheckedChange = { isAgeCheckboxChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = deepPlum,
                                    uncheckedColor = deepPlum
                                ),
                                modifier = Modifier.testTag("dialog_checkbox_18plus")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "আমি ১৮+ এবং এই স্বাস্থ্য তথ্য পড়তে চাই",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = deepPlum
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { showAgeVerificationDialog = false },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = softPink,
                                    contentColor = deepPlum
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_cancel_18plus_dialog")
                            ) {
                                Text(
                                    text = "বাতিল / Cancel",
                                    fontFamily = OutfitFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Button(
                                onClick = {
                                    if (isAgeCheckboxChecked) {
                                        showAgeVerificationDialog = false
                                        viewModel.navigateToModule(AppModule.ADULT_WELLNESS_EDUCATION)
                                    }
                                },
                                enabled = isAgeCheckboxChecked,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = deepPlum,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_confirm_18plus_dialog")
                            ) {
                                Text(
                                    text = "পড়ুন / Open",
                                    fontFamily = OutfitFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        val moduleCards = listOf(
            Triple(AppModule.BRA_CALCULATOR, R.drawable.guide_step1_underbust_clear, R.string.guide_bra_combined),
            Triple(AppModule.PERIOD_TRACKER, R.drawable.real_period, R.string.guide_period),
            Triple(AppModule.BREAST_CARE, R.drawable.real_selfcheck, R.string.guide_self_check),
            Triple(AppModule.DAILY_FITNESS, R.drawable.real_fitness, R.string.guide_fitness),
            Triple(AppModule.ADULT_WELLNESS, R.drawable.real_selfcheck, R.string.guide_wellness),
            Triple(AppModule.DIET_WATER, R.drawable.real_water, R.string.guide_water),
            Triple(AppModule.DOCTOR_FINDER, R.drawable.real_doctor, R.string.guide_doctor)
        )

        val filteredModules = moduleCards.filter { (module, _, guideRes) ->
            if (searchQuery.isBlank()) {
                true
            } else {
                val title = stringResource(module.titleRes)
                val sub = stringResource(module.subtitleRes)
                val guide = stringResource(guideRes)
                title.contains(searchQuery, ignoreCase = true) ||
                    sub.contains(searchQuery, ignoreCase = true) ||
                    guide.contains(searchQuery, ignoreCase = true) ||
                    module.name.contains(searchQuery, ignoreCase = true)
            }
        }

        // Section Heading
        Text(
            text = stringResource(R.string.section_all_modules),
            fontFamily = OutfitFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = deepPlum,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 2-Column Grid with 16.dp gap and equal height cards (140.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            filteredModules.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { (module, drawableRes, _) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(140.dp)
                                .testTag("card_module_${module.name.lowercase()}")
                                .clickable { viewModel.navigateToModule(module) },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Image(
                                        painter = painterResource(id = drawableRes),
                                        contentDescription = stringResource(module.titleRes),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(1.dp, softPink, RoundedCornerShape(14.dp))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(lavenderAccent)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = stringResource(module.navLabelRes),
                                            fontFamily = OutfitFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = deepPlum,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = stringResource(module.titleRes),
                                        fontFamily = OutfitFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp,
                                        color = deepPlum,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = stringResource(module.subtitleRes),
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Light,
                                        fontSize = 11.sp,
                                        lineHeight = 14.sp,
                                        color = captionGray,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodTrackerScreen(
    viewModel: FemCareViewModel,
    localizedContext: android.content.Context
) {
    BackHandler { viewModel.navigateToModule(AppModule.HOME) }

    val cycleLength by viewModel.cycleLengthDays.collectAsStateWithLifecycle()
    val periodDuration by viewModel.periodDurationDays.collectAsStateWithLifecycle()
    val logs by viewModel.periodLogs.collectAsStateWithLifecycle()
    val prediction = viewModel.getCurrentCyclePrediction()

    val notifPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        viewModel.triggerCycleNotification(localizedContext)
    }

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val lavenderAccent = Color(0xFFE8D7FF)
    val bodyDark = Color(0xFF333333)
    val captionGray = Color(0xFF888888)

    Column(modifier = Modifier.fillMaxWidth()) {
        SoftModuleHeader(
            illustrationResId = R.drawable.real_period,
            title = stringResource(R.string.mod_period_title),
            subtitle = stringResource(R.string.mod_period_subtitle),
            onBack = { viewModel.navigateToModule(AppModule.HOME) }
        )

        RealGuideCard(
            imageRes = R.drawable.real_period,
            title = stringResource(R.string.guide_period_title),
            steps = listOf(
                stringResource(R.string.guide_period_step1),
                stringResource(R.string.guide_period_step2)
            ),
            testTagName = "period_real_guide_card"
        )

        PremiumCard(
            modifier = Modifier.testTag("period_prediction_card"),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { prediction.cycleProgressFraction },
                        modifier = Modifier.size(dimensionResource(R.dimen.progress_ring_size)),
                        color = deepPlum,
                        trackColor = softPink,
                        strokeWidth = dimensionResource(R.dimen.progress_ring_stroke)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = prediction.daysUntilNextPeriod.toString(),
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = deepPlum
                        )
                        Text(
                            text = stringResource(R.string.period_days_until),
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = captionGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(lavenderAccent)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.period_phase_label,
                            stringResource(prediction.phaseNameRes)
                        ),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = deepPlum
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.period_next_date_label, prediction.nextPeriodFormatted),
                    fontFamily = OutfitFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = deepPlum
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.period_ovulation_label, prediction.ovulationWindowFormatted),
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = bodyDark
                )
            }
        }

        PremiumCard(
            modifier = Modifier.testTag("period_controls_card")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.period_cycle_length_label, cycleLength),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = deepPlum
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SoftCareButton(
                            text = "",
                            icon = Icons.Default.Remove,
                            onClick = { viewModel.adjustCycleLength(-1) },
                            containerColor = softPink,
                            contentColor = deepPlum,
                            testTagName = "btn_cycle_minus"
                        )
                        SoftCareButton(
                            text = "",
                            icon = Icons.Default.Add,
                            onClick = { viewModel.adjustCycleLength(1) },
                            containerColor = deepPlum,
                            contentColor = Color.White,
                            testTagName = "btn_cycle_plus"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.period_duration_label, periodDuration),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = deepPlum
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SoftCareButton(
                            text = "",
                            icon = Icons.Default.Remove,
                            onClick = { viewModel.adjustPeriodDuration(-1) },
                            containerColor = softPink,
                            contentColor = deepPlum,
                            testTagName = "btn_duration_minus"
                        )
                        SoftCareButton(
                            text = "",
                            icon = Icons.Default.Add,
                            onClick = { viewModel.adjustPeriodDuration(1) },
                            containerColor = deepPlum,
                            contentColor = Color.White,
                            testTagName = "btn_duration_plus"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SoftCareButton(
                    text = stringResource(R.string.period_btn_log_today),
                    onClick = {
                        viewModel.logPeriodStart(0, localizedContext.getString(R.string.symptom_calm))
                    },
                    icon = Icons.Default.CheckCircle,
                    containerColor = Color(0xFF4CAF50),
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "btn_log_period_today"
                )

                Spacer(modifier = Modifier.height(10.dp))

                SoftCareButton(
                    text = stringResource(R.string.period_btn_notify),
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.triggerCycleNotification(localizedContext)
                        }
                    },
                    icon = Icons.Default.NotificationsActive,
                    containerColor = deepPlum,
                    modifier = Modifier.fillMaxWidth(),
                    testTagName = "btn_period_notify"
                )

                if (logs.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = stringResource(R.string.period_logs_title) + " (${logs.size})",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = captionGray
                    )
                }
            }
        }
    }
}

@Composable
fun BreastCareScreen(
    viewModel: FemCareViewModel
) {
    BackHandler { viewModel.navigateToModule(AppModule.HOME) }

    val cards = listOf(
        Pair(R.string.care_card1_title, R.string.care_card1_body),
        Pair(R.string.care_card2_title, R.string.care_card2_body),
        Pair(R.string.care_card3_title, R.string.care_card3_body),
        Pair(R.string.care_card4_title, R.string.care_card4_body)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        SoftModuleHeader(
            illustrationResId = R.drawable.real_selfcheck,
            title = stringResource(R.string.mod_care_title),
            subtitle = stringResource(R.string.mod_care_subtitle),
            onBack = { viewModel.navigateToModule(AppModule.HOME) }
        )

        RealGuideCard(
            imageRes = R.drawable.real_selfcheck,
            title = stringResource(R.string.guide_care_title),
            steps = listOf(
                stringResource(R.string.guide_care_step1),
                stringResource(R.string.guide_care_step2)
            ),
            testTagName = "care_real_guide_card"
        )

        cards.forEachIndexed { index, (titleRes, bodyRes) ->
            PremiumCard(
                modifier = Modifier.testTag("care_edu_card_$index")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color(0xFFFFD6E7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = stringResource(titleRes),
                                tint = Color(0xFF6B2D4B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(titleRes),
                            style = MaterialTheme.typography.headlineLarge,
                            color = FemCareColors.primaryPlum
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(bodyRes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = FemCareColors.textBodyDark
                    )
                }
            }
        }
    }
}
