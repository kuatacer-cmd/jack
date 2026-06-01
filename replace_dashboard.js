const fs = require('fs');

let content = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');

const newDashboard = `@Composable
fun DashboardScreen(viewModel: FocusMindViewModel) {
    val focusScoreVal by viewModel.focusScore.collectAsStateWithLifecycle()
    val prodScoreVal by viewModel.productivityScore.collectAsStateWithLifecycle()
    val discScoreVal by viewModel.disciplineLevel.collectAsStateWithLifecycle()
    val streakVal by viewModel.currentStreak.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(text = "Hello, System User.", style = MaterialTheme.typography.displaySmall, color = TextWhite, fontWeight = FontWeight.Bold)
                Text(text = viewModel.formattedDate, style = MaterialTheme.typography.labelMedium, color = AccentPurple)
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                CustomMetricCard(modifier = Modifier.weight(1f), title = "Focus", value = focusScoreVal.toString(), icon = Icons.Outlined.CenterFocusStrong)
                CustomMetricCard(modifier = Modifier.weight(1f), title = "Productivity", value = prodScoreVal.toString(), icon = Icons.Outlined.BarChart)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                CustomMetricCard(modifier = Modifier.weight(1f), title = "Discipline", value = "Lvl $discScoreVal", icon = Icons.Outlined.CheckCircle)
                CustomMetricCard(modifier = Modifier.weight(1f), title = "Streak", value = "$streakVal days", icon = Icons.Outlined.Whatshot)
            }
        }
        
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardGraphite), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Today's Agenda", style = MaterialTheme.typography.titleMedium, color = TextWhite, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("• 3 Tasks scheduled via AI Planner", color = TextWhite)
                    Text("• Morning Workout (Habit pending)", color = TextWhite)
                    Text("• 1 Upcoming Event (14:00 Sync)", color = TextWhite)
                }
            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = AccentPurple.copy(alpha = 0.15f)), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = AccentPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Insights", color = AccentPurple, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Based on your data, your focus peaks between 10 AM and 1 PM. I have suggested scheduling your hardest task for this window.", color = TextWhite)
                }
            }
        }

        item {
            Text("Quick Actions", style = MaterialTheme.typography.titleMedium, color = TextWhite, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { viewModel.setTab("planner") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)) {
                    Text("Planner")
                }
                Button(onClick = { viewModel.setTab("focus") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)) {
                    Text("Focus Session")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.setTab("chat") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = CardGraphite)) {
                Text("Talk to AI Assistant", color = TextWhite)
            }
        }
    }
}

@Composable
fun CustomMetricCard(modifier: Modifier = Modifier, title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(colors = CardDefaults.cardColors(containerColor = CardGraphite), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = TextMuted)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = TextMuted, style = MaterialTheme.typography.bodySmall)
            Text(value, color = TextWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
    }
}
`;

const regex = /@Composable\nfun DashboardScreen\(viewModel: FocusMindViewModel\) \{[\s\S]*?(?=\n\/\/ ==========================================|\n@Composable\nfun ChatScreen)/;

// Wait, let's just do a simpler replace. I will find where `fun DashboardScreen` starts and where `fun ChatScreen` starts to replace the whole block.
let dashStart = content.indexOf('@Composable\nfun DashboardScreen');
let chatStart = content.indexOf('\n// ==========================================\n// 4. CHAT PAGE (AI COACH)');
if (chatStart === -1) chatStart = content.indexOf('@Composable\nfun ChatScreen');

if (dashStart !== -1 && chatStart !== -1) {
    content = content.substring(0, dashStart) + newDashboard + content.substring(chatStart);
} else {
    console.log("Could not find boundaries.");
}

fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', content, 'utf8');

console.log("Updated Dashboard");
