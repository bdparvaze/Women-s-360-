package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.config.SupportedLanguage
import com.example.ui.theme.FemCareColors
import com.example.ui.theme.OutfitFontFamily

@Composable
fun SoftGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
    ) {
        content()
    }
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(clickModifier),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
fun SoftCareCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    borderColor: Color = FemCareColors.softBorderColor,
    shape: Shape? = null,
    onClick: (() -> Unit)? = null,
    testTagName: String = "soft_care_card",
    content: @Composable ColumnScope.() -> Unit
) {
    val resolvedShape = shape ?: RoundedCornerShape(24.dp)
    val clickModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag(testTagName)
            .then(clickModifier),
        shape = resolvedShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            content = content
        )
    }
}

@Composable
fun PremiumTopAppBar(
    title: String = stringResource(R.string.app_name),
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color(0xFFFFD6E7), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = title,
                    tint = Color(0xFF6B2D4B),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF6B2D4B),
                modifier = Modifier.testTag("app_header_title")
            )
        }
    }
}

@Composable
fun SoftCareButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = Color(0xFF6B2D4B),
    contentColor: Color = Color.White,
    testTagName: String = "soft_care_button"
) {
    val shape = RoundedCornerShape(16.dp)

    Surface(
        modifier = modifier
            .testTag(testTagName)
            .heightIn(min = 48.dp)
            .clip(shape)
            .clickable { onClick() },
        shape = shape,
        color = containerColor,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text.ifEmpty { stringResource(R.string.app_name) },
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                if (text.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun LanguageSwitcherBar(
    selectedLanguage: SupportedLanguage,
    onLanguageSelected: (SupportedLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("language_switcher_card")
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SupportedLanguage.entries.forEach { lang ->
            val isSelected = lang == selectedLanguage
            val shape = RoundedCornerShape(50)
            val bg = if (isSelected) Color(0xFF6B2D4B) else Color.White
            val txtColor = if (isSelected) Color.White else Color(0xFF6B2D4B)
            val borderCol = if (isSelected) Color(0xFF6B2D4B) else Color(0xFFFFD6E7)

            Box(
                modifier = Modifier
                    .testTag("lang_btn_${lang.isoCode}")
                    .heightIn(min = 38.dp)
                    .clip(shape)
                    .background(bg)
                    .border(1.dp, borderCol, shape)
                    .clickable { onLanguageSelected(lang) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(lang.labelRes),
                    fontFamily = OutfitFontFamily,
                    fontSize = 13.sp,
                    color = txtColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SoftModuleHeader(
    @DrawableRes illustrationResId: Int,
    title: String,
    subtitle: String,
    onBack: (() -> Unit)? = null
) {
    PremiumCard(
        modifier = Modifier.testTag("module_soft_header"),
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (onBack != null) {
                Row(
                    modifier = Modifier
                        .testTag("btn_back_hub")
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFFFD6E7))
                        .clickable { onBack() }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.btn_back_home),
                        tint = Color(0xFF6B2D4B),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.btn_back_home),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF6B2D4B)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF6B2D4B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888888)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = illustrationResId),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFFFD6E7), CircleShape)
                )
            }
        }
    }
}

@Composable
fun Flat2DGuideCard(
    @DrawableRes drawableResId: Int,
    @StringRes captionResId: Int,
    modifier: Modifier = Modifier,
    onlineFallbackUrl: String? = null,
    testTagName: String = "flat_2d_guide_card"
) {
    val caption = stringResource(captionResId)
    val corner = dimensionResource(R.dimen.soft_button_corner)

    PremiumCard(
        modifier = modifier.testTag(testTagName)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (onlineFallbackUrl != null) {
                AsyncImage(
                    model = onlineFallbackUrl,
                    placeholder = painterResource(id = drawableResId),
                    error = painterResource(id = drawableResId),
                    fallback = painterResource(id = drawableResId),
                    contentDescription = caption,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.guide_image_height))
                        .clip(RoundedCornerShape(corner))
                )
            } else {
                Image(
                    painter = painterResource(id = drawableResId),
                    contentDescription = caption,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.guide_image_height))
                        .clip(RoundedCornerShape(corner))
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = caption,
                style = MaterialTheme.typography.titleMedium,
                color = FemCareColors.textRose,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}
