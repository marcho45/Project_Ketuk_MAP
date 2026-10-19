package com.chat.ketukprojectmap.ui.screens.guardian

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.BuildConfig
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.ActivityItem
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.ui.theme.ErrorMerah
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota
import kotlinx.coroutines.launch

private data class GuardianBottomNavItem(
    val index: Int,
    val label: String,
    val iconRes: Int
)

/**
 * Screen M10: Alur Penjaga Utama (05. Core Phase Penjaga.png & flowchart.png).
 * Memiliki 3 tab: Beranda (Tab 0), Lokasi (Tab 1), dan Profil Penjaga (Tab 2).
 */
@Composable
fun GuardianHomeScreen(
    viewModel: GuardianViewModel = viewModel(),
    onOpenNotifications: () -> Unit = {},
    onOpenLocationHistory: () -> Unit = {},
    onNavigateBackToRole: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val colors = KetukTheme.colors

    // TODO(tim): tombol khusus demo, tidak ada di Figma
    BackHandler(enabled = BuildConfig.DEMO_MODE && selectedTab == 0) {
        onNavigateBackToRole()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
    ) {
        when (selectedTab) {
            0 -> GuardianHomeContent(
                uiState = uiState,
                onOpenNotifications = onOpenNotifications,
                onNavigateBackToRole = onNavigateBackToRole,
                onConfirmSafe = {
                    viewModel.confirmSafe { message ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                },
                onHelpRequested = {
                    viewModel.requestHelp { message ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                },
                onOpenLocationTab = { selectedTab = 1 },
                onQuickAction = { actionName ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            if (actionName == "Lacak") {
                                "Simulasi Mockup: Membuka navigasi rute ke Rumah Oma Diyas..."
                            } else {
                                "Simulasi Mockup: Menghubungi Nanda Valeri (+62 812 3456 7890)..."
                            }
                        )
                    }
                },
                onHistoryItemClick = { item ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Simulasi Mockup: ${item.title} (${item.timestamp})")
                    }
                }
            )
            1 -> GuardianLokasiScreen(
                onOpenLocationHistory = onOpenLocationHistory,
                snackbarHostState = snackbarHostState
            )
            2 -> GuardianProfileScreen(
                onLogout = onLogout
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )

        BoxWithConstraints(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            GuardianBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}

@Composable
private fun GuardianHomeContent(
    uiState: GuardianHomeUiState,
    onOpenNotifications: () -> Unit,
    onNavigateBackToRole: () -> Unit,
    onConfirmSafe: () -> Unit,
    onHelpRequested: () -> Unit,
    onOpenLocationTab: () -> Unit,
    onQuickAction: (String) -> Unit,
    onHistoryItemClick: (ActivityItem) -> Unit
) {
    val colors = KetukTheme.colors
    val navBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 64.dp + navBarInset + 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Header: Tombol Panah Demo + Sapaan + Lonceng Notifikasi dengan Titik Merah
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO(tim): tombol panah khusus demo, tidak ada di Figma
            if (BuildConfig.DEMO_MODE) {
                IconButton(
                    onClick = onNavigateBackToRole,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Kembali ke Pilih Peran",
                        tint = Terakota
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Halo, Markolintang",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Penjaga terdekat - Oma Diyas",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }

            // Ikon Lonceng Notifikasi dengan Badge Titik Merah
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onOpenNotifications),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.nav_notifikasi),
                    contentDescription = "Notifikasi",
                    modifier = Modifier.size(24.dp)
                )
                if (uiState.unreadNotificationsCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-6).dp, y = 6.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(ErrorMerah)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. Banner Komposit Interaktif: "Tolong ketuk pintu Oma Diyas" + 2 Tombol
        GuardianAlertBannerCard(
            status = uiState.checkStatus,
            onConfirmSafe = onConfirmSafe,
            onHelpRequested = onHelpRequested
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Kartu Mini Peta: "Rumah Oma Diyas - Jl. Syeckh Nawawi Albantani 13"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenLocationTab),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
            border = BorderStroke(1.dp, colors.cardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.peta_lokasi),
                        contentDescription = "Peta Rumah Oma Diyas",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = Terakota.copy(alpha = 0.14f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Home,
                                    contentDescription = null,
                                    tint = Terakota,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Rumah Oma Diyas",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = DummyData.defaultElder.address,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = "Buka Lokasi",
                        tint = Terakota,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. Bagian "Aksi cepat" (Lacak & Hubungi)
        Text(
            text = "Aksi cepat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Kartu Lacak (Terakota Muda)
            GuardianQuickActionCard(
                title = "Lacak",
                icon = Icons.Rounded.Navigation,
                backgroundColor = colors.aksiAktivitas,
                iconColor = Terakota,
                onClick = { onQuickAction("Lacak") },
                modifier = Modifier.weight(1f)
            )

            // Kartu Hubungi (Hijau Muda)
            GuardianQuickActionCard(
                title = "Hubungi",
                icon = Icons.Rounded.Call,
                backgroundColor = colors.aksiHp,
                iconColor = colors.accentGreen,
                onClick = { onQuickAction("Hubungi") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 5. Bagian "Riwayat"
        Text(
            text = "Riwayat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
            border = BorderStroke(1.dp, colors.cardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                uiState.historyItems.forEachIndexed { index, item ->
                    GuardianHistoryRow(
                        item = item,
                        onClick = { onHistoryItemClick(item) }
                    )
                    if (index < uiState.historyItems.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = colors.divider
                        )
                    }
                }
            }
        }
    }
}

/**
 * Banner komposit interaktif permintaan ketuk pintu Oma Diyas.
 * Menempelkan dua tombol "Oma aman" dan "Butuh bantuan" di bagian bawah.
 */
@Composable
private fun GuardianAlertBannerCard(
    status: GuardianCheckStatus,
    onConfirmSafe: () -> Unit,
    onHelpRequested: () -> Unit
) {
    val colors = KetukTheme.colors

    val (cardBg, borderStrokeColor) = when (status) {
        GuardianCheckStatus.IDLE -> Terakota.copy(alpha = 0.12f) to Terakota.copy(alpha = 0.35f)
        GuardianCheckStatus.SAFE_CONFIRMED -> colors.accentGreen.copy(alpha = 0.12f) to colors.accentGreen.copy(alpha = 0.40f)
        GuardianCheckStatus.HELP_REQUESTED -> Terakota.copy(alpha = 0.18f) to Terakota
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderStrokeColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    color = when (status) {
                        GuardianCheckStatus.IDLE -> Terakota.copy(alpha = 0.20f)
                        GuardianCheckStatus.SAFE_CONFIRMED -> colors.accentGreen.copy(alpha = 0.20f)
                        GuardianCheckStatus.HELP_REQUESTED -> Terakota.copy(alpha = 0.25f)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (status) {
                                GuardianCheckStatus.IDLE -> Icons.Rounded.NotificationImportant
                                GuardianCheckStatus.SAFE_CONFIRMED -> Icons.Rounded.CheckCircle
                                GuardianCheckStatus.HELP_REQUESTED -> Icons.Rounded.Warning
                            },
                            contentDescription = null,
                            tint = if (status == GuardianCheckStatus.SAFE_CONFIRMED) colors.accentGreen else Terakota,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when (status) {
                            GuardianCheckStatus.IDLE -> "Tolong ketuk pintu Oma Diyas"
                            GuardianCheckStatus.SAFE_CONFIRMED -> "Pengecekan selesai: Oma Aman"
                            GuardianCheckStatus.HELP_REQUESTED -> "Bantuan darurat dilaporkan"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = when (status) {
                            GuardianCheckStatus.IDLE -> "Oma belum merespons. Bisa bantu cek ke rumahnya?"
                            GuardianCheckStatus.SAFE_CONFIRMED -> "Terima kasih sudah mengecek langsung! Kabar baik telah dikirim ke keluarga."
                            GuardianCheckStatus.HELP_REQUESTED -> "Peringatan darurat telah diteruskan ke keluarga dan pihak terkait."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Baris Tombol Aksi "Oma aman" & "Butuh bantuan"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol "Oma aman" (Hijau Sage)
                Button(
                    onClick = onConfirmSafe,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.chipAman,
                        contentColor = colors.textPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Oma aman",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Tombol "Butuh bantuan" (Merah muda / Terakota muda)
                Button(
                    onClick = onHelpRequested,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.cardWali,
                        contentColor = Terakota
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PriorityHigh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Butuh bantuan",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun GuardianQuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: androidx.compose.ui.graphics.Color,
    iconColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KetukTheme.colors

    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.20f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }
    }
}

@Composable
private fun GuardianHistoryRow(
    item: ActivityItem,
    onClick: () -> Unit
) {
    val colors = KetukTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(colors.accentGreen)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }
        }

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = colors.textSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.GuardianBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val colors = KetukTheme.colors
    val tabs = listOf(
        GuardianBottomNavItem(0, "Beranda", R.drawable.nav_home),
        GuardianBottomNavItem(1, "Lokasi", R.drawable.nav_lokasi),
        GuardianBottomNavItem(2, "Profil", R.drawable.nav_profil)
    )

    val sidePadding = 16.dp
    val pillInset = 4.dp
    val slotWidth = (maxWidth - sidePadding * 2) / tabs.size

    val pillOffset by animateDpAsState(
        targetValue = sidePadding + slotWidth * selectedTab + pillInset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "guardianPillOffset"
    )

    Surface(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(),
        color = colors.barBackground,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = pillOffset)
                    .width(slotWidth - pillInset * 2)
                    .height(44.dp)
                    .align(Alignment.CenterStart)
                    .background(colors.activePill, RoundedCornerShape(50))
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = sidePadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clickable(
                                onClick = { onTabSelected(tab.index) },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(tab.iconRes),
                            contentDescription = tab.label,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Guardian Home - Light", showBackground = true)
@Composable
private fun PreviewGuardianHomeLight() {
    KetukTheme(darkTheme = false) {
        GuardianHomeScreen()
    }
}

@Preview(name = "Guardian Home - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGuardianHomeDark() {
    KetukTheme(darkTheme = true) {
        GuardianHomeScreen()
    }
}
