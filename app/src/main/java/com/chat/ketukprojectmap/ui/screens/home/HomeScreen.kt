package com.chat.ketukprojectmap.ui.screens.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.BuildConfig
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.model.*
import com.chat.ketukprojectmap.ui.screens.riwayat.RiwayatScreen
import com.chat.ketukprojectmap.ui.screens.lokasi.LokasiScreen
import com.chat.ketukprojectmap.ui.screens.profile.ProfileScreen
import com.chat.ketukprojectmap.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onOpenNotifications: () -> Unit = {},
    onOpenDetailAlert: (String) -> Unit = {},
    onNavigateBackToRole: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val homeData by viewModel.homeData.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var contentVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val colors = KetukTheme.colors

    // TODO(tim): tombol khusus demo, tidak ada di Figma
    BackHandler(enabled = BuildConfig.DEMO_MODE && selectedTab == 0) {
        onNavigateBackToRole()
    }

    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        contentVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
    ) {
        when (selectedTab) {
            0 -> HomeContent(
                homeData = homeData,
                contentVisible = contentVisible,
                onOpenNotifications = onOpenNotifications,
                onOpenDetailAlert = onOpenDetailAlert,
                onNavigateBackToRole = onNavigateBackToRole,
                onQuickActionClick = { action ->
                    scope.launch {
                        snackbarHostState.showSnackbar("Simulasi Mockup: Aksi cepat ${action.title}")
                    }
                },
                onSwitchToTab = { tab -> selectedTab = tab }
            )
            1 -> RiwayatScreen(
                onItemClick = onOpenDetailAlert
            )
            2 -> LokasiScreen()
            3 -> ProfileScreen(onLogout = onLogout)
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
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}

@Composable
fun HomeContent(
    homeData: HomeData,
    contentVisible: Boolean,
    onOpenNotifications: () -> Unit,
    onOpenDetailAlert: (String) -> Unit = {},
    onNavigateBackToRole: () -> Unit = {},
    onQuickActionClick: (QuickAction) -> Unit = {},
    onSwitchToTab: (Int) -> Unit
) {
    // Ruang bawah = tinggi bottom bar (64dp) + inset navigation bar + jarak 24dp
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

        FadeSlideIn(visible = contentVisible, delayMillis = 0) {
            HeaderSection(
                userName = homeData.userName,
                onOpenNotifications = onOpenNotifications,
                onNavigateBackToRole = onNavigateBackToRole
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FadeSlideIn(visible = contentVisible, delayMillis = 80) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { onSwitchToTab(2) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lihat lokasi",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        FadeSlideIn(visible = contentVisible, delayMillis = 160) {
            ElderCard(
                elderName = homeData.elderName,
                elderActivity = homeData.elderActivity,
                elderStatus = homeData.elderStatus
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FadeSlideIn(visible = contentVisible, delayMillis = 240) {
            StatusSection(
                statusItems = homeData.statusItems,
                onViewDetail = { onOpenDetailAlert("act_2") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FadeSlideIn(visible = contentVisible, delayMillis = 320) {
            QuickActionsSection(
                quickActions = homeData.quickActions,
                onQuickActionClick = onQuickActionClick
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FadeSlideIn(visible = contentVisible, delayMillis = 400) {
            RecentActivitiesSection(
                recentActivities = homeData.recentActivities,
                onActivityClick = onOpenDetailAlert,
                onViewAll = { onSwitchToTab(1) }
            )
        }
    }
}

@Composable
fun FadeSlideIn(
    visible: Boolean,
    delayMillis: Int,
    content: @Composable () -> Unit
) {
    var localVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            delay(delayMillis.toLong().milliseconds)
            localVisible = true
        } else {
            localVisible = false
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (localVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "alpha"
    )

    val offsetY by animateDpAsState(
        targetValue = if (localVisible) 0.dp else 16.dp,
        animationSpec = tween(durationMillis = 400),
        label = "offsetY"
    )

    Box(
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha
            translationY = offsetY.toPx()
        }
    ) {
        content()
    }
}

@Composable
fun HeaderSection(
    userName: String,
    onOpenNotifications: () -> Unit,
    onNavigateBackToRole: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // TODO(tim): tombol khusus demo, tidak ada di Figma
            if (BuildConfig.DEMO_MODE) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNavigateBackToRole,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Terakota
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Halo, $userName",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Text(
                    text = "Halo, $userName",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Oma kamu hari ini bagaimana?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box {
            Surface(
                onClick = onOpenNotifications,
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = colors.cardHighlight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.nav_notifikasi),
                        contentDescription = "Notifikasi",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp)
                    .background(Terakota, CircleShape)
            )
        }
    }
}

@Composable
fun ElderCard(
    elderName: String,
    elderActivity: String,
    elderStatus: String
) {
    val colors = KetukTheme.colors
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.oma_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = elderName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = elderActivity,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = colors.chipSehat
            ) {
                Text(
                    text = elderStatus,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = colors.accentGreen
                )
            }
        }
    }
}

@Composable
fun StatusSection(
    statusItems: List<StatusItem>,
    onViewDetail: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Status hari ini",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    onClick = onViewDetail,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            ) {
                Text(
                    text = "Lihat detail",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
            border = BorderStroke(1.dp, colors.cardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                statusItems.forEachIndexed { index, item ->
                    StatusItemRow(item)
                    if (index < statusItems.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 1.dp,
                            color = colors.cardBorder
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusItemRow(item: StatusItem) {
    val colors = KetukTheme.colors
    val icon = when (item.type) {
        StatusType.AKTIVITAS -> Icons.Rounded.MonitorHeart
        StatusType.HP -> Icons.Rounded.PhoneAndroid
        StatusType.SAPA -> Icons.Rounded.WavingHand
    }

    val statusColor = when (item.statusColor) {
        "HijauDaun" -> colors.accentGreen
        "KuningMadu" -> colors.accentAmber
        else -> MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = colors.cardHighlight,
            border = BorderStroke(1.dp, colors.cardBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = item.status,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = statusColor
        )
    }
}

@Composable
fun QuickActionsSection(
    quickActions: List<QuickAction>,
    onQuickActionClick: (QuickAction) -> Unit = {}
) {
    Column {
        Text(
            text = "Aksi cepat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            quickActions.forEach { action ->
                QuickActionCard(
                    action = action,
                    onClick = { onQuickActionClick(action) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    action: QuickAction,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = KetukTheme.colors
    val (backgroundColor, iconColor, icon) = when (action.type) {
        QuickActionType.AKTIVITAS -> Triple(
            colors.aksiAktivitas,
            MaterialTheme.colorScheme.primary,
            Icons.Rounded.CalendarMonth
        )
        QuickActionType.SAPA -> Triple(
            colors.aksiSapa,
            KuningMadu,
            Icons.Rounded.ChatBubble
        )
        QuickActionType.HP -> Triple(
            colors.aksiHp,
            HijauDaun,
            Icons.Rounded.Call
        )
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.onAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = action.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = action.description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RecentActivitiesSection(
    recentActivities: List<RecentActivity>,
    onActivityClick: (String) -> Unit = {},
    onViewAll: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Aktivitas terbaru",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    onClick = onViewAll,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            ) {
                Text(
                    text = "Lihat Semua",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            recentActivities.forEachIndexed { index, activity ->
                val itemId = when (index) {
                    0 -> "act_1"
                    1 -> "act_2"
                    else -> "act_7"
                }
                TimelineItem(
                    activity = activity,
                    showLine = index < recentActivities.lastIndex,
                    onClick = { onActivityClick(itemId) }
                )
            }
        }
    }
}

@Composable
fun TimelineItem(
    activity: RecentActivity,
    showLine: Boolean,
    onClick: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            val (iconColor, icon) = when (activity.type) {
                ActivityIconType.CHECK -> IkonHijau to Icons.Rounded.Check
                ActivityIconType.BELL -> IkonOranye to Icons.Rounded.Notifications
                ActivityIconType.CHAT -> IkonBiru to Icons.Rounded.ChatBubble
            }

            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = iconColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.onAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            if (showLine) {
                Canvas(
                    modifier = Modifier
                        .width(2.dp)
                        .height(56.dp)
                ) {
                    drawLine(
                        color = colors.cardBorder,
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (showLine) 16.dp else 0.dp)
        ) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (activity.quote != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"${activity.quote}\"",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = activity.time,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun BoxWithConstraintsScope.BottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val colors = KetukTheme.colors
    val tabs = listOf(
        BottomNavItem(0, "Home", R.drawable.nav_home),
        BottomNavItem(1, "Riwayat", R.drawable.nav_riwayat),
        BottomNavItem(2, "Lokasi", R.drawable.nav_lokasi),
        BottomNavItem(3, "Profil", R.drawable.nav_profil)
    )

    // Hitungan pill harus sama persis dengan Row tab:
    // Row punya padding horizontal 12dp, lalu dibagi 4 slot sama lebar (weight 1f).
    val sidePadding = 12.dp
    val pillInset = 4.dp
    val slotWidth = (maxWidth - sidePadding * 2) / tabs.size

    val pillOffset by animateDpAsState(
        targetValue = sidePadding + slotWidth * selectedTab + pillInset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pillOffset"
    )

    Surface(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(),
        color = colors.barBackground,
        shadowElevation = 8.dp
    ) {
        // navigationBarsPadding di dalam Surface, supaya warna bar menutup sampai dasar layar
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

data class BottomNavItem(
    val index: Int,
    val label: String,
    val iconRes: Int
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KetukTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, fontScale = 1.3f)
@Composable
fun HomeScreenPreviewSmall() {
    KetukTheme {
        HomeScreen()
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        HomeScreen()
    }
}