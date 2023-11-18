import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-theme-switch-animation' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const IS_SUPPORTED_PLATFORM =
  Platform.OS === 'android' || Platform.OS === 'ios';

const module = (() => {
  if (IS_SUPPORTED_PLATFORM) {
    if (isTurboModuleEnabled) {
      return require('./NativeThemeSwitchAnimationModule').default;
    }
    if (NativeModules.ThemeSwitchAnimationModule) {
      return NativeModules.ThemeSwitchAnimationModule;
    }
    throw new Error(LINKING_ERROR);
  }
  return undefined;
})();

export default module;
