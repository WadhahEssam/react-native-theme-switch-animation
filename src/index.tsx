import { NativeModules, Platform, Dimensions } from 'react-native';
import type {
  AnimationConfig,
  CircularAnimationConfig,
  CircularAnimationConfigExact,
  ThemeSwitcherHookProps,
} from './types';
import ThemeSwitchAnimationListener from './ThemeSwitchAnimationListener';
import {
  calculateActualRation,
  calculateRatio,
  validateCoordinates,
} from './helpers';

const { width, height } = Dimensions.get('screen');

const LINKING_ERROR =
  `The package 'react-native-theme-switch-animation' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ThemeSwitchAnimation = NativeModules.ThemeSwitchAnimationModule
  ? NativeModules.ThemeSwitchAnimationModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

let switchFunction: () => void = () => {};
let localAnimationConfig: AnimationConfig = {
  type: 'fade',
  duration: 500,
};

const themeSwitchAnimationListener = new ThemeSwitchAnimationListener();

themeSwitchAnimationListener.addEventListener(() => {
  setTimeout(() => {
    if (switchFunction) {
      switchFunction();
      if (localAnimationConfig) {
        unfreezeWrapper();
      }
    }
  });
});

const switchTheme = ({
  switchThemeFunction: incomingSwitchThemeFunction,
  animationConfig,
}: ThemeSwitcherHookProps) => {
  localAnimationConfig = animationConfig || localAnimationConfig;
  ThemeSwitchAnimation.freezeScreen();
  switchFunction = incomingSwitchThemeFunction;
};

const unfreezeWrapper = () => {
  const defaultRatio = 0.5;
  setImmediate(() => {
    if (
      localAnimationConfig.type === 'circular' ||
      localAnimationConfig.type === 'inverted-circular'
    ) {
      if (
        'cx' in localAnimationConfig.startingPoint &&
        'cy' in localAnimationConfig.startingPoint
      ) {
        const { cx, cy } = (
          localAnimationConfig as CircularAnimationConfigExact
        )?.startingPoint;

        if (
          validateCoordinates(cx, width, 'cx') &&
          validateCoordinates(cy, height, 'cy')
        ) {
          const cxRatio = calculateRatio(cx, width);
          const cyRatio = calculateRatio(cy, height);

          ThemeSwitchAnimation.unfreezeScreen(
            localAnimationConfig.type,
            localAnimationConfig.duration,
            cxRatio,
            cyRatio
          );
        } else {
          // cleanup
          ThemeSwitchAnimation.unfreezeScreen('fade', 200, 0.5, 0.5);
        }
      } else if (
        'cxRatio' in localAnimationConfig.startingPoint &&
        'cyRatio' in localAnimationConfig.startingPoint
      ) {
        const { cxRatio, cyRatio } = (
          localAnimationConfig as CircularAnimationConfig
        )?.startingPoint;

        if (
          validateCoordinates(cxRatio, 1, 'cxRatio') &&
          validateCoordinates(cyRatio, 1, 'cyRatio')
        ) {
          const cxRatioActual = calculateActualRation(cxRatio);
          const cyRatioActual = calculateActualRation(cyRatio);

          ThemeSwitchAnimation.unfreezeScreen(
            localAnimationConfig.type,
            localAnimationConfig.duration,
            cxRatioActual,
            cyRatioActual
          );
        } else {
          // cleanup
          ThemeSwitchAnimation.unfreezeScreen('fade', 500, 0.5, 0.5);
        }
      }
    } else {
      ThemeSwitchAnimation.unfreezeScreen(
        localAnimationConfig.type,
        localAnimationConfig.duration,
        defaultRatio,
        defaultRatio
      );
    }
  });
};

export default switchTheme;
