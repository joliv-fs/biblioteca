// =====================================================================
// SERVIÇO DE ACESSO À API
//
// TODA conversa do app com a API passa por este arquivo.
// As telas só importam e chamam as funções exportadas no final.
// =====================================================================

// Endereço da API. Rodando o app no NAVEGADOR, é localhost mesmo.
// (Num emulador Android seria http://10.0.2.2:8080)
const API_URL = "http://localhost:8080";

// ---------------------------------------------------------------------
// ErroApi: o erro que as funções deste arquivo lançam quando algo dá errado.
//
// Ele carrega as mesmas informações do JSON de erro da API (ErroResponseDTO):
//   erro.message → mensagem para mostrar ao usuário
//   erro.status  → código HTTP (400, 404, 409...). 0 = nem chegou na API
//   erro.campos  → erros por campo, ex.: { titulo: 'O título é obrigatório' }
// ---------------------------------------------------------------------
export class ErroApi extends Error {
  constructor(status, mensagem, campos = {}) {
    super(mensagem); // guarda a mensagem (lida depois com erro.message)
    this.status = status;
    this.campos = campos;
  }
}

// ---------------------------------------------------------------------
// requisitar: a função base. Todas as outras usam ela.
//
//   caminho → parte do endereço depois da porta, ex.: '/livros'
//   opcoes  → método, cabeçalhos e corpo (só quando não é GET)
//
// "async" quer dizer que a função demora (ela espera a API responder).
// "await" quer dizer "espere esta parte terminar antes de seguir".
// ---------------------------------------------------------------------
async function requisitar(caminho, opcoes) {
  let resposta;

  // 1. Tenta falar com a API.
  try {
    resposta = await fetch(`${API_URL}${caminho}`, opcoes);
  } catch {
    // O fetch só cai aqui se NEM conseguiu falar com a API:
    // API desligada, endereço errado ou CORS bloqueando.
    throw new ErroApi(0, "Não foi possível conectar à API. Ela está rodando?");
  }

  // 2. A API respondeu, mas com erro (400, 404, 409...).
  //    resposta.ok é true só para os códigos 200 a 299.
  if (!resposta.ok) {
    // A API manda um JSON de erro: { status, mensagem, campos }.
    // O .catch(() => null) evita quebrar se, por algum motivo, o corpo não for JSON.
    const erro = await resposta.json().catch(() => null);
    throw new ErroApi(
      resposta.status,
      erro?.mensagem ?? `Erro ${resposta.status}`, // ?. e ?? = "se existir, use; senão, use o outro"
      erro?.campos ?? {},
    );
  }

  // 3. Deu certo, mas sem corpo (204, resposta do DELETE).
  if (resposta.status === 204) {
    return null;
  }

  // 4. Deu certo com corpo: transforma o JSON em objeto/array do JavaScript.
  return resposta.json();
}

// =====================================================================
// FUNÇÕES QUE AS TELAS USAM
// =====================================================================

// ---------- Livros ----------
// Um livro chega assim (LivroResponseDTO):
// {
//   id: 1,
//   titulo: 'Dom Casmurro',
//   anoPublicacao: 1899,               // pode ser null
//   autor: { id: 1, nome: 'Machado de Assis', nacionalidade: 'Brasileira' },
//   generos: [ { id: 2, nome: 'Realismo' }, { id: 1, nome: 'Romance' } ]
// }

// Busca todos os livros. Devolve um ARRAY de livros.
export function listarLivros() {
  return requisitar("/livros");
}

// Exclui o livro com este id. Não devolve nada.
export function excluirLivro(id) {
  return requisitar(`/livros/${id}`, { method: "DELETE" });
}

// ---------- Livros (cadastro) ----------

// Cadastra um livro. Faz um POST /livros.
//
// "dados" é um objeto no formato do LivroRequestDTO da API:
//   { titulo: 'Dom Casmurro', anoPublicacao: 1899, autorId: 1 }
//   (anoPublicacao pode ser null)
//
// Se der certo: devolve o livro criado (com id, autor e gêneros).
// Se os dados estiverem errados: lança um ErroApi com status 400 e,
// em erro.campos, a mensagem de cada campo, ex.: { titulo: 'O título é obrigatório' }
export function criarLivro(dados) {
  return requisitar("/livros", {
    method: "POST", // POST = criar
    headers: { "Content-Type": "application/json" }, // avisa a API que o corpo é JSON (sem isso: erro 415)
    body: JSON.stringify(dados), // transforma o objeto JS em texto JSON
  });
}

// ---------- Autores ----------
// Um autor chega assim (AutorResponseDTO):
//   { id: 1, nome: 'Machado de Assis', nacionalidade: 'Brasileira' }

// Busca todos os autores. Devolve um ARRAY de autores.
// Usado na tela de cadastro, para o usuário escolher o autor do livro.
export function listarAutores() {
  return requisitar("/autores");
}
