package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SolvedProblem
import com.example.ui.theme.AppWhite
import com.example.ui.theme.LightGrayBorder
import com.example.ui.theme.LightGraySurface
import com.example.ui.theme.NumeriqBrandGreen
import com.example.ui.theme.NumeriqGreenBoxBg
import com.example.ui.theme.NumeriqGreenDarkText
import com.example.ui.theme.NumeriqGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.NumeriqViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: NumeriqViewModel,
    onNavigateToQuestion: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onOpenProblem: (SolvedProblem) -> Unit,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val isPro by viewModel.isPro.collectAsState()
    val recentProblems by viewModel.recentProblems.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppWhite)
            .statusBarsPadding()
            .testTag("home_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 430.dp)
                .padding(horizontal = 22.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 18.dp, bottom = 110.dp)
        ) {
            // 1. TOP BAR: Moved down from status bar, Avatar + Hello Tommy! (no arrow, no streak),
            // and on right: green circle with outlined (non-filled) black star icon!
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar + "Hello, {name}!" (clean, no click action, no modal)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_avatar_1790595505701),
                            contentDescription = "User profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color(0xFFE5E7EB), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Hello, $userName!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            ),
                            color = TextDark
                        )
                    }

                    // Green circle with black outlined star (not filled)
                    Surface(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.showProDialog() }
                            .testTag("pro_sparkle_btn"),
                        shape = CircleShape,
                        color = NumeriqBrandGreen
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = "Pro perks",
                                tint = TextDark, // black/dark, outlined
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // 2. BIG HEADING: "Need help" (Need help bold) "studying today?" ("today" bold)
            item {
                val headingText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TextDark)) {
                        append("Need help\n")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = TextDark)) {
                        append("studying ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TextDark)) {
                        append("today?")
                    }
                }

                Text(
                    text = headingText,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 32.sp,
                        lineHeight = 38.sp
                    ),
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 22.dp)
                )
            }

            // 3. ACTION ROW: "Ask a question" pill + 2 options ONLY: Write and Camera (NO VOICE)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // "Ask a question" search pill
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .clickable { onNavigateToQuestion() }
                            .testTag("ask_question_pill"),
                        shape = RoundedCornerShape(25.dp),
                        color = AppWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ask a question...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextMuted,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }

                    // 1. Write / Question Option
                    Surface(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable { onNavigateToQuestion() }
                            .testTag("write_icon_btn"),
                        shape = CircleShape,
                        color = AppWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Write Question",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 2. Camera Option
                    Surface(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable { onNavigateToScan() }
                            .testTag("camera_icon_btn"),
                        shape = CircleShape,
                        color = AppWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Scan Problem",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // 4. NUMERIQ PRO CARD: Slim, NO shadow, clean #AFE976 card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp)
                        .testTag("pro_promo_card"),
                    shape = RoundedCornerShape(22.dp),
                    color = NumeriqBrandGreen
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.35f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.WorkspacePremium,
                                    contentDescription = "Pro Crown",
                                    tint = TextDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isPro) "Numeriq Pro Active" else "Unlock Numeriq Pro",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = TextDark
                                )
                                Text(
                                    text = "Upgrade your learning with unlimited access to premium perks.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    ),
                                    color = TextDark.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.showProDialog() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = TextDark
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text(
                                text = if (isPro) "Manage Pro" else "+ Upgrade to Pro",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                color = TextDark
                            )
                        }
                    }
                }
            }

            // 5. 3 FEATURE CARDS: "Math Solver", "Ask Question", "History"
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeActionCard(
                        title = "Math Solver",
                        icon = Icons.Outlined.CameraAlt,
                        badgeColor = Color(0xFFF3F4F6),
                        iconColor = TextDark,
                        onClick = onNavigateToScan,
                        modifier = Modifier.weight(1f),
                        testTag = "card_math_solver"
                    )

                    HomeActionCard(
                        title = "Ask Question",
                        icon = Icons.Outlined.QuestionAnswer,
                        badgeColor = NumeriqGreenBoxBg,
                        iconColor = NumeriqGreenDarkText,
                        onClick = onNavigateToQuestion,
                        modifier = Modifier.weight(1f),
                        testTag = "card_ask_question"
                    )

                    HomeActionCard(
                        title = "History",
                        icon = Icons.Outlined.History,
                        badgeColor = Color(0xFFF3F4F6),
                        iconColor = TextDark,
                        onClick = onNavigateToHistory,
                        modifier = Modifier.weight(1f),
                        testTag = "card_history"
                    )
                }
            }

            // 6. "Recent Solutions" Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Solutions",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        color = TextDark
                    )

                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = NumeriqGreenDarkText
                        ),
                        modifier = Modifier
                            .clickable { onNavigateToHistory() }
                            .padding(4.dp)
                    )
                }
            }

            // Recent Solutions List (clean, flat, no heavy shadows)
            if (recentProblems.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(18.dp),
                        color = AppWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No solved problems yet",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = TextDark
                            )
                            Text(
                                text = "Ask your first question or scan a problem to see solutions.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                items(recentProblems) { problem ->
                    HomeRecentSolutionCard(
                        problem = problem,
                        onClick = {
                            viewModel.openHistoryItem(problem)
                            onOpenProblem(problem)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun HomeActionCard(
    title: String,
    icon: ImageVector,
    badgeColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        color = AppWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(badgeColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                ),
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HomeRecentSolutionCard(
    problem: SolvedProblem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateStr = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(problem.timestamp))

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("recent_problem_${problem.id}"),
        shape = RoundedCornerShape(18.dp),
        color = AppWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(NumeriqGreenBoxBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Solved",
                    tint = NumeriqGreenDarkText,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${problem.category} • $dateStr",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                )

                Text(
                    text = problem.question,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = problem.finalAnswer,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
