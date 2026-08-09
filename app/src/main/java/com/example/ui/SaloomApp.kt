package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.viewmodel.SaloomViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "الرئيسية", Icons.Default.Home)
    object Invoices : Screen("invoices", "الفواتير", Icons.AutoMirrored.Filled.ReceiptLong)
    object Search : Screen("search", "البحث", Icons.Default.Search)
    object Reports : Screen("reports", "التقارير", Icons.Default.BarChart)
    object Settings : Screen("settings", "الإعدادات", Icons.Default.Settings)
    object NewInvoice : Screen("new_invoice", "فاتورة جديدة", Icons.Default.Add)
    object ScanInvoice : Screen("scan", "تصوير", Icons.Default.CameraAlt)
}

@Composable
fun SaloomApp(viewModel: SaloomViewModel) {
    val navController = rememberNavController()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val invoices by viewModel.invoices.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()

    var isLocked by remember { mutableStateOf(true) }

    val correctPin = settings?.pinCode ?: "0000"

    if (isLocked) {
        LockScreen(
            correctPin = correctPin,
            onUnlocked = { isLocked = false }
        )
    } else {
        val context = LocalContext.current
        val isReducedMotion = try {
            val durationScale = android.provider.Settings.Global.getFloat(
                context.contentResolver,
                android.provider.Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            )
            durationScale == 0f
        } catch (e: Exception) {
            false
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val items = listOf(
            Screen.Home,
            Screen.Invoices,
            Screen.Search,
            Screen.Reports,
            Screen.Settings
        )

        val showBottomBar = currentRoute in items.map { it.route }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = Color(0xFF0F2537),
                        contentColor = Color.White
                    ) {
                        items.forEach { screen ->
                            val selected = currentRoute == screen.route
                            val scale by animateFloatAsState(
                                targetValue = if (selected) 1.15f else 1.0f,
                                animationSpec = if (isReducedMotion) snap() else spring(stiffness = Spring.StiffnessMedium),
                                label = "nav_icon_scale"
                            )
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
                                    )
                                },
                                label = { Text(screen.title, color = if (selected) Color(0xFF00B4D8) else Color(0xFF94A3B8)) },
                                selected = selected,
                                onClick = {
                                    if (!selected) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF00B4D8),
                                    unselectedIconColor = Color(0xFF94A3B8),
                                    indicatorColor = Color(0xFF163853)
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { if (isReducedMotion) EnterTransition.None else fadeIn(animationSpec = tween(150)) + slideInHorizontally(animationSpec = tween(150)) { it / 15 } },
                exitTransition = { if (isReducedMotion) ExitTransition.None else fadeOut(animationSpec = tween(150)) + slideOutHorizontally(animationSpec = tween(150)) { -it / 15 } },
                popEnterTransition = { if (isReducedMotion) EnterTransition.None else fadeIn(animationSpec = tween(150)) + slideInHorizontally(animationSpec = tween(150)) { -it / 15 } },
                popExitTransition = { if (isReducedMotion) ExitTransition.None else fadeOut(animationSpec = tween(150)) + slideOutHorizontally(animationSpec = tween(150)) { it / 15 } }
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        settings = settings,
                        invoices = invoices,
                        products = products,
                        onNavigate = { route -> navController.navigate(route) },
                        onOpenInvoice = { invoiceNo ->
                            // Open invoice details or list
                            navController.navigate(Screen.Invoices.route)
                        }
                    )
                }
                composable(Screen.Invoices.route) {
                    InvoicesScreen(
                        invoices = invoices,
                        onNewInvoice = { navController.navigate(Screen.NewInvoice.route) },
                        onOpenInvoice = { invoiceNo -> },
                        onDeleteInvoice = { invoiceNo -> viewModel.deleteInvoice(invoiceNo) }
                    )
                }
                composable(Screen.Search.route) {
                    SearchScreen(
                        invoices = invoices,
                        onOpenInvoice = { invoiceNo -> navController.navigate(Screen.Invoices.route) }
                    )
                }
                composable(Screen.Reports.route) {
                    ReportsScreen(
                        invoices = invoices,
                        products = products
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        settings = settings,
                        totalInvoicesCount = invoices.size,
                        onUpdateSettings = { newSet -> viewModel.updateSettings(newSet) },
                        onLockApp = { isLocked = true },
                        onClearData = { viewModel.clearAllData() }
                    )
                }
                composable(Screen.NewInvoice.route) {
                    NewInvoiceScreen(
                        existingInvoicesCount = invoices.size,
                        onSaveInvoice = { invoice, itemsList ->
                            viewModel.addInvoice(invoice, itemsList)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.ScanInvoice.route) {
                    ScanInvoiceScreen(
                        onBack = { navController.popBackStack() },
                        onManualEntry = {
                            navController.navigate(Screen.NewInvoice.route) {
                                popUpTo(Screen.Home.route)
                            }
                        }
                    )
                }
            }
        }
    }
}
