package com.example.ceramicsymphony.ComposeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseDeliveryScreenGoodsDelivery
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.GoodsDeliveryState
import com.example.ceramicsymphony.ViewModels.DeliveryScreenViewModel

@Composable
fun DeliveryScreen(navController: NavController? = null, innerNavController: NavController? = null) {

    val viewModelGoodsDelivery: DeliveryScreenViewModel = viewModel()
    val goodsDeliveryState by viewModelGoodsDelivery.goodsDeliveryState.collectAsState()

    LaunchedEffect(Unit) {
        viewModelGoodsDelivery.getAllGoodsDelivery()
    }

    Box(
        Modifier.fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.padding(5.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Ceramic Symphony Warehouses",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "ПРЕДСТОЯЩИЕ ПОСТАВКИ",
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            when(goodsDeliveryState){
                is GoodsDeliveryState.Loading -> {
                    CircularProgressIndicator(color = Color.Black)
                }

                is GoodsDeliveryState.Success -> {
                    val response = (goodsDeliveryState as GoodsDeliveryState.Success).responseDeliveryScreenGoodsDeliveryItem
                    val deliveries = response.data ?: emptyList()
                    if (deliveries.isEmpty()){
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Нет данных о поставках",
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                    }else{
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(deliveries) {delivery ->
                                CardGoodsDelivery(
                                    name_product = delivery.name_products,
                                    article = delivery.article,
                                    date_delivery = delivery.date_delivery,
                                    document = delivery.document,
                                    comment = delivery.comment,
                                    name_manufacturer = delivery.name_manufacturer
                                )
                            }
                        }
                    }
                }

                else -> {
                    GoodsDeliveryState.Idle
                }
            }
        }
    }
}

@Composable
fun CardGoodsDelivery(
    name_product: String?,
    article: String?,
    date_delivery: String?,
    document: String?,
    comment: String?,
    name_manufacturer: String?
){
    Card(
        Modifier.fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(
            Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "${document}",
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Row {
                Text(
                    text = "${name_product}",
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

                Spacer(Modifier.width(10.dp))

                Text("|", color = Color.Black)

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "${name_manufacturer}",
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }

            Text(
                text = "${article}",
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )

            Text(
                text = "${comment}",
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )

            Text(
                text = "${date_delivery}",
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFFF7437),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
