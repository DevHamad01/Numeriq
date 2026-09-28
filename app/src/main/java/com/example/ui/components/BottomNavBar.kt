package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkPillBg
import com.example.ui.theme.FloatingNavBg
import com.example.ui.theme.TextMuted

enum class AppTab(val route: String, val title: String) {
    HOME("home", "Home"),
    HISTORY("history", "History"),
    SCAN("scan", "Scan"),
    SETTINGS("settings", "Settings")
}

@Composable
fun FloatingBottomNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Gray floating capsule with increased height & generous touch targets
    Surface(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(36.dp), spotColor = Color(0x22000000))
            .testTag("floating_bottom_nav"),
        shape = RoundedCornerShape(36.dp),
        color = Color(0xFFF1F3F5), // Gray background as requested
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E4E8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp) // Increased height for comfortable, non-slim look
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                Triple(AppTab.HOME, Icons.Filled.Home, Icons.Outlined.Home),
                Triple(AppTab.HISTORY, Icons.Filled.History, Icons.Outlined.History),
                Triple(AppTab.SCAN, Icons.Filled.CameraAlt, Icons.Outlined.CameraAlt),
                Triple(AppTab.SETTINGS, Icons.Filled.Settings, Icons.Outlined.Settings)
            )

            tabs.forEach { (tab, filledIcon, outlinedIcon) ->
                val isSelected = currentTab == tab

                val pillBgColor by animateColorAsState(
                    targetValue = if (isSelected) DarkPillBg else Color.Transparent,
                    animationSpec = tween(220),
                    label = "pillBg"
                )

                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color(0xFF555B64),
                    animationSpec = tween(220),
                    label = "contentColor"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(26.dp))
                        .background(pillBgColor)
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = if (isSelected) 16.dp else 14.dp, vertical = 10.dp)
                        .testTag("nav_tab_${tab.route}"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) filledIcon else outlinedIcon,
                            contentDescription = tab.title,
                            tint = contentColor,
                            modifier = Modifier.size(22.dp)
                        )

                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = contentColor
                            )
                        }
                    }
                }
            }
        }
    }
}
