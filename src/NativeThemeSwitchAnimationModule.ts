import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type { Double } from 'react-native/Libraries/Types/CodegenTypes';

export interface Spec extends TurboModule {
  freezeScreen(captureType: string): void;
  unfreezeScreen(
    type: string,
    duration: Double,
    cxRatio: Double,
    cyRatio: Double
  ): void;

  addListener: (eventName: string) => void;
  removeListeners: (count: number) => void;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'ThemeSwitchAnimationModule'
);
