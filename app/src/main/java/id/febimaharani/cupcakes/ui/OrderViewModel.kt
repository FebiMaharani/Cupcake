package id.febimaharani.cupcakes.ui

import androidx.lifecycle.ViewModel
import id.febimaharani.cupcakes.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// harga 1 cupcake
private const val PRICE_PER_CUPCAKE = 2.00

// biaya tambahan pickup hari yang sama
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

// menyimpan informasi cupccake untuk jumlah, rasa dan pickup. dan hitung total harga berdasarkan detail pesanan.
class OrderViewModel : ViewModel() {

    // status pesanan
    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions())) // status UI dengan opsi pickup
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow() // akses ke status UI sebagai StateFlow

    // mengatur jumlah [numberCupcakes] cupcake untuk status pesanan dan update harga
    fun setQuantity(numberCupcakes: Int) {
        _uiState.update { currentState -> // update status UI
            currentState.copy(
                quantity = numberCupcakes, // update jumlah 
                price = calculatePrice(quantity = numberCupcakes) // hitung dan update harga baru
            )
        }
    }

    // mengatur [desiredFlavor] untuk status pesanan hanya 1 rasa
    fun setFlavor(desiredFlavor: String) {
        _uiState.update { currentState -> // update status UI
            currentState.copy(flavor = desiredFlavor) // Update flavor
        }
    }

    // mengatur pickup time dan perbarui harga
    fun setDate(pickupDate: String) {
        _uiState.update { currentState -> // update status UI
            currentState.copy(
                date = pickupDate, // update tanggal pickup
                price = calculatePrice(pickupDate = pickupDate) // hitung dan update harga berdasarkan tanggal
            )
        }
    }

    // perbarui status pesan
    fun resetOrder() {
        _uiState.value = OrderUiState(pickupOptions = pickupOptions()) 
    }

    // mengembalikan harga berdasarkan detail order
    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity, // ambil jumlah dari status UI 
        pickupDate: String = _uiState.value.date // ambil tanggal dari status UI
    ): String {
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE // hitung harga
        // jika ambil hari ini
        if (pickupOptions()[0] == pickupDate) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP // tambahan biaya
        }
        val formattedPrice = NumberFormat.getCurrencyInstance().format(calculatedPrice) // format harga jadi dolar
        return formattedPrice
    }

    // mengembalikan daftar opsi ke 3 tanggal berikutnya
    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault()) // format tanggal yang diinginkan
        val calendar = Calendar.getInstance() // kalender menambahkan 3 tanggal berikutnya
        repeat(4) {
            dateOptions.add(formatter.format(calendar.time)) // tambah tanggal sekarang
            calendar.add(Calendar.DATE, 1) // tambah 1 hari
        }
        return dateOptions // memngembalikan daftar opsi tanggal 
    }
}
