# Biblioteca

Sistema de biblioteca com uma API em Java/Spring Boot e um app em React Native/Expo.

- **Autores** e **gêneros** são cadastrados pela API.
- **Livros** são listados, cadastrados e excluídos pelo app.

Relacionamentos: um autor tem vários livros (1:N) e um livro tem vários gêneros (N:N).

## Estrutura

| Pasta  | O que tem                                                        |
| ------ | ---------------------------------------------------------------- |
| `api/` | API em Java com Spring Boot, JPA/Hibernate e banco H2 em arquivo |
| `app/` | App em React Native com Expo, rodando no navegador               |

## Como rodar

### API

```bash
cd api
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

A API fica em http://localhost:8080

### App

(será preenchido no M4)

## Fluxo de trabalho de cada milestone

1. Criar as sub-issues do milestone no GitHub
2. `git switch main` e `git pull`
3. `git switch -c mX-nome`
4. Resolver as sub-issues com commits (`Mx.y: ...`)
5. `git push -u origin mX-nome`
6. Pull request para a `main` com `Closes #N` de cada issue
7. Merge
8. `git switch main` e `git pull`
9. `git branch -d mX-nome` e `git push origin --delete mX-nome`
