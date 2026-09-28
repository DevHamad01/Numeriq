package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solved_problems")
data class SolvedProblem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val question: String,
    val recognizedText: String? = null,
    val category: String = "General Math",
    val finalAnswer: String,
    val stepsJson: String, // JSON array of SolutionStep
    val tutorMode: String = TutorMode.TUTOR_AI.id,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val keyConcept: String = "",
    val proTip: String = "",
    val commonPitfall: String = ""
)
