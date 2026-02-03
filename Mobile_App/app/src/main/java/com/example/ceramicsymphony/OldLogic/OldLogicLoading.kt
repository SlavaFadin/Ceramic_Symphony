package com.example.ceramicsymphony.OldLogic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ceramicsymphony.ComposeScreens.CardTile
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInWarehousesState
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductsState

//fun OldLogicLoadingCard(){
//    if (!filterWarehouses){
//        when(productsState){
//            is ProductsState.Loading -> {
//                Box(
//                    Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = Color.Black)
//                }
//            }
//
//            is ProductsState.Error -> {
//                val errorMessage = (productsState as ProductsState.Error).message
//                Box(
//                    Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "error",
//                            tint = Color.Red,
//                            modifier = Modifier.size(40.dp)
//                        )
//
//                        Spacer(Modifier.height(15.dp))
//
//                        Text(
//                            text = errorMessage,
//                            color = Color.Red,
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                }
//            }
//
//            is ProductsState.Success -> {
//                val response = (productsState as ProductsState.Success).responseAllProducts
//                val products = response.data ?: emptyList()
//                val filteredProducts = if (searchText.isNotEmpty()){
//                    products.filter {product ->
//                        product.name_products?.contains(searchText, ignoreCase = true) == true || product.article?.contains(searchText, ignoreCase = true) == true
//                    }
//                }else {
//                    products
//                }
//
//                if (filteredProducts.isEmpty()){
//                    Box(
//                        Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = if (searchText.isNotEmpty()) "NOT FOUND" else "NO PRODUCTS",
//                            color = Color.Black
//                        )
//                    }
//                }else {
//                    LazyColumn(
//                        Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.spacedBy(10.dp)
//                    ) {
//                        items(filteredProducts){product ->
//                            CardTile(
//                                name_product = product.name_products,
//                                article = product.article,
//                                size = product.size,
//                                image = product.image ?: ""
//                            )
//                        }
//                    }
//                }
//            }
//
//            else -> {
//                ProductsState.Idle
//            }
//        }
//    }else{
//        when(productInWarehousesState){
//            is ProductInWarehousesState.Loading -> {
//                Box(
//                    Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = Color.Black)
//                }
//            }
//
//            is ProductInWarehousesState.Success -> {
//                val response = (productInWarehousesState as ProductInWarehousesState.Success).responseProductInWarehousesItem
//                val productsInWarehouses = response.data ?: emptyList()
//                val filteredProductsInWarehouses = if (searchText.isNotEmpty()) {
//                    productsInWarehouses.filter { product ->
//                        product.name_products?.contains(searchText, ignoreCase = true) == true
//                    }
//                }else{
//                    productsInWarehouses
//                }
//
//                if (filteredProductsInWarehouses.isEmpty()){
//                    Box(
//                        Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = if (searchText.isNotEmpty()) "NOT FOUND" else "NO PRODUCTS",
//                            color = Color.Black
//                        )
//                    }
//                }else{
//                    LazyColumn(
//                        Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.spacedBy(10.dp)
//                    ) {
//                        items(filteredProductsInWarehouses){product ->
//                            CardTile(
//                                name_product = product.name_products,
//                                article = product.article,
//                                size = product.size,
//                                image = product.image ?: ""
//                            )
//                        }
//                    }
//                }
//            }
//
//            is ProductInWarehousesState.Error -> {
//                val errorMessage = (productInWarehousesState as ProductInWarehousesState.Error).message
//                Box(
//                    Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "error",
//                            tint = Color.Red,
//                            modifier = Modifier.size(40.dp)
//                        )
//
//                        Spacer(Modifier.height(15.dp))
//
//                        Text(
//                            text = errorMessage,
//                            color = Color.Red,
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                }
//            }
//
//            else -> {
//                ProductInWarehousesState.Idle
//            }
//        }
//    }
//}