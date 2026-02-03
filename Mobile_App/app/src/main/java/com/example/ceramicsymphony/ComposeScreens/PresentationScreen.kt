package com.example.ceramicsymphony.ComposeScreens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun PresentationScreen(navController: NavController){
    val getLogoCompanyURL = "http://192.168.31.40:11111/api/getFullLogoCompany"
    val infiniteTransition = rememberInfiniteTransition(label = "glow")

    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )

    Box(
        Modifier.fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = getLogoCompanyURL,
                contentDescription = "FULL LOGO COMPANY",
                Modifier.width(300.dp)
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(300.dp))

            Button(
                onClick = {
                    navController.navigate("authorization_screen")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFd60303).copy(alpha = glowIntensity)
                ),
                modifier = Modifier
                    .width(250.dp)
                    .height(55.dp)
                    .shadow(
                        elevation = (glowIntensity * 16).dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color(0xFFFF4747).copy(alpha = glowIntensity * 0.5f)
                    )
                    .clip(RoundedCornerShape(12.dp)),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "АВТОРИЗОВАТЬСЯ",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
