Feature: Incrimento automatico do piggybank
Como utilizador
sempre que fizer uma transação com casa decimais
quero que o valor seje arredondado e que o restante seje aguardado no piggybank.

Background:
Given estou no ecra de enviar dinheiro

Scenario: Incrimento do piggybank com sucesso
And preencho o "To send" com "9.5"
And preencho o "Pin Code" com "1234"
And clico no botão "SEND"
Then verifico a presenca de "Added to PG-0.5 Money sent"

Scenario: Incrimento do piggybank sem sucesso
And preencho o "To send" com "90"
And preencho o "Pin Code" com "1234"
And clico no botão "SEND"
Then verifico a presenca de "Money sent"