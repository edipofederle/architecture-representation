package arquitetura.api.touml;

import arquitetura.exceptions.NotSuppportedOperation;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class UsageOperations implements Relationship  {

	private DocumentManager documentManager;
	
	private String clientElement;
	private String supplierElement;
	private String name;
	
	public UsageOperations(DocumentManager doc) {
		this.documentManager = doc;
	}

	public UsageOperations(DocumentManager documentManager2, String name2) {
		this.documentManager = documentManager2;
		this.name = name2;
	}

	public Relationship createRelation(String name) {
		if(("".equals(name) || name == null)) name = "usage";
		return new UsageOperations(this.documentManager, name);
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
		final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement, this.supplierElement);
		
		arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				dependencyNode.createDependency("usage");
			}
		});
		
		return ""; //TODO return id;

	}

	public Relationship withMultiplicy(String string) throws NotSuppportedOperation {
		throw new NotSuppportedOperation("Usage dont have multiplicy");
	}

}