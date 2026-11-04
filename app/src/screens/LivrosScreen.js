import { useEffect, useState } from "react";
import {
  ActivityIndicator,
  FlatList,
  Pressable,
  StyleSheet,
  Text,
  View,
} from "react-native";

import LivroCard from "../components/LivroCard";
import { excluirLivro, listarLivros } from "../services/api";

// =====================================================================
// LivrosScreen – a tela com a lista de livros.
//
// ESTRUTURA VISUAL (use exatamente estes estilos):
//
//   SE carregando:
//     View ........................ styles.centro
//     └── ActivityIndicator ....... size="large" color="#e8710a"
//
//   SE tem erro:
//     View ........................ styles.centro
//     ├── Text .................... styles.erro → a mensagem de erro
//     └── Pressable ............... styles.botao → onPress chama carregar
//         └── Text ................ styles.botaoTexto → "Tentar de novo"
//
//   SENÃO (pronto):
//     View ........................ styles.container
//     ├── Text .................... styles.titulo → "Livros"
//     └── FlatList
//         ├── data → os livros
//         ├── keyExtractor → o id de cada livro, como texto
//         ├── renderItem → um LivroCard para cada livro (com a prop onExcluir)
//         └── ListEmptyComponent → Text styles.vazio "Nenhum livro cadastrado."
// =====================================================================

export default function LivrosScreen({ onNovo }) {
  // ✏️ TODO 1 – Estados
  // Crie os três estados da tela, com ESTES nomes (o código pronto abaixo usa eles):
  //   livros     → começa com []
  //   carregando → começa com true
  //   erro       → começa com null

  const [livros, setLivros] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);

  // -------------------------------------------------------------------
  // 🟢 PRONTO – Buscar os livros na API
  // Liga o "carregando", limpa o erro, busca os livros e guarda no estado.
  // Se der errado, guarda a mensagem de erro. No fim, desliga o "carregando".
  // -------------------------------------------------------------------
  async function carregar() {
    setCarregando(true);
    setErro(null);
    try {
      const dados = await listarLivros(); // espera a API responder
      setLivros(dados); // guarda os livros no estado → a tela redesenha
    } catch (e) {
      setErro(e.message); // ex.: "Não foi possível conectar à API..."
    } finally {
      setCarregando(false); // roda sempre, tenha dado certo ou não
    }
  }

  // -------------------------------------------------------------------
  // 🟢 PRONTO – Buscar quando a tela abre
  // O useEffect com [] no final roda UMA vez, logo depois que a tela aparece.
  // (useEffect é assunto das próximas aulas: por enquanto, ele vem pronto.)
  // -------------------------------------------------------------------
  useEffect(() => {
    carregar();
  }, []);

  // -------------------------------------------------------------------
  // 🟢 PRONTO – Excluir na API
  // Pede para a API excluir. Se der certo, chama removerDaLista (que é sua).
  // Se der errado, mostra o erro.
  // -------------------------------------------------------------------
  async function excluir(id) {
    try {
      await excluirLivro(id);
      removerDaLista(id);
    } catch (e) {
      setErro(e.message);
    }
  }

  // ✏️ TODO 2 – Tirar o livro da lista
  // A API já excluiu o livro. Agora tire ele do estado "livros",
  // para ele sumir da tela sem buscar tudo de novo.
  // Dica: filter, mantendo só os livros cujo id é DIFERENTE do id recebido.
  // (É o mesmo "remover" da lista de tarefas.)
  function removerDaLista(id) {
    setLivros(livros.filter((livro) => livro.id !== id));
  }

  // ✏️ TODO 3 – Tela "carregando"
  // Se carregando for true, devolva (return) a tela de carregando.

  if (carregando) {
    return (
      <View style={styles.centro}>
        <ActivityIndicator></ActivityIndicator>
      </View>
    );
  }

  // ✏️ TODO 4 – Tela de erro
  // Se erro não for null, devolva a tela de erro.
  // O botão "Tentar de novo" chama a função carregar.

  if (erro) {
    return (
      <View style={styles.centro}>
        <Text style={styles.erro}>{erro}</Text>
        <Pressable style={styles.botao} onPress={carregar}>
          <Text style={styles.botaoTexto}>Tentar de novo</Text>
        </Pressable>
      </View>
    );
  }

  // ✏️ TODO 5 – Tela pronta
  // Devolva o título e a FlatList, seguindo a ESTRUTURA VISUAL.
  // No renderItem, passe para o LivroCard:
  //   livro={item}
  //   onExcluir={excluir}   ← a função PRONTA lá de cima (e não removerDaLista)
  return (
    <View style={styles.container}>
      <View style={styles.cabecalho}>
        <Text style={styles.titulo}>Livros</Text>
        <Pressable style={styles.botao} onPress={onNovo}>
          <Text style={styles.botaoTexto}>Novo livro</Text>
        </Pressable>
      </View>

      <FlatList
        data={livros}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <LivroCard livro={item} onExcluir={() => excluir(item.id)} />
        )}
        ListEmptyComponent={
          <Text style={styles.vazio}>Nenhum livro cadastrado.</Text>
        }
      ></FlatList>
    </View>
  );
}

// =====================================================================
// 🟢 PRONTO – Estilos
// =====================================================================
const styles = StyleSheet.create({
  // Tela pronta: ocupa tudo, com espaço nas bordas.
  container: {
    flex: 1,
    padding: 16,
  },
  // Telas de carregando e de erro: conteúdo centralizado no meio da tela.
  centro: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 16,
    gap: 16,
  },
  // NOVO NO M5: linha com o título à esquerda e o botão "Novo livro" à direita.
  cabecalho: {
    flexDirection: "row",
    justifyContent: "space-between", // um em cada ponta
    alignItems: "center",
    marginBottom: 16,
  },
  // O marginBottom saiu daqui e foi para o cabecalho.
  titulo: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#333",
  },
  // Botão laranja (Tentar de novo).
  botao: {
    backgroundColor: "#e8710a",
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: 6,
  },
  botaoTexto: {
    color: "#fff",
    fontWeight: "bold",
  },
  erro: {
    color: "#c62828",
    fontSize: 16,
    textAlign: "center",
  },
  // Aviso de lista vazia.
  vazio: {
    textAlign: "center",
    color: "#777",
    marginTop: 32,
  },
});
