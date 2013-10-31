package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.GeneralizationRelationship;

public class OperationsOverGeneralization {
	
	private Architecture architecture;

	public OperationsOverGeneralization(Architecture architecture) {
		this.architecture = architecture;
	}

	public void moveGeneralizationParent(GeneralizationRelationship gene, Class targetClass) {
		gene.setParent(targetClass);
	}

	public void moveGeneralizationSubClass(GeneralizationRelationship gene, Class class1) {
		gene.setChild(class1);
	}

	/**
	 * 
	 * @param gene
	 * @param parent
	 * @param child
	 */
	public void moveGeneralization(GeneralizationRelationship gene, Class parent, Class child) {
		gene.setParent(parent);
		gene.setChild(child);
	}

	public void addChildToGeneralization(GeneralizationRelationship generalizationRelationship,	Element newChild) {
		GeneralizationRelationship g = new GeneralizationRelationship(generalizationRelationship.getParent(), newChild, this.architecture, UtilResources.getRandonUUID());
		this.architecture.getAllRelationships().add(g);
	}

	public void createGeneralization(Class parent, Class child) {
		GeneralizationRelationship g = new GeneralizationRelationship(parent, child, this.architecture, UtilResources.getRandonUUID());
		this.architecture.getAllRelationships().add(g);
	}

}
