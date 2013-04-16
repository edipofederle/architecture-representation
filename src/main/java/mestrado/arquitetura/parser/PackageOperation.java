package mestrado.arquitetura.parser;

public class PackageOperation {
	
	
	private Object documentManager;
	private Object umlModelChild;
	private Object notatioChildren;

	public PackageOperation(DocumentManager documentManager) {
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
	}
	
	public void createPacakge(final String packageName){
		
	}

}