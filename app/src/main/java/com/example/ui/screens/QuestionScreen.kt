package com.example.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MathSolution
import com.example.ui.components.AdditionalInfoCard
import com.example.ui.components.AnswerHighlightBox
import com.example.ui.components.SolutionShimmerLoading
import com.example.ui.components.StepTimelineItem
import com.example.ui.components.TutorModeSelector
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
import com.example.ui.viewmodel.SolveUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: NumeriqViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val solveState by viewModel.solveUiState.collectAsState()
    val currentMode by viewModel.currentTutorMode.collectAsState()
    val isPro by viewModel.isPro.collectAsState()
    val currentQuestion by viewModel.currentQuestionText.collectAsState()

    var inputQuestion by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(AppWhite)
            .statusBarsPadding()
            .testTag("question_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Question",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = TextDark
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("question_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextDark
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val state = solveState
                            if (state is SolveUiState.Success) {
                                shareSolution(context, state.question, state.solution)
                            } else if (currentQuestion.isNotBlank()) {
                                shareText(context, currentQuestion)
                            }
                        },
                        modifier = Modifier.testTag("share_solution_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share Solution",
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppWhite
                )
            )
        },
        bottomBar = {
            // Clean bottom prompt input bar - lifted comfortably above soft keyboard
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                shape = RoundedCornerShape(28.dp),
                color = AppWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputQuestion,
                        onValueChange = { inputQuestion = it },
                        placeholder = {
                            Text(
                                text = "Ask a question...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("question_input_bar"),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NumeriqBrandGreen,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = AppWhite,
                            unfocusedContainerColor = AppWhite
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (inputQuestion.isNotBlank()) {
                                    viewModel.solveProblem(inputQuestion)
                                    inputQuestion = ""
                                }
                            }
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (inputQuestion.isNotBlank()) {
                                    viewModel.solveProblem(inputQuestion)
                                    inputQuestion = ""
                                }
                            }
                            .testTag("send_question_btn"),
                        shape = CircleShape,
                        color = NumeriqBrandGreen
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = TextDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = AppWhite
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .widthIn(max = 430.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp, bottom = 16.dp)
        ) {
            // 1. TOP QUESTION CARD: NO SHADOW, NO PERSON IMAGE! Clean problem statement card.
            item {
                val displayQuestion = when (val s = solveState) {
                    is SolveUiState.Success -> s.question
                    else -> currentQuestion.ifBlank { "Type a math problem below..." }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .testTag("user_question_card"),
                    shape = RoundedCornerShape(18.dp),
                    color = NumeriqGreenBoxBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NumeriqBrandGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = "Problem Statement",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = NumeriqGreenDarkText
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = displayQuestion,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 21.sp
                            ),
                            color = TextDark
                        )
                    }
                }
            }

            // 2. SOLUTION / CONTENT (AI Assistant switcher removed as requested)
            when (val state = solveState) {
                is SolveUiState.Idle -> {
                    item {
                        if (currentQuestion.isBlank()) {
                            QuickPromptsView(onSelect = { q ->
                                viewModel.solveProblem(q)
                            })
                        }
                    }
                }

                is SolveUiState.Loading -> {
                    item {
                        SolutionShimmerLoading()
                    }
                }

                is SolveUiState.Error -> {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = AppWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Couldn't solve problem",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextDark
                                )
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                if (state.canRetry) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Button(
                                        onClick = { viewModel.retryCurrentQuestion() },
                                        shape = RoundedCornerShape(18.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = NumeriqBrandGreen,
                                            contentColor = TextDark
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Refresh,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Retry", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                is SolveUiState.Success -> {
                    val solution = state.solution

                    // Final Answer Highlight Box (clean, flat)
                    item {
                        AnswerHighlightBox(
                            finalAnswer = solution.finalAnswer,
                            modifier = Modifier.padding(bottom = 14.dp)
                        )
                    }

                    // ONE single expandable card for Key Concept, Pro Tip, Common Pitfall!
                    // Default state is closed as requested!
                    item {
                        AdditionalInfoCard(
                            keyConcept = solution.keyConcept,
                            proTip = solution.proTip,
                            commonPitfall = solution.commonPitfall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Step-by-Step Explanation Heading
                    item {
                        Text(
                            text = "Step-by-step Explanation",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = TextDark,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Numbered Steps
                    itemsIndexed(solution.steps) { index, step ->
                        StepTimelineItem(
                            step = step,
                            isLast = index == solution.steps.lastIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickPromptsView(
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(18.dp),
        color = AppWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Quick Practice Prompts",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
            )
            Text(
                text = "Tap any question to see AI step-by-step solution:",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            val samplePrompts = listOf(
                "sin θ / (1 + cos θ) + (1 + cos θ) / sin θ = 2 csc θ",
                "Solve 2x² + 5x - 3 = 0",
                "Evaluate ∫ x cos(x) dx",
                "Find derivative of f(x) = ln(x² + 4x + 1)"
            )

            samplePrompts.forEach { prompt ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelect(prompt) },
                    shape = RoundedCornerShape(12.dp),
                    color = NumeriqGreenBoxBg
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        color = NumeriqGreenDarkText,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

private fun shareSolution(context: Context, question: String, solution: MathSolution) {
    val builder = StringBuilder()
    builder.append("Numeriq Math Solution\n")
    builder.append("Problem: $question\n\n")
    builder.append("Final Answer: ${solution.finalAnswer}\n\n")
    builder.append("Steps:\n")
    solution.steps.forEach { step ->
        builder.append("${step.stepNumber}. ${step.title}\n")
        if (step.expression.isNotBlank()) builder.append("   ${step.expression}\n")
        if (step.explanation.isNotBlank()) builder.append("   ${step.explanation}\n")
        builder.append("\n")
    }
    builder.append("Solved with Numeriq")

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Numeriq Solution: $question")
        putExtra(Intent.EXTRA_TEXT, builder.toString())
    }
    context.startActivity(Intent.createChooser(intent, "Share Solution"))
}

private fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Question"))
}
