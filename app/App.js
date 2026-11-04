import { StyleSheet, View } from "react-native";
import { StatusBar } from "expo-status-bar";

// ✏️ TODO 1 – Importe a LivrosScreen (ela está em ./src/screens/LivrosScreen)
import LivrosScreen from "./src/screens/LivrosScreen";

// =====================================================================
// App – ponto de entrada do app.
//
// ESTRUTURA VISUAL:
//   View ............ styles.container
//   ├── LivrosScreen
//   └── StatusBar ... style="auto"
// =====================================================================
export default function App() {
  // ✏️ TODO 2 – Coloque a LivrosScreen dentro da View, antes da StatusBar.
  return (
    <View style={styles.container}>
      <LivrosScreen />
      <StatusBar style="auto" />
    </View>
  );
}

// 🟢 PRONTO – Estilos
const styles = StyleSheet.create({
  // Ocupa a tela toda, com fundo cinza bem claro (os cards brancos se destacam).
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
  },
});
