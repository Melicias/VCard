Feature: Filtrar Contactos
Como utilizador
quero que apareça uma caixa de filtragem nos contactos
quero que possa ser possível filtrar por nome ou numero
e quero que seja filtrado enquanto estiver a escrever

 

Scenario: Filtrar Por Nome
Given Tendo a dashboard aberta
And clico no botão "SEND MONEY"
And preencho o "Search" com "Teste"
#then "vejo "contactos" pois ja existe um metodo groovy que verifica a presença do primeiro
#contacto que aparece na lista, no caso o contacto filtrado.
Then vejo "contactos"

Scenario: Filtrar Por Numero
    Given Tendo a dashboard aberta
	And clico no botão "SEND MONEY"
	And preencho o "Search" com "900000001"
	#then "vejo "contactos" pois ja existe um metodo groovy que verifica a presença do primeiro
	#contacto que aparece na lista, no caso o contacto filtrado.
	Then vejo "contactos"