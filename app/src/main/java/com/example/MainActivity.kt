package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.config.AppConfig
import com.example.config.AppModule
import com.example.data.local.FemCareDatabase
import com.example.data.local.FemCareRepository
import com.example.ui.FemCareViewModel
import com.example.ui.components.LanguageSwitcherBar
import com.example.ui.components.PremiumTopAppBar
import com.example.ui.components.SoftGradientBackground
import com.example.ui.screens.AdultWellnessEducationScreen
import com.example.ui.screens.AdultWellnessScreen
import com.example.ui.screens.BraCalculatorScreen
import com.example.ui.screens.BreastCareScreen
import com.example.ui.screens.DailyFitnessScreen
import com.example.ui.screens.DietWaterScreen
import com.example.ui.screens.DoctorFinderScreen
import com.example.ui.screens.FirstTimeBraGuideScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.PeriodTrackerScreen
import com.example.ui.theme.FemCare360Theme
import com.example.ui.theme.OutfitFontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = FemCareDatabase.getInstance(applicationContext)
        val repository = FemCareRepository(database.femCareDao())

        setContent {
            val femCareViewModel: FemCareViewModel = viewModel(
                factory = FemCareViewModel.provideFactory(repository)
            )
            FemCare360App(viewModel = femCareViewModel)
        }
    }
}

@Composable
fun FemCare360App(
    viewModel: FemCareViewModel
) {
    val selectedLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val currentModule by viewModel.currentModule.collectAsStateWithLifecycle()
    val baseContext = LocalContext.current

    val activityResultRegistryOwner = LocalActivityResultRegistryOwner.current
    val backPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

    val localizedContext = remember(selectedLanguage, baseContext) {
        AppConfig.createLocalizedContext(baseContext, selectedLanguage)
    }
    val localizedConfiguration = remember(selectedLanguage, localizedContext) {
        localizedContext.resources.configuration
    }
    val layoutDirection = remember(selectedLanguage) {
        AppConfig.getLayoutDirection(selectedLanguage)
    }

    val providers = buildList {
        add(LocalContext provides localizedContext)
        add(LocalConfiguration provides localizedConfiguration)
        add(LocalLayoutDirection provides layoutDirection)
        if (activityResultRegistryOwner != null) {
            add(LocalActivityResultRegistryOwner provides activityResultRegistryOwner)
        }
        if (backPressedDispatcherOwner != null) {
            add(LocalOnBackPressedDispatcherOwner provides backPressedDispatcherOwner)
        }
    }.toTypedArray()

    CompositionLocalProvider(*providers) {
        FemCare360Theme {
            SoftGradientBackground {
                Scaffold(
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        PremiumTopAppBar()
                    },
                    bottomBar = {
                        SoftModuleBottomBar(
                            currentModule = currentModule,
                            onSelectModule = { viewModel.navigateToModule(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .widthIn(max = dimensionResource(R.dimen.max_content_width))
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = 8.dp)
                        ) {
                            LanguageSwitcherBar(
                                selectedLanguage = selectedLanguage,
                                onLanguageSelected = { viewModel.selectLanguage(it) }
                            )

                            when (currentModule) {
                                AppModule.HOME -> HomeDashboardScreen(viewModel = viewModel)
                                AppModule.BRA_CALCULATOR -> BraCalculatorScreen(
                                    viewModel = viewModel,
                                    localizedContext = localizedContext
                                )
                                AppModule.PERIOD_TRACKER -> PeriodTrackerScreen(
                                    viewModel = viewModel,
                                    localizedContext = localizedContext
                                )
                                AppModule.BREAST_CARE -> BreastCareScreen(viewModel = viewModel)
                                AppModule.DAILY_FITNESS -> DailyFitnessScreen(viewModel = viewModel)
                                AppModule.ADULT_WELLNESS -> AdultWellnessScreen(
                                    viewModel = viewModel,
                                    localizedContext = localizedContext
                                )
                                AppModule.DIET_WATER -> DietWaterScreen(
                                    viewModel = viewModel,
                                    localizedContext = localizedContext
                                )
                                AppModule.DOCTOR_FINDER -> DoctorFinderScreen(viewModel = viewModel)
                                AppModule.FIRST_TIME_GUIDE -> FirstTimeBraGuideScreen(
                                    viewModel = viewModel,
                                    onNavigateToCalculator = {
                                        viewModel.navigateToModule(AppModule.BRA_CALCULATOR)
                                    }
                                )
                                AppModule.ADULT_WELLNESS_EDUCATION -> AdultWellnessEducationScreen(
                                    viewModel = viewModel,
                                    initialAgeVerified = true,
                                    onNavigateToDoctors = {
                                        viewModel.navigateToModule(AppModule.DOCTOR_FINDER)
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

private data class BottomNavItem(
    val module: AppModule,
    val icon: ImageVector
)

@Composable
fun SoftModuleBottomBar(
    currentModule: AppModule,
    onSelectModule: (AppModule) -> Unit
) {
    val navItems = listOf(
        BottomNavItem(AppModule.HOME, Icons.Default.Home),
        BottomNavItem(AppModule.BRA_CALCULATOR, Icons.Default.Straighten),
        BottomNavItem(AppModule.PERIOD_TRACKER, Icons.Default.CalendarMonth),
        BottomNavItem(AppModule.DOCTOR_FINDER, Icons.Default.LocalHospital)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = currentModule == item.module ||
                    (item.module == AppModule.HOME && currentModule !in navItems.map { it.module })
                val pillBg = if (isSelected) Color(0xFFFFD6E7) else Color.Transparent
                val contentCol = if (isSelected) Color(0xFF6B2D4B) else Color(0xFF888888)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .testTag("nav_tab_${item.module.name.lowercase()}")
                        .clip(RoundedCornerShape(16.dp))
                        .background(pillBg)
                        .clickable { onSelectModule(item.module) }
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.module.navLabelRes),
                        tint = contentCol,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(item.module.navLabelRes),
                        fontFamily = OutfitFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp,
                        color = contentCol,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
