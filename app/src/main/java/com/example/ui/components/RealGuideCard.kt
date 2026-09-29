package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealGuideCard(
    @DrawableRes imageRes: Int,
    title: String,
    steps: List<String>,
    modifier: Modifier = Modifier,
    testTagName: String = "real_guide_card"
) {
    var isZoomed by remember { mutableStateOf(false) }
    val primaryPlum = Color(0xFF6B2D4B)
    val softPink = Color(0xFFFFD6E7)
    val bodyDark = Color(0xFF333333)
    val captionGray = Color(0xFF888888)

    PremiumCard(
        modifier = modifier.testTag(testTagName)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { isZoomed = true },
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.outfit)),
                    fontWeight = Bold,
                    fontSize = 18.sp,
                    color = primaryPlum
                )
            )
            Spacer(Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                steps.forEachIndexed { index, step ->
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
                                text = "${index + 1}",
                                fontWeight = Bold,
                                fontSize = 13.sp,
                                color = primaryPlum
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = step,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.poppins)),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = bodyDark
                            )
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.disclaimer_modest),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = captionGray
                )
            )
        }
    }

    if (isZoomed) {
        BasicAlertDialog(
            onDismissRequest = { isZoomed = false }
        ) {
            PremiumCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isZoomed = false }
            ) {
                Column {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = title,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.outfit)),
                            fontWeight = Bold,
                            fontSize = 18.sp,
                            color = primaryPlum
                        )
                    )
                }
            }
        }
    }
}
