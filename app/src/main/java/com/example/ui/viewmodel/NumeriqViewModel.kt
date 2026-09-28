package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferences
import com.example.data.model.MathSolution
import com.example.data.model.SolutionStep
import com.example.data.model.SolvedProblem
import com.example.data.model.TutorMode
import com.example.data.remote.GeminiMathService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

sealed interface SolveUiState {
    data object Idle : SolveUiState
    data object Loading : SolveUiState
    data class Success(val solution: MathSolution, val question: String) : SolveUiState
    data class Error(val message: String, val canRetry: Boolean = true) : SolveUiState
}

class NumeriqViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val problemDao = db.problemDao()
    private val userPrefs = UserPreferences(application)
    private val geminiService = GeminiMathService()

    // User & App States
    private val _userName = MutableStateFlow(userPrefs.userName)
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isPro = MutableStateFlow(userPrefs.isPro)
    val isPro: StateFlow<Boolean> = _isPro.asStateFlow()

    private val _isDarkMode = MutableStateFlow(userPrefs.isDarkMode)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _streakDays = MutableStateFlow(userPrefs.streakDays)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _questionsUsedToday = MutableStateFlow(userPrefs.getQuestionsUsedToday())
    val questionsUsedToday: StateFlow<Int> = _questionsUsedToday.asStateFlow()

    private val _showProDialog = MutableStateFlow(false)
    val showProDialog: StateFlow<Boolean> = _showProDialog.asStateFlow()

    private val _showNameEditDialog = MutableStateFlow(false)
    val showNameEditDialog: StateFlow<Boolean> = _showNameEditDialog.asStateFlow()

    // Solver state
    private val _currentTutorMode = MutableStateFlow(TutorMode.fromId(userPrefs.preferredTutorMode))
    val currentTutorMode: StateFlow<TutorMode> = _currentTutorMode.asStateFlow()

    private val _solveUiState = MutableStateFlow<SolveUiState>(SolveUiState.Idle)
    val solveUiState: StateFlow<SolveUiState> = _solveUiState.asStateFlow()

    private val _currentQuestionText = MutableStateFlow("")
    val currentQuestionText: StateFlow<String> = _currentQuestionText.asStateFlow()

    private val _scannedBitmap = MutableStateFlow<Bitmap?>(null)
    val scannedBitmap: StateFlow<Bitmap?> = _scannedBitmap.asStateFlow()

    // History and Search
    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    val allProblems: StateFlow<List<SolvedProblem>> = combine(
        problemDao.getAllProblems(),
        _historySearchQuery
    ) { problems, query ->
        if (query.isBlank()) {
            problems
        } else {
            problems.filter {
                it.question.contains(query, ignoreCase = true) ||
                it.finalAnswer.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentProblems: StateFlow<List<SolvedProblem>> = problemDao.getRecentProblems(4)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        userPrefs.checkAndRecordDailyActivity()
        _streakDays.value = userPrefs.streakDays
        _questionsUsedToday.value = userPrefs.getQuestionsUsedToday()
        // Pre-populate initial sample solutions if history is empty
        viewModelScope.launch {
            problemDao.getCount().collect { count ->
                if (count == 0) {
                    insertInitialSamples()
                }
            }
        }
    }

    private suspend fun insertInitialSamples() {
        val sample1 = SolvedProblem(
            question = "sin θ / (1 + cos θ) + (1 + cos θ) / sin θ",
            category = "Trigonometry",
            finalAnswer = "Proved:  sin θ / (1 + cos θ) + (1 + cos θ) / sin θ  =  2 csc θ",
            stepsJson = serializeSteps(listOf(
                SolutionStep(1, "Take the LHS", "sin θ / (1 + cos θ) + (1 + cos θ) / sin θ", "Begin with the left hand side expression."),
                SolutionStep(2, "Rationalize / Common Denominator", "= [sin²θ + (1 + cos θ)²] / [(1 + cos θ) sin θ]", "Find common denominator."),
                SolutionStep(3, "Apply Pythagorean Identity", "= 2(1 + cos θ) / [(1 + cos θ) sin θ] = 2 / sin θ", "Use sin²θ + cos²θ = 1."),
                SolutionStep(4, "Simplify to Cosecant", "= 2 csc θ = RHS", "Conclude with reciprocal trigonometric identity.")
            )),
            tutorMode = TutorMode.TUTOR_AI.id,
            keyConcept = "Pythagorean Identity & Common Denominator",
            proTip = "Expanding the squared binomial cleanly reveals the identity.",
            commonPitfall = "Missing the cross product term 2 cos θ."
        )

        val sample2 = SolvedProblem(
            question = "Solve 2x² + 5x - 3 = 0",
            category = "Algebra",
            finalAnswer = "x = 1/2   or   x = -3",
            stepsJson = serializeSteps(listOf(
                SolutionStep(1, "Factor the Quadratic", "2x² + 6x - x - 3 = 0", "Find numbers multiplying to -6 and adding to +5."),
                SolutionStep(2, "Group Common Terms", "(2x - 1)(x + 3) = 0", "Factor by grouping."),
                SolutionStep(3, "Zero Product Property", "2x - 1 = 0 => x = 1/2 ; x + 3 = 0 => x = -3", "Solve each factor for x.")
            )),
            tutorMode = TutorMode.TUTOR_AI_PRO.id,
            keyConcept = "Quadratic Factoring by Grouping",
            proTip = "Product a*c = -6 gives immediate clues.",
            commonPitfall = "Sign errors in the negative constant."
        )

        problemDao.insertProblem(sample1)
        problemDao.insertProblem(sample2)
    }

    fun setTutorMode(mode: TutorMode) {
        _currentTutorMode.value = mode
        userPrefs.preferredTutorMode = mode.id

        // If a question was already solved, re-solve it with the newly selected mode!
        val currentState = _solveUiState.value
        if (currentState is SolveUiState.Success) {
            solveProblem(currentState.question, _scannedBitmap.value)
        }
    }

    fun setQuestionInput(text: String) {
        _currentQuestionText.value = text
    }

    fun setScannedBitmap(bitmap: Bitmap?) {
        _scannedBitmap.value = bitmap
    }

    fun solveProblem(question: String, bitmap: Bitmap? = null) {
        val query = question.trim().ifBlank {
            if (bitmap != null) "Solve the math problem shown in this image" else return
        }

        _currentQuestionText.value = query
        _scannedBitmap.value = bitmap
        _solveUiState.value = SolveUiState.Loading

        viewModelScope.launch {
            try {
                userPrefs.incrementQuestionsUsed()
                _questionsUsedToday.value = userPrefs.getQuestionsUsedToday()

                val solution = geminiService.solveMathProblem(
                    questionText = query,
                    imageBitmap = bitmap,
                    tutorMode = _currentTutorMode.value
                )

                _solveUiState.value = SolveUiState.Success(solution, query)

                // Save to Room DB
                val problemEntity = SolvedProblem(
                    question = query,
                    category = solution.problemCategory,
                    finalAnswer = solution.finalAnswer,
                    stepsJson = serializeSteps(solution.steps),
                    tutorMode = _currentTutorMode.value.id,
                    keyConcept = solution.keyConcept,
                    proTip = solution.proTip,
                    commonPitfall = solution.commonPitfall
                )
                problemDao.insertProblem(problemEntity)
            } catch (e: Exception) {
                _solveUiState.value = SolveUiState.Error(
                    message = e.localizedMessage ?: "Failed to compute solution. Please check connection and try again."
                )
            }
        }
    }

    fun retryCurrentQuestion() {
        val q = _currentQuestionText.value
        val bmp = _scannedBitmap.value
        if (q.isNotBlank() || bmp != null) {
            solveProblem(q, bmp)
        }
    }

    fun openHistoryItem(item: SolvedProblem) {
        val steps = deserializeSteps(item.stepsJson)
        val solution = MathSolution(
            problemTitle = item.question,
            problemCategory = item.category,
            finalAnswer = item.finalAnswer,
            steps = steps,
            keyConcept = item.keyConcept,
            proTip = item.proTip,
            commonPitfall = item.commonPitfall
        )
        _currentTutorMode.value = TutorMode.fromId(item.tutorMode)
        _currentQuestionText.value = item.question
        _solveUiState.value = SolveUiState.Success(solution, item.question)
    }

    fun deleteProblem(item: SolvedProblem) {
        viewModelScope.launch {
            problemDao.deleteProblem(item)
        }
    }

    fun deleteSelectedProblems(items: List<SolvedProblem>) {
        if (items.isEmpty()) return
        viewModelScope.launch {
            problemDao.deleteProblems(items)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            problemDao.deleteAll()
        }
    }

    fun setHistorySearchQuery(query: String) {
        _historySearchQuery.value = query
    }

    fun updateUserName(name: String) {
        userPrefs.userName = name
        _userName.value = userPrefs.userName
        _showNameEditDialog.value = false
    }

    fun toggleDarkMode(enabled: Boolean) {
        userPrefs.isDarkMode = enabled
        _isDarkMode.value = enabled
    }

    fun upgradeToPro() {
        userPrefs.isPro = true
        _isPro.value = true
        _showProDialog.value = false
    }

    fun toggleProTesting(enabled: Boolean) {
        userPrefs.isPro = enabled
        _isPro.value = enabled
    }

    fun showProDialog() {
        _showProDialog.value = true
    }

    fun dismissProDialog() {
        _showProDialog.value = false
    }

    fun showNameEditDialog() {
        _showNameEditDialog.value = true
    }

    fun dismissNameEditDialog() {
        _showNameEditDialog.value = false
    }

    fun resetApp() {
        viewModelScope.launch {
            problemDao.deleteAll()
            userPrefs.userName = "Tommy"
            userPrefs.isPro = false
            _userName.value = "Tommy"
            _isPro.value = false
            insertInitialSamples()
        }
    }

    fun resetAppPreferences() {
        resetApp()
    }

    // Helper JSON serialization for Room
    private fun serializeSteps(steps: List<SolutionStep>): String {
        val jsonArray = JSONArray()
        steps.forEach { step ->
            val obj = JSONObject().apply {
                put("stepNumber", step.stepNumber)
                put("title", step.title)
                put("expression", step.expression)
                put("explanation", step.explanation)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }

    private fun deserializeSteps(jsonStr: String): List<SolutionStep> {
        val list = mutableListOf<SolutionStep>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    SolutionStep(
                        stepNumber = obj.optInt("stepNumber", i + 1),
                        title = obj.optString("title", ""),
                        expression = obj.optString("expression", ""),
                        explanation = obj.optString("explanation", "")
                    )
                )
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }
}
