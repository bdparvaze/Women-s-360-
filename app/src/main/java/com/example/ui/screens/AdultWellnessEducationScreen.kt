package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.AppModule
import com.example.ui.FemCareViewModel
import com.example.ui.components.PremiumCard
import com.example.ui.components.SoftModuleHeader
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PoppinsFontFamily

@Composable
fun AdultWellnessEducationScreen(
    viewModel: FemCareViewModel? = null,
    initialAgeVerified: Boolean = true,
    onNavigateToDoctors: (() -> Unit)? = null
) {
    if (viewModel != null) {
        BackHandler { viewModel.navigateToModule(AppModule.HOME) }
    }

    var isAgeConfirmed by remember { mutableStateOf(initialAgeVerified) }
    var checkboxChecked by remember { mutableStateOf(initialAgeVerified) }

    val deepPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val lavenderAccent = Color(0xFFE8D7FF)
    val successGreen = Color(0xFF4CAF50)
    val successGreenSoft = Color(0xFFE8F5E9)
    val dangerRed = Color(0xFFD94362)
    val dangerRedSoft = Color(0xFFFFEBEE)
    val bodyDark = Color(0xFF333333)
    val captionGray = Color(0xFF888888)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("adult_wellness_education_screen")
    ) {
        if (viewModel != null) {
            SoftModuleHeader(
                illustrationResId = R.drawable.ic_mental_health,
                title = "নিরাপদ ব্যক্তিগত স্বাস্থ্য অভ্যাস - সম্পূর্ণ গাইড (18+)",
                subtitle = stringResource(R.string.mod_adult_edu_subtitle),
                onBack = { viewModel.navigateToModule(AppModule.HOME) }
            )
        }

        if (!isAgeConfirmed) {
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_age_gate_card")
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
                                contentDescription = "18+ Age Verification",
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "১৮+ বয়স যাচাইকরণ / 18+ Age Verification",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 18.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(softPink.copy(alpha = 0.4f))
                            .clickable { checkboxChecked = !checkboxChecked }
                            .padding(8.dp)
                            .testTag("checkbox_row_adult_edu_18plus"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkboxChecked,
                            onCheckedChange = { checkboxChecked = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = deepPlum,
                                uncheckedColor = deepPlum
                            ),
                            modifier = Modifier.testTag("checkbox_adult_edu_18plus")
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "আমি ১৮+ এবং এই স্বাস্থ্য তথ্য পড়তে চাই",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = Bold,
                            fontSize = 14.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (checkboxChecked) {
                                isAgeConfirmed = true
                            }
                        },
                        enabled = checkboxChecked,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = deepPlum,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_confirm_adult_edu_18plus")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockOpen,
                            contentDescription = "Continue",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "প্রবেশ করুন / Continue (18+)",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        } else {
            // Title & Clinical Disclaimer Card
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_header_card"),
                containerColor = softPink
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(lavenderAccent)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "18+ MATURE • CLINICAL GUIDE",
                                fontFamily = OutfitFontFamily,
                                fontWeight = Bold,
                                fontSize = 10.sp,
                                color = deepPlum
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "নিরাপদ ব্যক্তিগত স্বাস্থ্য অভ্যাস - সম্পূর্ণ গাইড (18+)",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.outfit)),
                            fontWeight = Bold,
                            fontSize = 20.sp,
                            color = deepPlum
                        ),
                        modifier = Modifier.testTag("adult_edu_main_title")
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "১৮+ নারী স্বাস্থ্য শিক্ষা / Adult Women's Wellness (18+)",
                        fontFamily = OutfitFontFamily,
                        fontWeight = Bold,
                        fontSize = 15.sp,
                        color = deepPlum
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "ডিসক্লেইমার: এটি চিকিৎসা পরামর্শ নয়, শুধুমাত্র সাধারণ স্বাস্থ্য শিক্ষা। কোনো সমস্যা হলে ডাক্তারের পরামর্শ নিন।",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight.Light,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = captionGray
                        )
                    )
                }
            }

            // Section 1: ১. কখন করা নিরাপদ, কখন নয়?
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_section_1")
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
                                painter = painterResource(id = R.drawable.ic_mental_health),
                                contentDescription = "১. কখন করা নিরাপদ, কখন নয়?",
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "১. কখন করা নিরাপদ, কখন নয়?",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 17.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "• কোনো নির্দিষ্ট \"ভালো সময়\" নেই, এটি সম্পূর্ণ ব্যক্তিগত পছন্দ। শরীর ও মন রিল্যাক্স থাকলে, ব্যক্তিগত ও নিরাপদ স্থানে।\n• ক্লান্ত, মানসিক চাপে, বা জোর করে নয়। নিজের ইচ্ছায়।\n• মাসিকের সময়: মেডিকেলি নিষিদ্ধ নয়। অনেক নারী বলেন এতে ক্র্যাম্প ও মুড ভালো লাগে। তবে মাসিকের সময় ইনফেকশনের ঝুঁকি বেশি থাকে, তাই অতিরিক্ত পরিচ্ছন্নতা জরুরি - হাত ভালোভাবে ধোয়া, এবং পরে পরিষ্কার হওয়া। যদি ব্যথা বাড়ে বা অস্বস্তি হয়, করবেন না।\n• অসুস্থ, জ্বর, যোনিতে ইনফেকশন বা ক্ষত থাকলে করবেন না।",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = bodyDark
                    )
                }
            }

            // Section 2: ২. কী দিয়ে করা উচিত, কী দিয়ে একদম উচিত না? - এটা সবচেয়ে জরুরি
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_section_2")
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
                                painter = painterResource(id = R.drawable.ic_hygiene),
                                contentDescription = "২. কী দিয়ে করা উচিত, কী দিয়ে একদম উচিত না?",
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "২. কী দিয়ে করা উচিত, কী দিয়ে একদম উচিত না? - এটা সবচেয়ে জরুরি",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 17.sp,
                            color = deepPlum
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Harmful Box (❌)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(dangerRedSoft)
                            .border(1.dp, dangerRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "❌ যা ব্যবহার করা উচিত না (ক্ষতিকর):",
                                fontFamily = OutfitFontFamily,
                                fontWeight = Bold,
                                fontSize = 15.sp,
                                color = dangerRed
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "• ঘরের জিনিস - সবজি, ফল, বোতল, কলম, চিরুনি, বা যেকোনো ধারালো / ভঙ্গুর জিনিস। এগুলো ভেঙে ভিতরে আটকে যেতে পারে, কেটে যেতে পারে, মারাত্মক ইনফেকশন করতে পারে। প্রতি বছর হাসপাতালে এই কারণে অনেক কেস আসে।\n• মুখে দেওয়ার জিনিস, সুগন্ধি, সাবান, ডেটল, শ্যাম্পু ভিতরে ব্যবহার - এগুলো pH নষ্ট করে ইনফেকশন করে।\n• অন্যের ব্যবহৃত জিনিস শেয়ার করা - ইনফেকশন ছড়ায়।\n• টাইট বা আঁটসাঁট কিছু যা রক্ত চলাচল বন্ধ করে দেয়।",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = bodyDark
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Safe Box (✅)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(successGreenSoft)
                            .border(1.dp, successGreen.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "✅ নিরাপদ উপায় কী?",
                                fontFamily = OutfitFontFamily,
                                fontWeight = Bold,
                                fontSize = 15.sp,
                                color = deepPlum
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "• সবচেয়ে নিরাপদ হলো নিজের হাত - পরিষ্কার হাতে, নখ ছোট রেখে, আস্তে। কোনো বাইরের বস্তুর ঝুঁকি নেই।\n• যদি কেউ টয় ব্যবহার করতে চায়, তবে শুধুমাত্র মেডিকেল-গ্রেড সিলিকনের, বডি-সেফ লেখা প্রোডাক্ট, বিশ্বস্ত ফার্মেসি/ব্র্যান্ড থেকে। ব্যবহারের আগে ও পরে গরম পানি ও মৃদু সাবান দিয়ে ধোয়া।\n• মনে রাখবেন: টয় বাধ্যতামূলক নয়। অনেক নারী কখনোই টয় ব্যবহার করেন না।",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = bodyDark
                            )
                        }
                    }
                }
            }

            // Section 3: ৩. সঠিক স্বাস্থ্যবিধি - না মানলে ইনফেকশন হবে
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_section_3")
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
                                painter = painterResource(id = R.drawable.ic_hygiene),
                                contentDescription = "৩. সঠিক স্বাস্থ্যবিধি - না মানলে ইনফেকশন হবে",
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "৩. সঠিক স্বাস্থ্যবিধি - না মানলে ইনফেকশন হবে",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 17.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "• আগে ও পরে হাত ভালোভাবে সাবান দিয়ে ২০ সেকেন্ড ধুয়ে নিন\n• প্রস্রাবের রাস্তা থেকে পায়খানার রাস্তার দিকে কখনো যাবেন না (সামনে থেকে পিছনে পরিষ্কার)\n• করার পর হালকা পানি দিয়ে বাইরের অংশ পরিষ্কার, ভিতরে সাবান দিয়ে ধোয়ার দরকার নেই\n• করার পর প্রস্রাব করলে ইউরিন ইনফেকশন (UTI) এর ঝুঁকি কমে\n• ব্যথা, জ্বালা, রক্ত, চুলকানি হলে সাথে সাথে থামুন এবং ডাক্তার দেখান",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = bodyDark
                    )
                }
            }

            // Section 4: ৪. মাসিকের সময় বিশেষ যত্ন
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_section_4")
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
                                painter = painterResource(id = R.drawable.ic_hygiene),
                                contentDescription = "৪. মাসিকের সময় বিশেষ যত্ন",
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "৪. মাসিকের সময় বিশেষ যত্ন",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 17.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "• করা যায়, তবে ঝুঁকি বেশি\n• হাত ও শরীর অতিরিক্ত পরিষ্কার রাখুন\n• প্যাড/কাপ খুলে, পরিষ্কার হয়ে, পরে আবার নতুন প্যাড ব্যবহার করুন\n• যদি ক্র্যাম্প বাড়ে, মাথা ঘোরায়, তবে করবেন না\n• মাসিকের সময় শরীর বেশি সংবেদনশীল থাকে",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = bodyDark
                    )
                }
            }

            // Section 5: ৫. কখন ডাক্তার দেখাবেন?
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_section_5")
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
                                painter = painterResource(id = R.drawable.ic_doctor_female),
                                contentDescription = "৫. কখন ডাক্তার দেখাবেন?",
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "৫. কখন ডাক্তার দেখাবেন?",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 17.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "• যোনিতে দুর্গন্ধ, সবুজ/হলুদ স্রাব, চুলকানি\n• প্রস্রাবে জ্বালা, তলপেটে ব্যথা\n• কোনো বস্তু ভিতরে আটকে গেলে - নিজে বের করার চেষ্টা না করে দ্রুত হাসপাতালে যান\n• মন খারাপ, অপরাধবোধ, পড়াশোনা/কাজে সমস্যা হলে কাউন্সেলরের সাথে কথা বলুন",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = bodyDark
                    )
                }
            }

            // Section 6: ৬. শেষ কথা + Privacy + DoctorFinder CTA
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_section_6")
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
                                painter = painterResource(id = R.drawable.ic_mental_health),
                                contentDescription = "৬. শেষ কথা",
                                tint = deepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "৬. শেষ কথা",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 17.sp,
                            color = deepPlum
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "এটি আপনার শরীর, আপনার সিদ্ধান্ত। লজ্জা নয়, সচেতনতা জরুরি। ভুল জিনিস ব্যবহার করে নিজের ক্ষতি করবেন না। যেকোনো সন্দেহে FemCare 360 এর DoctorFinder থেকে একজন নারী ডাক্তারের সাথে গোপনে কথা বলুন।",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = bodyDark
                    )
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (onNavigateToDoctors != null) {
                                onNavigateToDoctors()
                            } else {
                                viewModel?.navigateToModule(AppModule.DOCTOR_FINDER)
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = deepPlum,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_adult_edu_find_doctor")
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_doctor_female),
                            contentDescription = "DoctorFinder",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "নারী ডাক্তারের সাথে কথা বলুন (DoctorFinder)",
                            fontFamily = OutfitFontFamily,
                            fontWeight = Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Privacy Card
            PremiumCard(
                modifier = Modifier.testTag("adult_edu_privacy_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "গোপনীয়তা",
                        tint = deepPlum,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "আপনার কোনো তথ্য অ্যাপে সেভ হয় না।",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = Bold,
                        fontSize = 13.sp,
                        color = deepPlum
                    )
                }
            }
        }
    }
}
