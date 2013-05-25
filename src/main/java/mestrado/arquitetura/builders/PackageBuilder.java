package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

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
		Package pkg = new Package(architecture, name, isVariationPoint, variantType, modelElement.getNamespace().getQualifiedName(), getXmiId(modelElement));
		pkg.getElements().addAll(getNestedPackages(modelElement)); //TODO ver sobre pai
		getNestedPackages(modelElement);
		pkg.getElements().addAll(getClasses(modelElement, pkg));
		
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