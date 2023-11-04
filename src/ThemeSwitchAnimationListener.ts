import { NativeModules, NativeEventEmitter } from 'react-native';

export default class ThemeSwitchAnimationListener {
  private listenerAdded: boolean;
  private eventEmitter: NativeEventEmitter;

  constructor() {
    this.listenerAdded = false;
    this.eventEmitter = new NativeEventEmitter(
      NativeModules.ThemeSwitchAnimationModule
    );
  }

  addEventListener(callback: () => void) {
    if (!this.listenerAdded) {
      this.eventEmitter.addListener('FINISHED_FREEZING_SCREEN', callback);
      this.listenerAdded = true;
    }
  }
}
