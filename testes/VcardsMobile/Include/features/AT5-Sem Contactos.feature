Feature: Mostrar Contactos
  Como utilizador
  quero abrir a dashboard
  quero clicar no botão Enviar Dinheiro
  quero ver todos os meus contactos
  e quero ver um botão para adicionar contacto
  
  Scenario: Não ha contactos
  Given Tendo a dashboard aberta
    When clico no botão "SEND MONEY"
    Then vejo o botão para adicionar contactos
    Then não vejo "contactos"
