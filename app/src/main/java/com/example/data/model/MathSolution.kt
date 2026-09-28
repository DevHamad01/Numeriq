package com.example.data.model

data class SolutionStep(
    val stepNumber: Int,
    val title: String,
    val expression: String,
    val explanation: String
)

data class MathSolution(
    val problemTitle: String,
    val problemCategory: String, // Algebra, Calculus, Geometry, Trigonometry, Arithmetic, etc.
    val finalAnswer: String,
    val steps: List<SolutionStep>,
    val keyConcept: String = "",
    val proTip: String = "",
    val commonPitfall: String = ""
)
