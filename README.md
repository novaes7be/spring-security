# Spring Essentials — JPA & Security Training

Projeto de estudo em **Java + Spring Boot**, criado para praticar dois pilares muito usados no dia a dia de back-end: **persistência de dados com Spring Data JPA** e **autenticação/autorização com Spring Security + JWT**.

Este README foi escrito pensando em quem está começando com Spring Boot — cada seção explica não só *o que* o código faz, mas *por que* ele existe.

---

## 📦 Sobre o projeto

A API gerencia um domínio de treino/academia, com recursos como:

- Exercícios (criar, listar, listar por grupo muscular, deletar)
- Avaliações físicas de alunos
- Alunos (cadastro)
- Treinos
- Produtos
- Autenticação (registrar / login)

Esses endpoints foram testados via Postman, como mostra a collection abaixo:

![Postman - Login retornando JWT](postman-login.png)

> No print acima, o endpoint `POST /v1/auth/login` retorna um **token JWT** e o tempo de expiração (`expiresIn`). Esse token é usado depois para acessar as rotas protegidas.

---

## 🗄️ Parte 1 — Spring Data JPA

O **Spring Data JPA** é o módulo do Spring que abstrai a comunicação com o banco de dados relacional. Em vez de escrever SQL na mão para cada operação, você trabalha com **entidades Java** e **interfaces de repositório**, e o Spring gera as queries por baixo dos panos.

Conceitos-chave usados neste projeto:

| Conceito | O que é | Para que serve aqui |
|---|---|---|
| `@Entity` | Anotação que transforma uma classe Java em uma tabela do banco | Modelar Exercicio, Avaliacao, Aluno, Treino, Produto |
| `JpaRepository<T, ID>` | Interface pronta do Spring com métodos como `save()`, `findById()`, `deleteById()` | Evita escrever CRUD manual para cada entidade |
| Query Methods | Métodos como `findByGrupoMuscular(...)` | Usado em "Listar Exercícios By grupo", por exemplo |
| DTOs | Classes que representam o formato de entrada/saída da API | Evita expor a entidade do banco diretamente no JSON de resposta |

### Fluxo típico de uma requisição JPA

```
Cliente (Postman) → Controller → Service → Repository → Banco de Dados
```

1. O **Controller** recebe a requisição HTTP (ex: `POST /v1/exercicio`).
2. O **Service** aplica as regras de negócio.
3. O **Repository** (via Spring Data JPA) conversa com o banco.
4. A resposta volta em formato JSON.

> 💡 Dica para iniciantes: você raramente implementa o Repository — normalmente basta criar uma interface que estende `JpaRepository`, e o Spring gera a implementação em tempo de execução.

---

## 🔐 Parte 2 — Spring Security + JWT

O **Spring Security** é o framework responsável por proteger a aplicação: decidir *quem pode acessar o quê*. Neste projeto ele é combinado com **JWT (JSON Web Token)**, um padrão para autenticação **stateless** (sem guardar sessão no servidor).

### 2.1 `SecurityConfiguration`

Essa classe é o "coração" da configuração de segurança:

![SecurityConfiguration](security-config.png)

O que cada trecho faz, em português claro:

- **`.csrf(AbstractHttpConfigurer::disable)`** — desativa a proteção CSRF, comum em APIs REST stateless (o CSRF é mais relevante em apps que usam sessão/cookies com formulários HTML).
- **`.sessionManagement(... STATELESS)`** — diz ao Spring para **não criar sessão HTTP**. Cada requisição precisa se autenticar sozinha, geralmente enviando o token JWT no header.
- **`.exceptionHandling(...)`** — define o que responder quando o usuário não está autenticado (`401 Unauthorized`) ou não tem permissão (`403 Forbidden`).
- **`.authorizeHttpRequests(...)`** — define as regras de acesso por rota:
  - `POST /v1/auth/**` com `.permitAll()` → rota pública (ex: login/registro).
  - `POST /v1/auth/**` com `.hasRole("ADMIN")` → só usuários com papel ADMIN.
  - `.anyRequest().authenticated()` → qualquer outra rota exige estar logado.
- **`.addFilterBefore(jwtAuthenticationFilter, ...)`** — insere o filtro customizado que lê o token JWT **antes** do filtro padrão de login por usuário/senha do Spring Security.
- **`PasswordEncoder` (`BCryptPasswordEncoder`)** — garante que senhas nunca sejam salvas em texto puro no banco; o BCrypt aplica hash + salt automaticamente.

### 2.2 Fluxo de autenticação JWT

```
1. Cliente envia login/senha → POST /v1/auth/login
2. Servidor valida credenciais
3. Servidor gera um JWT assinado e devolve ao cliente
4. Cliente envia esse JWT no header Authorization: Bearer <token>
5. JwtAuthenticationFilter valida o token em cada requisição protegida
```

Isso é exatamente o que aparece no print do Postman: o campo `Auth Type = Bearer Token` usa uma variável (`{{auth_secret_1891}}`) que guarda o JWT recebido no login.

> ⚠️ Nunca coloque o token JWT direto no corpo da requisição salvo na collection do Postman/Insomnia se for versionar/compartilhar a collection — prefira variáveis de ambiente, como já é feito aqui.

### 2.3 `GlobalExceptionHandler`

Trata erros de forma centralizada, devolvendo respostas JSON padronizadas em vez de stack traces:

![GlobalExceptionHandler](global-exception-handler.png)

- `NotFoundException` → responde `404 Not Found`
- `AccessDeniedException` → responde `403 Forbidden` (usuário autenticado, mas sem permissão)
- `Exception` (genérica) → responde `500 Internal Server Error`

A anotação usada normalmente junto a essa classe é `@RestControllerAdvice`, que intercepta exceções lançadas em qualquer Controller da aplicação.

---

## 🔒 Boas práticas de segurança aplicadas neste repositório

- Credenciais e segredos (ex: `application.yaml`, chave de assinatura do JWT) **não são versionados** — estão no `.gitignore`.
- Senhas são armazenadas com hash (`BCryptPasswordEncoder`), nunca em texto plano.
- Autenticação stateless via JWT, sem depender de sessão no servidor.
- Erros tratados de forma centralizada, evitando vazar detalhes internos da aplicação nas respostas.

> ⚠️ **Atenção:** confira se o nome do arquivo real de configuração (`application.yaml`) bate exatamente com o que está no `.gitignore` (`application.yml`). Extensões `.yaml` e `.yml` são diferentes para o Git — se não baterem, o arquivo sensível pode acabar sendo versionado por engano.

---

## 🚀 Como rodar o projeto

```bash
./mvnw spring-boot:run
```

A API sobe por padrão em `http://localhost:8082`.

### Exemplo de login (via curl)

```bash
curl -X POST http://localhost:8082/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "seu_usuario", "senha": "sua_senha"}'
```

A resposta trará um token JWT que deve ser enviado no header `Authorization: Bearer <token>` nas próximas requisições autenticadas.

---

## 📚 Próximos passos de estudo sugeridos

- Entender `@ManyToOne`, `@OneToMany` no JPA para relacionar Aluno ↔ Avaliação ↔ Treino.
- Explorar `@PreAuthorize` para autorização a nível de método.
- Estudar refresh tokens (o token atual expira em `expiresIn`, então vale entender como renovar sem pedir login de novo).
