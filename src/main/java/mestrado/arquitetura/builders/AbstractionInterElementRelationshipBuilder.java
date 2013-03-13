package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.AbstractionInterElementRelationship;
import mestrado.arquitetura.representation.Package;

import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Builder Responsável por criar relacionamentos entre Pacote e Classe.
 * 
 * @author edipofederle
 *
 */
public class AbstractionInterElementRelationshipBuilder {

	private PackageBuilder packageBuilder;
	/*
	 * Aqui a relação é Pacote -> interface. Mas como não temos interfaces
	 * propriamente ditas, mas sim classes com o estereótipo <<interface>>.
	 * 
	 */
	private ClassBuilder classBuilder;

	private static ModelHelper modelHelper;

	static {
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}

	public AbstractionInterElementRelationshipBuilder(PackageBuilder packageBuilder, ClassBuilder classBuilder) {
		this.packageBuilder = packageBuilder;
		this.classBuilder = classBuilder;
	}

	/**
	 * Cria o elemento AbstractionInterElementRelationship
	 * 
	 * @param modelElement
	 * @return
	 */
	public AbstractionInterElementRelationship create(Abstraction modelElement) {

		NamedElement modelParent = modelElement.getSuppliers().get(0);
		NamedElement modelChild = modelElement.getClients().get(0);

		mestrado.arquitetura.representation.Class parent = classBuilder.getElementByXMIID(modelHelper.getXmiId(modelChild));
		Package child = packageBuilder.getElementByXMIID(modelHelper.getXmiId(modelParent));
		
		return new AbstractionInterElementRelationship(parent, child);
	}

}
