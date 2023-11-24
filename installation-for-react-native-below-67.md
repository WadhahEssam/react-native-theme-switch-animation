## For React Native Versions 0.67 and Below
### iOS:
 **Update your `Podfile`** with the following line:
   ```
   pod 'react-native-theme-switch-animation', :path => '../node_modules/react-native-theme-switch-animation'
   ```
   
### Android:

Modify your `android/app/build.gradle` file to include:
```
implementation project(':react-native-theme-switch-animation')
```

Update your `settings.gradle` file with:
```
include ':react-native-theme-switch-animation'
project(':react-native-theme-switch-animation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-theme-switch-animation/android')
```

Update `MainActivity.java`:
```
import com.themeswitchanimation.ThemeSwitchAnimationPackage;
```

```
packages.add(new ThemeSwitchAnimationPackage());
```
