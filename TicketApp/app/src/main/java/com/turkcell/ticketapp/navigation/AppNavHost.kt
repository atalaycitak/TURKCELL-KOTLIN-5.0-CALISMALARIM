package com.turkcell.ticketapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.turkcell.core.domain.AuthRepository
import com.turkcell.core.domain.UserRole
import com.turkcell.ticketapp.screen.CheckinScreen
import com.turkcell.ticketapp.screen.EventDetailScreen
import com.turkcell.ticketapp.screen.EventListScreen
import com.turkcell.ticketapp.screen.LoginScreen
import com.turkcell.ticketapp.screen.MyPurchasesScreen
import com.turkcell.ticketapp.screen.MyTicketsScreen
import com.turkcell.ticketapp.screen.RegisterScreen
import com.turkcell.ticketapp.screen.TicketDetailScreen
import com.turkcell.ticketapp.viewmodel.EventDetailViewModel
import com.turkcell.ticketapp.viewmodel.TicketDetailViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import com.turkcell.ticketapp.R


@Composable
fun AppNavHost(
    authRepository: AuthRepository = koinInject()
) {
    val isLoggedIn by authRepository.isLoggedIn.collectAsStateWithLifecycle(initialValue = null)

    when (isLoggedIn) {
        null -> SplashScreen()
        true -> AuthedNavHost(authRepository)
        false -> UnAuthedNavHost()
    }
}

@Composable
private fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthedNavHost(
    authRepository: AuthRepository
) {
    val navController: NavHostController = rememberNavController()
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val userRole by authRepository.userRole.collectAsStateWithLifecycle(initialValue = null)

    val showBottomBar = currentDestination?.let {
        it.hasRoute<Events>() || it.hasRoute<MyTickets>() || it.hasRoute<MyPurchases>() || it.hasRoute<Checkin>()
    } ?: true

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = { Text(stringResource(R.string.top_bar_title)) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch { authRepository.logout() }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = stringResource(R.string.logout))
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<Events>() == true,
                        onClick = {
                            navController.navigate(Events) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Event, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_events)) }
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<MyTickets>() == true,
                        onClick = {
                            navController.navigate(MyTickets) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Filled.ConfirmationNumber,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(R.string.nav_my_tickets)) }
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<MyPurchases>() == true,
                        onClick = {
                            navController.navigate(MyPurchases) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(R.string.nav_my_purchases)) }
                    )
                    if (userRole == UserRole.STAFF || userRole == UserRole.ADMIN) {
                        NavigationBarItem(
                            selected = currentDestination?.hasRoute<Checkin>() == true,
                            onClick = {
                                navController.navigate(Checkin) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Filled.QrCodeScanner, contentDescription = null) },
                            label = { Text(stringResource(R.string.nav_checkin)) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Events,
            modifier = Modifier.padding(padding)
        ) {
            composable<Events> {
                EventListScreen(
                    onEventClick = { eventId ->
                        navController.navigate(EventDetail(eventId))
                    }
                )
            }
            composable<MyTickets> {
                MyTicketsScreen(
                    onTicketClick = { ticketId ->
                        navController.navigate(TicketDetail(ticketId))
                    }
                )
            }
            composable<MyPurchases> {
                MyPurchasesScreen()
            }
            composable<Checkin> {
                CheckinScreen()
            }
            composable<EventDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<EventDetail>()
                val viewModel: EventDetailViewModel = koinViewModel(
                    parameters = { parametersOf(route.eventId) }
                )
                EventDetailScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onPurchaseSuccess = {
                        navController.navigate(MyTickets) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<TicketDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<TicketDetail>()
                val viewModel: TicketDetailViewModel = koinViewModel(
                    parameters = { parametersOf(route.ticketId) }
                )
                TicketDetailScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun UnAuthedNavHost() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {},
                onNavigateToRegister = { navController.navigate(Register) }
            )
        }
        composable<Register> {
            RegisterScreen(
                onRegisterSuccess = {},
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
    }
}