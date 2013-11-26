package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.helpers.UtilResources;


/**
 * 
 * {@link Package} representa um elemento Pacote dentro da arquitetura.
 * 
 * Pacotes podem conter {@link Class}'s {@link Interface} e outros {@link Package}'s
 * 
 * 
 * @author edipofederle <edipofederle@gmail.com>
 *
 */
public class Package extends Element {

	private static final long serialVersionUID = -3080328928563871488L;
	static Logger LOGGER = LogManager.getLogger(Package.class.getName());

	private Set<Class> classes = new HashSet<Class>();
	private Set<Interface> interfaces = new HashSet<Interface>();
	
	private Set<Interface> implementedInterfaces = new HashSet<Interface>();
	private Set<Interface> requiredInterfaces = new HashSet<Interface>();
	public  Set<Package> nestedPackages = new HashSet<Package>();
	
	/**
	 * Construtor Para um Elemento do Tipo Pacote
	 * 
	 * @param architecture - A qual arquitetura pertence
	 * @param name - Nome do Pacote
	 * @param isVariationPoint - Se o mesmo é um ponto de variação
	 * @param variantType - Qual o tipo ( {@link VariantType} ) da variante
	 * @param parent - Qual o {@link Element} pai
	 */
	public Package(Architecture architecture, String name, Variant variantType,  String namespace, String id) {
		super(architecture, name,  variantType, "package", namespace, id);
	}
	
	public Package(Architecture architecture, String name) {
		this(architecture, name, null, UtilResources.createNamespace(architecture.getName(), name), UtilResources.getRandonUUID());
		architecture.addPackage(this);
	}
	
	/**
	 * Retorna todas as {@link Interface}  do pacote.
	 * 
	 * @return
	 */
	public Set<Interface> getAllInterfaces(){
		return this.interfaces;
	}
	/**
	 * Retorna todas {@link Class} que pertencem ao pacote. 
	 * 
	 * @return List<{@link Class}>
	 */
	public Set<Class> getAllClasses(){
		return this.classes;
	}
	
	/**
	 * Retorna todos {@link Package} dentro do pacote em questão.
	 * 
	 * @return List<{@link Package}>
	 */
	public Set<Package> getNestedPackages(){
		return this.nestedPackages;
	}

	public void addImplementedInterface(Element interfacee) {
		implementedInterfaces.add((Interface) interfacee);
	}
	
	/**
	 * Retorna as interfaces implementadas pelo pacote e as interfaces
	 * implementadas pelas classes que estão dentro do pacote.
	 * 
	 * @return {@link Set} imutável
	 */
	public Set<Interface> getImplementedInterfaces() {
		Set<Interface> implementedInterfecesForClassIntoPackage = new HashSet<Interface>();
		for(Class klass : this.getAllClasses())
			implementedInterfecesForClassIntoPackage.addAll(klass.getImplementedInterfaces());
		
		implementedInterfaces.addAll(implementedInterfecesForClassIntoPackage);
		return Collections.unmodifiableSet(implementedInterfaces);
	}

	public void addRequiredInterface(Interface interfacee) {
		requiredInterfaces.add(interfacee);
	}

	public Set<Interface> getRequiredInterfaces() {
		Set<Interface> requiredInterfacesForPackage = new HashSet<Interface>();
		for(Class klass : this.getAllClasses())
			requiredInterfacesForPackage.addAll(klass.getRequiredInterfaces());
		
		requiredInterfaces.addAll(requiredInterfacesForPackage);
		return Collections.unmodifiableSet(requiredInterfaces);
	}

	/**
	 * Cria uma classe e adiciona no pacote em questão.
	 * 
	 * @param className - Nome da classe
	 * @param isAbstract - abstrata ou não
	 * @return {@link Class}
	 * @throws Exception
	 */
	public Class createClass(String className, boolean isAbstract) throws Exception {
		Class c = new Class(getArchitecture(), className, isAbstract, this.getName());
		this.classes.add(c);
		return c;
	}
	
	/**
	 * Cria uma interface dentro do pacote
	 * 
	 * @param name
	 * @return
	 */
	public Interface createInterface(String name) {
		Interface inter = new Interface(getArchitecture(), name);
		this.interfaces.add(inter);
		return inter;
	}

	@Override
	public List<Concern> getAllConcerns() {
		List<Concern> concerns = new ArrayList<Concern>();
		
		for (Element klass : this.classes) 
			concerns.addAll(klass.getAllConcerns());
		for (Element inter : this.interfaces) 
			concerns.addAll(inter.getAllConcerns());
		for (Interface interfc:getImplementedInterfaces())
			concerns.addAll(interfc.getAllConcerns());
		
		return concerns;
	}

	public void setElements(List<? extends Element> elements) {
		for (Element element : elements)
			getElements().add(element);
	}

	public void moveClassToPackage(Element klass, Package packageToMove) {
		if (!classes.contains(klass)) return;
		
		removeClass(klass);
		packageToMove.addExternalClass(klass);
	}

	public void addExternalClass(Element klass) {
		classes.add((Class) klass);
	}
	

	public void addExternalInterface(Interface inter) {
		interfaces.add(inter);
	}
	
	public void removeClass(Element klass) {
		this.classes.remove(klass);
		LOGGER.info("Classe: "+klass.getName() + " removida do pacote: "+this.getName());
	}
	
	public void removeInterface(Element klass) {
		this.interfaces.remove(klass);
		LOGGER.info("Interface: "+klass.getName() + " removida do pacote: "+this.getName());
	}

	public boolean removeImplementedInterface(Interface interface_) {
		if (!implementedInterfaces.contains(interface_)) return false;
		implementedInterfaces.remove(interface_);
		return true;
	}
	
	public boolean removeRequiredInterface(Interface supplier) {
		if (!requiredInterfaces.contains(supplier)) return false;
		requiredInterfaces.remove(supplier);
		return true;
	}

	public Set<Element> getElements() {
		Set<Element> elementsPackage = new HashSet<Element>();
		for(Class k : this.classes)
			elementsPackage.add(k);
		for(Interface i : this.interfaces)
			elementsPackage.add(i);
		
		return elementsPackage;
	}


}
