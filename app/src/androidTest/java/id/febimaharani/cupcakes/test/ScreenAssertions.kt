package id.febimaharani.cupcakes.test

import androidx.navigation.NavController
import org.junit.Assert

// fungsi NavController
fun NavController.assertCurrentRouteName(expectedRouteName: String) { // verifikasi nama rute sekarang
    Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}
