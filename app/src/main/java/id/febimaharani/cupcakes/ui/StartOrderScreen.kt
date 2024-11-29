package id.febimaharani.cupcakes.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.febimaharani.cupcakes.R
import id.febimaharani.cupcakes.data.DataSource
import id.febimaharani.cupcakes.ui.theme.CupcakeTheme

@Composable
fun StartOrderScreen( // terdapat column, spacer, image, 
    quantityOptions: List<Pair<Int, Int>>, // daftar pilihan jumlah cupcake
    onNextButtonClicked: (Int) -> Unit, // menerima jumlah yang dipilih
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // menyusun elemen secara vertikal
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // menyusun elemen secara horizontal di teengah kolom
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // memberi jarak antar elemen
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))) // menambah ruang kosong di atas gambaar cupcake
            Image( // foto cupcake
                painter = painterResource(R.drawable.cupcakes),
                contentDescription = null,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))) // menambah jarak dibawah gambar cupcake
            Text( // menampilkan judul dan menerapkan typography
                text = stringResource(R.string.order_cupcakes), 
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small))) // menambah ruang kosoong di bawah judul
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // mengatur elemen secara horizontal
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            quantityOptions.forEach { item -> // iterasi dari bagian 1- selesai 
                SelectQuantityButton( 
                    labelResourceId = item.first, 
                    onClick = { onNextButtonClicked(item.second) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun SelectQuantityButton( // fungsi compasable untuk tombol pilihan jumlah cupcake
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResourceId))
    }
}

@Preview
@Composable
fun StartOrderPreview() {
    CupcakeTheme {
        StartOrderScreen(
            quantityOptions = DataSource.quantityOptions, // mengambil jumlah dari DataSource
            onNextButtonClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
