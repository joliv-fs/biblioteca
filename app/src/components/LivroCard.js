import { useState } from "react";
import { Pressable, StyleSheet, Text, View } from "react-native";

// =====================================================================
// LivroCard – mostra UM livro e o botão de excluir.
//
// Props:
//   livro     → objeto do livro (veja o formato em services/api.js)
//   onExcluir → função da tela; chame onExcluir(livro.id) quando o usuário confirmar
//
// ESTRUTURA VISUAL (use exatamente estes estilos):
//
//   View .................................. styles.card
//   ├── View .............................. styles.info
//   │   ├── Text ......................... styles.titulo   → título do livro
//   │   ├── Text ......................... styles.detalhe  → "Machado de Assis · 1899"
//   │   └── Text ......................... styles.generos  → "Realismo, Romance"
//   │                                       (só aparece se o livro tiver gêneros)
//   │
//   ├── SE confirmando = false:
//   │   └── Pressable .................... styles.botao
//   │       └── Text ..................... styles.botaoTexto → "Excluir"
//   │
//   └── SE confirmando = true:
//       └── View ......................... styles.acoes
//           ├── Text ..................... styles.pergunta → "Excluir?"
//           ├── Pressable ................ [styles.botao, styles.botaoPerigo]
//           │   └── Text ................. styles.botaoTextoClaro → "Sim"
//           └── Pressable ................ styles.botao
//               └── Text ................. styles.botaoTexto → "Não"
// =====================================================================

export default function LivroCard({ livro, onExcluir }) {
  // ✏️ TODO 1 – Estado
  // Crie o estado "confirmando" (começa false).
  // Ele diz se o card está mostrando o botão "Excluir" ou a pergunta "Excluir? Sim / Não".

  const [confirmando, setConfirmando] = useState(false);

  // ✏️ TODO 2 – Texto dos gêneros
  // livro.generos é um array de objetos: [{ id: 2, nome: 'Realismo' }, { id: 1, nome: 'Romance' }]
  // Devolva o texto 'Realismo, Romance'.
  // Se o livro não tiver gêneros (array vazio), devolva '' (texto vazio).
  // Dica: map para pegar só os nomes; depois .join(', ') junta um array de textos com ", ".
  function textoDosGeneros() {
    return livro.generos?.map((g) => g.nome).join(", ") || "";
  }

  // ✏️ TODO 3 – Texto do autor e do ano
  // Com ano:  'Machado de Assis · 1899'
  // Sem ano (anoPublicacao é null): 'Machado de Assis'
  // Dica: o nome do autor está em livro.autor.nome; use template string.
  function textoDoAutor() {
    return `${livro.autor?.nome || "Autor desconhecido"}${livro.anoPublicacao ? ` · ${livro.anoPublicacao}` : ""}`;
  }

  // ✏️ TODO 4 – JSX
  // Monte o card seguindo a ESTRUTURA VISUAL do comentário lá em cima.
  //   - Use textoDoAutor() e textoDosGeneros() dentro dos Text.
  //   - O Text dos gêneros só aparece se textoDosGeneros() não for vazio.
  //   - Use o estado "confirmando" para escolher entre o botão "Excluir" e a pergunta.
  //   - Pressable "Excluir" → muda confirmando para true
  //   - Pressable "Sim"     → chama onExcluir(livro.id)
  //   - Pressable "Não"     → muda confirmando para false
  //
  // ATENÇÃO: para mostrar algo só às vezes, use o ternário (condição ? <A /> : null),
  // e não condição && <A />. Com texto vazio, o && quebra a tela no React Native.
  return (
    <View style={styles.card}>
      <View style={styles.info}>
        <Text style={styles.titulo}>{livro.titulo}</Text>
        <Text style={styles.detalhe}>{textoDoAutor()}</Text>
        {textoDosGeneros() ? (
          <Text style={styles.generos}>{textoDosGeneros()}</Text>
        ) : null}
        ;
      </View>

      {!confirmando ? (
        <Pressable style={styles.botao} onPress={() => setConfirmando(true)}>
          <Text style={styles.botaoTexto}>Excluir</Text>
        </Pressable>
      ) : (
        <View style={styles.acoes}>
          <Text style={styles.pergunta}>Excluir?</Text>
          <Pressable
            style={[styles.botao, styles.botaoPerigo]}
            onPress={() => onExcluir(livro.id)}
          >
            <Text style={styles.botaoTextoClaro}>Sim</Text>
          </Pressable>
          <Pressable style={styles.botao} onPress={() => setConfirmando(false)}>
            <Text style={styles.botaoTexto}>Não</Text>
          </Pressable>
        </View>
      )}
    </View>
  );
}

// =====================================================================
// 🟢 PRONTO – Estilos
// =====================================================================
const styles = StyleSheet.create({
  // Caixa do card: fundo branco, borda clara, cantos arredondados.
  // flexDirection 'row' coloca as informações à esquerda e os botões à direita.
  card: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#fff",
    padding: 16,
    borderRadius: 8,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: "#e5e5e5",
    gap: 12,
  },
  // Coluna das informações: flex 1 ocupa todo o espaço que sobrar ao lado dos botões.
  info: {
    flex: 1,
  },
  titulo: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#333",
  },
  detalhe: {
    fontSize: 14,
    color: "#555",
    marginTop: 4,
  },
  generos: {
    fontSize: 13,
    color: "#e8710a",
    marginTop: 6,
  },
  // Linha da confirmação: "Excluir?" + Sim + Não, lado a lado.
  acoes: {
    flexDirection: "row",
    alignItems: "center",
    gap: 8,
  },
  pergunta: {
    color: "#555",
  },
  // Botão neutro (Excluir e Não).
  botao: {
    paddingVertical: 6,
    paddingHorizontal: 12,
    borderRadius: 6,
    borderWidth: 1,
    borderColor: "#ccc",
    backgroundColor: "#fff",
  },
  // Complemento do botão "Sim": vermelho, para mostrar que é uma ação perigosa.
  // Usado junto com o botao: style={[styles.botao, styles.botaoPerigo]}
  botaoPerigo: {
    backgroundColor: "#c62828",
    borderColor: "#c62828",
  },
  botaoTexto: {
    color: "#333",
    fontWeight: "bold",
  },
  // Texto branco, para o botão vermelho.
  botaoTextoClaro: {
    color: "#fff",
    fontWeight: "bold",
  },
});
