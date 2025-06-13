# 📚 O Catálogo do Sábio - API RESTful

API desenvolvida como parte de um **desafio técnico para desenvolvedor java pleno**, simulando uma livraria online com funcionalidades avançadas: autenticação JWT, paginação, filtros, visualização recente de livros, cache com Redis, arquitetura limpa, testes unitários e automatizados.

---

## 1. Arquitetura de Solução e Arquitetura Técnica

### Arquitetura da Solução

O projeto foi desenvolvido seguindo os princípios da **Clean Architecture** e **Hexagonal Architecture**, promovendo separação clara de responsabilidades, baixo acoplamento e facilidade para manutenção e testes.

- **Camadas principais**:
  - `domain`: contém as entidades do negócio (`Book`, `Usuario`, `RecentlyViewedBook`) e interfaces (ports).
  - `application`: serviços, DTOs e regras de negócio.
  - `api`: controllers REST que expõem os endpoints.
  - `infrastructure`: implementação técnica, incluindo segurança, JWT, configuração Redis e exception.

Essa estrutura facilita a escalabilidade e a substituição de implementações sem impactar a lógica de negócio.

### Tecnologias Utilizadas

- **Java 17** e **Spring Boot**
- **Spring Security** com JWT para autenticação
- **Spring Data JPA** com **PostgreSQL** para persistência
- **Redis** para caching de consultas comuns (`@Cacheable`)
- **ModelMapper** para conversão entre Entidades e DTOs
- **Docker e Docker Compose** para ambiente integrado com containers (app, db, redis, testes)
- **JUnit 5 + Mockito** para testes unitários
- **Swagger / OpenAPI** para documentação interativa da API

---

## 2. Explicação sobre o Case Desenvolvido (Plano de Implementação)

### Modelo de Dados e Entidades

- **Book**: entidade que representa o livro, com atributos `id`, `title`, `author`, `genre` e `description`.
- **Usuario**: representa o usuário do sistema, com campos `id`, `username` e `password` (armazenada de forma segura com bcrypt).
- **RecentlyViewedBook**: tabela que registra o histórico de visualizações do usuário, relacionando `Usuario`, `Book` e a data/hora da visualização (`viewedAt`), permitindo exibir livros visualizados recentemente, ordenados por data.

### Fluxo de Autenticação JWT

- Endpoints `/login` e `/auth/register` são públicos.
- Ao autenticar com username e password, a aplicação gera um token JWT com uma chave secreta forte.
- O token contém o `username` e o `id` do usuário, e expira em 30 minutos.
- Cada requisição protegida deve enviar o token no header `Authorization: Bearer <token>`.
- Um filtro intercepta cada requisição, valida o token e autentica o usuário no contexto de segurança.

### Funcionalidades Principais

- **Login e Registro de Usuário**: criação e autenticação segura com JWT.
- **Consulta de livros**: endpoints com paginação e filtros por autor e gênero.
- **Visualização recente**: endpoint que retorna livros visualizados ordenados da visualização mais recente para a mais antiga, baseado na entidade `RecentlyViewedBook`.
- **Cache Redis**: caching de métodos como `findById`, buscas por autor e gênero, com invalidação automática após salvamento.
- **População inicial**: classe `DataLoader` insere clássicos da literatura para testes e demonstração.

### Configuração Redis e Cache

- Redis configurado via `RedisConfig`, com conexão standalone.
- Cache configurado para serializar objetos com JSON via `CacheConfig`.
- Serviços aplicam `@Cacheable` para acelerar respostas em consultas frequentes.

### Docker Compose

- Serviços:
  - `app`: aplicação Spring Boot.
  - `db`: banco PostgreSQL.
  - `redis`: cache Redis.
  - `tests`: container separado que executa testes automatizados com profile `test` (desabilita segurança para executar separadamente os testes).
- Comando para subir tudo:
  ```bash
  docker-compose up --build
  ```

- Testes podem ser executados isoladamente:

  ```bash
  docker-compose run --rm tests
  ```

### Testes Unitários

- Cobertura das principais camadas: serviços e controllers.
- Uso de mocks para dependências.
- Ambiente isolado com profile `test` sem autenticação.

---

## 3. Melhorias e Considerações Finais

### Possíveis melhorias

- Implementar busca por palavra-chave nos títulos e descrições.
- Adicionar testes de integração usando Testcontainers para garantir comportamento real da base e cache.
- Refinar controle de expiração do JWT e refresh token.
- Monitoramento de performance e métricas.
- Melhor tratamento de exceções e mensagens customizadas para API.

### Desafios encontrados

- Garantir segurança consistente nos testes exigiu perfil separado sem autenticação.
- Configuração do JWT e filtro para integração perfeita com Spring Security.

---

## 📑 Documentação e Endpoints

A documentação interativa está disponível via Swagger:
```bash
  http://localhost:8080/swagger-ui.html
```
---

### Autenticação

| Método | Endpoint         | Descrição                           |
| ------ | ---------------- | ----------------------------------- |
| POST   | `/auth/register` | Cadastro de novo usuário            |
| POST   | `/login`         | Autenticação e geração de token JWT |

---

### Livros (exigem token JWT no header Authorization)

| Método | Endpoint                 | Descrição                                                         |
| ------ | ------------------------ | ----------------------------------------------------------------- |
| GET    | `/books`                 | Lista livros com paginação                                        |
| GET    | `/books/{id}`            | Busca livro por ID (cacheado)                                     |
| GET    | `/books/author/{author}` | Busca livros por autor com paginação (cache)                      |
| GET    | `/books/genre/{genre}`   | Busca livros por gênero com paginação (cache)                     |
| GET    | `/books/recently-viewed` | Livros visualizados recentemente pelo usuário, ordenados por data |

---

## 🏁 Conclusão

Este projeto demonstra a aplicação sólida de boas práticas em desenvolvimento Java com Spring Boot, incluindo arquitetura limpa, segurança robusta via JWT, caching eficiente com Redis, testes automatizados e deploy containerizado. A solução é escalável, segura e de fácil manutenção, atendendo com excelência ao desafio técnico proposto.

---

## 📋 Requisitos

Antes de executar a aplicação, certifique-se de que o ambiente possua as seguintes ferramentas instaladas e configuradas:

- **Git** (para clonar o repositório)  
- **Docker** (versão estável, para executar containers)  
- **Docker Compose** (para orquestrar múltiplos containers)  
- **Java 17** (necessário para desenvolvimento local ou build manual, opcional se usar Docker)

> *Obs.:* A aplicação roda via Docker Compose, que já inclui o ambiente necessário para a API, banco de dados e cache Redis, então não é necessário instalar manualmente PostgreSQL ou Redis localmente.

---

## 🐳 Como rodar

1. Clone o repositório:
  ```bash
    git clone https://github.com/RodrigoBritoGit/livraria-sabio-api.git
  ```
2. Execute o comando para subir a aplicação com Docker Compose:
  ```bash
    docker-compose up --build
  ```
3. Acesse
  ```bash
    http://localhost:8080
  ```
4. Utilize Swagger, Insomnia ou Postman para explorar a API e testar os endpoints

---

## 👤 Autor

**Rodrigo dos Santos Brito**  
📧 [rodrigodossantosbrito@hotmail.com](mailto:rodrigodossantosbrito@hotmail.com)  
🔗 GitHub: [https://github.com/RodrigoBritoGit](https://github.com/RodrigoBritoGit)  
🔗 LinkedIn: [https://www.linkedin.com/in/rodrigo-brito-19bb54136/](https://www.linkedin.com/in/rodrigo-brito-19bb54136/)

Qualquer dúvida, estou à disposição!



