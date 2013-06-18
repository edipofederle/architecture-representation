package arquitetura.builders;


import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.UsageRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
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
			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(el));
			e.getIdsRelationships().add((getModelHelper().getXmiId(element)));
		}
		
		Element client = architecture.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
		Element supplier = architecture.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
		architecture.getAllIds().add(getModelHelper().getXmiId(element));
		return new UsageRelationship(element.getName(), supplier, client, getModelHelper().getXmiId(element));
	}

}
