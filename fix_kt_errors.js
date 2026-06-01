const fs = require('fs');

let content = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');

const fixes = [
    { target: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, focusScoreVal.toString())), style =`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, focusScoreVal.toString()), style =` },
    { target: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, prodScoreVal.toString())), style =`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, prodScoreVal.toString()), style =` },
    { target: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.level_value, discScoreVal.toString())), style =`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.level_value, discScoreVal.toString()), style =` },
    { target: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.streak_days, streakVal.toString())), style =`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.streak_days, streakVal.toString()), style =` },
    { target: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, currentEnergyLevel.toString())), style =`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, currentEnergyLevel.toString()), style =` },
    { target: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.streak_days_short, habit.streak.toString())), style =`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.streak_days_short, habit.streak.toString()), style =` },
    { target: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, goal.progress.toString())), style =`, replacement: `Text(androidx.compose.ui.res.stringResource(com.example.R.string.focus_score_value, goal.progress.toString()), style =` },
];

fixes.forEach(r => {
    content = content.replace(r.target, r.replacement);
});

fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', content, 'utf8');

console.log("Fixed KT syntax errors successfully");
