import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type { Int32 } from 'react-native/Libraries/Types/CodegenTypes';

export interface Spec extends TurboModule {
  freezeScreen(): void;
  unfreezeScreen(
    type: string,
    duration: Int32,
    cxRatio: Int32,
    cyRatio: Int32
  ): void;

  addListener: (eventName: string) => void;
  removeListeners: (count: number) => void;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'ThemeSwitchAnimationModule'
);
