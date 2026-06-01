const fs = require('fs');

const d_en = {
    "nav_home": "Home",
    "nav_ai_coach": "AI Assistant",
    "nav_planner": "Planner",
    "nav_goals": "Goals",
    "nav_habits": "Habits",
    "nav_focus": "Focus",
    "nav_glow_up": "Glow-Up",
    "nav_analytics": "Analytics",
    "nav_settings": "Settings",
    "nav_profile": "Profile"
};

const d_ru = {
    "nav_home": "Главная",
    "nav_ai_coach": "AI Ассистент",
    "nav_planner": "Планировщик",
    "nav_goals": "Цели",
    "nav_habits": "Привычки",
    "nav_focus": "Фокус",
    "nav_glow_up": "Glow-Up",
    "nav_analytics": "Аналитика",
    "nav_settings": "Настройки",
    "nav_profile": "Профиль"
};

const d_es = {
    "nav_home": "Inicio",
    "nav_ai_coach": "Asistente UI",
    "nav_planner": "Planificador",
    "nav_goals": "Metas",
    "nav_habits": "Hábitos",
    "nav_focus": "Enfoque",
    "nav_glow_up": "Glow-Up",
    "nav_analytics": "Análisis",
    "nav_settings": "Ajustes",
    "nav_profile": "Perfil"
};

const d_zh = {
    "nav_home": "主页",
    "nav_ai_coach": "AI助手",
    "nav_planner": "计划",
    "nav_goals": "目标",
    "nav_habits": "习惯",
    "nav_focus": "专注",
    "nav_glow_up": "发光",
    "nav_analytics": "分析",
    "nav_settings": "设置",
    "nav_profile": "个人资料"
};

const d_ar = {
    "nav_home": "الرئيسية",
    "nav_ai_coach": "مساعد الذكاء الاصطناعي",
    "nav_planner": "مخطط",
    "nav_goals": "أهداف",
    "nav_habits": "عادات",
    "nav_focus": "تركيز",
    "nav_glow_up": "تطوير الذات",
    "nav_analytics": "تحليلات",
    "nav_settings": "إعدادات",
    "nav_profile": "ملف شخصي"
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
        } else {
            // replace
            let p2 = new RegExp(`<string name="${name}">.*?</string>`, 'g');
            content = content.replace(p2, `<string name="${name}">${value}</string>`);
        }
    }
    fs.writeFileSync(filePath, content, 'utf8');
});

console.log("Updated navigation strings");
