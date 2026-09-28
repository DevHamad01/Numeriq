package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.theme.AppWhite
import com.example.ui.theme.BorderLight
import com.example.ui.theme.LightGrayBorder
import com.example.ui.theme.LightGraySurface
import com.example.ui.theme.NumeriqBrandGreen
import com.example.ui.theme.NumeriqGreenBorder
import com.example.ui.theme.NumeriqLogoFont
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import kotlin.math.roundToInt

@Composable
fun SplashScreen(
    onGetStarted: () -> Unit,
    onSignIn: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSignInDialog by remember { mutableStateOf(false) }
    var inputName by remember { mutableStateOf("") }

    // Smooth continuous right-to-left slide animation for carousel
    val infiniteTransition = rememberInfiniteTransition(label = "carouselScroll")
    val slideAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "slideX"
    )

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

            // 2. CENTER CAROUSEL + CONNECTING ARC LINES + STAR BADGE
            // Height constrained container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Arc lines drawn from bottom center of left and right cards to mid left/right of center star
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val w = size.width
                    val h = size.height

                    // Left card bottom mid: left card is centered around ~w * 0.20f, card height 146dp
                    val leftCardBottomMidX = w * 0.20f
                    val leftCardBottomMidY = 146.dp.toPx()

                    // Right card bottom mid: right card is centered around ~w * 0.80f, card height 146dp
                    val rightCardBottomMidX = w * 0.80f
                    val rightCardBottomMidY = 146.dp.toPx()

                    // Center star box: centered at w / 2, y = 205.dp (bottom ~235dp), size = 52dp
                    val starCenterY = 210.dp.toPx()
                    val starHalfWidth = 26.dp.toPx()
                    val starLeftMidX = (w / 2f) - starHalfWidth
                    val starRightMidX = (w / 2f) + starHalfWidth

                    val lineColor = Color(0xFFC7D3C5)
                    val stroke = Stroke(width = 2.2f.dp.toPx(), cap = StrokeCap.Round)

                    // Arc from Left Image bottom mid to Star left mid
                    val leftArc = Path().apply {
                        moveTo(leftCardBottomMidX, leftCardBottomMidY)
                        // Smooth downward arc curving rightward into star left
                        cubicTo(
                            leftCardBottomMidX, leftCardBottomMidY + 36.dp.toPx(),
                            starLeftMidX - 25.dp.toPx(), starCenterY,
                            starLeftMidX, starCenterY
                        )
                    }
                    drawPath(leftArc, color = lineColor, style = stroke)

                    // Arc from Right Image bottom mid to Star right mid
                    val rightArc = Path().apply {
                        moveTo(rightCardBottomMidX, rightCardBottomMidY)
                        // Smooth downward arc curving leftward into star right
                        cubicTo(
                            rightCardBottomMidX, rightCardBottomMidY + 36.dp.toPx(),
                            starRightMidX + 25.dp.toPx(), starCenterY,
                            starRightMidX, starCenterY
                        )
                    }
                    drawPath(rightArc, color = lineColor, style = stroke)
                }

                // 3 Separate Images arranged in Carousel sliding right to left
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp)
                        .clip(RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Sliding row with infinite smooth shift right to left
                    Row(
                        modifier = Modifier
                            .offset { IntOffset(x = slideAnim.roundToInt(), y = 0) }
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Render looping sequence so cards smoothly slide right to left
                        val imageList = listOf(
                            R.drawable.student_left_1790619279252,
                            R.drawable.student_center_1790619296314,
                            R.drawable.student_right_1790619314567,
                            R.drawable.student_left_1790619279252,
                            R.drawable.student_center_1790619296314,
                            R.drawable.student_right_1790619314567
                        )

                        imageList.forEachIndexed { index, resId ->
                            val isProminent = (index % 3) == 1
                            Surface(
                                modifier = Modifier
                                    .width(if (isProminent) 130.dp else 112.dp)
                                    .height(if (isProminent) 170.dp else 146.dp)
                                    .clip(RoundedCornerShape(22.dp)),
                                shape = RoundedCornerShape(22.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isProminent) 2.dp else 1.dp,
                                    color = if (isProminent) NumeriqBrandGreen else LightGrayBorder
                                ),
                                shadowElevation = if (isProminent) 6.dp else 1.dp
                            ) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = "Student $index",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }

                // Green Box with Star/Sparkle at bottom center (where arcs meet from left & right)
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
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = "Star Sparkle",
                        tint = TextDark,
                        modifier = Modifier.size(24.dp)
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
                        color = Color(0xFF9CA3AF), // light gray as requested
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
                        color = Color(0xFF9CA3AF), // light gray as requested
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
