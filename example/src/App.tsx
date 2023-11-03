/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';

import { StyleSheet, View, Button } from 'react-native';
import switchTheme from 'react-native-theme-switch-animation';

export default function App() {
  const [theme, setTheme] = React.useState('light');

  return (
    <View
      style={{
        ...styles.container,
        backgroundColor: theme === 'light' ? 'white' : 'black',
      }}
    >
      <Button
        title="Switch Theme"
        onPress={() => {
          switchTheme({
            switchThemeFunction: () => {
              setTheme(theme === 'light' ? 'dark' : 'light');
            },
            animationConfig: {
              type: 'fade',
              duration: 900,
            },
          });
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
