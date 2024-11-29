package id.febimaharani.cupcakes.data

// kelas yang menjelaskan status UI quantity, flavor, dateOptions, date, dan price
data class OrderUiState(
    // banyak cupcake (1, 6, 12) */
    val quantity: Int = 0,
    // rasa cupcake 
    val flavor: String = "",
    // tanggal pickup
    val date: String = "",
    // total pesanan
    val price: String = "",
    // daftar tanggal pickup
    val pickupOptions: List<String> = listOf()
)
