

Esse pacote contém classes responsáveis pela escrita de um arquitetura representada em memória (*ver pacote: mestrado.arquitetura.representation*).

Por meio da classe **Operations** se tem acesso as funcionalidades.

Por exemplo, se desejarmos criar uma classe chamada *Foo*.

		DocumentManager doc = givenADocument("newModel");
		Operations op = new Operations(doc);

	  op.forClass().createClass("Foo").build();

O código acima irá gerar dois arquivos:

	* newModel.uml
	* newModel.notation
	* newModel.di

Esses arquivos serão exportados para o diretório configurado para *directoryToExportModels* no arquivo *application.yml*.	

O newModel irá aparecer como no diretório alvo. Para o exemplo acima o seguinte resultado é obtido:
[id]: https://dl.dropboxusercontent.com/u/6730822/aclasse.png "Resultado"
	 