package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import java.util.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.database.DailyInsight
import com.example.data.database.Habit
import com.example.data.database.Task
import com.example.data.database.Goal
import com.example.ui.theme.*
import com.example.ui.viewmodel.FocusMindViewModel
import com.example.ui.viewmodel.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FocusMindApp(viewModel: FocusMindViewModel) {
    val currentScreen by viewModel.currentScreenState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DeepBlack
    ) {
        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
            when (screen) {
                "landing" -> LandingScreen(
                    onGetStarted = { viewModel.setScreen("main") }
                )
                "main" -> MainFrame(viewModel = viewModel)
            }
        }
    }
}

// ==========================================
// 1. LANDING PAGE
// ==========================================
@Composable
fun LandingScreen(onGetStarted: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundGlow")
    val gradientShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Shift"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
    ) {
        // Futuristic Ambient Soft Purple/Blue Radial Glow Points
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center1 = Offset(size.width * 0.2f + (gradientShift * 0.1f) % 200f, size.height * 0.2f)
            val center2 = Offset(size.width * 0.8f - (gradientShift * 0.1f) % 200f, size.height * 0.7f)

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AccentPurple.copy(alpha = 0.12f), Color.Transparent),
                    center = center1,
                    radius = 450.dp.toPx()
                ),
                radius = 450.dp.toPx(),
                center = center1
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AccentBlue.copy(alpha = 0.1f), Color.Transparent),
                    center = center2,
                    radius = 500.dp.toPx()
                ),
                radius = 500.dp.toPx(),
                center = center2
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Upper Status Log
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .border(1.dp, BorderGraphite, RoundedCornerShape(100.dp))
                        .background(CardGraphite.copy(alpha = 0.6f))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AccentPurple)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.system_active),
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Interactive Hero Space
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                // Central Luxury Visual Ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 24.dp)
                ) {
                    val ringTransition = rememberInfiniteTransition(label = "RingRotation")
                    val angleOffset by ringTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(8000, easing = LinearEasing)
                        ),
                        label = "Rotation"
                    )

                    Canvas(modifier = Modifier.size(120.dp)) {
                        drawArc(
                            color = AccentPurple.copy(alpha = 0.4f),
                            startAngle = angleOffset,
                            sweepAngle = 120f,
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = AccentBlue.copy(alpha = 0.4f),
                            startAngle = angleOffset + 180f,
                            sweepAngle = 90f,
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(DeepBlack)
                            .border(1.dp, BorderGraphite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.focus_mind_logo),
                            contentDescription = "FocusMind Logo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.hero_title),
                    style = MaterialTheme.typography.displayLarge,
                    color = TextWhite,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.hero_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // CTA Foot Buttons
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(containerColor = TextWhite),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("get_started_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.get_started),
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = DeepBlack
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Forward Icon",
                            tint = DeepBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Inline quick specs row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    userScrollEnabled = false
                ) {
                    val elements = listOf("AI COACHING", "POMODORO SPACE", "ADAPTIVE PLANNER", "STATS LOGS")
                    items(elements) { item ->
                        Box(
                            modifier = Modifier
                                .background(CardGraphite.copy(alpha = 0.4f), RoundedCornerShape(100.dp))
                                .border(1.dp, BorderGraphite.copy(alpha = 0.4f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. MAIN NAV OVERLAY FRAME
// ==========================================
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MainFrame(viewModel: FocusMindViewModel) {
    val activeTab by viewModel.currentTabState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DeepBlack,
                drawerTonalElevation = 0.dp,
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .border(1.dp, BorderGraphite, RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            ) {
                Spacer(Modifier.height(24.dp).windowInsetsPadding(WindowInsets.statusBars))
                
                // User Profile Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setTab("profile")
                            scope.launch { drawerState.close() }
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(CardGraphite),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = TextMuted)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Darth Vader", color = TextWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Level 15 Discipline", color = AccentPurple, style = MaterialTheme.typography.bodySmall)
                    }
                }

                Divider(color = BorderGraphite, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))

                val navItems = listOf(
                    Triple("dashboard", Icons.Outlined.Home, com.example.R.string.nav_home),
                    Triple("chat", Icons.Outlined.ChatBubbleOutline, com.example.R.string.nav_ai_coach),
                    Triple("planner", Icons.Outlined.CalendarToday, com.example.R.string.nav_planner),
                    Triple("goals", Icons.Outlined.Flag, com.example.R.string.nav_goals),
                    Triple("habits", Icons.Outlined.CheckCircle, com.example.R.string.nav_habits),
                    Triple("focus", Icons.Outlined.CenterFocusStrong, com.example.R.string.nav_focus),
                    Triple("glow_up", Icons.Outlined.AutoAwesome, com.example.R.string.nav_glow_up),
                    Triple("analytics", Icons.Outlined.BarChart, com.example.R.string.nav_analytics),
                    Triple("settings", Icons.Outlined.Settings, com.example.R.string.nav_settings)
                )

                LazyColumn {
                    items(navItems) { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.second, contentDescription = null) },
                            label = { Text(androidx.compose.ui.res.stringResource(item.third)) },
                            selected = activeTab == item.first,
                            onClick = {
                                viewModel.setTab(item.first)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = AccentPurple.copy(alpha = 0.15f),
                                unselectedContainerColor = Color.Transparent,
                                selectedIconColor = AccentPurple,
                                unselectedIconColor = TextMuted,
                                selectedTextColor = AccentPurple,
                                unselectedTextColor = TextWhite
                            ),
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = DeepBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(activeTab.uppercase(Locale.getDefault()), color = TextWhite, style = MaterialTheme.typography.titleSmall) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = TextWhite)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = DeepBlack,
                        titleContentColor = TextWhite
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (activeTab) {
                    "dashboard" -> DashboardScreen(viewModel = viewModel)
                    "chat" -> ChatScreen(viewModel = viewModel)
                    "planner" -> PlannerScreen(viewModel = viewModel)
                    "focus" -> FocusScreen(viewModel = viewModel)
                    "settings" -> SettingsScreen(viewModel = viewModel)
                    "goals" -> GoalsScreen(viewModel = viewModel)
                    "habits" -> HabitsScreen(viewModel = viewModel)
                    "glow_up" -> GlowUpScreen(viewModel = viewModel)
                    "analytics" -> AnalyticsScreen(viewModel = viewModel)
                    "profile" -> ProfileScreen(viewModel = viewModel)
                    else -> DashboardScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun StubScreen(desc: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(desc, color = TextMuted)
    }
}



// ==========================================
// 3. DASHBOARD SCREEN
// ==========================================
@Composable
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
@Composable
fun ChatScreen(viewModel: FocusMindViewModel) {
    val chatHistory by viewModel.chatHistory.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isChatGenerating.collectAsStateWithLifecycle()
    val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()
    val voiceReplyText by viewModel.voiceResponseText.collectAsStateWithLifecycle()

    var textInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.cognitive_coaching_node), style = MaterialTheme.typography.labelSmall, color = AccentPurple)
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.focusmind_assistant), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }

            // Quick coaching templates rows
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val templates = listOf(
                    "Design study discipline roadmap",
                    "Analyze my daily burnout state",
                    "Dopamine detox blueprint",
                    "Generate custom evening routine"
                )
                items(templates) { template ->
                    Box(
                        modifier = Modifier
                            .background(CardGraphite, RoundedCornerShape(100.dp))
                            .border(1.dp, BorderGraphite, RoundedCornerShape(100.dp))
                            .clickable {
                                viewModel.sendChatMessage(template)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = template,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextWhite,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Message Board
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(chatHistory) { msg ->
                    ChatBubble(message = msg)
                }

                if (isGenerating) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                "Thought pattern reconfiguring...",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentPurple
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(2.dp)
                                    .clip(CircleShape),
                                color = AccentPurple,
                                trackColor = BorderGraphite
                            )
                        }
                    }
                }
            }

            // Gemini-Style Actions bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .background(CardGraphite, RoundedCornerShape(26.dp))
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Upload attachment */ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Attach File",
                        tint = TextWhite,
                        modifier = Modifier.size(24.dp)
                    )
                }

                androidx.compose.foundation.text.BasicTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextWhite),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (textInput.isNotBlank()) {
                            viewModel.sendChatMessage(textInput)
                            textInput = ""
                            focusManager.clearFocus()
                        }
                    }),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(AccentBlue),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (textInput.isEmpty()) {
                                Text(androidx.compose.ui.res.stringResource(com.example.R.string.ask_focusmind), color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                            }
                            innerTextField()
                        }
                    }
                )

                if (textInput.isEmpty()) {
                    IconButton(onClick = { viewModel.startVoiceSession() }) {
                        Icon(
                            imageVector = Icons.Outlined.Mic,
                            contentDescription = "Voice Input",
                            tint = TextWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    IconButton(onClick = { viewModel.startVoiceSession() }) {
                        Icon(
                            imageVector = Icons.Outlined.GraphicEq,
                            contentDescription = "Live Voice Chat",
                            tint = TextWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            viewModel.sendChatMessage(textInput)
                            textInput = ""
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.size(36.dp).background(TextWhite, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = DeepBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }

        // 10. Voice assistant holding visual model overlay
        if (voiceState != "idle") {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DeepBlack.copy(alpha = 0.95f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.voice_link_established),
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentPurple
                        )

                        // Moving pulsating vocal larynx custom wave canvas
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(200.dp)
                        ) {
                            val infiniteTransition = rememberInfiniteTransition(label = "VoiceWave")
                            val phase by infiniteTransition.animateFloat(
                                initialValue = 0f,
                                targetValue = 2f * Math.PI.toFloat(),
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1200, easing = LinearEasing)
                                ),
                                label = "Phase"
                            )

                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val midY = size.height / 2f
                                val amplitude1 = if (voiceState == "listening") 35.dp.toPx() else 15.dp.toPx()
                                val amplitude2 = if (voiceState == "speaking") 55.dp.toPx() else 20.dp.toPx()

                                val points1 = mutableListOf<Offset>()
                                val points2 = mutableListOf<Offset>()

                                for (x in 0..size.width.toInt() step 4) {
                                    val factor = sin(x.toFloat() * 0.02f + phase)
                                    val y1 = midY + factor * amplitude1 * sin(x.toFloat() * 0.005f)
                                    val y2 = midY + sin(x.toFloat() * 0.03f - phase) * amplitude2 * sin(x.toFloat() * 0.007f)

                                    points1.add(Offset(x.toFloat(), y1))
                                    points2.add(Offset(x.toFloat(), y2))
                                }

                                for (i in 0 until points1.size - 1) {
                                    drawLine(
                                        color = AccentPurple.copy(alpha = 0.7f),
                                        start = points1[i],
                                        end = points1[i + 1],
                                        strokeWidth = 3.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                    drawLine(
                                        color = AccentBlue.copy(alpha = 0.5f),
                                        start = points2[i],
                                        end = points2[i + 1],
                                        strokeWidth = 2.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (voiceState == "listening") "SPEAK NOW..." else "PROCESSING COGNITIVE STREAM...",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .border(1.dp, BorderGraphite, RoundedCornerShape(12.dp))
                                .background(CardGraphite)
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = voiceReplyText.ifEmpty { "Awaiting speech transmission limits. Calibrate focus ratios." },
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextMuted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isUser = message.sender == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .background(
                    color = if (isUser) AccentBlue.copy(alpha = 0.12f) else CardGraphite,
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 0.dp,
                        bottomEnd = if (isUser) 0.dp else 14.dp
                    )
                )
                .border(
                    width = 1.dp,
                    color = if (isUser) AccentBlue.copy(alpha = 0.5f) else BorderGraphite,
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 0.dp,
                        bottomEnd = if (isUser) 0.dp else 14.dp
                    )
                )
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (isUser) "USER INSTRUCTION" else "FOCUSMIND ADVISOR",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isUser) AccentBlue else AccentPurple,
                    fontSize = 9.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextWhite
                )
            }
        }
    }
}

