      Feature: Historico de Transacoes
  Como utilizador
  quero abrir a dashboard
  quero clicar no botão Historico
  quero ver todas as minhas transações
  quero que diga com quem foi feita a transação o valor e a data
  quero poder filtrar as transações
      
      Scenario: Não ha transações
      Given Tendo a dashboard aberta
    When clico no botão "history"
    Then não vejo "transacões"