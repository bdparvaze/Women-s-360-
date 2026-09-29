package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.config.AppConfig
import com.example.config.AppModule
import com.example.config.StoreRegion
import com.example.ui.FemCareViewModel
import com.example.ui.components.PremiumCard
import com.example.ui.components.SoftCareButton
import com.example.ui.components.SoftModuleHeader
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PoppinsFontFamily
import com.example.utils.BraResult
import com.example.utils.BraSizeCalculator
import java.util.Locale
import kotlinx.coroutines.launch

private fun parseLocalizedDouble(input: String): Double? {
    val normalized = buildString {
        for (ch in input.trim()) {
            when (ch) {
                in '০'..'৯' -> append((ch - '০' + '0'.code).toChar())
                in '०'..'९' -> append((ch - '०' + '0'.code).toChar())
                in '٠'..'٩' -> append((ch - '٠' + '0'.code).toChar())
                ',' -> append('.')
                else -> append(ch)
            }
        }
    }
    return normalized.toDoubleOrNull()
}

private data class BraMeasurementSlide(
    val stepNumber: Int,
    @DrawableRes val imageRes: Int,
    val title: String,
    val overlayBanner: String,
    val arrowHint: String,
    val steps: List<String>,
    val banglaVoiceScript: String
)

@Composable
fun BraCalculatorScreen(
    viewModel: FemCareViewModel? = null,
    localizedContext: Context? = null
) {
    if (viewModel != null) {
        BackHandler { viewModel.navigateToModule(AppModule.HOME) }
    }

    var underbust by remember { mutableStateOf("78") }
    var bust by remember { mutableStateOf("88") }
    var unit by remember { mutableStateOf("cm") }
    var result by remember {
        mutableStateOf<BraResult?>(
            BraSizeCalculator.calculateBraSize(78.0, 88.0, "cm")
        )
    }
    var hasError by remember { mutableStateOf(false) }

    val baseContext = LocalContext.current
    val region = if (viewModel != null) {
        val r by viewModel.selectedStoreRegion.collectAsStateWithLifecycle()
        r
    } else {
        StoreRegion.SAUDI_ARABIA
    }

    // Bangla Text-To-Speech (TTS) engine for voice guide
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var speakingStepIndex by remember { mutableIntStateOf(-1) }

    DisposableEffect(baseContext) {
        var engine: TextToSpeech? = null
        try {
            engine = TextToSpeech(baseContext.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val banglaLocale = Locale.forLanguageTag("bn-BD")
                    val langResult = engine?.setLanguage(banglaLocale)
                    if (langResult == TextToSpeech.LANG_MISSING_DATA ||
                        langResult == TextToSpeech.LANG_NOT_SUPPORTED
                    ) {
                        engine?.setLanguage(Locale.forLanguageTag("bn"))
                    }
                    engine?.setPitch(1.02f)
                    engine?.setSpeechRate(0.92f)
                    engine?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {}
                        override fun onDone(utteranceId: String?) {
                            speakingStepIndex = -1
                        }
                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            speakingStepIndex = -1
                        }
                    })
                    isTtsReady = true
                }
            }
            ttsEngine = engine
        } catch (_: Throwable) {
            // Safe fallback in unit test / headless environments
        }
        onDispose {
            try {
                engine?.stop()
                engine?.shutdown()
            } catch (_: Throwable) {
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel?.detectLiveGpsRegion(baseContext)
    }

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val lavenderAccent = Color(0xFFE8D7FF)
    val successGreen = Color(0xFF4CAF50)
    val tapeYellow = Color(0xFFFFEB3B)
    val arrowHotPink = Color(0xFFE91E63)
    val bodyDark = Color(0xFF333333)
    val captionGray = Color(0xFF888888)

    val slides = listOf(
        BraMeasurementSlide(
            stepNumber = 1,
            imageRes = R.drawable.guide_step1_underbust_clear,
            title = stringResource(R.string.bra_step1_title),
            overlayBanner = stringResource(R.string.bra_step1_overlay),
            arrowHint = "⬇ নিচে মাপুন (Underbust) ⬇",
            steps = listOf(
                stringResource(R.string.guide_bra_step1_bn),
                stringResource(R.string.guide_bra_step2_bn)
            ),
            banglaVoiceScript = stringResource(R.string.bra_step1_tts_bn)
        ),
        BraMeasurementSlide(
            stepNumber = 2,
            imageRes = R.drawable.guide_step2_bust_clear,
            title = stringResource(R.string.bra_step2_title),
            overlayBanner = stringResource(R.string.bra_step2_overlay),
            arrowHint = "➡ সবচেয়ে উঁচু অংশ (Fullest Bust) ⬅",
            steps = listOf(
                stringResource(R.string.guide_bra_step3_bn),
                "ফিতা সমান রাখুন, বেশি টাইট করবেন না"
            ),
            banglaVoiceScript = stringResource(R.string.bra_step2_tts_bn)
        ),
        BraMeasurementSlide(
            stepNumber = 3,
            imageRes = R.drawable.guide_step3_side_check,
            title = stringResource(R.string.bra_step3_title),
            overlayBanner = stringResource(R.string.bra_step3_overlay),
            arrowHint = "↔ মেঝের সাথে সমান্তরাল (Level 90°) ↔",
            steps = listOf(
                "আয়নায় পাশ (90°) থেকে দেখুন ফিতা সোজা আছে কিনা",
                "পিঠের দিকে ফিতা যেন উপরে উঠে না যায়"
            ),
            banglaVoiceScript = stringResource(R.string.bra_step3_tts_bn)
        )
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 1. Header
        SoftModuleHeader(
            illustrationResId = R.drawable.guide_step1_underbust_clear,
            title = stringResource(R.string.bra_header_find_fit),
            subtitle = stringResource(R.string.bra_header_find_fit_sub),
            onBack = if (viewModel != null) {
                { viewModel.navigateToModule(AppModule.HOME) }
            } else null
        )

        // 2. Privacy & Shy User Reassurance Card
        PremiumCard(
            modifier = Modifier.testTag("bra_shy_privacy_card"),
            containerColor = softPink
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.bra_shy_privacy_note),
                        tint = deepPlum,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "একা ঘরে, আয়নার সামনে, টি-শার্টের উপর দিয়ে মাপুন। কাউকে দেখানোর দরকার নেই।",
                        fontFamily = OutfitFontFamily,
                        fontWeight = Bold,
                        fontSize = 15.sp,
                        lineHeight = 21.sp,
                        color = deepPlum,
                        modifier = Modifier.testTag("bra_shy_privacy_text")
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.bra_shy_privacy_sub),
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = bodyDark
                    )
                }
            }
        }

        // 3. Horizontal Swipeable 3-Step Measurement Guide Pager with Dots & Bangla Voice Note
        PremiumCard(
            modifier = Modifier.testTag("bra_guide_pager_card")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.guide_bra_title),
                        fontFamily = OutfitFontFamily,
                        fontWeight = Bold,
                        fontSize = 18.sp,
                        color = deepPlum
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(lavenderAccent)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1} / ${slides.size} • Swipe ➔",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 12.sp,
                            color = deepPlum
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bra_guide_horizontal_pager")
                ) { pageIndex ->
                    val slide = slides[pageIndex]
                    val isSpeakingThis = speakingStepIndex == pageIndex

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("bra_guide_slide_$pageIndex")
                    ) {
                        // 1:1 Square High-Clarity Image with corner FemCare 360 badge, Pink Arrows & Bangla+English Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.dp, softPink, RoundedCornerShape(20.dp))
                        ) {
                            Image(
                                painter = painterResource(id = slide.imageRes),
                                contentDescription = slide.overlayBanner,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Top-right small "FemCare 360" Logo Badge in corner
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White.copy(alpha = 0.92f))
                                    .border(1.dp, softPink, RoundedCornerShape(50))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "FemCare 360",
                                    fontFamily = OutfitFontFamily,
                                    fontWeight = Bold,
                                    fontSize = 11.sp,
                                    color = deepPlum
                                )
                            }

                            // Top-left Step Number + Yellow Tape indicator pill
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(deepPlum.copy(alpha = 0.90f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(tapeYellow, CircleShape)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "Step ${slide.stepNumber}/3",
                                    fontFamily = OutfitFontFamily,
                                    fontWeight = Bold,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }

                            // Bottom Overlay Banner on Image itself (Pink Arrows + Bangla & English text)
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(arrowHotPink)
                                        .padding(horizontal = 12.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = slide.arrowHint,
                                        fontFamily = OutfitFontFamily,
                                        fontWeight = Bold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color.White.copy(alpha = 0.95f))
                                        .border(1.5.dp, arrowHotPink, RoundedCornerShape(14.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = slide.overlayBanner,
                                        fontFamily = OutfitFontFamily,
                                        fontWeight = Bold,
                                        fontSize = 14.sp,
                                        color = deepPlum,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Voice Note Button below each image: "শুনুন কিভাবে মাপবেন" (TTS in Bangla)
                        Button(
                            onClick = {
                                if (isSpeakingThis) {
                                    try {
                                        ttsEngine?.stop()
                                    } catch (_: Throwable) {
                                    }
                                    speakingStepIndex = -1
                                } else {
                                    speakingStepIndex = pageIndex
                                    try {
                                        if (isTtsReady) {
                                            ttsEngine?.speak(
                                                slide.banglaVoiceScript,
                                                TextToSpeech.QUEUE_FLUSH,
                                                null,
                                                "bra_step_$pageIndex"
                                            )
                                        }
                                    } catch (_: Throwable) {
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSpeakingThis) arrowHotPink else softPink,
                                contentColor = if (isSpeakingThis) Color.White else deepPlum
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_voice_guide_step_$pageIndex")
                        ) {
                            Icon(
                                imageVector = if (isSpeakingThis) {
                                    Icons.Default.StopCircle
                                } else {
                                    Icons.AutoMirrored.Filled.VolumeUp
                                },
                                contentDescription = stringResource(R.string.bra_voice_btn_listen),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (isSpeakingThis) {
                                    stringResource(R.string.bra_voice_btn_speaking)
                                } else {
                                    stringResource(R.string.bra_voice_btn_listen)
                                },
                                fontFamily = OutfitFontFamily,
                                fontWeight = Bold,
                                fontSize = 15.sp
                            )
                        }

                        if (isSpeakingThis) {
                            Spacer(Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(lavenderAccent.copy(alpha = 0.55f))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "🔊 ${slide.banglaVoiceScript}",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 13.sp,
                                    color = deepPlum
                                )
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = slide.title,
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 18.sp,
                            color = deepPlum
                        )

                        Spacer(Modifier.height(10.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            slide.steps.forEachIndexed { idx, stepText ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(softPink, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${idx + 1}",
                                            fontWeight = Bold,
                                            fontSize = 13.sp,
                                            color = deepPlum
                                        )
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = stepText,
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = 14.sp,
                                        color = bodyDark
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Pager Dots Indicator (Interactive)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("bra_guide_pager_dots")
                ) {
                    slides.forEachIndexed { index, _ ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .testTag("bra_pager_dot_$index")
                                .height(10.dp)
                                .width(if (isSelected) 28.dp else 10.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) deepPlum else softPink)
                                .clickable {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.disclaimer_modest),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = captionGray,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 4. Calculator Form PremiumCard (78cm + 88cm = 34B intact)
        PremiumCard(
            modifier = Modifier.testTag("bra_calculator_form_card")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.mod_bra_title),
                    fontFamily = OutfitFontFamily,
                    fontWeight = Bold,
                    fontSize = 20.sp,
                    color = deepPlum
                )
                Spacer(Modifier.height(14.dp))

                // Unit Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (unit != "cm") {
                                unit = "cm"
                                underbust = "78"
                                bust = "88"
                                result = BraSizeCalculator.calculateBraSize(78.0, 88.0, "cm")
                                viewModel?.setMeasurementUnit(true)
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (unit == "cm") deepPlum else softPink,
                            contentColor = if (unit == "cm") Color.White else deepPlum
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_unit_cm")
                    ) {
                        Text("CM", fontFamily = OutfitFontFamily, fontWeight = Bold, fontSize = 15.sp)
                    }

                    Button(
                        onClick = {
                            if (unit != "inch") {
                                unit = "inch"
                                underbust = "30"
                                bust = "35"
                                result = BraSizeCalculator.calculateBraSize(30.0, 35.0, "inch")
                                viewModel?.setMeasurementUnit(false)
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (unit == "inch") deepPlum else softPink,
                            contentColor = if (unit == "inch") Color.White else deepPlum
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_unit_inches")
                    ) {
                        Text("Inch", fontFamily = OutfitFontFamily, fontWeight = Bold, fontSize = 15.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Inputs
                OutlinedTextField(
                    value = underbust,
                    onValueChange = {
                        underbust = it
                        hasError = false
                        viewModel?.onUnderbustChanged(it)
                        val u = parseLocalizedDouble(it)
                        val b = parseLocalizedDouble(bust)
                        if (u != null && b != null && u > 0 && b >= u) {
                            result = BraSizeCalculator.calculateBraSize(u, b, unit)
                        }
                    },
                    label = { Text("Underbust - নিচের মাপ ($unit)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        .testTag("input_underbust")
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = bust,
                    onValueChange = {
                        bust = it
                        hasError = false
                        viewModel?.onBustChanged(it)
                        val u = parseLocalizedDouble(underbust)
                        val b = parseLocalizedDouble(it)
                        if (u != null && b != null && u > 0 && b >= u) {
                            result = BraSizeCalculator.calculateBraSize(u, b, unit)
                        }
                    },
                    label = { Text("Bust - উপরের মাপ ($unit)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        .testTag("input_bust")
                )

                if (hasError) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.bra_invalid_input),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD94362)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val u = parseLocalizedDouble(underbust) ?: 0.0
                        val b = parseLocalizedDouble(bust) ?: 0.0
                        if (u > 0.0 && b >= u) {
                            hasError = false
                            result = BraSizeCalculator.calculateBraSize(u, b, unit)
                            viewModel?.onUnderbustChanged(u.toString())
                            viewModel?.onBustChanged(b.toString())
                            viewModel?.calculateAndSaveBraFit()
                        } else {
                            hasError = true
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = deepPlum,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("btn_calculate_bra")
                ) {
                    Text(
                        text = "Calculate - হিসাব করুন",
                        fontFamily = OutfitFontFamily,
                        fontWeight = Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // 5. Result Card & Store Finder
        result?.let { it ->
            PremiumCard(
                modifier = Modifier.testTag("bra_result_card"),
                containerColor = Color.White
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(successGreen.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = it.fullSize,
                                    tint = successGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "তোমার সাইজ: ${it.fullSize}",
                                fontFamily = OutfitFontFamily,
                                fontSize = 22.sp,
                                fontWeight = Bold,
                                color = deepPlum,
                                modifier = Modifier.testTag("bra_result_size_text")
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(lavenderAccent)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = it.fullSize,
                                fontFamily = OutfitFontFamily,
                                fontWeight = Bold,
                                fontSize = 14.sp,
                                color = deepPlum
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Band: ${it.bandSize}, Cup: ${it.cupSize}",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = bodyDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Sister Sizes: ${it.sisterSizes.joinToString(", ")}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        color = bodyDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Under: ${it.underbustCm.toInt()}cm, Bust: ${it.bustCm.toInt()}cm",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = captionGray
                    )

                    Spacer(Modifier.height(16.dp))

                    val storeLinks = AppConfig.getStoreLinksForRegion(region, it.fullSize)
                    Button(
                        onClick = {
                            val primaryUrl = storeLinks.firstOrNull()?.url
                                ?: ("https://www.daraz.com.bd/catalog/?q=women+bra+" + it.fullSize)
                            AppConfig.openExternalUri(baseContext, primaryUrl)
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = successGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_buy_this_size")
                    ) {
                        Text(
                            text = "এই সাইজের জন্য কিনুন (${it.fullSize})",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            PremiumCard(
                modifier = Modifier.testTag("bra_location_store_card")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.bra_location_card_title),
                        fontFamily = OutfitFontFamily,
                        fontWeight = Bold,
                        fontSize = 20.sp,
                        color = deepPlum
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(region.statusRes),
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        color = bodyDark
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    SoftCareButton(
                        text = stringResource(R.string.bra_btn_detect_gps),
                        onClick = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        icon = Icons.Default.LocationOn,
                        containerColor = deepPlum,
                        modifier = Modifier.fillMaxWidth(),
                        testTagName = "btn_detect_live_gps"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StoreRegion.entries.forEach { r ->
                            val active = r == region
                            SoftCareButton(
                                text = stringResource(r.labelRes),
                                onClick = { viewModel?.selectStoreRegion(r) },
                                modifier = Modifier.weight(1f),
                                containerColor = if (active) deepPlum else softPink,
                                contentColor = if (active) Color.White else deepPlum,
                                testTagName = "btn_region_${r.code.lowercase()}"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val storeLinks = AppConfig.getStoreLinksForRegion(region, it.fullSize)
                    storeLinks.forEachIndexed { idx, link ->
                        SoftCareButton(
                            text = stringResource(link.buttonLabelRes, it.fullSize),
                            onClick = { AppConfig.openExternalUri(baseContext, link.url) },
                            icon = Icons.Default.ShoppingBag,
                            containerColor = deepPlum,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            testTagName = "btn_shop_link_$idx"
                        )
                    }
                }
            }
        }
    }
}
