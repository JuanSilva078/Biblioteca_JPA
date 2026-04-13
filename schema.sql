-- =====================================================
-- Sistema de Biblioteca - Mapeamento SQL
-- Questão 1: Elaborar o mapeamento em código SQL
-- =====================================================

-- Tabela ALUNO
CREATE TABLE aluno (
    id              SERIAL          PRIMARY KEY,
    matricula_aluno INTEGER         NOT NULL UNIQUE,
    nome            VARCHAR(100)    NOT NULL
);

-- Tabela PUBLICACAO
CREATE TABLE publicacao (
    id          SERIAL          PRIMARY KEY,
    codigo_pub  INTEGER         NOT NULL UNIQUE,
    titulo      VARCHAR(200)    NOT NULL,
    autor       VARCHAR(100)    NOT NULL,
    ano         INTEGER         NOT NULL,
    tipo        VARCHAR(50)     NOT NULL
);

-- Tabela EMPRESTIMO
-- Relacionamento: Aluno (1) -> (*) Emprestimo   [navegação de Emprestimo para Aluno]
-- Relacionamento: Emprestimo (1) -> (0..*) Publicacao [navegação de Emprestimo para Publicacao]
CREATE TABLE emprestimo (
    id                  SERIAL          PRIMARY KEY,
    data_devolucao      DATE,
    data_emprestimo     DATE            NOT NULL,
    aluno_id            INTEGER         NOT NULL,
    CONSTRAINT fk_emprestimo_aluno
        FOREIGN KEY (aluno_id) REFERENCES aluno(id)
);

-- Tabela de associação: EMPRESTIMO <-> PUBLICACAO  (relação 1 Empréstimo -> N Publicações)
CREATE TABLE emprestimo_publicacao (
    emprestimo_id   INTEGER NOT NULL,
    publicacao_id   INTEGER NOT NULL,
    PRIMARY KEY (emprestimo_id, publicacao_id),
    CONSTRAINT fk_ep_emprestimo
        FOREIGN KEY (emprestimo_id) REFERENCES emprestimo(id),
    CONSTRAINT fk_ep_publicacao
        FOREIGN KEY (publicacao_id) REFERENCES publicacao(id)
);

-- =====================================================
-- Índices para melhorar performance de consultas
-- =====================================================
CREATE INDEX idx_emprestimo_aluno_id  ON emprestimo(aluno_id);
CREATE INDEX idx_aluno_nome           ON aluno(nome);
CREATE INDEX idx_publicacao_titulo    ON publicacao(titulo);
