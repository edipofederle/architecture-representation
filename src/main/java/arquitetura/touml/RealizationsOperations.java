package arquitetura.touml;

import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.representation.Architecture;

public class RealizationsOperations implements Relationship {
	
private static final String REALIZATION = "realization";

private DocumentManager documentManager;
	
	private String clientElement;
	private String supplierElement;
	private String name;
	private Architecture a;
	
	public RealizationsOperations(DocumentManager doc) {
		this.documentManager = doc;
	}

	public RealizationsOperations(DocumentManager documentManager2, String name2) {
		this.documentManager = documentManager2;
		this.name = name2;
	}

	public Relationship createRelation(String name) {
		return new DependencyOperations(this.documentManager, name);
	}

	public Relationship between(String idElement) {
		this.clientElement = idElement;
		return this;
	}

	public Relationship and(String idElement) {
		this.supplierElement = idElement;
		return this;
	}

	public String build() {
		final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement, this.supplierElement,a);
		
		arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				dependencyNode.createDependency(REALIZATION);
			}
		});
		
		return ""; //TODO return id;

	}

	public Relationship withMultiplicy(String string) throws NotSuppportedOperation {
		throw new NotSuppportedOperation("Realization dont have multiplicy");
	}


}
