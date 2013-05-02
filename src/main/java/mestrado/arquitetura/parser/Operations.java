package mestrado.arquitetura.parser;

/**
 * 
 * 
 * @author edipofederle
 *
 */
public class Operations {
	
	 private DocumentManager doc;
	 private ClassOperations classOperation;
	 private AssociationOperations associationOperation;
	 private PackageOperations packageOperaiton;

	 public Operations(DocumentManager doc2) {
		 this.doc = doc2;
		 createClassOperation();
		 createAssociationOperations();
		 createPackageOperations();
	 }

	private void createPackageOperations() {
		this.packageOperaiton = new PackageOperations(doc);
	}

	private void createAssociationOperations() {
		this.associationOperation = new AssociationOperations(doc);
	}

	private void createClassOperation() {
		this.classOperation = new ClassOperations(doc);
	}

	public ClassOperations forClass() { return classOperation; }
	public AssociationOperations forAssociation(){ return associationOperation; }
	public PackageOperations forPackage(){ return packageOperaiton; }


}