import { useState } from "react";
import { StyleSheet, View } from "react-native";
import { StatusBar } from "expo-status-bar";

import LivrosScreen from "./src/screens/LivrosScreen";
import CadastroLivroScreen from "./src/screens/CadastroLivroScreen";

// =====================================================================
// App – ponto de entrada. Decide qual tela aparece.
//
// ESTRUTURA VISUAL:
//   View ................................ styles.container
//   ├── SE tela = 'lista':    LivrosScreen         (prop onNovo)
//   ├── SE tela = 'cadastro': CadastroLivroScreen  (props onVoltar e onSalvo)
//   └── StatusBar ....................... style="auto"
// =====================================================================
export default function App() {
  // ✏️ TODO 1 – Estado
  // Crie o estado "tela", começando com 'lista'.
  const [tela, setTela] = useState("lista");

  // ✏️ TODO 2 – Abrir o cadastro
  // Muda a tela para 'cadastro'. Vai ser passada para a LivrosScreen (prop onNovo).
  function abrirCadastro() {
    setTela("cadastro");
  }

  // ✏️ TODO 3 – Voltar para a lista
  // Muda a tela para 'lista'. Vai ser passada para a CadastroLivroScreen
  // (props onVoltar e onSalvo: nos dois casos, a ação é voltar para a lista).
  function voltarParaLista() {
    setTela("lista");
  }

  // ✏️ TODO 4 – JSX
  // Dentro da View, antes da StatusBar, mostre UMA das duas telas, conforme o estado "tela".
  // Dica: ternário → tela === 'lista' ? <A /> : <B />
  // Passe as funções dos TODOs 2 e 3 como props (sem parênteses: onNovo={abrirCadastro}).
  return (
    <View style={styles.container}>
      {tela === "lista" ? (
        <LivrosScreen onNovo={abrirCadastro} />
      ) : (
        <CadastroLivroScreen
          onVoltar={voltarParaLista}
          onSalvo={voltarParaLista}
        />
      )}
      <StatusBar style="auto" />
    </View>
  );
}

// 🟢 PRONTO – Estilos
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
  },
});
