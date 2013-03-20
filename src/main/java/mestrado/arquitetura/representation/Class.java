package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import mestrado.arquitetura.exceptions.AttributeNotFoundException;
import mestrado.arquitetura.exceptions.MethodNotFoundException;
import mestrado.arquitetura.representation.relationship.Relationship;

/**
 * 
 * @author edipofederle
 *
 */
public class Class extends Element {
	
	private final static Logger LOGGER = Logger.getLogger(Class.class.getName()); 
	
	private boolean isAbstract;
	private final List<Attribute> attributes = new ArrayList<Attribute>();
	private final List<Method> methods = new ArrayList<Method>();
	private String namespace;
	
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
	 * @param namespace
	 */
	public Class(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, boolean isAbstract, Element parent, String packageName, String namespace, String id) {
		super(architecture, name, isVariationPoint, variantType, "klass", parent, namespace, id);
		setAbstract(isAbstract);
		setNamespace(packageName);
	}

	//TODO verificar esses construtor
	public Class(Architecture architecture, String name) {
		this(architecture, name,  false, VariantType.NONE, false, null, "", "", ""); //TODO recber id, 
	}

	private void setNamespace(String packageName) {
		this.namespace = packageName;
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setAttribute(Attribute attr){
		this.attributes.add(attr);
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public List<Method> getAllMethods() {
		return methods;
	}

	public Attribute createAttribute(String name, String type) {
		Attribute a = new Attribute(getArchitecture(), name, type, this, "", "uniqueId"); //Verificar IDs
		getAttributes().add(a);
		return a;
	}

	public void removeAttribute(Attribute att) {
		getAttributes().remove(att);
	}

	public Attribute findAttributeByName(String name) throws AttributeNotFoundException {
		String message = "Attribute '" + name + "' not found in class '"+ this.getName() +"'.";
		
		for(Attribute att : getAttributes())
			if (name.equalsIgnoreCase(att.getName()))
				return att;
		LOGGER.info(message);
		throw new AttributeNotFoundException(message);
	}

	public void moveAttributeToClass(Attribute att, Class destinationKlass) {
		if (!getAttributes().contains(att)) return;
		
		removeAttribute(att);
		destinationKlass.getAttributes().add(att);
	}
	

	public Method createMethod(String name, String type, boolean isAbstract) {
		if (!methodExistsOnClass(name, type)){
			Method method = new Method(getArchitecture(), name, type, this, isAbstract);
			getAllMethods().add(method);
			return method;
		}
		return null; //TODO exp
	}

	private boolean methodExistsOnClass(String name, String type) {
		for(Method m : getAllMethods()){
			if((name.equalsIgnoreCase(m.getName())) && (type.equalsIgnoreCase(m.getReturnType()))){
				LOGGER.info("Method '"+ name + ":"+type + "' currently created in class '"+this.getName()+"'.");
				return true;
			}
		}
		
		return false;
	}

	//TODO verificar metodos com mesmo nome e tipo diferente
	public Method findMethodByName(String name) throws MethodNotFoundException {
		String message = "Method '" + name + "' not found in class '"+ this.getName() +"'.";
		
		for(Method m : getAllMethods())
			if((name.equalsIgnoreCase(m.getName())))
				return m;
		
		LOGGER.info(message);
		throw new MethodNotFoundException(message);
	}

	public void moveMethodToClass(Method m, mestrado.arquitetura.representation.Class destinationKlass) {
		if (!getAllMethods().contains(m)) return;
		
		removeMethod(m);
		destinationKlass.getAllMethods().add(m);
	}

	public void removeMethod(Method foo) {
		getAllMethods().remove(foo);
	}

	public List<Method> getAllAbstractMethods() {
		List<Method> abstractMethods = new ArrayList<Method>();
		
		for(Method m : getAllMethods())
			if (m.isAbstract()) abstractMethods.add(m);
		
		return abstractMethods;
	}

	public List<Relationship> getRelationships() {
		List<Relationship> relations = new ArrayList<Relationship>();

		for (Relationship relationship : getArchitecture().getAllRelationships())
			if(this.getIdsRelationships().contains(relationship.getId()))
				relations.add(relationship);
		
		return relations;
	}
	
}