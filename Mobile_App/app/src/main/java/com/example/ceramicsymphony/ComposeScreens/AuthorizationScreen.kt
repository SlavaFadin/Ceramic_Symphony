package com.example.ceramicsymphony.ComposeScreens

import android.content.Context
import android.graphics.Insets.add
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import com.example.ceramicsymphony.R
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.AuthorizationState
import com.example.ceramicsymphony.ViewModels.AuthorizationScreenViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun AuthorizationScreen(navController: NavController) {
    var loginUser by remember { mutableStateOf("") }
    var passwordUser by remember { mutableStateOf("") }
    val context = LocalContext.current
    val viewModel: AuthorizationScreenViewModel = viewModel()
    val authorizationState by viewModel.authorizationState.collectAsState()
    val logoUrl = "http://192.168.31.40:11111/api/getLogoCompany"
    val gifURL = "http://192.168.31.40:11111/api/getGifTile"

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF7437),
            Color(0xFFFF8D60),
            Color(0xFFFFFFFF),
            Color(0xFFFF8D60),
            Color(0xFFFF7437)
        )
    )

    val gifAnimationLoader = ImageLoader.Builder(context)
        .components{
            add(GifDecoder.Factory())
        }.build()

    LaunchedEffect(authorizationState) {
        when(val state = authorizationState){
            is AuthorizationState.Success -> {
                Toast.makeText(context, "USER: ${state.responseUserAuthorization.name_user}!", Toast.LENGTH_SHORT).show()
                navController.navigate("bottom_navigation_screen"){
                    popUpTo("authorization_screen") {inclusive = true}
                }
            }
            is AuthorizationState.Error -> {
                Toast.makeText(context, "USER: ${state.message}!", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Box(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .background(Color.White, RoundedCornerShape(50.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = logoUrl,
                contentDescription = "LOGO COMPANY",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50.dp)),
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(35.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "АВТОРИЗАЦИЯ",
                color = Color(0xFFFF7437),
                fontSize = 30.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(25.dp))

            AsyncImage(
                model = gifURL,
                contentDescription = "GIF COMPANY",
                imageLoader = gifAnimationLoader,
                modifier = Modifier.width(100.dp)
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = loginUser,
                onValueChange = { loginUser = it },
                label = { Text("ЛОГИН:") },
                enabled = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFd60303),
                    unfocusedBorderColor = Color(0xFFFF7437),
                    focusedLabelColor = Color(0xFFd60303),
                    unfocusedLabelColor = Color(0xFFFF7437),
                    cursorColor = Color(0xFFd60303),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = passwordUser,
                onValueChange = { passwordUser = it },
                label = { Text("ПАРОЛЬ:") },
                enabled = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFd60303),
                    unfocusedBorderColor = Color(0xFFFF7437),
                    focusedLabelColor = Color(0xFFd60303),
                    unfocusedLabelColor = Color(0xFFFF7437),
                    cursorColor = Color(0xFFd60303),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(3.dp))

            Button(
                onClick = {
                    rememberPassword(context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text("Забыли пароль?", color = Color(0xFFFF7437))
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (loginUser.isNotEmpty() && passwordUser.isNotEmpty()){
                        viewModel.authorizationUser(loginUser, passwordUser)
                    }else{
                        Toast.makeText(context, "ЗАПОЛНИТЕ ВСЕ ПОЛЯ", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7437)
                ),
                modifier = Modifier.width(250.dp),
                enabled = authorizationState !is AuthorizationState.Loading
            ) {
                if (authorizationState is AuthorizationState.Loading){
                    CircularProgressIndicator(
                        Modifier.size(20.dp),
                        color = Color.Black,
                        strokeWidth = 3.dp
                    )
                }else{
                    Text("ВОЙТИ", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

fun rememberPassword(context: Context) {
    Toast.makeText(
        context,
        "ОБРАТИТЕСЬ К АДМИНИСТРАТОРУ СКЛАДА",
        Toast.LENGTH_LONG
    ).show()
}
