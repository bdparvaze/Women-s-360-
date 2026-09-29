package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import com.example.R

@Composable
fun FemCare360Theme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = FemCareColors.primaryPlum,
        onPrimary = FemCareColors.whitePure,
        primaryContainer = FemCareColors.secondaryPink,
        onPrimaryContainer = FemCareColors.primaryPlum,
        secondary = FemCareColors.secondaryPink,
        onSecondary = FemCareColors.primaryPlum,
        secondaryContainer = FemCareColors.accentLavender,
        onSecondaryContainer = FemCareColors.primaryPlum,
        tertiary = FemCareColors.successGreen,
        onTertiary = FemCareColors.whitePure,
        tertiaryContainer = FemCareColors.successGreenSoft,
        onTertiaryContainer = FemCareColors.primaryPlum,
        background = FemCareColors.bgWarmCream,
        onBackground = FemCareColors.textBodyDark,
        surface = FemCareColors.whitePure,
        onSurface = FemCareColors.textBodyDark,
        surfaceVariant = FemCareColors.secondaryPink,
        onSurfaceVariant = FemCareColors.textCaptionGray
    )

    val cardCorner = dimensionResource(R.dimen.soft_card_corner)
    val buttonCorner = dimensionResource(R.dimen.soft_button_corner)
    val chipCorner = dimensionResource(R.dimen.soft_chip_corner)

    val softShapes = Shapes(
        extraSmall = RoundedCornerShape(chipCorner),
        small = RoundedCornerShape(chipCorner),
        medium = RoundedCornerShape(buttonCorner),
        large = RoundedCornerShape(cardCorner),
        extraLarge = RoundedCornerShape(cardCorner)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = softShapes,
        content = content
    )
}
