package id.febimaharani.cupcakes.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import id.febimaharani.cupcakes.R
import id.febimaharani.cupcakes.data.DataSource
import id.febimaharani.cupcakes.data.OrderUiState
import id.febimaharani.cupcakes.ui.OrderSummaryScreen
import id.febimaharani.cupcakes.ui.SelectOptionScreen
import id.febimaharani.cupcakes.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {

    // pengganti MainActivity
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeOrderUiState = OrderUiState( // uji objek OrderUiState 
        quantity = 6,
        flavor = "Vanilla",
        date = "Wed Jul 21",
        price = "$100",
        pickupOptions = listOf()
    )

    // menampilkan opsi jumlah ke StartOrderScreen
    @Test
    fun startOrderScreen_verifyContent() { // memuat StartOrderScreen

        composeTestRule.setContent {
            StartOrderScreen( // layar memulai order
                quantityOptions = DataSource.quantityOptions, // mengambil data jumlah dari DataSource
                onNextButtonClicked = {} //  memanggil kembali saat klik tombol next
            )
        }

        // menampilkan semua opsi
        DataSource.quantityOptions.forEach {
            composeTestRule.onNodeWithStringId(it.first).assertIsDisplayed() // verifikasi setiap opsi ditampilkan
        }
    }

    // menonaktifkan tombol next saat selectOptionScreen, opsi dan subtotal tampil
    @Test
    fun selectOptionScreen_verifyContent() {
        // daftar pilihan rasa
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // subtotal dan harga
        val subtotal = "$100"

        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors) // menampilkan subtotal dan opsi ke layar pilihan
        }

        // menampilkan semua opsi.
        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed() // verifikasi rasa ditampilkan
        }

        // menampilkan subtotal dengan benar.
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed() // verifikasi tampilan subtotal

        // menonaktifkan tombol next
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled() // verifikasi tombol next nonaktif
    }

    // menngaktifkan tombool next ketika daftar opsi dan subtotal ditampilkan
    @Test
    fun selectOptionScreen_optionSelected_NextButtonEnabled() {
        // memberikan daftar pillihan rasa
        val flavours = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // subtotal dan harga
        val subTotal = "$100"

        // SelectOptionScreen 
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subTotal, options = flavours) // menampilkan subtotal dan opsi ke layar pilihan
        }

        // satu pilihan dipilih
        composeTestRule.onNodeWithText("Vanilla").performClick() // klik rasa vanila

        // tombol next aktif
        composeTestRule.onNodeWithStringId(R.string.next).assertIsEnabled() // verifikasi tombol next aktif 
    }

    // menampilkan orderUiState
    @Test
    fun summaryScreen_verifyContentDisplay() {
        // memuat Summary Screen
        composeTestRule.setContent {
            OrderSummaryScreen(
                orderUiState = fakeOrderUiState, // menyediakan pilihan pesanan ke ringkasan pesanan
                onCancelButtonClicked = {}, 
                onSendButtonClicked = { _, _ -> }, // callback kosong saat batal diklik
            )
        }

        // memberbarui UI
        composeTestRule.onNodeWithText(fakeOrderUiState.flavor).assertIsDisplayed() // Verifikasi rasa
        composeTestRule.onNodeWithText(fakeOrderUiState.date).assertIsDisplayed() // verifikasi tanggal
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                fakeOrderUiState.price
            )
        ).assertIsDisplayed() // verifikasi total harga
    }
}
