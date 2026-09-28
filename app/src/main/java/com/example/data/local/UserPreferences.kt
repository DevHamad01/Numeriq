package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("numeriq_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_PRO = "is_pro"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_STREAK_DAYS = "streak_days"
        private const val KEY_LAST_ACTIVE_DATE = "last_active_date"
        private const val KEY_DAILY_QUESTIONS_COUNT = "daily_questions_count"
        private const val KEY_DAILY_DATE = "daily_date"
        private const val KEY_ONBOARDING_DONE = "onboarding_done"
        private const val KEY_PREFERRED_TUTOR_MODE = "preferred_tutor_mode"
        const val FREE_DAILY_LIMIT = 5
    }

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, "Tommy") ?: "Tommy"
        set(value) = prefs.edit().putString(KEY_USER_NAME, value.trim().ifEmpty { "Tommy" }).apply()

    var isPro: Boolean
        get() = prefs.getBoolean(KEY_IS_PRO, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_PRO, value).apply()

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var isOnboardingDone: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_DONE, value).apply()

    var preferredTutorMode: String
        get() = prefs.getString(KEY_PREFERRED_TUTOR_MODE, "tutor_ai") ?: "tutor_ai"
        set(value) = prefs.edit().putString(KEY_PREFERRED_TUTOR_MODE, value).apply()

    var streakDays: Int
        get() = prefs.getInt(KEY_STREAK_DAYS, 3)
        private set(value) = prefs.edit().putInt(KEY_STREAK_DAYS, value).apply()

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    fun checkAndRecordDailyActivity() {
        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_LAST_ACTIVE_DATE, "") ?: ""
        if (lastDate != today) {
            // Update streak
            val currentStreak = streakDays
            val cal = java.util.Calendar.getInstance()
            cal.add(java.util.Calendar.DAY_OF_YEAR, -1)
            val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
            if (lastDate == yesterday) {
                streakDays = currentStreak + 1
            } else if (lastDate.isEmpty()) {
                streakDays = 1
            }
            prefs.edit().putString(KEY_LAST_ACTIVE_DATE, today).apply()
        }
    }

    fun getQuestionsUsedToday(): Int {
        val today = getTodayDateString()
        val savedDate = prefs.getString(KEY_DAILY_DATE, "") ?: ""
        return if (savedDate == today) {
            prefs.getInt(KEY_DAILY_QUESTIONS_COUNT, 0)
        } else {
            0
        }
    }

    fun incrementQuestionsUsed(): Int {
        val today = getTodayDateString()
        val savedDate = prefs.getString(KEY_DAILY_DATE, "") ?: ""
        val currentCount = if (savedDate == today) prefs.getInt(KEY_DAILY_QUESTIONS_COUNT, 0) else 0
        val newCount = currentCount + 1
        prefs.edit()
            .putString(KEY_DAILY_DATE, today)
            .putInt(KEY_DAILY_QUESTIONS_COUNT, newCount)
            .apply()
        return newCount
    }

    fun canAskQuestion(): Boolean {
        if (isPro) return true
        return getQuestionsUsedToday() < FREE_DAILY_LIMIT
    }
}
