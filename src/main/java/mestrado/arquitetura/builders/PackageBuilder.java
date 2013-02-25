package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Package;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Builder resposável por criar element do tipo Pacote.
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
	public Package buildElement(NamedElement modelElement) {
		Package pkg = new Package(architecture, name, isVariationPoint, variantType);
		pkg.getClasses().addAll(getClasses(modelElement));
		return pkg;
	}
	
	/**
	 * Reotorna todas as classes de um dado pacote.
	 * @param modelElement
	 * @return List
	 */
	private List<Class> getClasses(NamedElement modelElement) {
		List<Class> listOfClasses = new ArrayList<Class>();
		List<Classifier> classes = modelHelper.getAllClasses(((org.eclipse.uml2.uml.Package) modelElement));
		for (NamedElement element : classes) {
			listOfClasses.add(classBuilder.create(element));
		}
		return listOfClasses;
	}
}