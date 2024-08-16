import React, { useEffect } from 'react';
import {
  Button,
  StyleSheet,
  Text,
  View,
  StatusBar,
  Platform,
} from 'react-native';
import switchTheme from 'react-native-theme-switch-animation';

export default function App() {
  const [theme, setTheme] = React.useState<'light' | 'dark'>('light');

  useEffect(() => {
    // checking if the app is in new arch or not
    // @ts-ignore
    const uiManager = global?.nativeFabricUIManager ? 'Fabric' : 'Paper';
    console.log(`Using ${uiManager}`);
  }, []);

  return (
    <View
      style={{
        ...styles.container,
        backgroundColor: theme === 'light' ? 'white' : 'black',
      }}
    >
      <StatusBar
        backgroundColor={theme === 'light' ? 'white' : 'black'}
        barStyle={theme === 'light' ? 'dark-content' : 'light-content'}
        animated={Platform.OS === 'ios'}
      />
      <Text style={{ color: theme === 'light' ? 'black' : 'white' }}>
        Open up App.tsx to start working on your app!
      </Text>
      <Button
        title={'Switch Theme'}
        onPress={() =>
          switchTheme({
            switchThemeFunction: () => {
              setTheme(theme === 'light' ? 'dark' : 'light');
            },
            animationConfig: {
              type: 'fade',
              duration: 500,
            },
          })
        }
      />

      <Button
        title={'Switch Theme Circular'}
        onPress={() =>
          switchTheme({
            switchThemeFunction: () => {
              setTheme(theme === 'light' ? 'dark' : 'light');
            },
            animationConfig: {
              type: theme === 'light' ? 'circular' : 'inverted-circular',
              startingPoint: {
                cxRatio: 0.5,
                cyRatio: 0.2,
              },
              duration: 1000,
            },
          })
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
