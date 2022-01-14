Feature: 
  
  Como utilizador
  Eu quero abrir a aplicação pela primeira vez,
  quero que a aplicação busque o numero de telefone 
  quero poder preencher as credenciais 
  e quero clicar no botão aceder
  para pode aceder as funcionalidades da aplicação.


Background:
 		Given tendo a aplicacao aberta
    When clico no texto "Login"

  	Scenario: Credenciais correta
    And preencho o "Nmero telemovel (login)" com "900000096"
    And preencho o "Password (login)" com "teste123"
    And clico no botão "START"
    Then entro na Dashboard da aplicação e vejo o balance
    
      Scenario: Introduzir password incorreta
    And preencho o "Nmero telemovel (login)" com "900000001"
    And preencho o "Password (login)" com "12345678"
    And clico no botão "START"
    Then verifico mensagem a "PhonePassword not correct"
    
      Scenario: Introduzir credenciais incorretas
    And preencho o "Nmero telemovel (login)" com "933333333"
    And preencho o "Password (login)" com "12345678"
    And clico no botão "START"
    Then verifico mensagem a "PhonePassword not correct"
    