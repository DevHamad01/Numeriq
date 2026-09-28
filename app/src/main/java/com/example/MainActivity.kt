package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.AppTab
import com.example.ui.components.FloatingBottomNavBar
import com.example.ui.components.ProPaywallDialog
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuestionScreen
import com.example.ui.screens.ScanSolveScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.AppWhite
import com.example.ui.theme.LightGrayBorder
import com.example.ui.theme.NumeriqBrandGreen
import com.example.ui.theme.NumeriqTheme
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.NumeriqViewModel

enum class ScreenState {
    SPLASH,
    HOME,
    QUESTION,
    SCAN,
    HISTORY,
    SETTINGS
}

class MainActivity : ComponentActivity() {
    private val viewModel: NumeriqViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val showProDialog by viewModel.showProDialog.collectAsState()
            val showNameEditDialog by viewModel.showNameEditDialog.collectAsState()
            val currentUserName by viewModel.userName.collectAsState()

            var currentScreen by remember { mutableStateOf(ScreenState.SPLASH) }
            var currentTab by remember { mutableStateOf(AppTab.HOME) }
            var editNameInput by remember { mutableStateOf("") }

            NumeriqTheme {
                BackHandler(enabled = currentScreen != ScreenState.HOME && currentScreen != ScreenState.SPLASH) {
                    currentScreen = ScreenState.HOME
                    currentTab = AppTab.HOME
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = AppWhite,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                fadeIn() + slideInHorizontally { width -> width / 4 } togetherWith
                                        fadeOut() + slideOutHorizontally { width -> -width / 4 }
                            },
                            label = "ScreenTransition"
                        ) { targetScreen ->
                            when (targetScreen) {
                                ScreenState.SPLASH -> {
                                    SplashScreen(
                                        onGetStarted = {
                                            currentScreen = ScreenState.HOME
                                            currentTab = AppTab.HOME
                                        },
                                        onSignIn = { name ->
                                            viewModel.updateUserName(name)
                                            currentScreen = ScreenState.HOME
                                            currentTab = AppTab.HOME
                                        }
                                    )
                                }

                                ScreenState.HOME -> {
                                    HomeScreen(
                                        viewModel = viewModel,
                                        onNavigateToQuestion = { currentScreen = ScreenState.QUESTION },
                                        onNavigateToScan = {
                                            currentScreen = ScreenState.SCAN
                                            currentTab = AppTab.SCAN
                                        },
                                        onNavigateToHistory = {
                                            currentScreen = ScreenState.HISTORY
                                            currentTab = AppTab.HISTORY
                                        },
                                        onOpenProblem = { currentScreen = ScreenState.QUESTION }
                                    )
                                }

                                ScreenState.QUESTION -> {
                                    QuestionScreen(
                                        viewModel = viewModel,
                                        onBack = {
                                            currentScreen = when (currentTab) {
                                                AppTab.HOME -> ScreenState.HOME
                                                AppTab.HISTORY -> ScreenState.HISTORY
                                                AppTab.SCAN -> ScreenState.SCAN
                                                AppTab.SETTINGS -> ScreenState.SETTINGS
                                            }
                                        }
                                    )
                                }

                                ScreenState.SCAN -> {
                                    ScanSolveScreen(
                                        viewModel = viewModel,
                                        onBack = {
                                            currentScreen = ScreenState.HOME
                                            currentTab = AppTab.HOME
                                        },
                                        onSolveComplete = { currentScreen = ScreenState.QUESTION }
                                    )
                                }

                                ScreenState.HISTORY -> {
                                    HistoryScreen(
                                        viewModel = viewModel,
                                        onOpenProblem = { currentScreen = ScreenState.QUESTION },
                                        onSolveFirstProblem = { currentScreen = ScreenState.QUESTION }
                                    )
                                }

                                ScreenState.SETTINGS -> {
                                    SettingsScreen(viewModel = viewModel)
                                }
                            }
                        }

                        // Floating Bottom Bar on primary tabs
                        if (currentScreen != ScreenState.SPLASH && currentScreen != ScreenState.QUESTION) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(
                                        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp
                                    )
                            ) {
                                FloatingBottomNavBar(
                                    currentTab = currentTab,
                                    onTabSelected = { tab ->
                                        currentTab = tab
                                        currentScreen = when (tab) {
                                            AppTab.HOME -> ScreenState.HOME
                                            AppTab.HISTORY -> ScreenState.HISTORY
                                            AppTab.SCAN -> ScreenState.SCAN
                                            AppTab.SETTINGS -> ScreenState.SETTINGS
                                        }
                                    }
                                )
                            }
                        }

                        // Pro Paywall Dialog
                        if (showProDialog) {
                            ProPaywallDialog(
                                onDismiss = { viewModel.dismissProDialog() },
                                onUpgrade = { viewModel.upgradeToPro() }
                            )
                        }

                        // Edit Name Dialog
                        if (showNameEditDialog) {
                            AlertDialog(
                                onDismissRequest = { viewModel.dismissNameEditDialog() },
                                title = { Text("Edit Student Name", fontWeight = FontWeight.Bold) },
                                text = {
                                    OutlinedTextField(
                                        value = editNameInput.ifEmpty { currentUserName },
                                        onValueChange = { editNameInput = it },
                                        singleLine = true,
                                        modifier = Modifier.padding(top = 4.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = NumeriqBrandGreen,
                                            unfocusedBorderColor = LightGrayBorder
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            val newName = editNameInput.trim().ifEmpty { currentUserName }
                                            viewModel.updateUserName(newName)
                                            viewModel.dismissNameEditDialog()
                                            editNameInput = ""
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = NumeriqBrandGreen,
                                            contentColor = TextDark
                                        )
                                    ) {
                                        Text("Save", fontWeight = FontWeight.Bold)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        viewModel.dismissNameEditDialog()
                                        editNameInput = ""
                                    }) {
                                        Text("Cancel", color = TextMuted)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
