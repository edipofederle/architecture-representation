package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.ArchitectureHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.AbstractionRelationship;

import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Builder Responsável por criar relacionamentos entre Pacote e Classe.
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AbstractionRelationshipBuilder extends ArchitectureHelper {

	private Architecture architecture;

	public AbstractionRelationshipBuilder(Architecture architecture) {
		this.architecture =  architecture;
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

		Element client = architecture.getElementByXMIID(getModelHelper().getXmiId(clientElement));
		Element supplier = architecture.getElementByXMIID(getModelHelper().getXmiId(supplierElement));
		
		architecture.getAllIds().add(getModelHelper().getXmiId(modelElement));
		return new AbstractionRelationship(client,supplier, getModelHelper().getXmiId(modelElement));
	}

}
