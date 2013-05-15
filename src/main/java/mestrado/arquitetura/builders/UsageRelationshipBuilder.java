package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.ArchitectureHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;

public class UsageRelationshipBuilder extends ArchitectureHelper {
	
	private Architecture architecture;

	public UsageRelationshipBuilder(Architecture architecture){
		this.architecture = architecture;
	}

	public UsageRelationship create(Usage element) {
		
		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();
		
		EList<org.eclipse.uml2.uml.Element> relatedElements = element.getRelatedElements();
		
		for (org.eclipse.uml2.uml.Element el : relatedElements) {
			mestrado.arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(el));
			e.getIdsRelationships().add((getModelHelper().getXmiId(element)));
		}
		
		Element client = architecture.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
		Element supplier = architecture.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
		architecture.getAllIds().add(getModelHelper().getXmiId(element));
		return new UsageRelationship(element.getName(), supplier, client, getModelHelper().getXmiId(element));
	}

}
