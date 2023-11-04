type CircularAnimationType = 'circular' | 'inverted-circular';

export type CircularAnimationConfig = {
  type: CircularAnimationType;
  duration: number;
  startingPoint: {
    cxRatio: number;
    cyRatio: number;
  };
};

export type CircularAnimationConfigExact = {
  type: CircularAnimationType;
  duration: number;
  startingPoint: {
    cx: number;
    cy: number;
  };
};

export type FadeAnimationConfig = {
  type: 'fade';
  duration: number;
};

export type AnimationConfig =
  | CircularAnimationConfig
  | FadeAnimationConfig
  | CircularAnimationConfigExact;

export type ThemeSwitcherHookProps = {
  switchThemeFunction: () => void;
  animationConfig?: AnimationConfig;
};
