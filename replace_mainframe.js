const fs = require('fs');
let content = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');

const replacement = `@Composable
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
                
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("FocusMind", style = MaterialTheme.typography.titleLarge, color = TextWhite, fontWeight = FontWeight.Bold)
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
                    Triple("settings", Icons.Outlined.Settings, com.example.R.string.nav_settings),
                    Triple("profile", Icons.Outlined.Person, com.example.R.string.nav_profile)
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
                    "goals" -> StubScreen("Goals List, Progress, Roadmap")
                    "habits" -> StubScreen("Habits List, streaks, Daily execution")
                    "glow_up" -> StubScreen("30 Day Glow-Up, Discipline Reset, Monk Mode")
                    "analytics" -> StubScreen("Productivity Score, AI Insights, Trends")
                    "profile" -> StubScreen("Avatar, Achievements, Levels, Stats")
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
`;

const stubPattern = `fun MainFrame\\(viewModel: FocusMindViewModel\\) \\{[\\s\\S]*?\\n\\}`;

const regex = /@Composable\s+fun MainFrame\(viewModel: FocusMindViewModel\) \{[\s\S]*?\}\n\}\n/m;
content = content.replace(regex, replacement + "\n\n");

fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', content, 'utf8');

console.log("Updated MainFrame");
