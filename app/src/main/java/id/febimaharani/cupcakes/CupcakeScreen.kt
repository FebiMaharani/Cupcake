package id.febimaharani.cupcakes

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.febimaharani.cupcakes.R
import id.febimaharani.cupcakes.data.DataSource
import id.febimaharani.cupcakes.data.OrderUiState
import id.febimaharani.cupcakes.ui.OrderSummaryScreen
import id.febimaharani.cupcakes.ui.OrderViewModel
import id.febimaharani.cupcakes.ui.SelectOptionScreen
import id.febimaharani.cupcakes.ui.StartOrderScreen

// enum class mendefinisikan start, flavor, pickup dan summary.
enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name), // layar awal
    Flavor(title = R.string.choose_flavor), // layar memilih rasa cupcake
    Pickup(title = R.string.choose_pickup_date), // memilih tanggal order
    Summary(title = R.string.order_summary) // menamiplkan ringkasan pesanan
}

@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen, 
    canNavigateBack: Boolean, // menentukan tampilan tombol kembali
    navigateUp: () -> Unit, // tampilan kembali ke layar sebelumnya
    modifier: Modifier = Modifier 
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) }, // judul layar
        colors = TopAppBarDefaults.mediumTopAppBarColors( 
            containerColor = MaterialTheme.colorScheme.primaryContainer // warna latar belakang layar
        ),
        modifier = modifier, 
        navigationIcon = {
            if (canNavigateBack) { // jika navigasi kembali, menampilkan ikon kembali
                IconButton(onClick = navigateUp) { // tombol tampilan kembali
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, // ikon panah kemebali
                        contentDescription = stringResource(R.string.back_button) // deskripsi konten aksesibilitas
                    )
                }
            }
        }
    )
}

// fungsi utama aplikasi 
@Composable
fun CupcakeApp( // yang terdapat di navigation host
    viewModel: OrderViewModel = viewModel(), // mengambil viewModel untuk mengelola status pesanan
    navController: NavHostController = rememberNavController() // navigation host
) {
   
    val backStackEntry by navController.currentBackStackEntryAsState() // entri back stack
    val currentScreen = CupcakeScreen.valueOf( // mendapatkan nama layar berdasarkan rute tampilan
        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name 
    )

    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen, // menampilkan layar saat ini ke AppBar
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() } // untuk menampilkan kembali ke layar sebelumnya
            )
        }
    ) { innerPadding -> // padding internal
        val uiState by viewModel.uiState.collectAsState() // mengambil status UI dari viewModel

        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.Start.name, // titik tampilan awal 
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding) // mengisi ukuran maks dan memungkinkan scroll vertikal.
        ) {
            composable(route = CupcakeScreen.Start.name) { // rute untuk layar StartOrderScreen
                StartOrderScreen( 
                    quantityOptions = DataSource.quantityOptions, // mengambil data jumlah cupcake
                    onNextButtonClicked = { 
                        viewModel.setQuantity(it) // business logic menyimpan jumlah yang dipilih ke viewModel
                        navController.navigate(CupcakeScreen.Flavor.name) // tampilan layar pilihan rasa setelah klik tombol
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))  // mengisi ukuran maks dan padding min pada StartOrderScreen
                )
            }
            composable(route = CupcakeScreen.Flavor.name) { // pilihan rasa
                val context = LocalContext.current 
                SelectOptionScreen(
                    subtotal = uiState.price, // menampilkan subtotal 
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) }, // tampilan pilihan tanggal pengambilan
                    onCancelButtonClicked = { 
                        cancelOrderAndNavigateToStart(viewModel, navController) 
                    }, // membatalkan pilihan tanggal dan kembali ke tampilan awal
                    options = DataSource.flavors.map { id -> context.resources.getString(id) }, // mengambil daftar rasa
                    onSelectionChanged = { viewModel.setFlavor(it) }, // menyimpan rasa ke viewmodel
                    modifier = Modifier.fillMaxHeight() // mengisi maks tinggi dari SelectOptionScreen
                )
            }
            composable(route = CupcakeScreen.Pickup.name) { // pilihan tanggal pengambilan SelectOptionScreen
                SelectOptionScreen(
                    subtotal = uiState.price, // menampilkan subtotal
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) }, // tampilan ringkasan pesanan
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController) // membatalkan pesanan
                    },
                    options = uiState.pickupOptions, // mengambil opsii tanggal ambil
                    onSelectionChanged = { viewModel.setDate(it) }, //  simpan tanggal ke viewModel saat berubah pilihan
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = CupcakeScreen.Summary.name) { // layar ringkasan pesanan
                val context = LocalContext.current 
                OrderSummaryScreen(
                    orderUiState = uiState, // mengirmkan status pesanan ke OrderSummaryScreen
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController) 
                    }, // batal pesan
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    }, // share pesanan
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

/**
 * Reset [OrderUiState] dan kembali ke [CupcakeScreen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder() // reset status pesanan di viewModel
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false) // kembali ke layar start
} 

private fun shareOrder(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply { // membuat intent ACTION_SEND
        type = "text/plain" // tipe data
        putExtra(Intent.EXTRA_SUBJECT, subject) // menambah subject
        putExtra(Intent.EXTRA_TEXT, summary) // tambah ringkasan
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order) // judul chooser ketika membagikan pesan
        )
    )
}
