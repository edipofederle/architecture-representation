package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Package;

import org.eclipse.uml2.uml.NamedElement;

/**
 * Builder responsável por criar element do tipo Pacote.
 * 
 * @author edipofederle
 *
 */
public class PackageBuilder extends ElementBuilder<Package> {

	private ClassBuilder classBuilder;
	private static ModelHelper modelHelper;

	static{
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}

	public PackageBuilder(Architecture architecture,  ClassBuilder classBuilder) {
		super(architecture);
		this.classBuilder = classBuilder;
	}

	@Override
	public Package buildElement(NamedElement modelElement, Element parent) {
		Package pkg = new Package(architecture, name, isVariationPoint, variantType, parent);
		pkg.getElements().addAll(getNestedPackages(modelElement, null)); //TODO ver sobre pai
		getNestedPackages(modelElement, null);
		pkg.getElements().addAll(getClasses(modelElement, pkg));
		
		return pkg;
	}
	
	private List<? extends Element> getNestedPackages(NamedElement modelElement, Object object) {
		List<Package> listOfPackes = new ArrayList<Package>();
		List<org.eclipse.uml2.uml.Package> paks = modelHelper.getAllPackages(modelElement);
		
		for (NamedElement element : paks)
			listOfPackes.add(this.create(element, null));
		
		return listOfPackes;
	}

	/**
	 * Retorna todas as classes de um dado pacote.
	 * @param modelElement
	 * @return List
	 */
	private List<Class> getClasses(NamedElement modelElement, Package pkg) {
		List<Class> listOfClasses = new ArrayList<Class>();
		List<org.eclipse.uml2.uml.Class> classes = modelHelper.getAllClasses(((org.eclipse.uml2.uml.Package) modelElement));

		for (NamedElement element : classes)
			listOfClasses.add(classBuilder.create(element, pkg));

		return listOfClasses;
	}
}