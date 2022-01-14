Feature:

Como utilizador

pretendo ordenar transações

por valor transferência ascendente ou descendente

 Background:
Given Tendo a dashboard aberta
And clico no botão "HISTORY"

Scenario: Ordenar do menos recente para o mais recente
And clico na imagem "OrderByData"
Then vejo "transacões"

Scenario: Ordenar do mais recente para o menos recente
And clico na imagem "OrderByData"
And clico na imagem "OrderByData"
Then vejo "transacões"


Scenario: Ordenar do menos valioso para o mais valioso
And clico na imagem "OrderByValue"
Then vejo "transacões"

Scenario: Ordenar do mais valioso para o menos valioso
And clico na imagem "OrderByValue"
And clico na imagem "OrderByValue"
Then vejo "transacões"