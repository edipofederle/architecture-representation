package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.AbstractionRelationship;
import mestrado.arquitetura.representation.Element;

import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Builder Responsável por criar relacionamentos entre Pacote e Classe.
 * 
 * @author edipofederle
 *
 */
public class AbstractionRelationshipBuilder extends RelationshipBase {

	private PackageBuilder packageBuilder;
	/*
	 * Aqui a relação é Pacote -> interface. Mas como não temos interfaces
	 * propriamente ditas, mas sim classes com o estereótipo <<interface>>.
	 * 
	 */
	private ClassBuilder classBuilder;


	public AbstractionRelationshipBuilder(PackageBuilder packageBuilder, ClassBuilder classBuilder) {
		this.packageBuilder = packageBuilder;
		this.classBuilder = classBuilder;
	}

	/**
	 * Cria o elemento AbstractionInterElementRelationship
	 * 
	 * @param modelElement
	 * @return
	 */
	public AbstractionRelationship create(Abstraction modelElement) {

		NamedElement clientElement = modelElement.getClients().get(0);
		NamedElement supplierElement = modelElement.getSuppliers().get(0);

		Element client;
		Element supplier;

		client = classBuilder.getElementByXMIID(getModelHelper().getXmiId(clientElement));
		supplier = classBuilder.getElementByXMIID(getModelHelper().getXmiId(supplierElement));
		
		if ((client == null) && (supplier != null)){
			client = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(clientElement));
		}else if ((supplier == null) && (client != null)){
			supplier = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(supplierElement));
		}else if((client == null) && (supplier == null)){
			supplier = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(supplierElement));
			client = packageBuilder.getElementByXMIID(getModelHelper().getXmiId(clientElement));
		}
		
		

		return new AbstractionRelationship(client,supplier );
	}

}
