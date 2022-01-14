
Feature:

Como utilizador 

pretendo ir á lista de contactos

e aparecer que contactos são vcard


Background:
Given estou na lista de contactos

Scenario:Contacto Com Vcard


And preencho o "Search" com "900000001"

Then vejo a imagem "Vcard"


Scenario:Contacto Sem Vcard


And preencho o "Search" com "925336073"

Then nao vejo a imagem "Vcard"