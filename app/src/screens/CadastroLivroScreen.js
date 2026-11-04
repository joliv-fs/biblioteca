import { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";

import { criarLivro, listarAutores } from "../services/api";

// =====================================================================
// CadastroLivroScreen – formulário para cadastrar um livro.
//
// Props:
//   onVoltar → função do App.js: volta para a lista sem salvar ("Cancelar")
//   onSalvo  → função do App.js: chamada depois que a API confirmou o cadastro
//
// ESTRUTURA VISUAL (use exatamente estes estilos):
//
//   ScrollView ............................. contentContainerStyle={styles.conteudo}
//   ├── Text ............................... styles.tituloTela → "Novo livro"
//   │
//   ├── Text ............................... styles.rotulo → "Título"
//   ├── TextInput .......................... styles.input  (placeholder "Ex.: Dom Casmurro")
//   ├── Text ............................... styles.erroCampo → errosCampos.titulo (só se existir)
//   │
//   ├── Text ............................... styles.rotulo → "Ano de publicação (opcional)"
//   ├── TextInput .......................... styles.input  (placeholder "Ex.: 1899", maxLength={4})
//   ├── Text ............................... styles.erroCampo → errosCampos.anoPublicacao (só se existir)
//   │
//   ├── Text ............................... styles.rotulo → "Autor"
//   ├── renderizarAutores() ................ (TODO 5: carregando, aviso ou os botões)
//   ├── Text ............................... styles.erroCampo → errosCampos.autorId (só se existir)
//   │
//   ├── Text ............................... styles.erroGeral → erro (só se existir)
//   │
//   └── View ............................... styles.botoes
//       ├── Pressable ...................... [styles.botao, styles.botaoSecundario] → onVoltar
//       │   └── Text ....................... styles.botaoSecundarioTexto → "Cancelar"
//       └── Pressable ...................... [styles.botao, salvando && styles.botaoDesabilitado]
//           │                                onPress={salvar}  disabled={salvando}
//           └── Text ....................... styles.botaoTexto → "Salvando..." ou "Salvar"
// =====================================================================

export default function CadastroLivroScreen({ onVoltar, onSalvo }) {
  // ✏️ TODO 1 – Estados dos campos
  // Crie os estados do formulário, com ESTES nomes (o código pronto usa eles):
  //   titulo  → começa com ''
  //   ano     → começa com ''
  //   autorId → começa com null
  const [titulo, setTitulo] = useState(" ");
  const [ano, setAno] = useState(" ");
  const [autorId, setAutorId] = useState(null);

  // -------------------------------------------------------------------
  // 🟢 PRONTO – Estados de controle
  // -------------------------------------------------------------------
  const [autores, setAutores] = useState([]); // autores vindos da API
  const [carregandoAutores, setCarregandoAutores] = useState(true);
  const [salvando, setSalvando] = useState(false); // true enquanto espera a API salvar
  const [erro, setErro] = useState(null); // mensagem geral de erro
  const [errosCampos, setErrosCampos] = useState({}); // { titulo: '...', autorId: '...' }

  // -------------------------------------------------------------------
  // 🟢 PRONTO – Buscar os autores quando a tela abre
  // -------------------------------------------------------------------
  useEffect(() => {
    async function carregarAutores() {
      try {
        setAutores(await listarAutores());
      } catch (e) {
        setErro(e.message);
      } finally {
        setCarregandoAutores(false);
      }
    }
    carregarAutores();
  }, []);

  // -------------------------------------------------------------------
  // 🟢 PRONTO – Salvar
  // Limpa os erros antigos, manda os dados para a API (usando montarDados, que é sua)
  // e, se der certo, chama onSalvo. Se der errado, guarda a mensagem geral em "erro"
  // e as mensagens de cada campo em "errosCampos".
  // -------------------------------------------------------------------
  async function salvar() {
    setSalvando(true);
    setErro(null);
    setErrosCampos({});
    try {
      await criarLivro(montarDados());
      onSalvo();
    } catch (e) {
      setErro(e.message);
      setErrosCampos(e.campos ?? {});
    } finally {
      setSalvando(false);
    }
  }

  // -------------------------------------------------------------------
  // 🟢 PRONTO – Deixa só os dígitos de um texto ('18a9x' → '189')
  // Use no onChangeText do campo de ano, para ele só aceitar números.
  // -------------------------------------------------------------------
  function somenteNumeros(texto) {
    return texto.replace(/[^0-9]/g, "");
  }

  // ✏️ TODO 2 – Montar os dados para a API
  // Devolva um objeto no formato que a API espera (LivroRequestDTO):
  //   { titulo: ..., anoPublicacao: ..., autorId: ... }
  // Atenção ao ano:
  //   - o estado "ano" é TEXTO ('1899'), mas a API espera NÚMERO (1899): use Number(ano);
  //   - se o campo estiver vazio (''), mande null (o ano é opcional).
  function montarDados() {
    return {
      titulo: titulo,
      anoPublicacao: ano === "" ? null : Number(ano),
      autorId: autorId,
    };
  }

  // ✏️ TODO 3 – Escolher um autor
  // Guarde o id recebido no estado autorId.
  function escolherAutor(id) {
    setAutorId;
  }

  // ✏️ TODO 4 – Saber se um autor é o escolhido
  // Devolva true se o id recebido for igual ao autorId; false se não for.
  // É usado para pintar de laranja o botão do autor escolhido.
  function estaSelecionado(id) {
    return id === autorId;
  }

  // ✏️ TODO 5 – Parte dos autores
  // Devolva o que aparece embaixo do rótulo "Autor". São três situações:
  //
  //   1. carregandoAutores é true:
  //        ActivityIndicator color="#e8710a"
  //
  //   2. a lista de autores está vazia:
  //        Text styles.aviso → "Nenhum autor cadastrado. Cadastre pela API primeiro."
  //
  //   3. senão, um botão para cada autor (map):
  //        View ............................ styles.opcoes
  //        └── para cada autor:
  //            Pressable ................... key={autor.id}
  //            │                             style={[styles.opcao, estaSelecionado(autor.id) && styles.opcaoSelecionada]}
  //            │                             onPress → escolherAutor(autor.id)
  //            └── Text .................... style={[styles.opcaoTexto, estaSelecionado(autor.id) && styles.opcaoTextoSelecionado]}
  //                                          → autor.nome
  function renderizarAutores() {
    if (carregandoAutores) {
      return <ActivityIndicator color="#e8710a" />;
    }

    if (autores.length === 0) {
      return (
        <Text style={styles.aviso}>
          Nenhum autor cadastrado. Cadastre pela API primeiro.
        </Text>
      );
    }

    return (
      <View style={styles.opcoes}>
        {autores.map((autor) => (
          <Pressable
            key={autor.id}
            style={[
              styles.opcao,
              estaSelecionado(autor.id) && styles.opcaoSelecionada,
            ]}
            onPress={() => setAutorId(autor.id)}
          >
            <Text
              style={[
                styles.opcaoTexto,
                estaSelecionado(autor.id) && styles.opcaoTextoSelecionado,
              ]}
            >
              {autor.nome}
            </Text>
          </Pressable>
        ))}
      </View>
    );
  }

  // ✏️ TODO 6 – JSX
  // Monte o formulário seguindo a ESTRUTURA VISUAL lá em cima.
  //   - Cada TextInput usa value e onChangeText com o seu estado.
  //   - No ano: onChangeText={(texto) => setAno(somenteNumeros(texto))}
  //   - Cada mensagem de erro só aparece se existir (ternário: condição ? <Text /> : null).
  //   - Botão Salvar: onPress={salvar} e disabled={salvando}.
  return (
    <ScrollView contentContainerStyle={styles.conteudo}>
      <Text style={styles.tituloTela}>Novo livro</Text>

      <Text style={styles.rotulo}>Título</Text>

      <TextInput
        style={styles.input}
        placeholder="Ex.: Dom Casmurro"
        value={titulo}
        onChangeText={setTitulo}
      />

      {errosCampos.titulo ? (
        <Text style={styles.erroCampo}>{errosCampos.titulo}</Text>
      ) : null}

      <Text style={styles.rotulo}>Ano de publicação (opcional)</Text>
      <TextInput
        style={styles.input}
        placeholder="Ex.: 1899"
        maxLength={4}
        onChangeText={(texto) => setAno(somenteNumeros(texto))}
      />

      {errosCampos.anoPublicacao ? (
        <Text style={styles.erroCampo}>{errosCampos.anoPublicacao}</Text>
      ) : null}

      <Text style={styles.rotulo}>Autor</Text>
      {renderizarAutores()}
      {errosCampos.autorId ? (
        <Text style={styles.erroCampo}>{errosCampos.autorId}</Text>
      ) : null}

      {erro ? <Text style={styles.erroGeral}>{erro}</Text> : null}

      <View style={styles.botoes}>
        <Pressable
          style={[styles.botao, styles.botaoSecundario]}
          onPress={onVoltar}
        >
          <Text style={styles.botaoSecundarioTexto}>Cancelar</Text>
        </Pressable>

        <Pressable
          style={[styles.botao, salvando && styles.botaoDesabilitado]}
          onPress={salvar}
          disabled={salvando}
        ></Pressable>
        <Text style={styles.botaoTexto}>
          {salvando ? "Salvando..." : "Salvar"}
        </Text>
      </View>
    </ScrollView>
  );
}

// =====================================================================
// 🟢 PRONTO – Estilos
// =====================================================================
const styles = StyleSheet.create({
  // Espaço nas bordas do formulário.
  conteudo: {
    padding: 16,
  },
  tituloTela: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 16,
  },
  // Rótulo em cima de cada campo.
  rotulo: {
    fontSize: 14,
    fontWeight: "bold",
    color: "#555",
    marginTop: 12,
    marginBottom: 6,
  },
  // Caixa de texto: fundo branco e borda cinza.
  input: {
    backgroundColor: "#fff",
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 6,
    padding: 10,
    fontSize: 16,
  },
  // Linha com os botões de autor. flexWrap quebra para a linha de baixo quando não cabe.
  opcoes: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 8,
  },
  // Botão de autor (formato de "pílula").
  opcao: {
    paddingVertical: 8,
    paddingHorizontal: 14,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: "#ccc",
    backgroundColor: "#fff",
  },
  // Complemento do botão do autor ESCOLHIDO: fundo laranja.
  opcaoSelecionada: {
    backgroundColor: "#e8710a",
    borderColor: "#e8710a",
  },
  opcaoTexto: {
    color: "#333",
  },
  // Complemento do texto do autor ESCOLHIDO: branco e em negrito.
  opcaoTextoSelecionado: {
    color: "#fff",
    fontWeight: "bold",
  },
  // Aviso de "nenhum autor".
  aviso: {
    color: "#777",
  },
  // Erro embaixo de um campo: vermelho e pequeno.
  erroCampo: {
    color: "#c62828",
    fontSize: 13,
    marginTop: 4,
  },
  // Erro geral, acima dos botões.
  erroGeral: {
    color: "#c62828",
    fontSize: 15,
    marginTop: 16,
    textAlign: "center",
  },
  // Linha dos botões, alinhada à direita.
  botoes: {
    flexDirection: "row",
    justifyContent: "flex-end",
    gap: 12,
    marginTop: 24,
  },
  // Botão laranja (Salvar).
  botao: {
    backgroundColor: "#e8710a",
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 6,
  },
  botaoTexto: {
    color: "#fff",
    fontWeight: "bold",
  },
  // Complemento do botão Cancelar: branco com borda.
  // Usado junto com o botao: style={[styles.botao, styles.botaoSecundario]}
  botaoSecundario: {
    backgroundColor: "#fff",
    borderWidth: 1,
    borderColor: "#ccc",
  },
  botaoSecundarioTexto: {
    color: "#333",
    fontWeight: "bold",
  },
  // Complemento do botão Salvar enquanto salva: meio transparente.
  botaoDesabilitado: {
    opacity: 0.6,
  },
});
