package com.example.ceramicsymphony.ComposeScreens

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.placeholder
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ProductDisplayItem
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseAllProducts
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInCategoriesInWarehousesState
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInFilterCategoriesState
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInWarehousesState
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductsState
import com.example.ceramicsymphony.ViewModels.ProductScreenViewModel
import com.example.ceramicsymphony.ViewModels.ProductScreenViewModelFilterCategories
import com.example.ceramicsymphony.ViewModels.ProductScreenViewModelFilterCategoriesInWarehouses
import com.example.ceramicsymphony.ViewModels.ProductScreenViewModelFilterWarehouses

@Composable
fun ProductsScreen(navController: NavController? = null, innerNavController: NavController? = null){
    var searchText by remember { mutableStateOf("") }
    var filterWarehouses by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val viewModelAllProducts: ProductScreenViewModel = viewModel()
    val productsState by viewModelAllProducts.productsState.collectAsState()

    val viewModelProductsInWarehouses: ProductScreenViewModelFilterWarehouses = viewModel()
    val productInWarehousesState by viewModelProductsInWarehouses.productInWarehousesState.collectAsState()

    val viewModelProductInFilterCategoriesState: ProductScreenViewModelFilterCategories = viewModel()
    val productsInFilterCategories by viewModelProductInFilterCategoriesState.productInCategoriesState.collectAsState()

    val viewModelProductInCategoriesInWarehousesState: ProductScreenViewModelFilterCategoriesInWarehouses = viewModel()
    val productsInFilterCategoriesInWarehouses by viewModelProductInCategoriesInWarehousesState.productScreenViewModelFilterCategoriesInWarehouses.collectAsState()

    var selectedItem by remember { mutableStateOf("Все категории") }
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf(
        "Все категории",
        "Керамогранит",
        "Керамическая",
        "Тротуарная",
        "Мозаика"
    )

    LaunchedEffect(Unit) {
        viewModelAllProducts.getAllProducts()
    }

    LaunchedEffect(filterWarehouses) {
        if (filterWarehouses){
            viewModelProductsInWarehouses.getAllProductInWarehouses()
        }
    }

    LaunchedEffect(selectedItem) {
        if (selectedItem != "Все категории"){
            viewModelProductInFilterCategoriesState.getAllProductInFilterCategories(selectedItem)
        }else{
            viewModelAllProducts.getAllProducts()
        }
    }

    LaunchedEffect(filterWarehouses, selectedItem) {
        if (filterWarehouses && selectedItem != "Все категории"){
            viewModelProductInCategoriesInWarehousesState.getAllProductInFilterCategoriesInWarehouses(selectedItem)
        }
    }

    val currentProducts = when {
        filterWarehouses && selectedItem != "Все категории" -> {
            when (productsInFilterCategoriesInWarehouses) {
                is ProductInCategoriesInWarehousesState.Success -> {
                    val data = (productsInFilterCategoriesInWarehouses as ProductInCategoriesInWarehousesState.Success)
                        .responseProductInCategoriesInWarehousesItem.data ?: emptyList()
                    data.map { product ->
                        ProductDisplayItem(
                            name_products = product.name_products,
                            article = product.article,
                            size = product.size,
                            image = product.image
                        )
                    }
                }

                else -> emptyList()
            }
        }

        filterWarehouses -> {
            when (productInWarehousesState){
                is ProductInWarehousesState.Success -> {
                    val data = (productInWarehousesState as ProductInWarehousesState.Success)
                        .responseProductInWarehousesItem.data ?: emptyList()
                    data.map { product ->
                        ProductDisplayItem(
                            name_products = product.name_products,
                            article = product.article,
                            size = product.size,
                            image = product.image
                        )
                    }
                }

                else -> emptyList()
            }
        }

        selectedItem != "Все категории" -> {
            when (productsInFilterCategories) {
                is ProductInFilterCategoriesState.Success -> {
                    val data = (productsInFilterCategories as ProductInFilterCategoriesState.Success)
                        .responseProductInFilterCategoriesItem.data ?: emptyList()
                    data.map { product ->
                        ProductDisplayItem(
                            name_products = product.name_products,
                            article = product.article,
                            size = product.size,
                            image = product.image
                        )
                    }
                }
                else -> emptyList()
            }
        }
        else -> {
            when (productsState) {
                is ProductsState.Success -> {
                    val data = (productsState as ProductsState.Success)
                        .responseAllProducts.data ?: emptyList()
                    data.map { product ->
                        ProductDisplayItem(
                            name_products = product.name_products,
                            article = product.article,
                            size = product.size,
                            image = product.image
                        )
                    }
                }
                else -> emptyList()
            }
        }
    }

    val isLoading = when {
        filterWarehouses && selectedItem != "Все категории" -> {
            productsInFilterCategoriesInWarehouses is ProductInCategoriesInWarehousesState.Loading
        }
        filterWarehouses -> productInWarehousesState is ProductInWarehousesState.Loading
        selectedItem != "Все категории" -> productsInFilterCategories is ProductInFilterCategoriesState.Loading
        else -> productsState is ProductsState.Loading
    }

    val currentError = when {
        filterWarehouses && selectedItem != "Все категории" -> {
            if (productsInFilterCategoriesInWarehouses is ProductInCategoriesInWarehousesState.Error){
                (productsInFilterCategoriesInWarehouses as ProductInCategoriesInWarehousesState.Error).message
            }else null
        }
        filterWarehouses -> {
            if (productInWarehousesState is ProductInWarehousesState.Error) {
                (productInWarehousesState as ProductInWarehousesState.Error).message
            } else null
        }
        selectedItem != "Все категории" -> {
            if (productsInFilterCategories is ProductInFilterCategoriesState.Error) {
                (productsInFilterCategories as ProductInFilterCategoriesState.Error).message
            } else null
        }
        else -> {
            if (productsState is ProductsState.Error) {
                (productsState as ProductsState.Error).message
            } else null
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            Modifier.padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Ceramic Symphony Warehouses",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(5.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = {newText ->
                    searchText = newText
                },
                Modifier.fillMaxWidth(),
                label = {
                    Text("Поиск")
                },
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF7437),
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color(0xFFFF7437),
                    unfocusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "CLEAR"
                            )
                        }
                    }
                }
            )

            Spacer(Modifier.height(1.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Товары на складе",
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .clickable { filterWarehouses = !filterWarehouses }
                )

                Spacer(Modifier.width(10.dp))

                Checkbox(
                    checked = filterWarehouses,
                    onCheckedChange = {filterWarehouses = it},
                    Modifier
                        .width(10.dp)
                        .height(10.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFFF7437),
                        disabledCheckedColor = Color(0xFFFF7437),
                        disabledUncheckedColor = Color(0xFFFF7437)
                    )
                )

                Spacer(Modifier.width(70.dp))

                Box(
                    Modifier.wrapContentHeight()
                        .width(150.dp)
                        .clickable { expanded = true }
                        .border(
                            1.dp,
                            Color.Black,
                            RoundedCornerShape(5.dp)
                        )
                        .padding(
                            horizontal = 5.dp,
                            vertical = 3.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedItem,
                        fontSize = 15.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        Modifier.fillMaxWidth(0.5f)
                    ) {
                        categories.forEach{ category ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = category, textAlign = TextAlign.Center)
                                },
                                onClick = {
                                    selectedItem = category
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(1.dp))

            when{
                isLoading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }

                currentError != null -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "error",
                                tint = Color.Red,
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(Modifier.height(15.dp))

                            Text(
                                text = currentError,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                else ->{
                    val filteredProducts = if (searchText.isNotEmpty()) {
                        currentProducts.filter { product ->
                            product.name_products?.contains(searchText, ignoreCase = true) == true ||
                                    product.article?.contains(searchText, ignoreCase = true) == true
                        }
                    } else {
                        currentProducts
                    }

                    if (filteredProducts.isEmpty()) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchText.isNotEmpty())
                                    "Товары не найдены"
                                else
                                    "Нет товаров для отображения",
                                color = Color.Black
                            )
                        }
                    } else {
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredProducts){product ->
                                CardTile(
                                    name_product = product.name_products,
                                    article = product.article,
                                    size = product.size,
                                    image = product.image ?: "",
                                    navController = navController ?: innerNavController ?: rememberNavController()
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun CardTile(
    name_product: String?,
    article: String?,
    size: String?,
    image: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
            .clickable {
                name_product?.let { productName ->
                    navController.navigate("details_product_screen/$productName")
                }
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Base64Image(
                base64String = image,
                contentDescription = name_product,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name_product ?: "Без названия",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Арт: ${article ?: "Нет артикула"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                if (!size.isNullOrEmpty()) {
                    Text(
                        text = "Размер: ${size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun Base64Image(
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
