const fs = require('fs');

const d_en = {
    "mood_awesome": "⚡ Awesome",
    "mood_calm": "🟢 Calm",
    "mood_balanced": "🟡 Balanced",
    "mood_tired": "💤 Tired",
    "mood_burned_out": "🩸 Burned Out"
};

const d_ru = {
    "mood_awesome": "⚡ Отлично",
    "mood_calm": "🟢 Спокойно",
    "mood_balanced": "🟡 Сбалансировано",
    "mood_tired": "💤 Усталость",
    "mood_burned_out": "🩸 Выгорание"
};

const d_es = {
    "mood_awesome": "⚡ Increíble",
    "mood_calm": "🟢 Tranquilo",
    "mood_balanced": "🟡 Equilibrado",
    "mood_tired": "💤 Cansado",
    "mood_burned_out": "🩸 Agotado"
};

const d_zh = {
    "mood_awesome": "⚡ 极佳",
    "mood_calm": "🟢 平静",
    "mood_balanced": "🟡 平衡",
    "mood_tired": "💤 疲惫",
    "mood_burned_out": "🩸 倦怠"
};

const d_ar = {
    "mood_awesome": "⚡ رائع",
    "mood_calm": "🟢 هادئ",
    "mood_balanced": "🟡 متوازن",
    "mood_tired": "💤 متعب",
    "mood_burned_out": "🩸 منهك"
};

const langs = [
    { code: '', data: d_en },
    { code: '-ru', data: d_ru },
    { code: '-es', data: d_es },
    { code: '-zh', data: d_zh },
    { code: '-ar', data: d_ar }
];

langs.forEach(lang => {
    let filePath = `app/src/main/res/values${lang.code}/strings.xml`;
    if (!fs.existsSync(filePath)) {
        return;
    }
    let content = fs.readFileSync(filePath, 'utf8');
    for (const [key, value] of Object.entries(lang.data)) {
        let name = key;
        let p = new RegExp(`name="${name}"`, 'g');
        if (!p.test(content)) {
            // escape
            let escapedVal = value;
            let insertion = `    <string name="${name}">${escapedVal}</string>\n</resources>`;
            content = content.replace("</resources>", insertion);
        }
    }
    fs.writeFileSync(filePath, content, 'utf8');
});

let kt = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');
function rep(orig, name) {
    kt = kt.replace(orig, `androidx.compose.ui.res.stringResource(com.example.R.string.${name})`);
}
rep(`"⚡ Awesome"`, `mood_awesome`);
rep(`"🟢 Calm"`, `mood_calm`);
rep(`"🟡 Balanced"`, `mood_balanced`);
rep(`"💤 Tired"`, `mood_tired`);
rep(`"🩸 Burned Out"`, `mood_burned_out`);

fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', kt, 'utf8');

console.log("Updated moods");
