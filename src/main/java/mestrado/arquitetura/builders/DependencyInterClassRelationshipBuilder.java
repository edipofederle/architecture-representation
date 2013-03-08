package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.DependencyInterClassRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

public class DependencyInterClassRelationshipBuilder {

	private ClassBuilder classBuilder;
	private static ModelHelper modelHelper;
	private Architecture architecture;

	static {
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}

	public DependencyInterClassRelationshipBuilder(ClassBuilder classBuilder,
			Architecture architecture) {
		this.classBuilder = classBuilder;
		this.architecture = architecture;
	}

	public DependencyInterClassRelationship create(Dependency element) {

		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();

		Class c = classBuilder.getElementByXMIID(modelHelper.getXmiId(clieents.get(0)));
		Class s = classBuilder.getElementByXMIID(modelHelper.getXmiId(suppliers.get(0)));

		return new DependencyInterClassRelationship(s,
				c, element.getName(), architecture);
	}

}
