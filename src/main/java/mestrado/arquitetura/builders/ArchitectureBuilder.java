package mestrado.arquitetura.builders;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.representation.Architecture;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

public class ArchitectureBuilder {
	
	private ModelHelper modelHelper;
	private Package model;
	private PackageBuilder packageBuilder;
	
	public ArchitectureBuilder(){
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}
	
	public Architecture create(String xmiFilePath) throws Exception {
		model = modelHelper.getModel(xmiFilePath);
		Architecture architecture = new Architecture(modelHelper.getName(xmiFilePath));
		
		initialize(architecture);
		
		architecture.getPackages().addAll(loadPackages());
		
		return architecture;
	}

	private Collection<mestrado.arquitetura.representation.Package> loadPackages() {
		Set<mestrado.arquitetura.representation.Package> packages = new HashSet<mestrado.arquitetura.representation.Package>();
		List<Classifier> packagess = modelHelper.getAllPackages(model);
		for (NamedElement pkg : packagess)
			packages.add(packageBuilder.create(pkg));
		
		return packages;
		
	}

	private void initialize(Architecture architecture) {
		ClassBuilder classBuilder = new ClassBuilder(architecture);
		packageBuilder = new PackageBuilder(architecture, classBuilder);
	}
	
}