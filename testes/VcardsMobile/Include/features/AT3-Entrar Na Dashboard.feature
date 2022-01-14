Feature: Entrar na Dashboard
Como utilizador
Eu quando abrir a Dashboard ,
quero ver o Saldo que tenho,
quero poder ver o dinheiro que tenho no meu mealheiro
quero ver a minha ultima transação caso exista,
tambem quero ter um botão para  Enviar dinheiro,
quero ter um botão de historico de transações,
quero ter um botão para aceder ao meu mealheiro
e quero ter um botão das notificações.

Background:
Given Tendo a dashboard aberta

Scenario: Entrar na Dashboard Com ultima Transação 
Then vejo "Last Transaction"


#tem de entrar em contas diferente para conseguir realizar este teste Nr:900000001 Pass:teste123
#tem transacao e o Nr:938652265 Pass:12345678 nao tem Transacao
Scenario: Entrar na Dashboard sem ultima Transação
Then não vejo "Last Transaction"