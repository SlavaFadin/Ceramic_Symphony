package com.example.ceramicsymphony.BottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SealedClassForScreenNavigation(
    val route: String,
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    object ProductScreen: SealedClassForScreenNavigation("product_screen","Плитка", Icons.Default.Home, Icons.Default.Home)
    object DeliveryScreen: SealedClassForScreenNavigation("delivery_screen","Поставки", Icons.Default.ShoppingCart, Icons.Default.ShoppingCart)

    companion object{
        val screens = listOf(
            ProductScreen,
            DeliveryScreen,
        )
    }
}
