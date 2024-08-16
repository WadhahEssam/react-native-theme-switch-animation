type CircularAnimationType = 'circular' | 'inverted-circular';

type CircularAnimationConfig = {
  type: CircularAnimationType;
  duration: number;
  startingPoint: {
    cxRatio: number;
    cyRatio: number;
  };
};

type CircularAnimationConfigExact = {
  type: CircularAnimationType;
  duration: number;
  startingPoint: {
    cx: number;
    cy: number;
  };
};

type FadeAnimationConfig = {
  type: 'fade';
  duration: number;
};

type AnimationConfig = (
  | CircularAnimationConfig
  | FadeAnimationConfig
  | CircularAnimationConfigExact
) & {
  captureType?: 'layer' | 'hierarchy';
};

interface ThemeSwitcherHookProps {
  switchThemeFunction: () => void;
  animationConfig?: AnimationConfig;
}

declare const switchTheme: (props: ThemeSwitcherHookProps) => void;

export {
  switchTheme,
  ThemeSwitcherHookProps,
  AnimationConfig,
  CircularAnimationConfigExact,
  CircularAnimationConfig,
};
