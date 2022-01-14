Feature: Filtro das transaçoes
Como utilizador
quero abrir a dashboard
quero clicar no botão "history"
quero poder ordenar as transactions e filtrar por "credito" ou "debito"


Background:
Given Tendo a dashboard aberta
And clico no botão "HISTORY"

Scenario: Ordenar do menos recente para o mais recente
And clico na imagem "Filtrar"
Then vejo as transactions ordenadas do menos recente para o mais recente.

Scenario: Ordenar do mais recente para o menos recente
And clico na imagem "Filtrar"
And clico na imagem "Filtrar"
Then vejo as transactions ordenadas do mais recente para o menos recente.

Scenario: Filtrar por "credito"
And clico na CheckBox "Credit"
Then vejo as transactions apenas de "credito"

Scenario: Filtrar por "Debito"
And clico na CheckBox "Debit"
Then vejo as transactions apenas de "Debito"
