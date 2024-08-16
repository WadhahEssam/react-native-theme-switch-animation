// Definition for CircularAnimationType
export type CircularAnimationType = 'circular' | 'inverted-circular';

// Definitions for specific animation configurations
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

// Composite type for any animation configuration
export type AnimationConfig =
    (| CircularAnimationConfig
    | FadeAnimationConfig
    | CircularAnimationConfigExact) & {
        captureType?: 'layer' | 'hierarchy';
    };

// Type for the properties expected by the switchTheme function
export interface ThemeSwitcherHookProps {
    switchThemeFunction: () => void;
    animationConfig?: AnimationConfig;
}

// Declaration of the switchTheme function
declare const switchTheme: (props: ThemeSwitcherHookProps) => void;

export default switchTheme;