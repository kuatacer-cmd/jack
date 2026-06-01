package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
// ...
import com.example.data.database.*
import com.example.data.repository.FocusMindRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class Message(
    val sender: String, // "user", "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isVoice: Boolean = false
)

data class GlowUpRoadmap(
    val category: String,
    val title: String,
    val summary: String,
    val steps: List<String>
)

class FocusMindViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FocusMindRepository
    private val prefs = application.getSharedPreferences("FocusMindSettings", Context.MODE_PRIVATE)

    private val sdf = SimpleDateFormat("yyyy-MM-DD", Locale.US)
    private val displaySdf = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())

    val todayStr: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val formattedDate: String = displaySdf.format(Date())

    // Settings state
    private val _currentLanguage = MutableStateFlow(prefs.getString("language", "English") ?: "English")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _currentTheme = MutableStateFlow(prefs.getString("theme", "System") ?: "System")
    val currentTheme: StateFlow<String> = _currentTheme.asStateFlow()

    // UI Navigation state
    private val _currentScreenState = MutableStateFlow("landing") // "landing", "main"
    val currentScreenState: StateFlow<String> = _currentScreenState.asStateFlow()

    private val _currentTabState = MutableStateFlow("dashboard") // "dashboard", "chat", "planner", "focus"
    val currentTabState: StateFlow<String> = _currentTabState.asStateFlow()

    // Database Flows
    val habits: StateFlow<List<Habit>>
    val goals: StateFlow<List<Goal>>
    val focusSessions: StateFlow<List<FocusSession>>
    val dailyInsights: StateFlow<List<DailyInsight>>
    val memories: StateFlow<List<AiMemory>>
    val tasks: StateFlow<List<Task>>

    // Chat states
    private val _chatHistory = MutableStateFlow<List<Message>>(emptyList())
    val chatHistory: StateFlow<List<Message>> = _chatHistory.asStateFlow()

    private val _isChatGenerating = MutableStateFlow(false)
    val isChatGenerating: StateFlow<Boolean> = _isChatGenerating.asStateFlow()

    // Voice session simulations
    private val _voiceState = MutableStateFlow("idle") // "idle", "listening", "speaking"
    val voiceState: StateFlow<String> = _voiceState.asStateFlow()

    private val _voiceResponseText = MutableStateFlow("")
    val voiceResponseText: StateFlow<String> = _voiceResponseText.asStateFlow()

    // Focus state
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _timeLeftSeconds = MutableStateFlow(1500) // 25 Min
    val timeLeftSeconds: StateFlow<Int> = _timeLeftSeconds.asStateFlow()

    private val _ambientSound = MutableStateFlow("None") // None, Rain, Brown Noise, Forest, Synth
    val ambientSound: StateFlow<String> = _ambientSound.asStateFlow()

    private val _isBreathingIn = MutableStateFlow(true)
    val isBreathingIn: StateFlow<Boolean> = _isBreathingIn.asStateFlow()

    // GlowUp Roadmaps & Adaptive Planner insights
    private val _glowUpRoadmap = MutableStateFlow<GlowUpRoadmap?>(null)
    val glowUpRoadmap: StateFlow<GlowUpRoadmap?> = _glowUpRoadmap.asStateFlow()

    private val _isGlowUpLoading = MutableStateFlow(false)
    val isGlowUpLoading: StateFlow<Boolean> = _isGlowUpLoading.asStateFlow()

    private val _plannerAdvice = MutableStateFlow("Review tasks. High energy reserves. Execute focus blocks.")
    val plannerAdvice: StateFlow<String> = _plannerAdvice.asStateFlow()

    private val _isPlannerLoading = MutableStateFlow(false)
    val isPlannerLoading: StateFlow<Boolean> = _isPlannerLoading.asStateFlow()

    // Scores
    val focusScore: StateFlow<Int>
    val productivityScore: StateFlow<Int>
    val disciplineLevel: StateFlow<Int>
    val currentStreak: StateFlow<Int>

    // Daily Insight today
    private val _todayInsightState = MutableStateFlow<DailyInsight?>(null)
    val todayInsightState: StateFlow<DailyInsight?> = _todayInsightState.asStateFlow()

    private var timerJob: Job? = null
    private var breathingJob: Job? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FocusMindRepository(database.dao())

        // Collect db streams
        habits = repository.allHabits.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        goals = repository.allGoals.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        focusSessions = repository.allFocusSessions.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        dailyInsights = repository.allDailyInsights.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        memories = repository.allMemories.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        tasks = repository.getTasksForDate(todayStr).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

        // Calculate scores dynamically based on stats
        focusScore = combine(focusSessions, habits) { sessions, habitList ->
            val todaySessions = sessions.filter { 
                val cal = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                val sdfSame = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                sdfSame.format(cal.time) == todayStr
            }
            val base = todaySessions.sumOf { it.focusScoreGained }
            val completedHabitBonus = habitList.count { it.isCompletedToday } * 15
            (base + completedHabitBonus).coerceIn(0, 100)
        }.stateIn(viewModelScope, SharingStarted.Lazily, 75)

        productivityScore = combine(tasks, habits) { taskList, habitList ->
            if (taskList.isEmpty() && habitList.isEmpty()) return@combine 80
            val completedTasks = taskList.count { it.isCompleted }
            val totalTasks = taskList.size
            val completedHabits = habitList.count { it.isCompletedToday }
            val totalHabits = habitList.size

            val taskRatio = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 1.0f
            val habitRatio = if (totalHabits > 0) completedHabits.toFloat() / totalHabits else 1.0f
            ((taskRatio * 60) + (habitRatio * 40)).toInt().coerceIn(20, 100)
        }.stateIn(viewModelScope, SharingStarted.Lazily, 80)

        disciplineLevel = habits.map { hList ->
            if (hList.isEmpty()) return@map 70
            val totalStreaks = hList.sumOf { it.streak }
            val completions = hList.count { it.isCompletedToday }
            (50 + (totalStreaks * 2) + (completions * 8)).coerceIn(40, 100)
        }.stateIn(viewModelScope, SharingStarted.Lazily, 70)

        currentStreak = habits.map { hList ->
            hList.maxOfOrNull { it.streak } ?: 0
        }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

        viewModelScope.launch {
            // Initial data pre-pop
            repository.checkAndResetDailyCompletion(todayStr)
            repository.prepopulateIfEmpty(todayStr)

            // Setup today's analytics assessment
            val todayInsight = repository.getDailyInsight(todayStr) ?: DailyInsight(
                date = todayStr, moodValue = 3, sleepHours = 7.5f, energyLevel = 80, burnoutRisk = 20, disciplineLevel = 75, productivityScore = 80
            )
            _todayInsightState.value = todayInsight
            repository.saveDailyInsight(todayInsight)

            // Setup starter assistant welcome message
            _chatHistory.value = listOf(
                Message(
                    sender = "ai",
                    text = "Welcome to FocusMind, I am your second brain and personal visual coach. I have pre-loaded your discipline dashboard, synced daily habits, and configured memory pathways. How can I help you elevate your focus or design your route today?"
                )
            )
        }

        startBreathingAnimation()
    }

    // Navigation triggers
    fun setScreen(screen: String) {
        _currentScreenState.value = screen
    }

    fun setTab(tab: String) {
        _currentTabState.value = tab
    }

    // Daily Insights update
    fun updateMoodAndSleep(mood: Int, sleep: Float, energy: Int) {
        viewModelScope.launch {
            val current = _todayInsightState.value ?: DailyInsight(date = todayStr)
            val baseBurnout = (100 - energy) + (6 - mood) * 10
            val calculatedRisk = baseBurnout.coerceIn(5, 95)
            val updated = current.copy(
                moodValue = mood,
                sleepHours = sleep,
                energyLevel = energy,
                burnoutRisk = calculatedRisk,
                productivityScore = productivityScore.value,
                disciplineLevel = disciplineLevel.value
            )
            _todayInsightState.value = updated
            repository.saveDailyInsight(updated)
        }
    }

    // Complete/uncomplete habits
    fun toggleHabit(habit: Habit) {
        viewModelScope.launch {
            if (habit.isCompletedToday) {
                repository.uncompleteHabit(habit)
            } else {
                repository.completeHabit(habit, todayStr)
            }
        }
    }

    fun addHabit(name: String, category: String, frequency: String = "Daily") {
        viewModelScope.launch {
            repository.insertHabit(Habit(name = name, category = category, frequency = frequency))
        }
    }

    fun removeHabit(id: Long) {
        viewModelScope.launch {
            repository.deleteHabit(id)
        }
    }

    // Tasks and Planners
    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun addTask(title: String, energy: String = "Medium") {
        viewModelScope.launch {
            repository.insertTask(Task(title = title, date = todayStr, energyRequired = energy))
        }
    }

    fun removeTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    // Goals managers
    fun addGoal(title: String, category: String, targetDate: String) {
        viewModelScope.launch {
            repository.insertGoal(Goal(title = title, category = category, targetDate = targetDate, progress = 0))
        }
    }

    fun updateGoalProgress(goal: Goal, progress: Int) {
        viewModelScope.launch {
            repository.updateGoal(goal.copy(progress = progress.coerceIn(0, 100), isCompleted = progress >= 100))
        }
    }

    fun removeGoal(id: Long) {
        viewModelScope.launch {
            repository.deleteGoal(id)
        }
    }

    // Focus Session operations
    fun setAmbientSound(sound: String) {
        _ambientSound.value = sound
    }

    fun toggleFocusTimer(durationMinutes: Int = 25) {
        if (_isTimerRunning.value) {
            // Stop and record
            stopFocusTimer(completed = false)
        } else {
            // Start
            _timeLeftSeconds.value = durationMinutes * 60
            _isTimerRunning.value = true
            timerJob = viewModelScope.launch(Dispatchers.Default) {
                while (_timeLeftSeconds.value > 0) {
                    delay(1000)
                    _timeLeftSeconds.value -= 1
                }
                // Completed session!
                stopFocusTimer(completed = true, durationMinutes = durationMinutes)
            }
        }
    }

    private fun stopFocusTimer(completed: Boolean, durationMinutes: Int = 25) {
        _isTimerRunning.value = false
        timerJob?.cancel()
        timerJob = null

        if (completed) {
            viewModelScope.launch {
                val baseScore = durationMinutes * 2
                val session = FocusSession(
                    durationSeconds = durationMinutes * 60,
                    ambientSoundUsed = _ambientSound.value,
                    focusScoreGained = baseScore.coerceIn(10, 50),
                    reflection = "Completed ultra focus segment with Sound: ${_ambientSound.value}"
                )
                repository.insertFocusSession(session)
                // Reset standard timer
                _timeLeftSeconds.value = 1500
            }
        }
    }

    private fun startBreathingAnimation() {
        breathingJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                _isBreathingIn.value = true
                delay(4000) // 4s inhale
                _isBreathingIn.value = false
                delay(4000) // 4s exhale
            }
        }
    }

    fun setLanguage(language: String) {
        _currentLanguage.value = language
        prefs.edit().putString("language", language).apply()
    }

    fun setTheme(theme: String) {
        _currentTheme.value = theme
        prefs.edit().putString("theme", theme).apply()
    }

    // System instruction helper incorporating memory block
    private suspend fun buildSystemInstruction(): String {
        val list = memories.value
        val userName = list.firstOrNull { it.key == "user_name" }?.value ?: "Elite Thinker"
        val userPref = list.firstOrNull { it.key == "communication_preference" }?.value ?: "Direct and structured"
        val learningStyle = list.firstOrNull { it.key == "user_learning_style" }?.value ?: "Pragmatic builder"

        return """
            You are "FocusMind", a premium futuristic AI operating system, cognitive coach, and cybernetic second brain.
            Your persona is inspired by Nothing Tech, Linear, Apple minimalism, and Notion's structural depth.
            You are conversational, calm, elite, and objective. You avoid excessive hype, promotional words, or robotic AI patterns.
            
            Current User Context:
            - Name: $userName
            - Style: $userPref
            - Background: $learningStyle
            
            Deliver extremely focused, structural advice. When requested for format, maintain elegant bullet points or compact summaries.
        """.trimIndent()
    }

    // Gemini Chat actions
    fun sendChatMessage(text: String) {
        if (text.trim().isEmpty()) return
        val userMsg = Message(sender = "user", text = text)
        _chatHistory.value = _chatHistory.value + userMsg

        _isChatGenerating.value = true
        viewModelScope.launch {
            try {
                val sys = buildSystemInstruction()
                val prompt = """
                    User text: "$text"
                    Provide an elegant coaching answer. If the user mentions updating their name (e.g. "my name is Alex"), goals, habit triggers, or memory states, state what you have updated so I can record it in my persistent database storage. Keep answers clean, calm, and visually inspiring.
                """.trimIndent()
                val aiResponse = repository.askGemini(prompt, sys)
                
                // Inspect response for name change or memory key requests and save them
                if (aiResponse.contains("name is now", ignoreCase = true) || aiResponse.contains("memorized", ignoreCase = true)) {
                    // Check if name updated
                    val match = Regex("name is officially (\\w+)", RegexOption.IGNORE_CASE).find(aiResponse)
                    match?.let {
                        val newName = it.groupValues[1]
                        repository.saveMemory("user_name", newName)
                    }
                }

                _chatHistory.value = _chatHistory.value + Message(sender = "ai", text = aiResponse)
            } finally {
                _isChatGenerating.value = false
            }
        }
    }

    // AI Adaptive Planner: adapt daily tasks dynamically based on user's energy
    fun triggerPlannerAdaptation() {
        val currentEnergy = _todayInsightState.value?.energyLevel ?: 80
        val plist = tasks.value
        val taskTitles = plist.joinToString { "- ${it.title} (Energy: ${it.energyRequired})" }

        _isPlannerLoading.value = true
        viewModelScope.launch {
            try {
                val sys = buildSystemInstruction()
                val prompt = """
                    My current energy score is $currentEnergy%.
                    Here is my planned list of tasks for today:
                    $taskTitles
                    
                    Re-allocate order or suggest adjustments or custom cognitive focus blocks based on this energy metric. Speak as a premium operating system advisor. Deliver compact structured bullet slots.
                """.trimIndent()

                val aiResponse = repository.askGemini(prompt, sys)
                _plannerAdvice.value = aiResponse
            } catch (e: Exception) {
                _plannerAdvice.value = "Schedule aligned. Energy set to $currentEnergy%. Focus on low-overhead items first."
            } finally {
                _isPlannerLoading.value = false
            }
        }
    }

    // AI Glow-Up roadmap generator
    fun generateGlowUpRoadmap(category: String) {
        _isGlowUpLoading.value = true
        _glowUpRoadmap.value = null
        viewModelScope.launch {
            try {
                val sys = buildSystemInstruction()
                val prompt = """
                    Generate a premium Futuristic discipline Roadmap for category: "$category".
                    Reply ONLY in JSON format corresponding to this schema:
                    {
                      "category": "$category",
                      "title": "Roadmap title",
                      "summary": "Short atmospheric 2-sentence description of the roadmap theme",
                      "steps": ["Step 1 mission", "Step 2 mission", "Step 3 mission"]
                    }
                """.trimIndent()

                val responseJson = repository.askGemini(prompt, sys)
                
                // Safe JSON parse implementation
                val parsed = parseRoadmapJson(responseJson, category)
                _glowUpRoadmap.value = parsed
            } catch (e: Exception) {
                _glowUpRoadmap.value = GlowUpRoadmap(
                    category = category,
                    title = "Futuristic ${category.replaceFirstChar { it.uppercase() }} Ascent",
                    summary = "Your system has designed a customized self-development pathway focused on strategic biological and cognitive calibration.",
                    steps = listOf(
                        "Phase Alpha: Strict 20-minute sensory isolation blocks.",
                        "Phase Beta: Circadian rhythm synchronization at 06:00.",
                        "Phase Gamma: Execute dopamine fasting protocols daily."
                    )
                )
            } finally {
                _isGlowUpLoading.value = false
            }
        }
    }

    private fun parseRoadmapJson(json: String, fallbackCategory: String): GlowUpRoadmap {
        // Since we want to ensure zero failures, we'll extract using regex or fall back nicely if parsing fails
        try {
            val titleRegex = Regex("\"title\"\\s*:\\s*\"([^\"]+)\"").find(json)
            val summaryRegex = Regex("\"summary\"\\s*:\\s*\"([^\"]+)\"").find(json)
            val stepsMatch = Regex("\"steps\"\\s*:\\s*\\[([^\\]]+)\\]").find(json)

            val title = titleRegex?.groupValues?.get(1) ?: "Ascent Ascent Route"
            val summary = summaryRegex?.groupValues?.get(1) ?: "AI neural configuration routine designed to optimize stamina and focus."
            val stepsRaw = stepsMatch?.groupValues?.get(1) ?: ""
            val steps = stepsRaw.split(",")
                .map { it.replace("\"", "").trim() }
                .filter { it.isNotEmpty() }

            return GlowUpRoadmap(
                category = fallbackCategory,
                title = title,
                summary = summary,
                steps = if (steps.isEmpty()) listOf("Calibrate mental margins", "Unpack executive actions") else steps
            )
        } catch (e: Exception) {
            throw e
        }
    }

    // Simulated Voice AI assistant wave
    fun startVoiceSession() {
        if (_voiceState.value != "idle") return
        _voiceState.value = "listening"
        viewModelScope.launch {
            delay(3500) // Simulated 3.5 seconds user talk
            _voiceState.value = "speaking"
            _voiceResponseText.value = "Processing cognitive stream..."

            val sys = buildSystemInstruction()
            val prompt = "User has issued a rapid voice command. Give a very short, supportive, 1-sentence response as a premium system coach. Then confirm focus state."
            try {
                val response = repository.askGemini(prompt, sys)
                _voiceResponseText.value = response
            } catch (e: Exception) {
                _voiceResponseText.value = "Voice channel established. Maintain perfect concentration."
            }
            delay(4000) // Sim speaking text
            _voiceState.value = "idle"
        }
    }

    // Save personalized AI memory entry manually
    fun saveCustomMemory(key: String, value: String) {
        viewModelScope.launch {
            repository.saveMemory(key, value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        breathingJob?.cancel()
    }
}
