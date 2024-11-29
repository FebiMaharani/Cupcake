package id.febimaharani.cupcakes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import id.febimaharani.cupcakes.R
import id.febimaharani.cupcakes.data.OrderUiState
import id.febimaharani.cupcakes.ui.components.FormattedPriceLabel
import id.febimaharani.cupcakes.ui.theme.CupcakeTheme

@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState, // orderUiState menampilkan status pesanan.
    onCancelButtonClicked: () -> Unit, // pembatalan pesanan
    onSendButtonClicked: (String, String) -> Unit, // mengirimkan ringkasan pesanan akhir
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources // mendapatkan sumber daya dari konteks lokal

    val numberOfCupcakes = resources.getQuantityString( // mendapatkan string jumlah cupcake
        R.plurals.cupcakes,
        orderUiState.quantity,
        orderUiState.quantity
    )
    val orderSummary = stringResource( // ringkasan pesanan
        R.string.order_details,
        numberOfCupcakes,
        orderUiState.flavor,
        orderUiState.date,
        orderUiState.quantity
    )
    val newOrder = stringResource(R.string.new_cupcake_order) // mendapatkan string subjek pesanan baru
    val items = listOf( // daftar pesanan yang akan tampil
        Pair(stringResource(R.string.quantity), numberOfCupcakes), // baris ringkasan ke-1
        Pair(stringResource(R.string.flavor), orderUiState.flavor), // baris ke-2 untuk rasa
        Pair(stringResource(R.string.pickup_date), orderUiState.date) // baris ke-3 untuk pickup date
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // menyusun elemen secara vertikal
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)), 
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // jarak antar elemen
        ) {
            items.forEach { item -> // iterasi setiap item ringkasan order
                Text(item.first.uppercase()) // mennampilkan label dengan huruf kapital
                Text(text = item.second, fontWeight = FontWeight.Bold) // menampilkan label dengan huruf tebal
                Divider(thickness = dimensionResource(R.dimen.thickness_divider)) 
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small))) // memberikan jarak di bawah daftar ringkasan 
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End) // merapihkan label harga ke kolom akhir
            )
        }
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) }
                ) {
                    Text(stringResource(R.string.send)) // menampilkan tombol "Share"
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel)) // menampilkan teks"cancel"
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderSummaryPreview() {
    CupcakeTheme {
        OrderSummaryScreen(
            orderUiState = OrderUiState(0, "Test", "Test", "$300.00"), // contoh status pesanan pratinjau
            onSendButtonClicked = { subject: String, summary: String -> },
            onCancelButtonClicked = {},
            modifier = Modifier.fillMaxHeight()
        )
    }
}
