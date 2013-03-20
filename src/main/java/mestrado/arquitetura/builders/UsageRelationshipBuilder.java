package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;

public class UsageRelationshipBuilder extends RelationshipBase {
	
	private Architecture architecture;

	public UsageRelationshipBuilder(Architecture architecture){
		this.architecture = architecture;
	}

	public UsageRelationship create(Usage element) {
		
		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();
		
		
		Element client = architecture.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
		Element supplier = architecture.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
		
		return new UsageRelationship(element.getName(), supplier, client, getModelHelper().getXmiId(element));
	}

}
