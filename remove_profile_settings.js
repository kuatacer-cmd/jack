const fs = require('fs');
let content = fs.readFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', 'utf8');

// remove profile section from settings screen
const profileSectionRegex = /\/\/ User Profile Section[\s\S]*?(?=\/\/ Preferences Section)/;
if(content.match(profileSectionRegex)) {
    content = content.replace(profileSectionRegex, "");
}

fs.writeFileSync('app/src/main/java/com/example/ui/components/FocusMindApp.kt', content, 'utf8');
console.log("Profile section removed from settings");
