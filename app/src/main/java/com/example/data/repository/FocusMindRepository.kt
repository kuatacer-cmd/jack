package com.example.data.repository

import com.example.data.api.GeminiClient
import com.example.data.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FocusMindRepository(private val dao: FocusMindDao) {

    val allHabits: Flow<List<Habit>> = dao.getAllHabits()
    val allGoals: Flow<List<Goal>> = dao.getAllGoals()
    val allFocusSessions: Flow<List<FocusSession>> = dao.getAllFocusSessions()
    val allDailyInsights: Flow<List<DailyInsight>> = dao.getAllDailyInsights()
    val allMemories: Flow<List<AiMemory>> = dao.getAllMemories()

    fun getTasksForDate(date: String): Flow<List<Task>> = dao.getTasksForDate(date)
    fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    suspend fun getDailyInsight(date: String): DailyInsight? = withContext(Dispatchers.IO) {
        dao.getDailyInsightByDate(date)
    }

    suspend fun saveDailyInsight(insight: DailyInsight) = withContext(Dispatchers.IO) {
        dao.insertDailyInsight(insight)
    }

    suspend fun insertHabit(habit: Habit) = withContext(Dispatchers.IO) {
        dao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) = withContext(Dispatchers.IO) {
        dao.updateHabit(habit)
    }

    suspend fun deleteHabit(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteHabitById(id)
    }

    suspend fun completeHabit(habit: Habit, date: String) = withContext(Dispatchers.IO) {
        if (habit.lastCompletedDate == date) return@withContext // already completed today

        val isCompletedToday = true
        val streak = habit.streak + 1
        val count = habit.totalCompletedCount + 1

        val updated = habit.copy(
            isCompletedToday = isCompletedToday,
            streak = streak,
            totalCompletedCount = count,
            lastCompletedDate = date
        )
        dao.updateHabit(updated)
    }

    suspend fun uncompleteHabit(habit: Habit) = withContext(Dispatchers.IO) {
        if (!habit.isCompletedToday) return@withContext

        val isCompletedToday = false
        val streak = (habit.streak - 1).coerceAtLeast(0)
        val count = (habit.totalCompletedCount - 1).coerceAtLeast(0)

        val updated = habit.copy(
            isCompletedToday = isCompletedToday,
            streak = streak,
            totalCompletedCount = count,
            lastCompletedDate = null
        )
        dao.updateHabit(updated)
    }

    suspend fun insertTask(task: Task) = withContext(Dispatchers.IO) {
        dao.insertTask(task)
    }

    suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        dao.updateTask(task)
    }

    suspend fun deleteTask(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteTaskById(id)
    }

    suspend fun insertGoal(goal: Goal) = withContext(Dispatchers.IO) {
        dao.insertGoal(goal)
    }

    suspend fun updateGoal(goal: Goal) = withContext(Dispatchers.IO) {
        dao.updateGoal(goal)
    }

    suspend fun deleteGoal(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteGoalById(id)
    }

    suspend fun insertFocusSession(session: FocusSession) = withContext(Dispatchers.IO) {
        dao.insertFocusSession(session)
    }

    suspend fun saveMemory(key: String, value: String) = withContext(Dispatchers.IO) {
        dao.insertMemory(AiMemory(key, value, System.currentTimeMillis()))
    }

    suspend fun getMemoryValue(key: String): String? = withContext(Dispatchers.IO) {
        dao.getMemoryValue(key)?.value
    }

    // Reset daily tags on a new day
    suspend fun checkAndResetDailyCompletion(todayDate: String) = withContext(Dispatchers.IO) {
        dao.resetDailyCompletion(todayDate)
    }

    // Prepopulate database with polished default data sets
    suspend fun prepopulateIfEmpty(todayDate: String) = withContext(Dispatchers.IO) {
        val habitsList = dao.getAllHabits().firstOrNull()
        if (habitsList.isNullOrEmpty()) {
            // Populate premium starting habits
            dao.insertHabit(Habit(name = "15-Min Focused Meditation", frequency = "Daily", category = "discipline"))
            dao.insertHabit(Habit(name = "Perform Hydration Ritual (3L)", frequency = "Daily", category = "fitness"))
            dao.insertHabit(Habit(name = "Read 10 Pages of Self-Improvement", frequency = "Daily", category = "studying"))
            dao.insertHabit(Habit(name = "8 Hours Cognitive Reset Sleep", frequency = "Daily", category = "sleep"))
            dao.insertHabit(Habit(name = "Dopamine Detox (No Feed Scrolling)", frequency = "Daily", category = "dopamine_detox"))

            // Populate sample tasks
            dao.insertTask(Task(title = "Review weekly focus roadmap", date = todayDate, energyRequired = "Medium", difficulty = "Easy"))
            dao.insertTask(Task(title = "Execute ultra-focus coding blocks", date = todayDate, energyRequired = "High", difficulty = "Hard", durationMinutes = 50))
            dao.insertTask(Task(title = "Perform physical stretch routines", date = todayDate, energyRequired = "Low", difficulty = "Easy"))

            // Populate starting goals
            dao.insertGoal(Goal(title = "Attain 21-Day Discipline Streak", category = "discipline", progress = 14, targetDate = "June 15, 2026"))
            dao.insertGoal(Goal(title = "Complete 'Second Brain' Architecture study", category = "studying", progress = 40, targetDate = "June 30, 2026"))

            // Populate first day insight
            dao.insertDailyInsight(
                DailyInsight(
                    date = todayDate,
                    moodValue = 4,
                    sleepHours = 7.8f,
                    energyLevel = 85,
                    burnoutRisk = 15,
                    disciplineLevel = 90,
                    productivityScore = 80
                )
            )

            // Populate first AI Memories
            dao.insertMemory(AiMemory("user_name", "FocusMind Creator"))
            dao.insertMemory(AiMemory("communication_preference", "Enthusiastic, mentoring, analytical, and highly structured digital coach."))
            dao.insertMemory(AiMemory("user_learning_style", "Pragmatic builder who loves dark minimalism, speed, and clean aesthetic discipline."))
        }
    }

    // Call external Gemini API via client
    suspend fun askGemini(prompt: String, systemInstruction: String? = null): String {
        return GeminiClient.generate(prompt, systemInstruction)
    }
}
