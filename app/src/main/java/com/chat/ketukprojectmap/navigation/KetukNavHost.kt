package com.chat.ketukprojectmap.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chat.ketukprojectmap.ui.screens.onboarding.OnboardingScreen
import com.chat.ketukprojectmap.ui.screens.splash.SplashScreen

@Composable
fun KetukNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.splash
    ) {
        composable(Routes.splash) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Routes.onboarding) {
                        popUpTo(Routes.splash) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.onboarding) {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.login) {
                        popUpTo(Routes.onboarding) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.login) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        composable(Routes.role) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Role",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        composable(Routes.home) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
