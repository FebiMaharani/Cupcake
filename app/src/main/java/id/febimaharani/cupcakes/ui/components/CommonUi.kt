package id.febimaharani.cupcakes.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import id.febimaharani.cupcakes.R

// menampilkan label harga
@Composable
fun FormattedPriceLabel(subtotal: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.subtotal_price, subtotal), // ambil dan format string untuk subtotal
        modifier = modifier, 
        style = MaterialTheme.typography.headlineSmall // menggunakan teks dengan tema material 
    )
}
