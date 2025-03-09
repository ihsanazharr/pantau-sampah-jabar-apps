package com.example.opendatajabar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.opendatajabar.R

val poppinsFont = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(fontFamily = poppinsFont, fontWeight = FontWeight.Normal),
    titleLarge = TextStyle(fontFamily = poppinsFont, fontWeight = FontWeight.Bold),
    labelLarge = TextStyle(fontFamily = poppinsFont, fontWeight = FontWeight.SemiBold)
)