Feature:

Como utilizador

pretendo que Dashboard atualize sempre que

fizer alteração ao saldo automaticamente


Background:   Given Tendo a dashboard aberta

Scenario: Atualização na transaction
  And tenho o saldo a "2550.00€"
  And faco a transaction
  Then tenho o saldo a "2540.00€"
  
  
  Scenario: Atualização no PiggyBank
  	And tenho o saldo a "2540.00€"
  	And coloco 10 no piggybank
    Then tenho o saldo a "2530.00€"
  
  