// ==========================================
// 5. SMART PLANNER & TRACKERS SCREEN
// ==========================================
@Composable
fun PlannerScreen(viewModel: FocusMindViewModel) {
    var subSection by remember { mutableStateOf("planner") } // "planner", "glowup"
    val habitList by viewModel.habits.collectAsStateWithLifecycle()
    val tasksList by viewModel.tasks.collectAsStateWithLifecycle()
    val goalsList by viewModel.goals.collectAsStateWithLifecycle()
    val todayInsight by viewModel.todayInsightState.collectAsStateWithLifecycle()
    val plannerAdviceText by viewModel.plannerAdvice.collectAsStateWithLifecycle()
    val isPlannerLoading by viewModel.isPlannerLoading.collectAsStateWithLifecycle()

    var customTaskTitle by remember { mutableStateOf("") }
    var customTaskEnergy by remember { mutableStateOf("Medium") } // High, Medium, Low
    var isInsertingTask by remember { mutableStateOf(false) }

    var customHabitTitle by remember { mutableStateOf("") }
    var customHabitCategory by remember { mutableStateOf("discipline") }
    var isInsertingHabit by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Upper segmented controllers
        item {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.cognitive_routine_planners), style = MaterialTheme.typography.labelSmall, color = AccentBlue)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(CardGraphite, RoundedCornerShape(10.dp))
                        .border(1.dp, BorderGraphite, RoundedCornerShape(10.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (subSection == "planner") BorderGraphite else Color.Transparent)
                            .clickable { subSection = "planner" }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.adaptive_planner), color = TextWhite, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (subSection == "glowup") BorderGraphite else Color.Transparent)
                            .clickable { subSection = "glowup" }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.glow_up_system), color = TextWhite, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ADAPTIVE PLANNER SUBSECTION
        if (subSection == "planner") {
            // Adaptive Energy Planner Advice Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardGraphite, RoundedCornerShape(16.dp))
                        .border(1.dp, BorderGraphite, RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AccentBlue))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.dynamical_energy_realignment), style = MaterialTheme.typography.labelMedium, color = AccentBlue)
                        }

                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.ai_inspects_energy, (todayInsight?.energyLevel ?: 85).toString()),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )

                        Box(
                            modifier = Modifier
                                .border(1.dp, BorderGraphite, RoundedCornerShape(12.dp))
                                .background(BorderGraphite.copy(alpha = 0.4f))
                                .padding(16.dp)
                        ) {
                            if (isPlannerLoading) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = AccentPurple, strokeWidth = 2.dp)
                                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.rearranging_cognitive_blocks), style = MaterialTheme.typography.bodyMedium, color = TextWhite)
                                }
                            } else {
                                Text(
                                    text = plannerAdviceText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextWhite
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.triggerPlannerAdaptation() },
                            colors = ButtonDefaults.buttonColors(containerColor = TextWhite),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.adapt_plan_via_ai), color = DeepBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Tasks List Title & Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.daily_agenda_blocks), style = MaterialTheme.typography.titleLarge, color = TextWhite, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = { isInsertingTask = true },
                        modifier = Modifier.background(CardGraphite, CircleShape).border(1.dp, BorderGraphite, CircleShape)
                    ) {
                        Icon(Icons.Filled.Add, "Append Task", tint = TextWhite)
                    }
                }
            }

            // Task List cards
            if (tasksList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderGraphite, RoundedCornerShape(12.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.Assignment, "Empty Archive", tint = TextMuted, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.no_agenda_slots), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                        }
                    }
                }
            } else {
                items(tasksList) { task ->
                    TaskCardRow(task = task, onChecked = { viewModel.toggleTask(task) }, onDelete = { viewModel.removeTask(task.id) })
                }
            }

            // Interactive Habit Tracker lists in Planner Screen
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.daily_habit_streaks), style = MaterialTheme.typography.titleLarge, color = TextWhite, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = { isInsertingHabit = true },
                        modifier = Modifier.background(CardGraphite, CircleShape).border(1.dp, BorderGraphite, CircleShape)
                    ) {
                        Icon(Icons.Filled.Add, "Append Habit", tint = TextWhite)
                    }
                }
            }

            items(habitList) { habit ->
                HabitCardRow(habit = habit, onCheckToggle = { viewModel.toggleHabit(habit) }, onDelete = { viewModel.removeHabit(habit.id) })
            }
        }

        // GLOW-UP SYSTEM ROUTINES
        if (subSection == "glowup") {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardGraphite, RoundedCornerShape(16.dp))
                        .border(1.dp, BorderGraphite, RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.glow_up_platform), style = MaterialTheme.typography.labelMedium, color = AccentPurple)
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.optimize_discipline),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )

                        // Selector Categories Grid
                        val cats = listOf(
                            Pair("discipline", "🛡️ Discipline"),
                            Pair("fitness", "🏃 Vigor & Fitness"),
                            Pair("sleep", "🌙 Circadian Sleep"),
                            Pair("dopamine_detox", "🧬 Dopamine Fast"),
                            Pair("studying", "📚 Deep Study")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(cats) { (catKey, desc) ->
                                Box(
                                    modifier = Modifier
                                        .background(BorderGraphite, RoundedCornerShape(8.dp))
                                        .clickable { viewModel.generateGlowUpRoadmap(catKey) }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text(desc, style = MaterialTheme.typography.bodyMedium, color = TextWhite)
                                }
                            }
                        }
                    }
                }
            }

            // Generated Roadmap Output Block
            item {
                val roadmap by viewModel.glowUpRoadmap.collectAsStateWithLifecycle()
                val isGlowupLoading by viewModel.isGlowUpLoading.collectAsStateWithLifecycle()

                if (isGlowupLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderGraphite, RoundedCornerShape(16.dp))
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            CircularProgressIndicator(color = AccentPurple)
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.calibrating_roadmap), style = MaterialTheme.typography.bodyMedium, color = TextWhite)
                        }
                    }
                } else if (roadmap != null) {
                    val road = roadmap!!
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardGraphite, RoundedCornerShape(16.dp))
                            .border(1.dp, BorderGraphite, RoundedCornerShape(16.dp))
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AccentPurple))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(road.category.replace("_", " ").uppercase(), style = MaterialTheme.typography.labelSmall, color = AccentPurple)
                            }

                            Text(road.title, style = MaterialTheme.typography.displayMedium, color = TextWhite, fontWeight = FontWeight.Bold)
                            Text(road.summary, style = MaterialTheme.typography.bodyMedium, color = TextMuted)

                            Divider(color = BorderGraphite)

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                road.steps.forEachIndexed { i, step ->
                                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(BorderGraphite),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = (i + 1).toString(),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = AccentBlue,
                                                fontSize = 11.sp
                                            )
                                        }
                                        Text(text = step, style = MaterialTheme.typography.bodyLarge, color = TextWhite)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderGraphite, RoundedCornerShape(16.dp))
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.select_category_to_configure), style = MaterialTheme.typography.bodyMedium, color = TextMuted, textAlign = TextAlign.Center)
                    }
                }
            }

            // Dynamic Goals Progress
            item {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.self_improvement_milestones), style = MaterialTheme.typography.titleLarge, color = TextWhite, fontWeight = FontWeight.Bold)
            }

            if (goalsList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().border(1.dp, BorderGraphite, RoundedCornerShape(12.dp)).padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.no_active_milestones), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    }
                }
            } else {
                items(goalsList) { goal ->
                    GoalCardRow(
                        goal = goal,
                        onProgressChange = { viewModel.updateGoalProgress(goal, it) },
                        onDelete = { viewModel.removeGoal(goal.id) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Modal dialog insertions
    if (isInsertingTask) {
        Dialog(onDismissRequest = { isInsertingTask = false }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderGraphite, RoundedCornerShape(16.dp))
                    .background(DeepBlack)
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.inject_agenda_block), style = MaterialTheme.typography.titleLarge, color = TextWhite)

                    OutlinedTextField(
                        value = customTaskTitle,
                        onValueChange = { customTaskTitle = it },
                        label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.task_title_hint), color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = AccentBlue,
                            unfocusedBorderColor = BorderGraphite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Energy complexity tabs
                    Column {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.energy_required), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("High", "Medium", "Low").forEach { e ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (customTaskEnergy == e) BorderGraphite else CardGraphite)
                                        .border(1.dp, if (customTaskEnergy == e) AccentBlue else BorderGraphite.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .clickable { customTaskEnergy = e }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(e, color = TextWhite, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { isInsertingTask = false },
                            colors = ButtonDefaults.buttonColors(containerColor = BorderGraphite),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.reject), color = TextWhite)
                        }

                        Button(
                            onClick = {
                                if (customTaskTitle.isNotBlank()) {
                                    viewModel.addTask(customTaskTitle.trim(), customTaskEnergy)
                                    customTaskTitle = ""
                                    isInsertingTask = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TextWhite),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.append_block), color = DeepBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (isInsertingHabit) {
        Dialog(onDismissRequest = { isInsertingHabit = false }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderGraphite, RoundedCornerShape(16.dp))
                    .background(DeepBlack)
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.inject_habit_engine), style = MaterialTheme.typography.titleLarge, color = TextWhite)

                    OutlinedTextField(
                        value = customHabitTitle,
                        onValueChange = { customHabitTitle = it },
                        label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.habit_title_hint), color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = AccentPurple,
                            unfocusedBorderColor = BorderGraphite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Habit categorics
                    Column {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.category_theme), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        val categories = listOf("discipline", "fitness", "sleep", "studying")
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            categories.forEach { c ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (customHabitCategory == c) BorderGraphite else CardGraphite)
                                        .border(1.dp, if (customHabitCategory == c) AccentPurple else BorderGraphite.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                        .clickable { customHabitCategory = c }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = c.replaceFirstChar { it.uppercase() },
                                        color = TextWhite,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { isInsertingHabit = false },
                            colors = ButtonDefaults.buttonColors(containerColor = BorderGraphite),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.reject), color = TextWhite)
                        }

                        Button(
                            onClick = {
                                if (customHabitTitle.isNotBlank()) {
                                    viewModel.addHabit(customHabitTitle.trim(), customHabitCategory)
                                    customHabitTitle = ""
                                    isInsertingHabit = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TextWhite),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.inject), color = DeepBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCardRow(task: Task, onChecked: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardGraphite, RoundedCornerShape(12.dp))
            .border(1.dp, BorderGraphite, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("task_toggle_${task.id}"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onChecked() },
                colors = CheckboxDefaults.colors(
                    checkedColor = AccentBlue,
                    checkmarkColor = DeepBlack,
                    uncheckedColor = BorderGraphite
                )
            )

            Column {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (task.isCompleted) TextMuted else TextWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .background(BorderGraphite, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.energy_label, task.energyRequired.uppercase()),
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentBlue,
                            fontSize = 8.sp
                        )
                    }
                }
            }
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.DeleteOutline, "Remove Task", tint = AlertRed)
        }
    }
}

