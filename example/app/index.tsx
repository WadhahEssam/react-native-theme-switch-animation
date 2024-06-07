import { StyleSheet, Button, View, Text } from 'react-native';

import React from 'react';
import switchTheme from 'react-native-theme-switch-animation';

export default function HomeScreen() {
  const [theme, setTheme] = React.useState<'light' | 'dark'>('light');

  return (
    <View
      style={{
        ...styles.container,
        backgroundColor: theme === 'light' ? 'white' : 'black',
      }}
    >
      <View
        style={{
          borderWidth: 1,
          borderColor: theme === 'light' ? 'black' : 'white',
          borderRadius: 1.4,
          padding: 50,
        }}
      >
        <Text
          style={{
            color: theme === 'light' ? 'black' : 'white',
          }}
        >
          test
        </Text>
      </View>
      <Button
        title="Switch Theme"
        onPress={() => {
          setTheme(theme === 'light' ? 'dark' : 'light');

          switchTheme({
            switchThemeFunction: () => {
              setTheme(theme === 'light' ? 'dark' : 'light');
            },
            animationConfig: {
              type: 'inverted-circular',
              duration: 1000,
              startingPoint: {
                cxRatio: 0.5,
                cyRatio: 0,
              },
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
