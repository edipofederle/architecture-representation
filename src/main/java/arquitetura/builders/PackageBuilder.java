package arquitetura.builders;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.uml2.uml.NamedElement;

import arquitetura.helpers.ModelHelper;
import arquitetura.helpers.ModelHelperFactory;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Package;

/**
 * Builder responsável por criar element do tipo Pacote.
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class PackageBuilder extends ElementBuilder<Package> {

	private ClassBuilder classBuilder;
	private static ModelHelper modelHelper;

	static{
		modelHelper = ModelHelperFactory.getModelHelper();
	}

	public PackageBuilder(Architecture architecture,  ClassBuilder classBuilder) {
		super(architecture);
		this.classBuilder = classBuilder;
	}

	@Override
	public Package buildElement(NamedElement modelElement) {
		Package pkg = new Package(architecture, name, variantType, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement));
		pkg.setElements(getNestedPackages(modelElement));
		getNestedPackages(modelElement);
		pkg.setElements(getClasses(modelElement, pkg));
		
		return pkg;
	}
	
	private List<? extends Element> getNestedPackages(NamedElement modelElement) {
		List<Package> listOfPackes = new ArrayList<Package>();
		List<org.eclipse.uml2.uml.Package> paks = modelHelper.getAllPackages(modelElement);
		
		for (NamedElement element : paks)
			listOfPackes.add(this.create(element));
		
		return listOfPackes;
	}

	/**
	 * Retorna todas as classes de um dado pacote.
	 * @param modelElement
	 * @return List
	 */
	private List<Element> getClasses(NamedElement modelElement, Package pkg) {
		List<Element> listOfClasses = new ArrayList<Element>();
		List<org.eclipse.uml2.uml.Class> classes = modelHelper.getAllClasses(((org.eclipse.uml2.uml.Package) modelElement));

		for (NamedElement element : classes){
			Class klass = classBuilder.create(element);
			pkg.getAllClassIdsForThisPackage().add(klass.getId());
			listOfClasses.add(klass);
		}

		return listOfClasses;
	}
}