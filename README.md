# SCCON - Geospatial Java Evaluation

Esta API REST foi desenvolvida com **Spring Boot 3.x** para o gerenciamento de registros de pessoas, focando em arquitetura limpa, separação de responsabilidades e cálculos precisos de progressão salarial.

## 🛠️ Tecnologias e Melhorias Aplicadas

* **Java 17 & Spring Boot 3.x**: Uso de *Records*, *Switch Expressions* e APIs de data modernas (`java.time`).
* **Arquitetura em Camadas**: Separação rigorosa entre `Controller`, `Service`, `Repository` e `Mapper` (DTO).
* **Programação Funcional**: Cálculos de progressão salarial implementados com **Java Streams (`Stream.iterate`)**.
* **Tratamento de Erros Semântico**: Exceções customizadas (`BusinessException` e `ResourceNotFoundException`) com *Static Factory Methods* e `@ResponseStatus`.
* **Persistência Moderna**: Estratégia de ID configurada como `IDENTITY` (Autoincremento nativo do banco H2).
* **Lombok**: Redução de boilerplate para getters, setters e construtores.

---

## 🚀 Como Executar o Projeto

1.  **Clonar o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/sccon-api.git](https://github.com/seu-usuario/sccon-api.git)
    ```
2.  **Compilar e baixar dependências:**
    ```bash
    mvn clean install
    ```
3.  **Executar a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
4.  **Acessar a documentação (Swagger/OpenAPI):**
    `http://localhost:8080/swagger-ui.html`

---

## 📌 Endpoints da API

### Gerenciamento de Pessoas (`/person`)
* `GET /person`: Lista todas as pessoas ordenadas de A-Z.
* `POST /person`: Cria um novo registro. O ID é gerado automaticamente pelo banco.
* `GET /person/{id}`: Busca detalhada. Retorna **404 Not Found** se inexistente.
* `PUT /person`: Atualização total de atributos (Nome, Datas).
* `PATCH /person/{id}`: Atualização **parcial** de atributos (Nome, Datas).
* `DELETE /person/{id}`: Remove o registro. Retorna **204 No Content** em caso de sucesso ou **404** se não encontrado.

### Cálculos e Regras de Negócio
* **Idade:** `GET /person/{id}/age?output={days|months|years}`
    * Calcula a diferença exata entre a data de nascimento e a data atual.
* **Salário:** `GET /person/{id}/salary?output={min|full}`
    * **Lógica de Progressão:** Salário base de R$ 1.558,00. A cada ano completo de casa, aplica-se: `(Salário Atual * 1.18) + 500.00`.
    * **Output full:** Salário acumulado em Reais (BRL) com arredondamento **CEILING**.
    * **Output min:** Valor convertido em quantidade de salários mínimos (Ref: R$ 1.302,00).

---

## 📊 Monitoramento e Observabilidade (Spring Actuator)

A aplicação utiliza o **Spring Boot Actuator** para fornecer insights em tempo real sobre a integridade do sistema.

### Endpoints Disponíveis:
* **Health Check**: `GET /actuator/health`
    * Indica se a aplicação e o banco de dados H2 estão operacionais.
* **Métricas**: `GET /actuator/metrics`
    * Exibe contadores de requisições, uso de memória JVM e CPU.
* **Logs**: `GET /actuator/loggers`
    * Permite visualizar e alterar o nível de log em tempo real sem reiniciar a aplicação.

> No ambiente Docker, o endpoint `/actuator/health` é utilizado para garantir que o container só receba tráfego quando estiver totalmente pronto.

---

## 🧪 Testes Unitários

O projeto utiliza **JUnit 5**, **Mockito** e **MockMvc** para garantir a confiabilidade em diferentes níveis:

### Testes Unitários
* **Service Test**: Validação da lógica de Streams para salário e cálculos de idade.
* **Mapper Test**: Integridade na conversão entre DTOs e Entidades.
* **Patch Test**: Garante que o método `PATCH` atualize apenas os campos enviados.

### Testes de Integração (End-to-End)
* **E2E Lifecycle**: Simula requisições HTTP reais usando `MockMvc` para validar o fluxo completo: Criar uma pessoa -> Buscar por ID -> Atualizar via PATCH -> Validar cálculo de salário.
* **Error Handling**: Valida se a API retorna os HTTP Status corretos (404, 409, 400) em cenários de erro.

Para rodar os testes:
```bash
mvn test
```
---
## 📊 Banco de Dados H2

O banco de dados é carregado em memória com dados iniciais via `import.sql`. O console administrativo pode ser acessado durante a execução da aplicação:

* **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
* **JDBC URL:** `jdbc:h2:mem:testdb`
* **User:** `sa`
* **Password:** `(vazio)`

> **Nota:** Certifique-se de que a JDBC URL no console do navegador seja exatamente a mesma definida acima para visualizar as tabelas e os dados populados.

---

## 🐳 Rodando com Docker

Se você tiver o Docker instalado, pode subir a aplicação completa sem precisar configurar o Maven ou o Java localmente:

1.  **Construir e rodar os containers:**
    ```bash
    docker-compose up -d --build
    ```
2.  **Acessar a API:**
    A aplicação estará disponível em `http://localhost:8080/swagger-ui/index.html`

3.  **Encerrar a execução:**
    ```bash
    docker-compose down
    ```

---
