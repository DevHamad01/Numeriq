package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TutorMode
import com.example.ui.theme.AppWhite
import com.example.ui.theme.LightGrayBorder
import com.example.ui.theme.LightGraySurface
import com.example.ui.theme.NumeriqBrandGreen
import com.example.ui.theme.NumeriqGreenBoxBg
import com.example.ui.theme.NumeriqGreenDarkText
import com.example.ui.theme.NumeriqLogoFont
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.NumeriqViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NumeriqViewModel,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val questionsToday by viewModel.questionsUsedToday.collectAsState()
    val allProblems by viewModel.allProblems.collectAsState()
    val currentMode by viewModel.currentTutorMode.collectAsState()

    var showEditNameDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(userName) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showFormulasToggle by remember { mutableStateOf(true) }

    // Remaining limit calculation: total limit = 5; remaining = (5 - questionsToday).coerceAtLeast(0)
    val remainingToday = (5 - questionsToday).coerceAtLeast(0)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(AppWhite)
            .statusBarsPadding()
            .testTag("settings_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = TextDark
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppWhite
                )
            )
        },
        containerColor = AppWhite
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .widthIn(max = 430.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. PROFILE CARD: NO USER PHOTO, NO "PRO/FREE" BADGE IN FRONT OF NAME
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .clickable {
                            tempName = userName
                            showEditNameDialog = true
                        }
                        .testTag("settings_profile_card"),
                    shape = RoundedCornerShape(22.dp),
                    color = AppWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Clean geometric profile icon (no image)
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(NumeriqGreenBoxBg, CircleShape)
                                .border(1.5.dp, NumeriqBrandGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Profile",
                                tint = NumeriqGreenDarkText,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    ),
                                    color = TextDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit name",
                                    tint = TextMuted,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Personal Math Study Account",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            // 2. 3 TOP BOXES: ALL 3 GREEN (NO GRAY), SOLVED COUNT, REMAINING TODAY, SELECTED MODEL
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Box 1: Total Problems Solved
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        color = NumeriqGreenBoxBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NumeriqBrandGreen)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${allProblems.size}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                ),
                                color = NumeriqGreenDarkText
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Solved",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextDark
                            )
                        }
                    }

                    // Box 2 (Center): Today Limit Remaining (total 5, subtract used)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        color = NumeriqGreenBoxBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NumeriqBrandGreen)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$remainingToday",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                ),
                                color = NumeriqGreenDarkText
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Remaining",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextDark
                            )
                        }
                    }

                    // Box 3: Currently Selected Model
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        color = NumeriqGreenBoxBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NumeriqBrandGreen)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currentMode.title.replace("Tutor ", ""),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = NumeriqGreenDarkText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Active Model",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextDark
                            )
                        }
                    }
                }
            }

            // 3. AI TUTOR PREFERENCES WITH PRO 5 FREE TRIALS, MAX 0 FREE TRIALS, AND EQUAL SPACING
            item {
                Text(
                    text = "AI TUTOR PREFERENCES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = TextMuted,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = AppWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TutorMode.entries.forEachIndexed { index, mode ->
                            val isSelected = currentMode == mode
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { viewModel.setTutorMode(mode) }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(
                                            if (isSelected) NumeriqBrandGreen else LightGraySurface,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (mode) {
                                            TutorMode.TUTOR_AI -> Icons.Outlined.School
                                            TutorMode.TUTOR_AI_PRO -> Icons.Outlined.Psychology
                                            TutorMode.TUTOR_AI_MAX -> Icons.Outlined.Star
                                        },
                                        contentDescription = null,
                                        tint = TextDark,
                                        modifier = Modifier.size(19.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (mode == TutorMode.TUTOR_AI) "Tutor AI (Basic)" else mode.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                fontSize = 14.sp
                                            ),
                                            color = TextDark
                                        )

                                        // Badge for Free Trials
                                        if (mode == TutorMode.TUTOR_AI_PRO) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = NumeriqGreenBoxBg
                                            ) {
                                                Text(
                                                    text = "5 free trials",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 10.sp
                                                    ),
                                                    color = NumeriqGreenDarkText,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        } else if (mode == TutorMode.TUTOR_AI_MAX) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = LightGraySurface
                                            ) {
                                                Text(
                                                    text = "0 free trials",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 10.sp
                                                    ),
                                                    color = TextMuted,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = mode.subtitle,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                        color = TextMuted
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "Selected",
                                        tint = NumeriqGreenDarkText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            if (index < TutorMode.entries.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(LightGrayBorder)
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. SHOW FORMULAS: CLEAN TWO-COLUMN ROW WITH WEIGHT, TEXT NEVER GOES UNDER TOGGLE SWITCH
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = AppWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(NumeriqGreenBoxBg, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Functions,
                                contentDescription = null,
                                tint = NumeriqGreenDarkText,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        ) {
                            Text(
                                text = "Show Formulas & Identities",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                ),
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Highlight theorems and key concepts",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Switch(
                            checked = showFormulasToggle,
                            onCheckedChange = { showFormulasToggle = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextDark,
                                checkedTrackColor = NumeriqBrandGreen,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = LightGraySurface
                            )
                        )
                    }
                }
            }

            // 5. DATA MANAGEMENT WITH EQUAL UNIFORM SPACING
            item {
                Text(
                    text = "DATA MANAGEMENT",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = TextMuted,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = AppWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        SettingsRow(
                            icon = Icons.Outlined.DeleteOutline,
                            title = "Clear Solved History",
                            subtitle = "${allProblems.size} problems saved locally",
                            onClick = { showClearHistoryDialog = true },
                            isDestructive = true
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(LightGrayBorder)
                        )

                        SettingsRow(
                            icon = Icons.Outlined.Refresh,
                            title = "Reset App Data",
                            subtitle = "Restore default settings & tutor configuration",
                            onClick = { showResetDialog = true },
                            isDestructive = true
                        )
                    }
                }
            }

            // 6. ABOUT & BRANDING FOOTER
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Numeriq",
                        fontFamily = NumeriqLogoFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Personal AI Math Study Companion • v1.2.0",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                    Text(
                        text = "Designed for high school & college STEM students",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }
        }

        // Edit Name Dialog
        if (showEditNameDialog) {
            AlertDialog(
                onDismissRequest = { showEditNameDialog = false },
                title = { Text("Edit Student Name", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        placeholder = { Text("Your name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NumeriqBrandGreen,
                            unfocusedBorderColor = LightGrayBorder
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (tempName.isNotBlank()) {
                                viewModel.updateUserName(tempName.trim())
                            }
                            showEditNameDialog = false
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
                    TextButton(onClick = { showEditNameDialog = false }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }

        // Clear History Dialog
        if (showClearHistoryDialog) {
            AlertDialog(
                onDismissRequest = { showClearHistoryDialog = false },
                title = { Text("Clear All Solved History?", fontWeight = FontWeight.Bold) },
                text = { Text("All your previously solved equations and step-by-step guides will be permanently removed.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearAllHistory()
                            showClearHistoryDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text("Clear All", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearHistoryDialog = false }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }

        // Reset App Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset App Preferences?", fontWeight = FontWeight.Bold) },
                text = { Text("This will reset your default tutor mode and usage preferences to factory defaults.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetAppPreferences()
                            showResetDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text("Reset", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    if (isDestructive) Color(0xFFFEE2E2) else LightGraySurface,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) Color(0xFFDC2626) else TextDark,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = if (isDestructive) Color(0xFFDC2626) else TextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = TextMuted
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}
