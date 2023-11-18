import { NativeEventEmitter } from 'react-native';
import module from './module';

export default class ThemeSwitchAnimationListener {
  private listenerAdded: boolean;
  private eventEmitter: NativeEventEmitter;

  constructor() {
    this.listenerAdded = false;
    this.eventEmitter = new NativeEventEmitter(module);
  }

  addEventListener(callback: () => void) {
    if (!this.listenerAdded) {
      this.eventEmitter.addListener('FINISHED_FREEZING_SCREEN', callback);
      this.listenerAdded = true;
    }
  }
}
