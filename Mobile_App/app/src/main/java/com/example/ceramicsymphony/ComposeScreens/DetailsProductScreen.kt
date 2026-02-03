package com.example.ceramicsymphony.ComposeScreens

import android.graphics.BitmapFactory
import android.telecom.Call.Details
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseDetailsProduct
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseDetailsProductItem
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.DetailsProductState
import com.example.ceramicsymphony.ViewModels.DetailsProductViewModel

@Composable
fun DetailsProductScreen(navController: NavController, name_product: String?){
    val viewModelDetailsProduct: DetailsProductViewModel = viewModel()
    val detailsProductState by viewModelDetailsProduct.detailsProductState.collectAsState()

    LaunchedEffect(name_product) {
        if (!name_product.isNullOrEmpty()){
            viewModelDetailsProduct.getDetailsProduct(name_product)
        }
    }

    Column(
        Modifier.fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        when(detailsProductState){
            is DetailsProductState.Loading -> {
                CircularProgressIndicator(color = Color.Black)
            }

            is DetailsProductState.Success -> {
                val response = (detailsProductState as DetailsProductState.Success).responseDetailsProductItem
                response.data?.firstOrNull()?.let { product ->
                    DetailsProductScreenContent(navController, product)
                } ?: run {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Товар не найден", fontSize = 15.sp, color = Color.Black)

                        Spacer(Modifier.height(10.dp))

                        Button(
                            onClick = { navController.navigateUp() }
                        ) {
                            Text("Назад")
                        }
                    }
                }
            }

            is DetailsProductState.Error -> {
                val error = (detailsProductState as DetailsProductState.Error).message
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ошибка: $error",
                        fontSize = 18.sp,
                        color = Color.Red
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigateUp() }
                    ) {
                        Text("Назад")
                    }
                }
            }

            else -> {
                DetailsProductState.Idle
            }
        }
    }
}

@Composable
fun DetailsProductScreenContent(
    navController: NavController,
    product: ResponseDetailsProduct
){
    Column(
        Modifier.padding(10.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(50.dp))

        Base64ImageForDetailsProduct(
            base64String = product.image,
            contentDescription = "Картинка плитки",
            modifier = Modifier.size(300.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(30.dp))

        Text(
            text = product.name_products ?: "Ниезвестная плитка",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            Modifier.background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = product.name_categories ?: "Неизвестная категория плитки",
                color = Color(0xFFFF7437),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = "|",
                color = Color.Black,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = product.name_manufacturer ?: "Неизвестный производитель",
                color = Color(0xFFFF7437),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(5.dp))

        Text(
            text = product.description ?: "нет описания",
            color = Color.Black,
            fontSize = 15.sp
        )

        Spacer(Modifier.height(25.dp))

        Column(
            Modifier.height(180.dp)
                .fillMaxWidth()
                .border(
                    1.dp,
                    color = Color.Black,
                    RoundedCornerShape(20.dp)
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ИНФОРМАЦИЯ О ПРОДУКТЕ:", fontSize = 20.sp, style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(20.dp))

            Row{
                Column {
                    Text("Артикль: ${product.article}")

                    Spacer(Modifier.height(10.dp))

                    Text("Размер: ${product.size}")

                    Spacer(Modifier.height(10.dp))

                    Text("Измеряймость: ${product.unit}")
                }

                Spacer(Modifier.width(65.dp))

                Column {
                    Text("Цвет: ${product.color}")

                    Spacer(Modifier.height(10.dp))

                    Text("Кол. на складе: ${if (product.quantity != null) product.quantity else 0}")
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        Row(
            Modifier.padding(1.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text("НАЗАД", fontSize = 15.sp, color = Color.White)
            }

            Spacer(Modifier.width(20.dp))

            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (product.status_ == "ЕСТЬ НА СКЛАДЕ") Color.Black else Color.Red
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${product.status_}", fontSize = 15.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun Base64ImageForDetailsProduct(
    base64String: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(base64String) {
        isLoading = true
        error = null
        imageBitmap = null

        if (!base64String.isNullOrEmpty()) {
            try {
                val cleanBase64 = if (base64String.startsWith("data:") && base64String.contains(",")) {
                    base64String.substring(base64String.indexOf(",") + 1)
                } else {
                    base64String
                }

                val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

                if (decodedBytes.isNotEmpty()) {
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                    if (bitmap != null) {
                        imageBitmap = bitmap
                    } else {
                        error = "Не удалось создать изображение"
                    }
                } else {
                    error = "Пустые данные изображения"
                }
            } catch (e: Exception) {
                error = "Ошибка загрузки изображения"
            }
        } else {
            error = "Нет данных изображения"
        }

        isLoading = false
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Gray,
                    strokeWidth = 2.dp
                )
            }
            imageBitmap != null -> {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            error != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = "Ошибка",
                        color = Color.Red,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                Text(
                    text = "Нет фото",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
