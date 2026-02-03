package com.example.ceramicsymphony.NavigationScreens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ceramicsymphony.ComposeScreens.AuthorizationScreen
import com.example.ceramicsymphony.ComposeScreens.BottomNavigationScreen
import com.example.ceramicsymphony.ComposeScreens.DeliveryScreen
import com.example.ceramicsymphony.ComposeScreens.DetailsProductScreen
import com.example.ceramicsymphony.ComposeScreens.PresentationScreen
import com.example.ceramicsymphony.ComposeScreens.ProductsScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "presentation_screen"
    ){
        composable("presentation_screen"){
            PresentationScreen(navController = navController)
        }

        composable("authorization_screen"){
            AuthorizationScreen(navController = navController)
        }

        composable("bottom_navigation_screen"){
            BottomNavigationScreen(navController = navController)
        }

        composable("products_screen"){
            ProductsScreen(navController = navController)
        }

        composable("details_product_screen/{productName}"){ backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")
            DetailsProductScreen(navController = navController, name_product = productName)
        }

        composable("delivery_screen"){
            DeliveryScreen()
        }
    }
}
