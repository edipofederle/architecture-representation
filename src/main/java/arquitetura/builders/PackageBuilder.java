package arquitetura.builders;

import arquitetura.exceptions.ConcernNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.NamedElement;

import arquitetura.helpers.ModelElementHelper;
import arquitetura.helpers.ModelHelper;
import arquitetura.helpers.ModelHelperFactory;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builder responsável por criar element do tipo Pacote.
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class PackageBuilder extends ElementBuilder<Package> {

	private ClassBuilder classBuilder;
	private InterfaceBuilder interfaceBuilder;
	private static ModelHelper modelHelper;

	static{
		modelHelper = ModelHelperFactory.getModelHelper();
	}

	public PackageBuilder(Architecture architecture,  ClassBuilder classBuilder, InterfaceBuilder interfaceBuilder) {
		super(architecture);
		this.classBuilder = classBuilder;
		this.interfaceBuilder = interfaceBuilder;
	}

	@Override
	public Package buildElement(NamedElement modelElement) {
		Package pkg = new Package(architecture.getRelationshipHolder(), name, variantType, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement));
            try {
                pkg.getNestedPackages().addAll(getNestedPackages(modelElement));
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(PackageBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                for(Class klass : getClasses(modelElement, pkg)){
                    pkg.addExternalClass(klass);
                }
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(PackageBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                for(Interface itf : getInterfaces(modelElement,pkg))
                    pkg.addExternalInterface(itf);
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(PackageBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
		
		return pkg;
	}
	
	private List<Package> getNestedPackages(NamedElement modelElement) throws ConcernNotFoundException {
		List<Package> listOfPackes = new ArrayList<Package>();
		List<org.eclipse.uml2.uml.Package> paks = modelHelper.getAllPackages(modelElement);
		
		for (NamedElement element : paks)
			listOfPackes.add(this.create(element));
		
		return listOfPackes;
	}

	private List<Class> getClasses(NamedElement modelElement, Package pkg) throws ConcernNotFoundException {
		List<Class> listOfClasses = new ArrayList<Class>();
		List<org.eclipse.uml2.uml.Class> classes = modelHelper.getAllClasses(((org.eclipse.uml2.uml.Package) modelElement));

		for (NamedElement element : classes){
			if(!ModelElementHelper.isInterface(element)){
				Class klass = classBuilder.create(element);
				listOfClasses.add(klass);
			}
		}

		return listOfClasses;
	}
	
	private List<Interface> getInterfaces(NamedElement modelElement, Package pkg) throws ConcernNotFoundException {
		List<Interface> allInterfaces = new ArrayList<Interface>();
		List<org.eclipse.uml2.uml.Class> classes = modelHelper.getAllClasses(((org.eclipse.uml2.uml.Package) modelElement));

		for (NamedElement element : classes){
			if(ModelElementHelper.isInterface(element)){
				Interface klass = interfaceBuilder.create(element);
				allInterfaces.add(klass);
			}
		}

		return allInterfaces;
	}
}