const fs = require('fs');

let content = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');

const replacements = [
    { target: `text = "FOCUSMIND v1.0 • SYSTEM ACTIVE"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.system_active)` },
    { target: `text = "Your AI-powered\\nsecond brain."`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.hero_title)` },
    { target: `text = "FocusMind helps you focus, organize your life, build discipline and achieve your goals with AI."`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.hero_subtitle)` },
    { target: `text = "Get Started"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.get_started)` },
    { target: `text = "Try FocusMind Direct"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.try_focusmind_direct)` },
    { target: `text = "Neural System Log"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.neural_system_log)` },
    { target: `Text("FOCUS SCORE"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score)` },
    { target: `Text("$focusScoreVal%"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, focusScoreVal.toString()))` },
    { target: `Text("PRODUCTIVITY"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.productivity)` },
    { target: `Text("$prodScoreVal%"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, prodScoreVal.toString()))` },
    { target: `Text("DISCIPLINE"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.discipline)` },
    { target: `Text("LEVEL $discScoreVal"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.level_value, discScoreVal.toString()))` },
    { target: `Text("Calibrated daily"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.calibrated_daily)` },
    { target: `Text("CORE STREAK"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.core_streak)` },
    { target: `Text("$streakVal Days"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.streak_days, streakVal.toString()))` },
    { target: `Text("Keep momentum high"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.keep_momentum_high)` },
    { target: `Text("BIOLOGICAL CALIBRATION"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.biological_calibration)` },
    { target: `Text("Cognitive Sleep Reset"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.cognitive_sleep_reset)` },
    { target: `Text(String.format(Locale.US, "%.1f Hrs", currentSleepHours)`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.sleep_hours_value, currentSleepHours)` },
    { target: `Text("$currentEnergyLevel%"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, currentEnergyLevel.toString()))` },
    { target: `text = "\${todayInsight?.burnoutRisk ?: 20}%"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, (todayInsight?.burnoutRisk ?: 20).toString())` },
    { target: `Text("COGNITIVE MEMORY SYSTEM"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.cognitive_memory_system)` },
    { target: `text = "Your second brain persists habits, preferred speech, and user learnings. Update parameters manually below."`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.cognitive_memory_desc)` },
    { target: `Text("Memory Key (e.g. style)"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.memory_key_hint)` },
    { target: `Text("Memory Content"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.memory_content_hint)` },
    { target: `Text("Teach your second brain"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.teach_second_brain)` },
    { target: `text = "VOICE LINK ESTABLISHED"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.voice_link_established)` },
    { target: `Text("DYNAMICAL ENERGY RE-ALIGNMENT"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.dynamical_energy_realignment)` },
    { target: `text = "AI inspects task complexity and aligns agenda elements with your physical energy metric (current: \${todayInsight?.energyLevel ?: 85}%)."`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.ai_inspects_energy, (todayInsight?.energyLevel ?: 85).toString())` },
    { target: `Text("Re-arranging cognitive blocks..."`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.rearranging_cognitive_blocks)` },
    { target: `Text("Adapt Plan via AI"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.adapt_plan_via_ai)` },
    { target: `Text("No agenda slots. Set blocks above."`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.no_agenda_slots)` },
    { target: `Text("GLOW-UP PLATFORM"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.glow_up_platform)` },
    { target: `text = "Optimize discipline, confidence, dopamine levels, sleep cycles, and physical performance. Generate structured roadmaps."`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.optimize_discipline)` },
    { target: `Text("Calibrating self-improvement roadmap..."`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.calibrating_roadmap)` },
    { target: `Text("Select a category above to configure your AI Ascent Roadmap."`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.select_category_to_configure)` },
    { target: `Text("Self-Improvement Milestones"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.self_improvement_milestones)` },
    { target: `Text("No active milestones."`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.no_active_milestones)` },
    { target: `Text("Inject Agenda Block"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.inject_agenda_block)` },
    { target: `Text("Task Title"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.task_title_hint)` },
    { target: `Text("Energy required"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.energy_required)` },
    { target: `Text("Reject"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.reject)` },
    { target: `Text("Append Block"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.append_block)` },
    { target: `Text("Inject Habit Engine"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.inject_habit_engine)` },
    { target: `Text("Habit Title"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.habit_title_hint)` },
    { target: `Text("Category Theme"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.category_theme)` },
    { target: `Text("Inject"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.inject)` },
    { target: `text = "ENERGY: \${task.energyRequired.uppercase()}"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.energy_label, task.energyRequired.uppercase())` },
    { target: `Text("\${habit.streak}d Streak"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.streak_days_short, habit.streak.toString()))` },
    { target: `Text("\${goal.progress}%"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, goal.progress.toString()))` },
    { target: `text = "SENSORY COGNITIVE EXPULSION"`, replacement: `text = androidx.compose.ui.res.stringResource(com.example.R.string.sensory_cognitive_expulsion)` },
    { target: `Text("AMBIENT FIELD ISOLATION"`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.ambient_field_isolation)` }
];

replacements.forEach(r => {
    content = content.replace(r.target, r.replacement);
});

fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', content, 'utf8');

console.log("Updated KT resource successfully");
