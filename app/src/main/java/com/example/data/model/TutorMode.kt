package com.example.data.model

enum class TutorMode(
    val id: String,
    val title: String,
    val subtitle: String,
    val isProOnly: Boolean = false,
    val systemPromptAddition: String
) {
    TUTOR_AI(
        id = "tutor_ai",
        title = "Tutor AI",
        subtitle = "Standard step-by-step guidance",
        isProOnly = false,
        systemPromptAddition = "Provide standard clear step-by-step mathematical explanations accessible for high school and college students."
    ),
    TUTOR_AI_PRO(
        id = "tutor_ai_pro",
        title = "Tutor AI Pro",
        subtitle = "Detailed derivations & proofs",
        isProOnly = false,
        systemPromptAddition = "Provide comprehensive academic depth, rigorous proofs, rigorous justification of each formula, and highlight key theorems used."
    ),
    TUTOR_AI_MAX(
        id = "tutor_ai_max",
        title = "Tutor AI Max",
        subtitle = "Deep theoretical breakdowns & alternate methods",
        isProOnly = false,
        systemPromptAddition = "Provide maximum pedagogical depth, presenting alternate solving techniques (e.g. geometric intuition, calculus vs algebraic methods), common student traps, and speed shortcuts."
    );

    companion object {
        fun fromId(id: String): TutorMode {
            return entries.firstOrNull { it.id == id } ?: TUTOR_AI
        }
    }
}
