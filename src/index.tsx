import { Platform, Dimensions } from 'react-native';
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
import module from './module';

const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('screen');
const IS_SUPPORTED_PLATFORM =
  Platform.OS === 'android' || Platform.OS === 'ios';
let ThemeSwitchAnimation: any = null;
let switchFunction: () => void = () => {};
let localAnimationConfig: AnimationConfig = {
  type: 'fade',
  duration: 500,
};

if (IS_SUPPORTED_PLATFORM) {
  ThemeSwitchAnimation = module;

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
}

const switchTheme = ({
  switchThemeFunction: incomingSwitchThemeFunction,
  animationConfig,
}: ThemeSwitcherHookProps) => {
  if (IS_SUPPORTED_PLATFORM) {
    localAnimationConfig = animationConfig || localAnimationConfig;
    ThemeSwitchAnimation.freezeScreen();
    switchFunction = incomingSwitchThemeFunction;
  } else {
    incomingSwitchThemeFunction();
  }
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
          validateCoordinates(cx, SCREEN_WIDTH, 'cx') &&
          validateCoordinates(cy, SCREEN_HEIGHT, 'cy')
        ) {
          const cxRatio = calculateRatio(cx, SCREEN_WIDTH);
          const cyRatio = calculateRatio(cy, SCREEN_HEIGHT);

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
