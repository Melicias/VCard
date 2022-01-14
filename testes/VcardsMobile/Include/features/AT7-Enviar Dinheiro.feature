Feature: Enviar Dinheiro
  Como utilizador
  quero apos escolher o contacto
  quero que me apareça o contacto no topo
  quero uma caixa para colocar o valor
  quero um botão para enviar dinheiro
  quero um botão para voltar para trás
  e também quero apos enviar dinheiro  estar de volta na Dashboard
  
   

  Scenario: Enviar Dinheiro Com sucesso
    Given estou no ecra de enviar dinheiro
    And preencho o "To send" com "1"
    And preencho o "Pin Code" com "1234"
    And clico no botão "SEND"
    Then verifico mensagem a "Last transaction was a success"
    
      Scenario: Não preencho nada
    Given estou no ecra de enviar dinheiro
    And clico no botão "SEND"
    Then verifico mensagem a "The value Field is required"
    
    
    #Testar com conta a 0.0
      Scenario: Enviar sem Saldo
    Given estou no ecra de enviar dinheiro
    And preencho o "To send" com "1"
    And preencho o "Pin Code" com "1234"
    And clico no botão "SEND"
    Then verifico mensagem a "The value must be less than or equal to 0.00"
