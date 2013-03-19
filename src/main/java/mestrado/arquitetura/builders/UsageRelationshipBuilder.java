package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;

public class UsageRelationshipBuilder extends RelationshipBase {
	
	private ClassBuilder classBuilder;
	private PackageBuilder packageBuilder;

	public UsageRelationshipBuilder(ClassBuilder classBuilder, PackageBuilder packageBuilder){
		this.classBuilder = classBuilder;
		this.packageBuilder = packageBuilder;
	}


	public UsageRelationship create(Usage element) {
		
		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();
		
		Element client;
		Element supplier;
		
		client = classBuilder.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
		supplier = classBuilder.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
		
		if ((client == null) && (supplier != null)){
			client = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
		}else if ((supplier == null) && (client != null)){
			supplier = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
		}else if ((supplier == null) && (client == null)){
			client = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
			supplier = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
		}
		
		return new UsageRelationship(element.getName(), supplier, client, getModelHelper().getXmiId(element));
	}

}
