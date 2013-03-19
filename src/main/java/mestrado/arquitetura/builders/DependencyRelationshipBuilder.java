package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

/**
 * 
 * @author edipofederle
 *
 */
public class DependencyRelationshipBuilder  extends RelationshipBase{

	private ClassBuilder classBuilder;
	private PackageBuilder packageBuilder;
	private Architecture architecture;
	private InterfaceBuilder interfaceBuilder;

	public DependencyRelationshipBuilder(ClassBuilder classBuilder, Architecture architecture, PackageBuilder packageBuilder, InterfaceBuilder interfaceBuilder) {
		this.classBuilder = classBuilder;
		this.architecture = architecture;
		this.packageBuilder = packageBuilder;
		this.interfaceBuilder = interfaceBuilder;
	}

	public DependencyRelationship create(Dependency element) {

		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();
		
		Element client;
		Element supplier;

		client = classBuilder.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
		supplier = classBuilder.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));

		if ((client == null) && (supplier != null)){
			client = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));;
		}else if ((supplier == null) && (client != null)){
			supplier = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));;
		}else if((supplier == null) && (client == null)){
			client = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(clieents.get(0)));
			supplier = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
			if(supplier == null)
				supplier = interfaceBuilder.getElementByXMIID(getModelHelper().getXmiId(suppliers.get(0)));
		}
		
		return new DependencyRelationship(supplier, client, element.getName(), architecture, getModelHelper().getXmiId(element));
	}

}

