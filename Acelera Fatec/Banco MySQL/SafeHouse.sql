CREATE DATABASE safehouse;
USE safehouse;

CREATE TABLE usuario
(
    ID INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(40) NOT NULL,
    caminho_foto VARCHAR(500) NOT NULL,
    tipo VARCHAR(5) NOT NULL,
    email VARCHAR(40) NOT NULL,
    data_cadastro VARCHAR(10) NOT NULL,
    hora_cadastro VARCHAR(10) NOT NULL,
    alarmeNomeCasa VARCHAR(30)
);

CREATE TABLE alarme 
(
  senha CHAR(6) NOT NULL,
  estado VARCHAR(10) NOT NULL
);

CREATE TABLE janela 
(
  estado VARCHAR(7) NOT NULL,
  horaAcao VARCHAR(10) NOT NULL,
  dataAcao CHAR(10) NOT NULL,
  hora CHAR(10) NOT NULL
);

CREATE TABLE portao 
(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  estado VARCHAR(10) NOT NULL,
  horaAcao VARCHAR(10) NOT NULL,
  dataAcao CHAR(10) NOT NULL,
  diaSemana CHAR(10) NOT NULL
);

CREATE TABLE registroAlarme 
(
  estado VARCHAR(10) NOT NULL,
  horaAcao VARCHAR(10) NOT NULL,
  dataAcao CHAR(10) NOT NULL,
  diaSemana CHAR(10) NOT NULL
);