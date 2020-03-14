-- -------------------------
-- ESTADO CIVIL
-- -------------------------
CREATE TABLE ESTADO_CIVIL (
    ID BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    UUID CHAR(36) NOT NULL UNIQUE,
    NOME VARCHAR(150) NOT NULL,
    DESCRICAO TEXT,
    INATIVO BOOLEAN DEFAULT FALSE
)
    ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE=utf8_general_ci;



-- -------------------------
-- PESSOA
-- -------------------------
CREATE TABLE PESSOA (
    ID BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    UUID CHAR(36) NOT NULL UNIQUE,

    ID_CIDADE BIGINT(20), FOREIGN KEY (ID_CIDADE) REFERENCES cidade(id),
    NOME VARCHAR(250) NOT NULL,
    FANTASIA VARCHAR(250),
    TIPO VARCHAR(150) NOT NULL,
    EMAIL VARCHAR(250),
    SITE VARCHAR(250),
    IMAGEM LONGBLOB,

    TELEFONE1 VARCHAR(20),
    TELEFONE1_OBS VARCHAR(100),
    TELEFONE2 VARCHAR(20),
    TELEFONE2_OBS VARCHAR(100),

    CEP VARCHAR(9),
    ENDERECO VARCHAR(250),
    BAIRRO VARCHAR(100),

#     estudante BOOLEAN DEFAULT FALSE,
#     cliente BOOLEAN DEFAULT FALSE,
#     fornecedor BOOLEAN DEFAULT FALSE,
#     colaborador BOOLEAN DEFAULT FALSE,
#     transportadora BOOLEAN DEFAULT FALSE,

    OBJ LONGBLOB,
    INATIVO BOOLEAN DEFAULT FALSE,

    EMPRESA_REPRESENTANTE_COMERCIAL BOOLEAN DEFAULT FALSE,
    EMPRESA_PRINCIPAL_OU_FILIAL BOOLEAN DEFAULT FALSE

#     data_criacao  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
#     data_alteracao  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
    ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE=utf8_general_ci;



-- -------------------------
-- PESSOA FISICA
-- -------------------------
CREATE TABLE PESSOA_FISICA (
    ID BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    UUID CHAR(36) NOT NULL UNIQUE,

    CPF VARCHAR(15) NOT NULL,
    RG VARCHAR(15),

    ORGAO_RG VARCHAR(10),
    DATA_EMISSAO_RG DATE,
    DATA_NASCIMENTO DATE,
    SEXO VARCHAR(1),
    NATURALIDADE VARCHAR(250),
    NACIONALIDADE VARCHAR(250),
    TIPO_SANGUE VARCHAR(5),

    CNH_NUMERO VARCHAR(30),
    CNH_CATEGORIA VARCHAR(2),
    CNH_VENCIMENTO DATE NULL,
    CNH_PRIMEIRA_HABILITACAO DATE NULL,
    CNH_EMISSAO_DATA DATE NULL,
    ID_CNH_EMISSAO_CIDADE BIGINT(20), FOREIGN KEY (ID_CNH_EMISSAO_CIDADE) REFERENCES cidade(ID),

    MOTORISTA_INATIVO BOOLEAN DEFAULT TRUE,

    TITULO_ELEITORAL_NUMERO VARCHAR(30),
    TITULO_ELEITORAL_ZONA VARCHAR(3),
    TITULO_ELEITORAL_SECAO VARCHAR(10),

    RESERVISTA_NUMERO VARCHAR(30),
    RESERVISTA_CATEGORIA VARCHAR(50),

    NOME_MAE VARCHAR(250),
    NOME_PAI VARCHAR(250),

#     DATA_CRIACAO  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
#     DATA_ALTERACAO  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    ID_ESTADO_CIVIL BIGINT(20), FOREIGN KEY (ID_ESTADO_CIVIL) REFERENCES estado_civil(ID),
    ID_PESSOA BIGINT(20), FOREIGN KEY (ID_PESSOA) REFERENCES pessoa(id)
)
    ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE=utf8_general_ci;


-- -------------------------
-- PESSOA_JURIDICA
-- -------------------------
CREATE TABLE PESSOA_JURIDICA (
    ID BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    UUID CHAR(36) NOT NULL UNIQUE,

    CNPJ VARCHAR(18) NOT NULL,

    INSCRICAO_MUNICIPAL VARCHAR(50),
    INSCRICAO_ESTADUAL VARCHAR(50),
    DATA_CONSTITUICAO DATE,

    TIPO_REGIME VARCHAR(20),

    -- CÓDIGO DE REGIME TRIBUTÁRIO: SIMPLES NACIONAL ....
    CRT VARCHAR(50),
    SUFRAMA VARCHAR(50),

    DATA_CRIACAO  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DATA_ALTERACAO  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    ID_PESSOA BIGINT(20), FOREIGN KEY (ID_PESSOA) REFERENCES pessoa(ID)
)
    ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE=utf8_general_ci;



-- -------------------------
-- DADOS
-- -------------------------
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Solteiro(a)','Quem nunca se casou, ou que teve o casamento anulado');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Casado(a)','Quem contraiu matrimônio, independente do regime de bens adotado');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Divorciado(a)','Após a homologação do divórcio pela justiça ou por uma escritura pública.');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Viúvo(a)','Pessoa cujo cônjuge faleceu');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Separado Judicialmente','Quando a vida conjugal tenha entrado em ruptura, a lei permite que, por decisão conjunta ou individual dos cônjuges, se ponha termo à vida em comum');
