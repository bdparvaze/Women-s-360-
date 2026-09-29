package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.AppModule
import com.example.ui.FemCareViewModel
import com.example.ui.components.PremiumCard
import com.example.ui.components.RealGuideCard
import com.example.ui.components.SoftModuleHeader
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PoppinsFontFamily

@Composable
fun FirstTimeBraGuideScreen(
    viewModel: FemCareViewModel? = null,
    onNavigateToCalculator: (() -> Unit)? = null
) {
    if (viewModel != null) {
        BackHandler { viewModel.navigateToModule(AppModule.HOME) }
    }

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val successGreen = Color(0xFF4CAF50)
    val bodyDark = Color(0xFF333333)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("first_time_bra_guide_screen")
    ) {
        if (viewModel != null) {
            SoftModuleHeader(
                illustrationResId = R.drawable.guide_firsttime_1_when,
                title = stringResource(R.string.mod_first_time_title),
                subtitle = stringResource(R.string.mod_first_time_subtitle),
                onBack = { viewModel.navigateToModule(AppModule.HOME) }
            )
        }

        // 1. Intro PremiumCard
        PremiumCard(
            modifier = Modifier.testTag("firsttime_intro_card"),
            containerColor = softPink
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "প্রথমবারের জন্য গাইড / First Time Guide",
                            tint = deepPlum,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "প্রথমবারের জন্য গাইড / First Time Guide",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.outfit)),
                            fontWeight = Bold,
                            fontSize = 22.sp,
                            color = deepPlum
                        ),
                        modifier = Modifier.testTag("firsttime_title_text")
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "লজ্জার কিছু নেই, এটা স্বাভাবিক। প্রত্যেক মেয়ের জীবনে এই সময় আসে। তুমি একা নও।",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = bodyDark
                    )
                )
            }
        }

        // 2. RealGuideCard 1: When you need first bra
        RealGuideCard(
            imageRes = R.drawable.guide_firsttime_1_when,
            title = "কখন বুঝবে দরকার?",
            steps = listOf(
                "টি-শার্ট টাইট লাগলে",
                "দৌড়ালে অস্বস্তি হলে",
                "বন্ধুদের সবার আছে বলে নয়, নিজের আরামের জন্য"
            ),
            testTagName = "firsttime_guide_when_card"
        )

        // 3. RealGuideCard 2: How to measure (2 minutes)
        RealGuideCard(
            imageRes = R.drawable.guide_firsttime_2_measure,
            title = "কিভাবে মাপবে? (২ মিনিট)",
            steps = listOf(
                "১. পাতলা টি-শার্ট পরো",
                "২. আয়নার সামনে সোজা দাঁড়াও",
                "৩. ছবির মতো ফিতা ধরো - বুকের সবচেয়ে উঁচু জায়গায়",
                "৪. মাপটা লিখে রাখো",
                "৫. কাউকে দেখানোর দরকার নেই, নিজে করো"
            ),
            testTagName = "firsttime_guide_measure_card"
        )

        // 4. Tips PremiumCard
        PremiumCard(
            modifier = Modifier.testTag("firsttime_tips_card")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "প্রথমবারের জন্য টিপস:",
                    fontFamily = OutfitFontFamily,
                    fontWeight = Bold,
                    fontSize = 18.sp,
                    color = deepPlum
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "• স্পোর্টস ব্রা দিয়ে শুরু করো - সবচেয়ে আরামদায়ক\n• সাদা, কালো, স্কিন কালার নাও - সব জামার সাথে যায়\n• এক সাইজ বড় নিও না, ছবির মতো মেপে সঠিক সাইজ নাও\n• অনলাইনে অর্ডার করার সময় 'Beginner / Sports Bra' লেখা দেখে নাও\n• মা, বড় বোন বা বান্ধবীর সাথে শেয়ার করতে পারো",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 23.sp,
                    color = bodyDark
                )
            }
        }

        // 5. RealGuideCard 3: Comfort
        RealGuideCard(
            imageRes = R.drawable.guide_firsttime_3_comfort,
            title = "আরামের জন্য",
            steps = listOf(
                "সুতি কাপড়ের ব্রা নাও",
                "টাইট নয়, আরামদায়ক নাও",
                "দিনে ৮-১০ ঘণ্টা পরো"
            ),
            testTagName = "firsttime_guide_comfort_card"
        )

        // 6. Privacy & Navigate to BraCalculator PremiumCard
        PremiumCard(
            modifier = Modifier.testTag("firsttime_privacy_cta_card")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(softPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "গোপনীয়তা",
                            tint = deepPlum,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "গোপনীয়তা: তোমার মাপ শুধু তোমার ফোনে থাকবে, কোথাও যাবে না।",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = Bold,
                        fontSize = 14.sp,
                        color = deepPlum
                    )
                }

                Button(
                    onClick = {
                        if (onNavigateToCalculator != null) {
                            onNavigateToCalculator()
                        } else {
                            viewModel?.navigateToModule(AppModule.BRA_CALCULATOR)
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = successGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("btn_firsttime_measure_now")
                ) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = "এখনই আমার মাপ নিন - ২ মিনিটে",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "এখনই আমার মাপ নিন - ২ মিনিটে",
                        fontFamily = OutfitFontFamily,
                        fontWeight = Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
