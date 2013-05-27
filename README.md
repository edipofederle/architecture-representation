# Arquitetura - Mestrado - Édipo Luis Féderle (UFPR)

## O que é ?

Leitura e escrita de modelos criado no Papyrus.

Um modelo criado no Papyrus é formado por três arquivos, são eles:

	- <nomedomodelo>.uml
	- <nomedomodelo>.notation
	- <nomedomodelo>.di

Os três arquivos listados acima estão no formato XMI. O arquivo **.uml** contém os elementos do modelo propriamente dito, por exemplo, as classes. Já o arquivo **.notation** trata-se da representação visual do modelo, esse esta diretamente relacionado com o arquivo **.uml**. Por fim, o arquivo **.di** armazena informações de quais diagramas o modelo possui. O arquivo **.di** não é usado diretamente nesse projeto, entretanto é necessário que ele exista quando um modelo for lido por esse projeto.

### Leitura

Um modelo é lido e instanciado na memória dentro do seguinte metamodelo:

<meta_modelo_thelma>

Pode se notar outras classes como **variability**, **variationPoint**, etc. Isso porque o projeto foi desenvolvido para manipular arquiteturas LPS, usando o SMarty como modelo de variabiliade. Mas nada impede a leitura de arquiteturas que não possuiem esse tipo de característica. 

Dado um modelo desenvolvido no Papyrus chamado **meuModelo**, pode-se realizar a leitura do mesmo utilizando o seguinte código:

	Architecture	a = new ArchitectureBuilder().create(path);

Na linha acima *path* indica o caminho para o arquivo **.uml**. Isso irá ler a arquitetura e instanciar os elementos dentro do metamodelo mostrado acima.

Após isso se tem acesso a alguns método, como por exemplo, para recuperar todas classes da arquitetura lida:

	a.getAllClasses()









