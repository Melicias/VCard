Feature: Apagar Vcard
  Como utilizador
  quero abrir a dashboard
  quero clicar no botão "Delete Vcard"
  quero poder apagar o Vcard.

  Background: 
    Given tendo a aplicacao aberta

  Scenario: Consigo apagar o Vcard
    When clico no texto "Login"
    And preencho o "Nmero telemovel (login)" com "999999999"
    And preencho o "Password (login)" com "teste123"
    And clico no botão "START"
    And clico no botão "DELETE VCARD"
    And preencho o "Delete Password confirmation" com "teste123"
    And clico no botão "DELETE"
    Then vejo "Button - REGISTER"
    
     Scenario: Não introduzo a password de confirmação!
    When clico no texto "Login"
    And preencho o "Nmero telemovel (login)" com "900000095"
    And preencho o "Password (login)" com "teste123"
    And clico no botão "START"
    And clico no botão "DELETE VCARD"
    And preencho o "Delete Password confirmation" com ""
    And clico no botão "DELETE"
    Then verifico mensagem a "The passowrd is not correct (Delete Vcard)"

  Scenario: Introduzo a password de confirmação errada!
    When clico no texto "Login"
    And preencho o "Nmero telemovel (login)" com "900000095"
    And preencho o "Password (login)" com "teste123"
    And clico no botão "START"
    And clico no botão "DELETE VCARD"
    And preencho o "Delete Password confirmation" com "PassErrada"
    And clico no botão "DELETE"
    Then verifico mensagem a "The password or confirmation_code are not correct"

  Scenario: Tenho saldo no Vard (Não consigo apagar o Vcard)
    When clico no texto "Login"
    And preencho o "Nmero telemovel (login)" com "900000095"
    And preencho o "Password (login)" com "teste123"
    And clico no botão "START"
    And clico no botão "DELETE VCARD"
    And preencho o "Delete Password confirmation" com "teste123"
    And clico no botão "DELETE"
    #And o "Balance Vcard" é maior que 0
    Then verifico mensagem a "The VCard Balance must be 0.00 to be deleted"

  Scenario: Tenho saldo no PiggyBank(Não consigo apagar o Vcard)
    When clico no texto "Login"
    And preencho o "Nmero telemovel (login)" com "900000096"
    And preencho o "Password (login)" com "teste123"
    And clico no botão "START"
    And clico no botão "DELETE VCARD"
    And preencho o "Delete Password confirmation" com "teste123"
    And clico no botão "DELETE"
    #And o "Balance PiggyBank" é maior que 0
    Then verifico mensagem a "The PiggyBank Balance must be 0.00 to be deleted"
