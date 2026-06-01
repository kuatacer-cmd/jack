const fs = require('fs');

let content = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');

const goalsScreen = `

@Composable
fun GoalsScreen(viewModel: FocusMindViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Your Goals", style = MaterialTheme.typography.headlineMedium, color = TextWhite, fontWeight = FontWeight.Bold)
            Text("Track objectives and milestones.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(2) { idx ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CardGraphite),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(if (idx == 0) "Launch Startup" else "Learn Jetpack Compose", color = TextWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Icon(Icons.Outlined.Flag, contentDescription = null, tint = AccentPurple)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(if (idx == 0) "Develop MVP and launch on ProductHunt." else "Master modern Android UI development.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Progress: \${if (idx == 0) 45 else 80}%", style = MaterialTheme.typography.labelSmall, color = TextWhite)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { if (idx == 0) 0.45f else 0.8f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = AccentBlue,
                        trackColor = BorderGraphite
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = BorderGraphite)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("AI Recommendations", style = MaterialTheme.typography.labelMedium, color = AccentPurple, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (idx == 0) "Your focus on marketing is low. Dedicate time to building a landing page this week." else "You are making steady progress. Try building a complex animation next.", style = MaterialTheme.typography.bodySmall, color = TextWhite.copy(alpha = 0.8f))
                }
            }
        }
    }
}
`;

const habitsScreen = `

@Composable
fun HabitsScreen(viewModel: FocusMindViewModel) {
    val categories = listOf("Health", "Learning", "Productivity", "Sports", "Self-Dev")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Habits Tracker", style = MaterialTheme.typography.headlineMedium, color = TextWhite, fontWeight = FontWeight.Bold)
        Text("Build discipline with daily repetition.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentPurple.copy(alpha = 0.2f),
                        selectedLabelColor = AccentPurple,
                        labelColor = TextWhite
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (selectedCategory == cat) AccentPurple else BorderGraphite,
                        enabled = true,
                        selected = selectedCategory == cat
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(3) { i ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardGraphite),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Morning Workout", color = TextWhite, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("Streak: 12 days 🔥", color = AccentPurple, style = MaterialTheme.typography.labelSmall)
                        }
                        Checkbox(
                            checked = i == 0,
                            onCheckedChange = {},
                            colors = CheckboxDefaults.colors(checkedColor = AccentPurple, uncheckedColor = TextMuted)
                        )
                    }
                }
            }
        }
    }
}
`;

const glowUpScreen = `

@Composable
fun GlowUpScreen(viewModel: FocusMindViewModel) {
    val programs = listOf(
        "30 Day Glow-Up" to "A complete transformation of body and mind.",
        "Discipline Reset" to "Regain control and eliminate bad habits.",
        "Monk Mode" to "Absolute focus and isolation for deep work.",
        "Dopamine Detox" to "Reset your brain's reward system.",
        "Study Beast" to "Maximize learning capacity and retention.",
        "Confidence Upgrade" to "Build unshakable self-belief."
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Glow-Up Programs", style = MaterialTheme.typography.headlineMedium, color = TextWhite, fontWeight = FontWeight.Bold)
            Text("Transform your life with AI-guided programs.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(programs.size) { i ->
            val p = programs[i]
            Card(
                colors = CardDefaults.cardColors(containerColor = CardGraphite),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(p.first, color = TextWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = AccentBlue)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(p.second, color = TextMuted, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                    ) {
                        Text("Start Program", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
`;

const analyticsScreen = `

@Composable
fun AnalyticsScreen(viewModel: FocusMindViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Analytics & Insights", style = MaterialTheme.typography.headlineMedium, color = TextWhite, fontWeight = FontWeight.Bold)
            Text("AI analysis of your performance.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Card(colors = CardDefaults.cardColors(containerColor = CardGraphite), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Productivity", color = TextMuted)
                        Text("85 / 100", color = TextWhite, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = CardGraphite), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Focus Score", color = TextMuted)
                        Text("92 / 100", color = TextWhite, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AccentPurple.copy(alpha=0.15f)), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.AutoGraph, contentDescription = null, tint = AccentPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Weekly AI Review", color = AccentPurple, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Your performance peaked on Wednesday but dropped on Friday. Try adjusting your sleep latency to improve weekend energy.", color = TextWhite, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
`;

const profileScreen = `

@Composable
fun ProfileScreen(viewModel: FocusMindViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(DarkGraphite),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(50.dp), tint = TextMuted)
        }
        Spacer(Modifier.height(16.dp))
        Text("Darth Vader", style = MaterialTheme.typography.headlineMedium, color = TextWhite, fontWeight = FontWeight.Bold)
        Text("Level 15 Discipline", style = MaterialTheme.typography.bodyMedium, color = AccentPurple, fontWeight = FontWeight.Bold)
        
        Spacer(Modifier.height(32.dp))
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("42", style = MaterialTheme.typography.headlineSmall, color = TextWhite, fontWeight = FontWeight.Bold)
                Text("Completed", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("14", style = MaterialTheme.typography.headlineSmall, color = TextWhite, fontWeight = FontWeight.Bold)
                Text("Day Streak", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("5", style = MaterialTheme.typography.headlineSmall, color = TextWhite, fontWeight = FontWeight.Bold)
                Text("Achievements", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
`;

content += goalsScreen + habitsScreen + glowUpScreen + analyticsScreen + profileScreen;
fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', content, 'utf8');

console.log("Appended screens.");
