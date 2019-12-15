CREATE TABLE ESTADO_CIVIL (
    ID BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    UUID CHAR(36) NOT NULL UNIQUE,
    NOME VARCHAR(150) NOT NULL,
    DESCRICAO TEXT,
    INATIVO BOOLEAN DEFAULT FALSE
)
    ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- -------------------------
-- DADOS
-- -------------------------
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Solteiro(a)','Quem nunca se casou, ou que teve o casamento anulado');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Casado(a)','Quem contraiu matrimônio, independente do regime de bens adotado');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Divorciado(a)','Após a homologação do divórcio pela justiça ou por uma escritura pública.');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Viúvo(a)','Pessoa cujo cônjuge faleceu');
INSERT INTO ESTADO_CIVIL (UUID, NOME, DESCRICAO) values(UUID(), 'Separado Judicialmente','Quando a vida conjugal tenha entrado em ruptura, a lei permite que, por decisão conjunta ou individual dos cônjuges, se ponha termo à vida em comum');
