package com.chat.ketukprojectmap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chat.ketukprojectmap.data.repository.AuthRepository
import com.chat.ketukprojectmap.ui.screens.home.HomeScreen
import com.chat.ketukprojectmap.ui.screens.login.LoginScreen
import com.chat.ketukprojectmap.ui.screens.notifikasi.NotifikasiScreen
import com.chat.ketukprojectmap.ui.screens.onboarding.OnboardingScreen
import com.chat.ketukprojectmap.ui.screens.register.RegisterScreen
import com.chat.ketukprojectmap.ui.screens.role.RoleSelectScreen
import com.chat.ketukprojectmap.ui.screens.splash.SplashScreen
import com.chat.ketukprojectmap.ui.screens.verification.VerificationScreen
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun KetukNavHost() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository() }

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
                },
                onNavigateToRole = {
                    navController.navigate(Routes.role) {
                        popUpTo(Routes.splash) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.home) {
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
            LoginScreen(
                // Dipanggil setelah login (email atau Google) sukses.
                // Sudah punya role -> Home, belum -> pilih role.
                onNavigateToRole = {
                    scope.launch {
                        val hasRole = try {
                            authRepository.getRole() != null
                        } catch (e: CancellationException) {
                            throw e
                        } catch (_: Exception) {
                            false
                        }
                        navController.navigate(if (hasRole) Routes.home else Routes.role) {
                            popUpTo(Routes.login) { inclusive = true }
                        }
                    }
                },
                onNavigateToVerification = { navController.navigate(Routes.verification) },
                onNavigateToRegister = { navController.navigate(Routes.register) },
                onNavigateBack = {
                    navController.navigate(Routes.onboarding) {
                        popUpTo(Routes.login) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.register) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                // Confirm email mati: akun langsung aktif, user baru belum punya role
                onRegisterSuccess = {
                    navController.navigate(Routes.role) {
                        popUpTo(Routes.login) { inclusive = true }
                    }
                },
                // Confirm email hidup: harus verifikasi dulu
                onNeedVerification = {
                    navController.navigate(Routes.verification) {
                        popUpTo(Routes.register) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.verification) {
            VerificationScreen(
                onConfirm = {
                    navController.navigate(Routes.role) {
                        // Hapus Login dan Verifikasi dari back stack
                        popUpTo(Routes.login) { inclusive = true }
                    }
                },
                onResend = {
                    // TODO: kirim ulang kode verifikasi
                }
            )
        }

        composable(Routes.role) {
            RoleSelectScreen(
                onNavigateBack = {
                    navController.navigate(Routes.login) {
                        popUpTo(Routes.role) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.home) {
                        popUpTo(Routes.role) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.home) {
            HomeScreen(
                onOpenNotifications = {
                    navController.navigate(Routes.notifikasi)
                },
                onLogout = {
                    navController.navigate(Routes.login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.notifikasi) {
            NotifikasiScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}