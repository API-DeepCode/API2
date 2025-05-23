create database bdDeepCode;

use bdDeepCode;

create table historico (
id int primary key auto_increment,
titulo varchar(255),
pergunta TEXT,
respostaIA TEXT,
dataPergunta DATE);