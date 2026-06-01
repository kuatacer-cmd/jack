package com.example.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- Entities ---

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isCompletedToday: Boolean = false,
    val streak: Int = 0,
    val frequency: String = "Daily", // Daily, Weekly
    val category: String = "discipline", // discipline, fitness, sleep, confidence, studying, productivity, dopamine_detox, self_development
    val totalCompletedCount: Int = 0,
    val lastCompletedDate: String? = null // "YYYY-MM-DD"
)

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val date: String, // "YYYY-MM-DD"
    val energyRequired: String = "Medium", // High, Medium, Low
    val difficulty: String = "Medium", // Easy, Medium, Hard
    val durationMinutes: Int = 25,
    val timeSlot: String? = null // e.g. "09:00 - 10:00" allocated by AI planner
)

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "self_development", // discipline, fitness, sleep, studying, etc.
    val progress: Int = 0, // 0 to 100
    val targetDate: String = "",
    val isCompleted: Boolean = false
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int,
    val ambientSoundUsed: String, // None, Rain, Brown Noise, Forest, Synth
    val focusScoreGained: Int,
    val reflection: String? = null
)

@Entity(tableName = "daily_insights")
data class DailyInsight(
    @PrimaryKey val date: String, // "YYYY-MM-DD"
    val moodValue: Int = 3, // 1 to 5
    val sleepHours: Float = 7.5f,
    val energyLevel: Int = 80, // 0 to 100
    val burnoutRisk: Int = 20, // 0 to 100
    val disciplineLevel: Int = 75, // 0 to 100
    val productivityScore: Int = 80
)

@Entity(tableName = "ai_memories")
data class AiMemory(
    @PrimaryKey val key: String,
    val value: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

// --- DAO Interface ---

@Dao
interface FocusMindDao {
    // Habits
    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Long)

    @Query("UPDATE habits SET isCompletedToday = 0 WHERE lastCompletedDate != :todayDate OR lastCompletedDate IS NULL")
    suspend fun resetDailyCompletion(todayDate: String)

    // Tasks
    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY id ASC")
    fun getTasksForDate(date: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    // Goals
    @Query("SELECT * FROM goals ORDER BY progress ASC")
    fun getAllGoals(): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoalById(id: Long)

    // Focus Sessions
    @Query("SELECT * FROM focus_sessions ORDER BY timestamp DESC")
    fun getAllFocusSessions(): Flow<List<FocusSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSession)

    // Daily Insights
    @Query("SELECT * FROM daily_insights ORDER BY date DESC")
    fun getAllDailyInsights(): Flow<List<DailyInsight>>

    @Query("SELECT * FROM daily_insights WHERE date = :date LIMIT 1")
    suspend fun getDailyInsightByDate(date: String): DailyInsight?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyInsight(insight: DailyInsight)

    // AI Memorization
    @Query("SELECT * FROM ai_memories")
    fun getAllMemories(): Flow<List<AiMemory>>

    @Query("SELECT * FROM ai_memories WHERE `key` = :key LIMIT 1")
    suspend fun getMemoryValue(key: String): AiMemory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: AiMemory)
}

// --- App Database ---

@Database(
    entities = [
        Habit::class,
        Task::class,
        Goal::class,
        FocusSession::class,
        DailyInsight::class,
        AiMemory::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): FocusMindDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focusmind_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
