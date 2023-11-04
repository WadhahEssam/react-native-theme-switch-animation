# react-native-theme-switch-animation

A Plug & Play Animations for Switching (Dark/Light) themes. 🌗

### 🦄 Features
- ✅  Supports multiple animation types.
- ✅  Blazing fast - [60/120]fps
- ✅  Plug and Play, doesn't matter whay you use for switching themes 
- ✅  Can be used for different theme colors, not necessarly for dark/light

<p align="center">
<img src="https://github.com/WadhahEssam/react-native-theme-switch-animation/assets/24798045/0aa19507-702e-4075-b045-303ad27b3dc0" width="600"/>
</p>

## Installation

```sh
npm install react-native-theme-switch-animation
```
OR
```sh
yarn add react-native-theme-switch-animation
```

## Link
(if you are using expo managed project, do a prebuild - `npx expo prebuild`)
```
npx pod-install
```

## Usage

```js
import switchTheme from 'react-native-theme-switch-animation';

export default function Example() {
  const [theme, setTheme] = React.useState('light');

  return (
    <Button
      title="Switch Theme"
      onPress={() => {

        switchTheme({
          switchThemeFunction: () => {
            setTheme(theme === 'light' ? 'dark' : 'light'); // your switch theme function
          },
          animationConfig: {
            type: 'fade',
            duration: 900,
          },
        });

      }}
    />
  );
}
```

Circular Example
```js
switchTheme({
  switchThemeFunction: () => {
    setTheme(theme === 'light' ? 'dark' : 'light'); // your switch theme function
  },
  animationConfig: {
    type: 'circular',
    duration: 900,
    startingPoint: {
      cxRatio: 0.5,
      cyRation: 0.5
    }
  },
});
```


## switchTheme Function Props
| Name | Type | Description |
| :------ | :------ | :------ |
| `switchThemeFunction` | `() => void` | Adds the function you use in your app to switch themes, doesn't matter if you use redux/context/zustand/mobx or any other way |
| `animationConfig` | `AnimationConfig` | Configuration for the animation -> type, duration, starting point (`default is 'fade' with 500ms duration`)  |

## animationConfig options
| Name | Type | Description |
| :------ | :------ | :------ |
| `type` | `fade` `circular` `inverted-circular` | Specifies animation type |
| `duration` | `number` | Specifies duration in milliseconds |
| `startingPoint` | `StartingPointConfig` | Configuration for the `circular` animation, where does the animation start in the screen |

## startingPoint options
| `cx` | `number` | Specifies starting x point for `circular` and `inverted-circular` animation (should not exceed your screen width) |
| `cy` | `number` | Specifies starting y point for `circular` and `inverted-circular` animation (should not exceed your screen height) |
| `cxRatio` | `number` | Specifies starting percentage of x point for `circular` and `inverted-circular` animation (should be number between -1 and 1) |
| `cyRatio` | `number` | Specifies starting percentage of y point for `circular` and `inverted-circular` animation (should be number between -1 and 1) |

## Auto Circular Animation Button
If you would like the circular animation to start from/to a button on your ui automatically, you can do the following

```js
import switchTheme from 'react-native-theme-switch-animation';

<TouchableOpacity
  onPress={(e) => {
    e.currentTarget.measure((x1, y1, width, height, px, py) => {
      switchTheme({
        switchThemeFunction: () => {
          setTheme(theme === 'light' ? 'dark' : 'light');
        },
        animationConfig: {
          type: 'circular',
          duration: 900,
          startingPoint: {
            cy: py + height / 2,
            cx: px + width / 2,
          }
        },
      });
    });
  }}
/>
```


## License

MIT

