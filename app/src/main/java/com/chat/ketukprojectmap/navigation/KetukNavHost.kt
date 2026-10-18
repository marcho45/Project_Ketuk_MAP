package com.chat.ketukprojectmap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chat.ketukprojectmap.data.repository.AuthRepository
import com.chat.ketukprojectmap.ui.screens.detail.DetailAlertScreen
import com.chat.ketukprojectmap.ui.screens.home.HomeScreen
import com.chat.ketukprojectmap.ui.screens.login.LoginScreen
import com.chat.ketukprojectmap.ui.screens.notifikasi.NotifikasiScreen
import com.chat.ketukprojectmap.ui.screens.onboarding.OnboardingScreen
import com.chat.ketukprojectmap.ui.screens.register.RegisterScreen
import com.chat.ketukprojectmap.ui.screens.role.RoleSelectScreen
import com.chat.ketukprojectmap.ui.screens.splash.SplashScreen
import com.chat.ketukprojectmap.ui.screens.verification.VerificationScreen
import com.chat.ketukprojectmap.ui.screens.walisetup.CircleKeluargaScreen
import com.chat.ketukprojectmap.ui.screens.walisetup.DetailOmaOpaScreen
import com.chat.ketukprojectmap.ui.screens.walisetup.PilihPenjagaScreen
import com.chat.ketukprojectmap.ui.screens.walisetup.QrWaliScreen
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
                    scope.launch {
                        val role = try {
                            authRepository.getRole()
                        } catch (e: CancellationException) {
                            throw e
                        } catch (_: Exception) {
                            null
                        }
                        navController.navigate(Routes.homeRouteForRole(role)) {
                            popUpTo(Routes.splash) { inclusive = true }
                        }
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
                // Sudah punya role -> Home sesuai role, belum -> pilih role.
                onNavigateToRole = {
                    scope.launch {
                        val role = try {
                            authRepository.getRole()
                        } catch (e: CancellationException) {
                            throw e
                        } catch (_: Exception) {
                            null
                        }
                        val targetRoute = if (role != null) Routes.homeRouteForRole(role) else Routes.role
                        navController.navigate(targetRoute) {
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
                // DIUBAH: Confirm email hidup, kirim email ke layar verifikasi.
                // Register tidak di-pop, jadi user bisa kembali kalau salah ketik email.
                onNeedVerification = { email ->
                    navController.navigate(Routes.verificationWithEmail(email))
                }
            )
        }

        // DIUBAH: route membawa argumen email
        composable(
            route = Routes.verificationPattern,
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            VerificationScreen(
                email = email,
                onConfirm = {
                    navController.navigate(Routes.role) {
                        // Hapus Login, Register, dan Verifikasi dari back stack
                        popUpTo(Routes.login) { inclusive = true }
                    }
                }
                // onResend dihapus: kirim ulang kode sudah ditangani VerificationViewModel
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
                    scope.launch {
                        val role = try {
                            authRepository.getRole()
                        } catch (e: CancellationException) {
                            throw e
                        } catch (_: Exception) {
                            null
                        }
                        navController.navigate(Routes.setupRouteForRole(role)) {
                            popUpTo(Routes.role) { inclusive = true }
                        }
                    }
                }
            )
        }

        // ===== Setup Wali (02. Initial Phase Wali) =====
        composable(Routes.detailOmaOpa) {
            DetailOmaOpaScreen(
                onNavigateBack = {
                    navController.navigate(Routes.role) {
                        popUpTo(Routes.detailOmaOpa) { inclusive = true }
                    }
                },
                onContinue = {
                    navController.navigate(Routes.circleKeluarga)
                }
            )
        }

        composable(Routes.circleKeluarga) {
            CircleKeluargaScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    navController.navigate(Routes.qrWali)
                }
            )
        }

        composable(Routes.qrWali) {
            QrWaliScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPairingComplete = {
                    navController.navigate(Routes.pilihPenjaga)
                }
            )
        }

        composable(Routes.pilihPenjaga) {
            PilihPenjagaScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onGuardianConfirmed = {
                    navController.navigate(Routes.home) {
                        popUpTo(Routes.detailOmaOpa) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.home) {
            HomeScreen(
                onOpenNotifications = {
                    navController.navigate(Routes.notifikasi)
                },
                onOpenDetailAlert = { itemId ->
                    navController.navigate(Routes.detailAlert(itemId))
                },
                // TODO(tim): tombol khusus demo, tidak ada di Figma
                onNavigateBackToRole = {
                    navController.navigate(Routes.role) {
                        popUpTo(Routes.home) { inclusive = true }
                    }
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
                },
                onOpenDetailAlert = { itemId ->
                    navController.navigate(Routes.detailAlert(itemId))
                }
            )
        }

        composable(
            route = Routes.detailAlertPattern,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                    defaultValue = "act_1"
                }
            )
        ) { entry ->
            val itemId = entry.arguments?.getString("itemId").orEmpty().ifBlank { "act_1" }
            DetailAlertScreen(
                itemId = itemId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}