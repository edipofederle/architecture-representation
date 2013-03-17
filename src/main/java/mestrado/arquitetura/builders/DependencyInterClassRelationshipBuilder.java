package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.DependencyInterClassRelationship;
import mestrado.arquitetura.representation.Element;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

/**
 * 
 * @author edipofederle
 *
 */
public class DependencyInterClassRelationshipBuilder {

	private ClassBuilder classBuilder;
	private PackageBuilder packageBuilder;
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

	public DependencyInterClassRelationshipBuilder(ClassBuilder classBuilder, Architecture architecture, PackageBuilder packageBuilder) {
		this.classBuilder = classBuilder;
		this.architecture = architecture;
		this.packageBuilder = packageBuilder;
	}

	public DependencyInterClassRelationship create(Dependency element) {

		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();
		
		Element client;
		Element supplier;

		client = classBuilder.getElementByXMIID(modelHelper.getXmiId(clieents.get(0)));
		supplier = classBuilder.getElementByXMIID(modelHelper.getXmiId(suppliers.get(0)));

		if ((client == null) && (supplier != null)){
			client = packageBuilder.getElementByXMIID(modelHelper.getXmiId(clieents.get(0)));;
		}else if ((supplier == null) && (client != null)){
			supplier = packageBuilder.getElementByXMIID(modelHelper.getXmiId(suppliers.get(0)));;
		}
		
		return new DependencyInterClassRelationship(supplier, client, element.getName(), architecture);
	}

}

