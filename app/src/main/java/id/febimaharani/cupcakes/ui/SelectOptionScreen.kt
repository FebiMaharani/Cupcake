package id.febimaharani.cupcakes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.febimaharani.cupcakes.R
import id.febimaharani.cupcakes.ui.components.FormattedPriceLabel
import id.febimaharani.cupcakes.ui.theme.CupcakeTheme

@Composable
fun SelectOptionScreen(
    subtotal: String, // total harga yang tampil
    options: List<String>, // opsi yang dipilih pengguna
    onSelectionChanged: (String) -> Unit = {}, // callback saat berubah pilihan. default=koksoong
    onCancelButtonClicked: () -> Unit = {}, // callback saat batal klik
    onNextButtonClicked: () -> Unit = {}, // callback ketika klik berikutnya
    modifier: Modifier = Modifier
) {
    var selectedValue by rememberSaveable { mutableStateOf("") } // menyimpan nilai yang dipilih

    Column(
        modifier = modifier, // menerapkan modifier ke kolom utama
        verticalArrangement = Arrangement.SpaceBetween //menyusun elemen vertikal 
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) { 
            options.forEach { item -> // iterasi setiap opsi dalam daftar opsi
                Row(
                    modifier = Modifier.selectable( 
                        selected = selectedValue == item, // menentukan opsi saat ini
                        onClick = {
                            selectedValue = item // update nilai yang dipilih
                            onSelectionChanged(item) // memanggil kembali dengan nilai baru
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically // elemen tampil secara vertikal
                ) {
                    RadioButton( // menampilkan tombol di samping teks opsi.
                        selected = selectedValue == item, // menentukan tombol yang dipilih
                        onClick = {
                            selectedValue = item // update nilai yang dipilih
                            onSelectionChanged(item) // memanggil kembali nilai baru
                        }
                    )
                    Text(item) 
                }
            }
            Divider( // menambahkan pemisak antara opsi dan subtotal
                thickness = dimensionResource(R.dimen.thickness_divider), // ketebalan pemisah
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium)) //menambahkan padding bawah 
            )
            FormattedPriceLabel( // menampilkan label harga ke composable
                subtotal = subtotal, //meneruskan subtotal ke composable
                modifier = Modifier 
                    .align(Alignment.End) // menyelaraskan label harga ke akhir kolom
                    .padding( // menambahkan padding pada label harga
                        top = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
            )
        }
        Row( // baris bagian bawah layar untuk tombol cancel dan  next
            modifier = Modifier
                .fillMaxWidth() // lebar maks layar
                .padding(dimensionResource(R.dimen.padding_medium)), //menambah padding pada baris
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)), // memberi jarak tombol
            verticalAlignment = Alignment.Bottom // merapihkan elemen vertika; ke bagian bawah baris
        ) {
            OutlinedButton( // tombol batal
                modifier = Modifier.weight(1f), 
                onClick = onCancelButtonClicked // memanggil kembali saat tombol di klik
            ) {
                Text(stringResource(R.string.cancel)) // menampilkan tombol "Cancel"
            }
            Button( 
                modifier = Modifier.weight(1f),
                enabled = selectedValue.isNotEmpty(), // tombol aktif jika pilihan dipilih
                onClick = onNextButtonClicked // memanggil kembali saat tombol di klik
            ) {
                Text(stringResource(R.string.next)) // menampilkan tombol "next"
            }
        }
    }

}

@Preview
@Composable
fun SelectOptionPreview() {
    CupcakeTheme { // tema cupcake
        SelectOptionScreen(
            subtotal = "299.99",
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
            modifier = Modifier.fillMaxHeight()
        )
    }
}
