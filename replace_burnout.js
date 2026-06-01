const fs = require('fs');

const d_en = {
    "burnout_critical": "CRITICAL LEVEL WARNING",
    "burnout_elevated": "ELEVATED LEVEL WARNING",
    "burnout_optimal": "OPTIMAL LEVEL ACTIVE"
};

const d_ru = {
    "burnout_critical": "КРИТИЧЕСКИЙ УРОВЕНЬ",
    "burnout_elevated": "ПОВЫШЕННЫЙ УРОВЕНЬ",
    "burnout_optimal": "ОПТИМАЛЬНЫЙ УРОВЕНЬ"
};

const d_es = {
    "burnout_critical": "ADVERTENCIA DE NIVEL CRÍTICO",
    "burnout_elevated": "ADVERTENCIA DE NIVEL ELEVADO",
    "burnout_optimal": "NIVEL ÓPTIMO ACTIVO"
};

const d_zh = {
    "burnout_critical": "严重警告",
    "burnout_elevated": "风险增加",
    "burnout_optimal": "最佳活动水平"
};

const d_ar = {
    "burnout_critical": "تحذير مستوى حرج",
    "burnout_elevated": "تحذير مستوى مرتفع",
    "burnout_optimal": "المستوى الأمثل نشط"
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
            let insertion = `    <string name="${name}">${value}</string>\n</resources>`;
            content = content.replace("</resources>", insertion);
        }
    }
    fs.writeFileSync(filePath, content, 'utf8');
});

let kt = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');
function rep(orig, name) {
    kt = kt.replace(orig, `androidx.compose.ui.res.stringResource(com.example.R.string.${name})`);
}
rep(`"CRITICAL LEVEL WARNING"`, `burnout_critical`);
rep(`"ELEVATED LEVEL WARNING"`, `burnout_elevated`);
rep(`"OPTIMAL LEVEL ACTIVE"`, `burnout_optimal`);

fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', kt, 'utf8');

console.log("Updated burnout strings");
