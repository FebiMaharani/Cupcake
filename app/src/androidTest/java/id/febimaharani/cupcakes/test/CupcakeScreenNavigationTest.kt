package id.febimaharani.cupcakes.test

import android.icu.util.Calendar
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import id.febimaharani.cupcakes.CupcakeApp
import id.febimaharani.cupcakes.CupcakeScreen
import id.febimaharani.cupcakes.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class CupcakeScreenNavigationTest {

    // menggunakan ComponenActivity sebagai pengganti MainActivity
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>() // aturan uji Compose Android

    private lateinit var navController: TestNavHostController // menyiapkan konten

    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply { 
                navigatorProvider.addNavigator(ComposeNavigator()) // menambahkan navigator compose ke navHost
            }
            CupcakeApp(navController = navController) // memangggil fungsi utama aplikasi dengan navHost
        }
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(CupcakeScreen.Start.name) // verifikasi layar awal start
    }

    @Test
    fun cupcakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button) // mendapatkan tombool kembali
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist() // verifikasi tombol kembali tidak ada di layar awal
    }

    @Test
    fun cupcakeNavHost_clickOneCupcake_navigatesToSelectFlavorScreen() {
        composeTestRule.onNodeWithStringId(R.string.one_cupcake) // cari node dengan ID String 1 cupcake
            .performClick() // klik node
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name) // verifikasi tampilan berhasil ke layar flavor
    }

    @Test
    fun cupcakeNavHost_clickNextOnFlavorScreen_navigatesToPickupScreen() { 
        navigateToFlavorScreen() // tampilan pilihan rasa
        composeTestRule.onNodeWithStringId(R.string.next) // mencari tombol next dengan ID String
            .performClick() /// klik next
        navController.assertCurrentRouteName(CupcakeScreen.Pickup.name)
    } // verifikasi tampilan berhasil ke layar pickup

    @Test
    fun cupcakeNavHost_clickBackOnFlavorScreen_navigatesToStartOrderScreen() {
        navigateToFlavorScreen() // tampilan ke flavor
        performNavigateUp() // kembali ke layar sebelumnya
        navController.assertCurrentRouteName(CupcakeScreen.Start.name) // verifikasi tampilan berhasil kembali ke layar start
    }

    @Test
    fun cupcakeNavHost_clickCancelOnFlavorScreen_navigatesToStartOrderScreen() {
        navigateToFlavorScreen() // tampilan ke layar flavor
        composeTestRule.onNodeWithStringId(R.string.cancel) // tombol batal dengan ID String
            .performClick() // klik tombol batal
        navController.assertCurrentRouteName(CupcakeScreen.Start.name) // verifikasi tampilan kembali ke start 
    }

    @Test
    fun cupcakeNavHost_clickNextOnPickupScreen_navigatesToSummaryScreen() {
        navigateToPickupScreen() // tempilan layar pilihan pickup time.
        composeTestRule.onNodeWithText(getFormattedDate()) // cari tanggal di node UI
            .performClick() // klik tanggal
        composeTestRule.onNodeWithStringId(R.string.next) // cari tanggal berikuutnya dengan ID String
            .performClick() // klik tanggal
        navController.assertCurrentRouteName(CupcakeScreen.Summary.name) // verifikasii tampilan berhasil ke summary
    }

    @Test
    fun cupcakeNavHost_clickBackOnPickupScreen_navigatesToFlavorScreen() { 
        navigateToPickupScreen() // tampilan layar pilihan taggal pickup
        performNavigateUp() // tampilan kembali ke layar sebelumnnya
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name) // verifikasi berhasil kembali ke layar flavor
    }

    @Test
    fun cupcakeNavHost_clickCancelOnPickupScreen_navigatesToStartOrderScreen() {
        navigateToPickupScreen() // menampilkan ke layar pickup time.
        composeTestRule.onNodeWithStringId(R.string.cancel) // tombol batal dengan ID String
            .performClick() // klik batal
        navController.assertCurrentRouteName(CupcakeScreen.Start.name) // verifikasi kembali ke layar start
    }

    @Test
    fun cupcakeNavHost_clickCancelOnSummaryScreen_navigatesToStartOrderScreen() {
        navigateToSummaryScreen() // menampilkan ke layer summary
        composeTestRule.onNodeWithStringId(R.string.cancel) // tombol batal dengan ID String
            .performClick() // klik batal
        navController.assertCurrentRouteName(CupcakeScreen.Start.name) // verifikasi tampilan berhasil ke layar start
    }

    private fun navigateToFlavorScreen() {
        composeTestRule.onNodeWithStringId(R.string.one_cupcake) // mencari node 1 cupcake
            .performClick() // klik node
        composeTestRule.onNodeWithStringId(R.string.chocolate) // cari rasa cokklat dengan ID String
            .performClick() // klik coklat
    }

    private fun navigateToPickupScreen() {
        navigateToFlavorScreen() // menampilkan layar flavor
        composeTestRule.onNodeWithStringId(R.string.next) // cari tombol next dengan ID String
            .performClick() // klik next
    }

    private fun navigateToSummaryScreen() {
        navigateToPickupScreen() // menampilkan ke layar pickup time
        composeTestRule.onNodeWithText(getFormattedDate()) // cari tanggal yang di format di node UI
            .performClick() // klik tanggal
        composeTestRule.onNodeWithStringId(R.string.next) //cari tombol next dengan ID String
            .performClick() // klikk next
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button) // tombol kembali
        composeTestRule.onNodeWithContentDescription(backText).performClick() // kembali
    }

    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance() //membuat kalender 
        calendar.add(java.util.Calendar.DATE, 1) // tambah satu hari dari tanggal sekarang
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault()) // memforomat tanggal
        return formatter.format(calendar.time) // mengambil tanggal sebagai string
    }
}
