package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.exceptions.AttributeNotFoundException;
import arquitetura.exceptions.MethodNotFoundException;
import arquitetura.flyweights.VariantFlyweight;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.touml.Types.Type;
import arquitetura.touml.VisibilityKind;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Class extends Element {
	
	private static final long serialVersionUID = -5450511036321846093L;

	static Logger LOGGER = LogManager.getLogger(Class.class.getName());
	
	private boolean isAbstract;
	private final Set<Attribute> attributes = new HashSet<Attribute>();
	private final Set<Method> methods = new HashSet<Method>();
	private Set<Interface> implementedInterfaces = new HashSet<Interface>();
	private Set<Interface> requiredInterfaces = new HashSet<Interface>();
	
	/**
	 * 
	 * @param architecture
	 * @param name
	 * @param isVariationPoint
	 * @param variantType
	 * @param isAbstract
	 * @param parent
	 * @param interfacee
	 * @param packageName
	 */
	public Class(Architecture architecture, String name, Variant variantType, boolean isAbstract, String namespace, String id) {
		super(architecture, name, variantType, "klass", namespace, id);
		setAbstract(isAbstract);
	}

	public Class(Architecture architecture, String name, boolean isAbstract) {
		this(architecture, name,  null, isAbstract,  UtilResources.createNamespace(architecture.getName(), name), UtilResources.getRandonUUID());
		architecture.addExternalClass(this);
	}
	
	public Class(Architecture architecture, String name, boolean isAbstract, String packageName) {
		this(architecture, name,  null, isAbstract,  UtilResources.createNamespace(architecture.getName()+"::"+packageName, name), UtilResources.getRandonUUID());
		architecture.addExternalClass(this);
	}

	public Attribute createAttribute(String name, Type type, VisibilityKind visibility) {
		String id = UtilResources.getRandonUUID();
		Attribute a = new Attribute(getArchitecture(), name, visibility.toString(), type.getName(), getArchitecture().getName()+"::"+this.getName(), id);
		addExternalAttribute(a);
		return a;
	}

	public void setAttribute(Attribute attr){
		this.attributes.add(attr);
	}
	
	public Set<Attribute> getAllAttributes() {
		return Collections.unmodifiableSet(attributes);
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public Set<Method> getAllMethods() {
		return Collections.unmodifiableSet(methods);
	}


	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean removeAttribute(Attribute attribute) {
		if (!this.attributes.contains(attribute)) return false;
		this.attributes.remove(attribute);
		return true;
	}

	public Attribute findAttributeByName(String name) throws AttributeNotFoundException {
		String message = "Attribute '" + name + "' not found in class '"+ this.getName() +"'.\n";
		
		for(Attribute att : getAllAttributes())
			if (name.equalsIgnoreCase(att.getName()))
				return att;
		LOGGER.info(message);
		throw new AttributeNotFoundException(message);
	}

	
	public boolean moveAttributeToClass(Attribute attribute, Class destinationKlass) {
		if (!this.attributes.contains(attribute)) return false;
		removeAttribute(attribute);
		destinationKlass.addExternalAttribute(attribute);
		attribute.setNamespace(getArchitecture().getName() + "::" + destinationKlass.getName());
		return true;
	}
	

	public Method createMethod(String name, String type, boolean isAbstract, List<ParameterMethod> parameters) {
		if (!methodExistsOnClass(name, type)){
			String id = UtilResources.getRandonUUID();
			Method method = new Method(getArchitecture(), name, type, this.getName(), isAbstract, id);
			if(parameters != null)
				method.getParameters().addAll(parameters);
			getAllMethods().add(method);
			return method;
		}
		return null; 
	}

	private boolean methodExistsOnClass(String name, String type) {
		for(Method m : getAllMethods()){
			if((name.equalsIgnoreCase(m.getName())) && (type.equalsIgnoreCase(m.getReturnType()))){
				LOGGER.info("Method '"+ name + ":"+type + "' currently created in class '"+this.getName()+"'.\n");
				return true;
			}
		}
		
		return false;
	}

	//TODO verificar metodos com mesmo nome e tipo diferente
	public Method findMethodByName(String name) throws MethodNotFoundException {
		String message = "Method '" + name + "' not found in class '"+ this.getName() +"'.\n";
		
		for(Method m : getAllMethods())
			if((name.equalsIgnoreCase(m.getName())))
				return m;
		
		LOGGER.info(message);
		throw new MethodNotFoundException(message);
	}

	/**
	 * Move um método de uma classe para outra.
	 * 
	 * @param method - Método a ser movido
	 * @param destinationKlass - Classe que irá receber o método
	 * 
	 * @return -false se o método a ser movido não existir na classe.<br/> -true se o método for movido com sucesso.
	 */
	public boolean moveMethodToClass(Method method, arquitetura.representation.Class destinationKlass) {
		if(!this.methods.contains(method)) return false;
		destinationKlass.addExternalMethod(method);
		if(removeMethod(method))
			LOGGER.info("Método: " + method.getName() + " removido da classe "+ this.getName());
		
		LOGGER.info("Moveu método: "+  method.getName() + " de "+this.getName() +" para " + destinationKlass.getName());
		method.setNamespace(getArchitecture().getName() + "::" + destinationKlass.getName());
		
		return true;
	}
	
	public void addExternalMethod(Method method) {
		if(methods.add(method))
			LOGGER.info("Metodo "+method.getName() + " adicionado na classe "+ this.getName());
	}

	/**
	 * Remove Método da classe
	 * 
	 * @param method - Método a ser removido.
	 * 
	 * @return -true se o método for removido.<br/>-false se método não existir na classe.
	 * 
	 */
	public boolean removeMethod(Method method) {
		return methods.remove(method);
	}

	public List<Method> getAllAbstractMethods() {
		List<Method> abstractMethods = new ArrayList<Method>();
		
		for(Method m : getAllMethods())
			if (m.isAbstract()) abstractMethods.add(m);
		
		return abstractMethods;
	}


	public boolean dontHaveAnyRelationship() {
		
		if(getRelationships().isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	public void updateId(String id) {
		super.id = id;
		
	}

	public Object getAllStereotype() {
		return null;
	}


	/**
	 * Retorna o tipo de variant (ex: alternative_OR).<br/>
	 * 
	 * Retorna null se não existir
	 * 
	 * @return String (ex: alternative_OR).
	 */
	public String getVariantType() {
		for(Variant v : VariantFlyweight.getInstance().getVariants()){
			if(v.getName().equalsIgnoreCase(this.getName()))
				return v.getVariantType();
		}
		
		return null;
	}

	@Override
	public Collection<Concern> getAllConcerns() {
		Collection<Concern> concerns = new ArrayList<Concern>(getOwnConcerns());

		for (Method method : getAllMethods())
			concerns.addAll(method.getAllConcerns());
		for (Attribute attribute : getAllAttributes())
			concerns.addAll(attribute.getAllConcerns());
		
		return concerns;
	}

	public List<AssociationClassRelationship> getAllAssociationClass() {
		List<AssociationClassRelationship> associationsClasses = new ArrayList<AssociationClassRelationship>();
		
		for(Relationship r : getRelationships()){
			if(r instanceof AssociationClassRelationship)
				associationsClasses.add((AssociationClassRelationship) r);
		}
		
		return associationsClasses;
	}

	public Set<Interface> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public Set<Interface> getRequiredInterfaces() {
		return requiredInterfaces;
	}

	public void addExternalAttribute(Attribute a) {
		this.attributes.add(a);
	}

	
//	/**
//	 * se classe for um ponto de variação retorna todas as variantes
//	 * @return 
//	 */
//	public List<Element> getVariants() {
//		List<Element> variants = new ArrayList<Element>();
//		if(this.isVariationPoint()){
//			List<Variability> variabilities = getArchitecture().getAllVariabilities();
//			for (Variability variability : variabilities) {
//				for(String element : variability.getVariants()){
//					try {
//						variants.add(getArchitecture().findClassByName(element));
//					} catch (ClassNotFound e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		return variants;
//	}
	
	
	
}