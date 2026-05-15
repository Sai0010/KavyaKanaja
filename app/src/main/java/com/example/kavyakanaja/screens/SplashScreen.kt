// screens/SplashScreen.kt

package com.example.kavyakanaja.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kavyakanaja.navigation.Screen
import com.example.kavyakanaja.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Fade in animation
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "splash_alpha"
    )

    // Auto navigate to Home after 2.5 seconds
    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // App icon placeholder — a simple decorative symbol
            Text(
                text = "📜",
                fontSize = 64.sp,
                modifier = Modifier
                    .alpha(alpha)
                    .padding(bottom = 24.dp)
            )

            // App name in Kannada
            Text(
                text = "ಕಾವ್ಯ ಕಣಜ",
                fontFamily = SerifFamily,
                fontSize = 32.sp,
                color = WarmBrown,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha)
            )

            // App name in English
            Text(
                text = "Kavya-Kanaja",
                fontFamily = SansFamily,
                fontSize = 14.sp,
                color = MutedText,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(alpha)
                    .padding(top = 8.dp)
            )

            // Tagline
            Text(
                text = "Poetry Granary of Karnataka",
                fontFamily = SerifFamily,
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                color = HintText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(alpha)
                    .padding(top = 6.dp)
            )
        }

        // Bottom credit text
        Text(
            text = "Internship Project · 2026",
            fontFamily = SansFamily,
            fontSize = 10.sp,
            color = HintText,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(alpha)
                .padding(bottom = 32.dp)
        )
    }
}