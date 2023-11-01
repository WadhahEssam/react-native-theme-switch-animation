import { NativeModules, Platform, DeviceEventEmitter } from 'react-native';
import { useEffect, useRef } from 'react';

const LINKING_ERROR =
  `The package 'react-native-theme-switch-animation' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ThemeSwitchAnimation = NativeModules.ThemeSwitchAnimation
  ? NativeModules.ThemeSwitchAnimation
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

type ThemeSwitcherHookProps = {
  switchThemeFunction: () => void;
};

const useThemeSwitcher = () => {
  const switchFunctionRef = useRef(() => {
    return;
  });

  useEffect(() => {
    const subscription = DeviceEventEmitter.addListener(
      'FINISHED_FREEZING_SCREEN',
      () => {
        setTimeout(() => {
          if (switchFunctionRef.current) {
            switchFunctionRef.current();
          }
          ThemeSwitchAnimation.unfreezeScreen();
        }, 20);
      }
    );

    return () => {
      subscription.remove();
    };
  }, []);

  const switchTheme = ({ switchThemeFunction }: ThemeSwitcherHookProps) => {
    ThemeSwitchAnimation.freezeScreen();
    switchFunctionRef.current = switchThemeFunction;
  };

  return {
    switchTheme: switchTheme,
  };
};

export default useThemeSwitcher;
