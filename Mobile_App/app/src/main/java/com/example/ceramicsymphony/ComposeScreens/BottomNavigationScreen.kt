package com.example.ceramicsymphony.ComposeScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ceramicsymphony.BottomNavigation.SealedClassForScreenNavigation

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationScreen(navController: NavController){
    val innerNavController = rememberNavController()

    Box(
        Modifier
            .background(Color.White),
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationView(
                    navController = innerNavController,
                    screens = listOf(
                        SealedClassForScreenNavigation.ProductScreen,
                        SealedClassForScreenNavigation.DeliveryScreen
                    ),
                    onItemClick = {screen ->
                        innerNavController.navigate(screen.route){
                            popUpTo(innerNavController.graph.startDestinationId){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ){innerPadding ->
            Box(
                Modifier.padding(innerPadding)
            ) {
                NavHost(
                    navController = innerNavController,
                    startDestination = "product_screen"
                ){
                    composable("product_screen"){
                        ProductsScreen(
                            navController = navController,
                            innerNavController = innerNavController
                        )
                    }

                    composable("delivery_screen"){
                        DeliveryScreen(
                            navController = navController,
                            innerNavController = innerNavController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlipIcon(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    contentDescription: String
){
    val animationRotation by animateFloatAsState(
        targetValue = if (isActive) 180f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioNoBouncy
        )
    )

    Box(
        Modifier.graphicsLayer { rotationY = animationRotation },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            rememberVectorPainter(image = if (animationRotation > 90f) activeIcon else inactiveIcon),
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    screen: SealedClassForScreenNavigation,
    isSelected: Boolean
){
    val animateHeight by animateDpAsState(
        targetValue = if (isSelected) 36.dp else 26.dp
    )
    val animatedElevation by animateDpAsState(
        targetValue = if (isSelected) 15.dp else 0.dp
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f
    )
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 20.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioNoBouncy
        )
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Row(
            Modifier
                .height(animateHeight)
                .shadow(
                    elevation = animatedElevation,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color(0xFFFF7437),
                    spotColor = Color(0xFFFF7437)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            FlipIcon(
                isActive = isSelected,
                activeIcon = screen.activeIcon,
                inactiveIcon = screen.inactiveIcon,
                contentDescription = "Icons",
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 11.dp)
                    .alpha(animatedAlpha)
                    .size(animatedIconSize)
            )

            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = screen.title,
                    modifier = Modifier.padding(start = 8.dp, end = 10.dp),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun BottomNavigationView(
    screens: List<SealedClassForScreenNavigation>,
    navController: NavController,
    onItemClick: (SealedClassForScreenNavigation) -> Unit
) {
    //var selectedScreen by remember { mutableStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        Modifier.shadow(5.dp)
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .height(64.dp)
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ){
        Row(
            Modifier.fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach{screen ->
                val isSelected = currentRoute == screen.route
                val animateWeight by animateFloatAsState(
                    targetValue = if (isSelected) 3f else 1f
                )

                Box(
                    Modifier.weight(animateWeight),
                    contentAlignment = Alignment.Center
                ){
                    val interactionSource = remember { MutableInteractionSource() }
                    BottomNavigationItem(
                        screen = screen,
                        isSelected = isSelected,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ){
                            onItemClick(screen)
                        }
                    )
                }
            }
        }
    }
}
