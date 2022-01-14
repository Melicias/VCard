Feature: Criar vCard
  Como utilizador
  quero abrir a aplicação pela primeira vez
  quero que a aplicação vá buscar o número de telefone
  quero introduzir a password e código de validação
  e conseguir finalizar a criação de um vCard
  com o objetivo de poder aceder à aplicação.

Background:
  Given tendo a aplicacao aberta

  Scenario: Criar vCard com sucesso
    And preencho o "Nmero telemovel" com "912345679"
    And preencho o "Password" com "12345678"
    And preencho o "Code" com "1234"
    And clico no botão "REGISTER"
    Then entro na Dashboard da aplicação e vejo o balance

    
      Scenario: Criar vCard sem preencher campos
    When clico no botão "REGISTER"
    Then verifico mensagem a "The code has to have 4 digits"
    
     Scenario: Criar vCard com numero ja existente
    And preencho o "Nmero telemovel" com "900000001"
    And preencho o "Password" com "12345678"
    And preencho o "Code" com "1234"
    And clico no botão "REGISTER"
    Then verifico mensagem a "The phone number has already been taken"