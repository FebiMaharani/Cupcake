package id.febimaharani.cupcakes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set typography yang digunakan dalam aplikasi
val Typography = Typography(
    bodyLarge = TextStyle( // gaya untuk body large
        fontFamily = FontFamily.Default, // jenis huruf default
        fontWeight = FontWeight.Normal, // ketebalan huruf normal
        fontSize = 16.sp, // uk font 16
        lineHeight = 24.sp, // tinggi baris
        letterSpacing = 0.5.sp // jarak antarr huruf
    )
)
