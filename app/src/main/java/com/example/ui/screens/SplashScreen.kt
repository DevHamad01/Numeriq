package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.theme.AppWhite
import com.example.ui.theme.LightGrayBorder
import com.example.ui.theme.LightGraySurface
import com.example.ui.theme.NumeriqBrandGreen
import com.example.ui.theme.NumeriqGreenBorder
import com.example.ui.theme.NumeriqLogoFont
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onGetStarted: () -> Unit,
    onSignIn: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSignInDialog by remember { mutableStateOf(false) }
    var inputName by remember { mutableStateOf("") }

    // Fast flick image cycle state (swaps in milliseconds, cards stay in place)
    var cycleIndex by remember { mutableIntStateOf(0) }

    val images = remember {
        listOf(
            R.drawable.student_left_1790619279252,
            R.drawable.student_center_1790619296314,
            R.drawable.student_right_1790619314567
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3200) // Stay stable in place
            cycleIndex = (cycleIndex + 1) % 3 // Fast millisecond flick transition
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppWhite)
            .statusBarsPadding()
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 430.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. TOP: ONLY "Numeriq" Serif wordmark (no subtext below)
            Text(
                text = "Numeriq",
                fontFamily = NumeriqLogoFont,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = TextDark,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. CENTER: 3 STATIONARY CARDS + CONNECTING ARCS INTO STAR CENTER + HUGEICONS SPARKLE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Arc lines drawn from bottom center of Left & Right cards directly into the CENTER of the Star Box
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val w = size.width
                    val h = size.height

                    // Left card: centered at w * 0.19f, bottom is at 148.dp
                    val leftCardBottomMidX = w * 0.19f
                    val leftCardBottomMidY = 148.dp.toPx()

                    // Right card: centered at w * 0.81f, bottom is at 148.dp
                    val rightCardBottomMidX = w * 0.81f
                    val rightCardBottomMidY = 148.dp.toPx()

                    // Star Box Center: exactly at center of bottom 52.dp box (box y: h - 52.dp to h)
                    val starCenterX = w / 2f
                    val starCenterY = h - 26.dp.toPx()

                    val lineColor = Color(0xFFC7D3C5)
                    val stroke = Stroke(width = 2.2f.dp.toPx(), cap = StrokeCap.Round)

                    // Arc from Left card bottom mid directly into star box center
                    val leftArc = Path().apply {
                        moveTo(leftCardBottomMidX, leftCardBottomMidY)
                        cubicTo(
                            leftCardBottomMidX, leftCardBottomMidY + 45.dp.toPx(),
                            starCenterX - 30.dp.toPx(), starCenterY,
                            starCenterX, starCenterY
                        )
                    }
                    drawPath(leftArc, color = lineColor, style = stroke)

                    // Arc from Right card bottom mid directly into star box center
                    val rightArc = Path().apply {
                        moveTo(rightCardBottomMidX, rightCardBottomMidY)
                        cubicTo(
                            rightCardBottomMidX, rightCardBottomMidY + 45.dp.toPx(),
                            starCenterX + 30.dp.toPx(), starCenterY,
                            starCenterX, starCenterY
                        )
                    }
                    drawPath(rightArc, color = lineColor, style = stroke)
                }

                // 3 Separate Images staying in their 3 positions (Left, Center, Right) and flick-cycling photos in ms
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Slot (Stationary card, fast millisecond photo flick)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(148.dp)
                            .clip(RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                    ) {
                        AnimatedContent(
                            targetState = images[cycleIndex % 3],
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)))
                                    .togetherWith(fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)))
                            },
                            label = "leftCardFlick"
                        ) { targetImg ->
                            Image(
                                painter = painterResource(id = targetImg),
                                contentDescription = "Student Left",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Center Slot (Stationary prominent card, fast millisecond photo flick)
                    Surface(
                        modifier = Modifier
                            .weight(1.22f)
                            .height(176.dp)
                            .shadow(8.dp, RoundedCornerShape(26.dp), spotColor = Color(0x33000000))
                            .clip(RoundedCornerShape(26.dp)),
                        shape = RoundedCornerShape(26.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(2.dp, NumeriqGreenBorder)
                    ) {
                        AnimatedContent(
                            targetState = images[(cycleIndex + 1) % 3],
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)))
                                    .togetherWith(fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)))
                            },
                            label = "centerCardFlick"
                        ) { targetImg ->
                            Image(
                                painter = painterResource(id = targetImg),
                                contentDescription = "Student Center",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Right Slot (Stationary card, fast millisecond photo flick)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(148.dp)
                            .clip(RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                    ) {
                        AnimatedContent(
                            targetState = images[(cycleIndex + 2) % 3],
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)))
                                    .togetherWith(fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)))
                            },
                            label = "rightCardFlick"
                        ) { targetImg ->
                            Image(
                                painter = painterResource(id = targetImg),
                                contentDescription = "Student Right",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                // Green Box with Hugeicons AI Sparkle at bottom center (where arcs meet in the center)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(52.dp)
                        .background(
                            color = NumeriqBrandGreen,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .border(3.dp, AppWhite, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Hugeicons-style 4-point AI magic sparkle
                    HugeiconsSparkleIcon(
                        modifier = Modifier.size(24.dp),
                        tint = TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. HEADINGS:
            // "Your Personal" -> Bold, larger size
            // "AI" -> Regular size, Light Gray
            // "Study " -> Regular size, Light Gray
            // "Companion" -> Bold, larger size
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                // Line 1: "Your Personal" (bold, large) + " AI" (regular, gray)
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Your Personal",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        ),
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 24.sp
                        ),
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                // Line 2: "Study" (regular, gray) + " Companion" (bold, large)
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "Study",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 24.sp
                        ),
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Companion",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        ),
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Subtext: EXACTLY 2 lines maximum
                Text(
                    text = "Solve any math problem with step-by-step AI\nexplanations, formulas & exam-ready tips.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 20.sp,
                        fontSize = 14.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = TextMuted,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. ACTION BUTTONS:
            // "Get Started" in #AFE976 pill
            // "Sign In / Set Name" in light gray pill
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Get Started
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("get_started_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NumeriqBrandGreen,
                        contentColor = TextDark
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = TextDark
                    )
                }

                // Sign In / Set Name (Light Gray Pill)
                Button(
                    onClick = { showSignInDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("sign_in_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGraySurface,
                        contentColor = TextDark
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Sign In / Set Name",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        ),
                        color = TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Sign In Name Input Dialog
        if (showSignInDialog) {
            Dialog(onDismissRequest = { showSignInDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = AppWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "What is your name?",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "We will personalize your companion greeting.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = inputName,
                            onValueChange = { inputName = it },
                            placeholder = { Text("Enter your name...", color = TextMuted) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("name_input_field"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NumeriqBrandGreen,
                                unfocusedBorderColor = LightGrayBorder,
                                focusedContainerColor = AppWhite,
                                unfocusedContainerColor = AppWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            TextButton(
                                onClick = { showSignInDialog = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel", color = TextMuted)
                            }
                            Button(
                                onClick = {
                                    val nameToSave = inputName.trim().ifBlank { "Student" }
                                    onSignIn(nameToSave)
                                    showSignInDialog = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NumeriqBrandGreen,
                                    contentColor = TextDark
                                )
                            ) {
                                Text("Save & Enter", fontWeight = FontWeight.Bold, color = TextDark)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Hugeicons-style 4-point AI magic sparkle:
 * Precision concave curved star with clean modern geometry and companion sparkle
 */
@Composable
fun HugeiconsSparkleIcon(
    modifier: Modifier = Modifier,
    tint: Color = TextDark
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Main 4-point sparkle centered slightly left-bottom
        val cx = w * 0.44f
        val cy = h * 0.54f
        val r = w * 0.38f
        val inner = r * 0.22f

        val mainSparkle = Path().apply {
            moveTo(cx, cy - r)
            quadraticTo(cx, cy, cx + inner, cy - inner)
            lineTo(cx + r, cy)
            quadraticTo(cx, cy, cx + inner, cy + inner)
            lineTo(cx, cy + r)
            quadraticTo(cx, cy, cx - inner, cy + inner)
            lineTo(cx - r, cy)
            quadraticTo(cx, cy, cx - inner, cy - inner)
            close()
        }
        drawPath(mainSparkle, color = tint)

        // Small companion sparkle at top-right
        val cx2 = w * 0.80f
        val cy2 = h * 0.24f
        val r2 = w * 0.16f
        val inner2 = r2 * 0.24f

        val companionSparkle = Path().apply {
            moveTo(cx2, cy2 - r2)
            quadraticTo(cx2, cy2, cx2 + inner2, cy2 - inner2)
            lineTo(cx2 + r2, cy2)
            quadraticTo(cx2, cy2, cx2 + inner2, cy2 + inner2)
            lineTo(cx2, cy2 + r2)
            quadraticTo(cx2, cy2, cx2 - inner2, cy2 + inner2)
            lineTo(cx2 - r2, cy2)
            quadraticTo(cx2, cy2, cx2 - inner2, cy2 - inner2)
            close()
        }
        drawPath(companionSparkle, color = tint)
    }
}
