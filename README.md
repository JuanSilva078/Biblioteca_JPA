# Sistema de Biblioteca — Java JPA/DAO
**Atividade 1ª Avaliação (3 pts) — Mapeamento Objeto-Relacional**

---

## 👥 Membros
| Nome | Matrícula |
|------|-----------|
| _(preencher)_ | _(preencher)_ |

---

## 📐 Diagrama de Classes (Modelo Conceitual)

```
Emprestimo                    Aluno
- dataDevolucao : Date  * ──► 1   - matriculaAluno : int
- dataEmprestimo : Date  +emprestimos   - nome : String
       │
       │ 1
       ▼ 0..*  +publicacao
    Publicacao
    - codigoPub : int
    - titulo : String
    - autor : String
    - ano : int
    - tipo : String
```

---

## 📦 Estrutura de Pacotes

```
src/
├── main/
│   ├── java/biblioteca/
│   │   ├── model/
│   │   │   ├── Aluno.java          → Entidade JPA da tabela aluno
│   │   │   ├── Publicacao.java     → Entidade JPA da tabela publicacao
│   │   │   └── Emprestimo.java     → Entidade JPA da tabela emprestimo
│   │   ├── dao/
│   │   │   └── EmprestimoDAO.java  → CRUD completo via JPA
│   │   └── util/
│   │       └── JPAUtil.java        → Singleton EntityManagerFactory
│   └── resources/META-INF/
│       └── persistence.xml         → Configuração JPA/Hibernate + H2
└── test/java/biblioteca/
    └── EmprestimoDAOTest.java      → Testes: incluir, alterar, deletar, consultar
schema.sql                          → DDL das tabelas (questão 1)
pom.xml                             → Dependências Maven
```

---

## 🗄️ Mapeamento SQL (Questão 1)

Três tabelas principais + uma tabela de associação N:M:

| Tabela | Descrição |
|--------|-----------|
| `aluno` | Dados do aluno (matrícula, nome) |
| `publicacao` | Dados da publicação (código, título, autor, ano, tipo) |
| `emprestimo` | Datas + FK para aluno |
| `emprestimo_publicacao` | Associação N:M entre empréstimo e publicação |

---

## 🔗 Mapeamento JPA (Questão 2)

| Relacionamento | Tipo JPA | Descrição |
|----------------|----------|-----------|
| `Emprestimo → Aluno` | `@ManyToOne` | Muitos empréstimos para 1 aluno |
| `Emprestimo → Publicacao` | `@ManyToMany` | 1 empréstimo contém N publicações |
| `Aluno → Emprestimo` | `@OneToMany` | Navegação reversa (opcional) |

---

## 📋 EmprestimoDAO (Questão 3)

Métodos implementados:

| Método | Descrição |
|--------|-----------|
| `incluir(Emprestimo)` | Persiste novo empréstimo |
| `alterar(Emprestimo)` | Atualiza empréstimo existente via `merge` |
| `deletar(Long id)` | Remove pelo id |
| `consultarPorId(Long)` | Busca por id → `Optional<Emprestimo>` |
| `consultarTodos()` | Lista todos os empréstimos |
| `consultarPorNomeAluno(String)` | Busca parcial/case-insensitive pelo nome |

---

## 🧪 Testes (Questão 4)

`EmprestimoDAOTest` cobre:
- ✅ Incluir empréstimo com publicações
- ✅ Alterar data de devolução
- ✅ Deletar empréstimo
- ✅ Consultar por Id (existente e inexistente)
- ✅ Consultar todos
- ✅ Consultar por nome do aluno (parcial, case-insensitive)

---

## ▶️ Como executar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Comandos

```bash
# Compilar
mvn compile

# Executar os testes
mvn exec:java -Dexec.mainClass="biblioteca.EmprestimoDAOTest"

# Ou simplesmente:
mvn exec:java
```

---

## 📝 Observações

- O **id** de cada entidade é definido com `@GeneratedValue(strategy = IDENTITY)` (auto-incremento).
- O banco H2 em memória é usado nos testes; para produção, basta trocar as propriedades no `persistence.xml`.
- As setas do diagrama indicam o **sentido de navegação**: `Emprestimo` possui referência ao `Aluno` e às `Publicacoes`.
