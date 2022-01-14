Feature: PiggyBack Manager
Como utilizador
quero abrir a dashboard
quero clicar no botão Piggybank
quero ver o balance do piggybank
quero ter uma caixa onde coloco um valor
quero ter 3 botoes um pra adicionar o valor ao piggy bank
quero ter outro pra remover o valor ao piggy bank
e outro para voltar a DashBoard


Background:
Given Tendo a dashboard aberta
And clico no botão "PIGGY BANK"

Scenario: Consigo abrir a pagina do Piggybank
Then verifico a presenca de "Balance (PiggyBank)"

Scenario: Consigo adicionar valor ao piggybank com sucesso
And preencho o "To send (PiggyBank)" com "1"
And clico no botão "ADD"
Then verifico mensagem a "Money added to piggy bank"

Scenario: Tento adicionar um valor maior que o balance
And preencho o "To send (PiggyBank)" com "999"
And clico no botão "ADD"
Then verifico mensagem a "Your balance should be above the amount written"

Scenario: Tentando adicionar um valor igual a 0
And preencho o "To send (PiggyBank)" com "0"
And clico no botão "ADD"
Then verifico mensagem a "The ammount should be higher than 0"

Scenario: Tentando remover um valor igual a 0
And preencho o "To send (PiggyBank)" com "0"
And clico no botão "REMOVE"
Then verifico mensagem a "The ammount should be higher than 0"

Scenario: Remover um valor com sucesso do piggybank
And preencho o "To send (PiggyBank)" com "1"
And clico no botão "REMOVE"
Then verifico mensagem a "Money added to Balance"

Scenario: Remover um valor com insucesso do piggyBank
And preencho o "To send (PiggyBank)" com "999"
And clico no botão "REMOVE"
Then verifico mensagem a "Your piggy bank balance should be above the amount"
    