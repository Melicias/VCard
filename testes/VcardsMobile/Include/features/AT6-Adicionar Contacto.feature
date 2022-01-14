Feature: Adicionar Contacto

Como utilizador
quero poder adicionar um contacto estando na lista de contactos

quero que apareça um caixa para o numero e outra para o nome

quero que seja adicionado a minha lista de contactos

e quero que seja possível voltar para a pagina anterior

 

Scenario: Adicionar contacto com sucesso

Given estou na lista de contactos

And clico no botão adicionar Contacto

And preencho o "Phone number" com "900000001"

And preencho o "Name" com "Teste"

And clico no botão "CREATE"

Then verifico a presenca de "900000001"



Scenario: Adicionar contacto sem preencher os campos
    Given estou na lista de contactos
    And clico no botão adicionar Contacto
    And clico no botão "CREATE"
    Then verifico mensagem a "The phone number must have 9 digits"