@Composable
fun HabitCardRow(habit: Habit, onCheckToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardGraphite, RoundedCornerShape(12.dp))
            .border(1.dp, BorderGraphite, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("habit_toggle_${habit.id}"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = habit.isCompletedToday,
                onCheckedChange = { onCheckToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = AccentPurple,
                    checkmarkColor = DeepBlack,
                    uncheckedColor = BorderGraphite
                )
            )

            Column {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (habit.isCompletedToday) TextMuted else TextWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocalFireDepartment, "Streak", tint = AmberYellow, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.streak_days_short, habit.streak.toString()), style = MaterialTheme.typography.labelMedium, color = AmberYellow, fontSize = 10.sp)
                    }

                    Box(
                        modifier = Modifier
                            .background(BorderGraphite, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = habit.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentPurple,
                            fontSize = 8.sp
                        )
                    }
                }
            }
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.DeleteOutline, "Delete Habit", tint = AlertRed)
        }
    }
}

@Composable
fun GoalCardRow(goal: Goal, onProgressChange: (Int) -> Unit, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardGraphite, RoundedCornerShape(12.dp))
            .border(1.dp, BorderGraphite, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(goal.category.replace("_", " ").uppercase(), style = MaterialTheme.typography.labelSmall, color = AccentPurple)
                    Text(goal.title, style = MaterialTheme.typography.titleLarge, color = TextWhite)
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.DeleteOutline, "Trash Goal", tint = AlertRed)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { goal.progress / 100f },
                    modifier = Modifier.weight(1f).height(6.dp).clip(CircleShape),
                    color = AccentPurple,
                    trackColor = BorderGraphite
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, goal.progress.toString()), style = MaterialTheme.typography.labelMedium, color = TextWhite)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0, 25, 50, 75, 100).forEach { p ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(BorderGraphite, RoundedCornerShape(4.dp))
                            .clickable { onProgressChange(p) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+$p", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = TextWhite)
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. FOCUS IMPERSIVE SPACE SCREEN
// ==========================================
@Composable
fun FocusScreen(viewModel: FocusMindViewModel) {
    val isRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()
    val timeLeft by viewModel.timeLeftSeconds.collectAsStateWithLifecycle()
    val activeSound by viewModel.ambientSound.collectAsStateWithLifecycle()
    val isBreathingIn by viewModel.isBreathingIn.collectAsStateWithLifecycle()

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val formattedTime = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Upper silent status
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = androidx.compose.ui.res.stringResource(com.example.R.string.sensory_cognitive_expulsion),
                style = MaterialTheme.typography.labelSmall,
                color = AccentPurple
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isRunning) "Deep Concentration active" else "Isolate distractions",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        // Circular breathing particle core segment
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(240.dp)
        ) {
            // Breathing expanding circles
            val breathScale by animateFloatAsState(
                targetValue = if (isBreathingIn) 1.6f else 1.0f,
                animationSpec = tween(4000, easing = LinearOutSlowInEasing),
                label = "BreathingExpansion"
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .blur(20.dp)
                    .clip(CircleShape)
                    .background(AccentPurple.copy(alpha = 0.12f * breathScale))
            )

            Box(
                modifier = Modifier
                    .size(100.dp * breathScale)
                    .border(1.dp, AccentPurple.copy(alpha = 0.3f), CircleShape)
            )

            // Dynamic core
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 44.sp),
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isBreathingIn) "INHALE ENERGY" else "EXHALE BURNOUT",
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentPurple,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Noise generators lists
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(androidx.compose.ui.res.stringResource(com.example.R.string.ambient_field_isolation), style = MaterialTheme.typography.labelSmall, color = TextMuted)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val fields = listOf("None", "Rain", "Brown Noise", "Forest", "Synth")
                items(fields) { f ->
                    val isActive = activeSound == f
                    Box(
                        modifier = Modifier
                            .background(if (isActive) BorderGraphite else CardGraphite, RoundedCornerShape(100.dp))
                            .border(1.dp, if (isActive) AccentPurple else BorderGraphite, RoundedCornerShape(100.dp))
                            .clickable { viewModel.setAmbientSound(f) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("sound_toggle_${f.replace(" ", "_")}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isActive) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(AccentPurple))
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(f, style = MaterialTheme.typography.labelSmall, color = if (isActive) TextWhite else TextMuted)
                        }
                    }
                }
            }

            // Big Start Trigger button
            FloatingActionButton(
                onClick = { viewModel.toggleFocusTimer(25) },
                containerColor = if (isRunning) AlertRed else TextWhite,
                contentColor = if (isRunning) TextWhite else DeepBlack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("pomodoro_toggle_button"),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = "Trigger Timer Control"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRunning) "TERMINATE FOCUS SEGMENT" else "INITIATE focus segment (25m)",
                        style = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 8. SETTINGS SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: FocusMindViewModel) {
    val languages = listOf("English", "Русский", "Español", "中文", "العربية")
    val currentLang = viewModel.currentLanguage.collectAsState().value
    var expandedLanguage by remember { mutableStateOf(false) }

    val themes = listOf("System", "Dark", "Light")
    val currentThemeState = viewModel.currentTheme.collectAsState().value
    var expandedTheme by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = androidx.compose.ui.res.stringResource(com.example.R.string.nav_settings),
            style = MaterialTheme.typography.displaySmall,
            color = TextWhite,
            fontWeight = FontWeight.Bold
        )

        // App Settings Section
        Card(
            colors = CardDefaults.cardColors(containerColor = CardGraphite),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.app_settings), style = MaterialTheme.typography.labelMedium, color = AccentPurple)
                Spacer(modifier = Modifier.height(16.dp))

                // Language Selection
                ExposedDropdownMenuBox(
                    expanded = expandedLanguage,
                    onExpandedChange = { expandedLanguage = it },
                ) {
                    OutlinedTextField(
                        value = currentLang,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.language_lbl), color = TextMuted) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLanguage) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = AccentPurple,
                            unfocusedBorderColor = BorderGraphite,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedLanguage,
                        onDismissRequest = { expandedLanguage = false },
                        modifier = Modifier.background(CardGraphite)
                    ) {
                        languages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang, color = TextWhite) },
                                onClick = {
                                    viewModel.setLanguage(lang)
                                    expandedLanguage = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Theme Selection
                ExposedDropdownMenuBox(
                    expanded = expandedTheme,
                    onExpandedChange = { expandedTheme = it },
                ) {
                    OutlinedTextField(
                        value = currentThemeState,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.theme_interface), color = TextMuted) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTheme) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = AccentPurple,
                            unfocusedBorderColor = BorderGraphite,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTheme,
                        onDismissRequest = { expandedTheme = false },
                        modifier = Modifier.background(CardGraphite)
                    ) {
                        themes.forEach { theme ->
                            DropdownMenuItem(
                                text = { Text(theme, color = TextWhite) },
                                onClick = {
                                    viewModel.setTheme(theme)
                                    expandedTheme = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.receive_ai_reminders), style = MaterialTheme.typography.bodyMedium, color = TextWhite)
                    androidx.compose.material3.Switch(
                        checked = true,
                        onCheckedChange = { /* TODO */ },
                        colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = AccentPurple, checkedTrackColor = AccentPurple.copy(alpha = 0.5f))
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(androidx.compose.ui.res.stringResource(com.example.R.string.strict_focus_mode), style = MaterialTheme.typography.bodyMedium, color = TextWhite)
                    androidx.compose.material3.Switch(
                        checked = false,
                        onCheckedChange = { /* TODO */ },
                        colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = AccentPurple, checkedTrackColor = AccentPurple.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}


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
                    
                    Text("Progress: ${if (idx == 0) 45 else 80}%", style = MaterialTheme.typography.labelSmall, color = TextWhite)
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


@Composable
fun ProfileScreen(viewModel: FocusMindViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(CardGraphite),
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
