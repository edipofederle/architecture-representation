package mestrado.arquitetura.builders;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Architecture;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

/**
 * Builder resposável por criar a arquitetura.
 * 
 * @author edipofederle
 *
 */
public class ArchitectureBuilder {
	
	private ModelHelper modelHelper;
	private Package model;
	private PackageBuilder packageBuilder;
	
	/**
	 *  Construtor. Initializa helpers.
	 */
	public ArchitectureBuilder(){
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cria a arquitetura.
	 * 
	 * @param xmiFilePath - arquivo da arquitetura (.uml)
	 * @return {@link Architecture}
	 * @throws Exception
	 */
	public Architecture create(String xmiFilePath) throws Exception {
		model = modelHelper.getModel(xmiFilePath);
		Architecture architecture = new Architecture(modelHelper.getName(xmiFilePath));
		
		initialize(architecture);
		
		architecture.getPackages().addAll(loadPackages());
		
		return architecture;
	}

	/**
	 * Retornar todos os pacotes
	 * @return {@link Collection<mestrado.arquitetura.representation.Package>}
	 */
	private Collection<mestrado.arquitetura.representation.Package> loadPackages() {
		Set<mestrado.arquitetura.representation.Package> packages = new HashSet<mestrado.arquitetura.representation.Package>();
		List<Classifier> packagess = modelHelper.getAllPackages(model);
		for (NamedElement pkg : packagess)
			packages.add(packageBuilder.create(pkg));
		
		return packages;
		
	}

	/**
	 * Inicializa os elementos da arquitetura. Instanciando as classes builders
	 * juntamente com susas depedências.
	 * @param architecture
	 */
	private void initialize(Architecture architecture) {
		ClassBuilder classBuilder = new ClassBuilder(architecture);
		packageBuilder = new PackageBuilder(architecture, classBuilder);
	}
	
}