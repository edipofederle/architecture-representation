package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author edipofederle
 *
 */
public class Class extends Element {
	
	private boolean isAbstract;
	private final List<Attribute> attributes = new ArrayList<Attribute>();
	private final List<Method> methods = new ArrayList<Method>();
	private boolean interfacee;
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
	public Class(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, boolean isAbstract, Element parent, boolean interfacee, String packageName, String namespace) {
		super(architecture, name, isVariationPoint, variantType, "klass", parent, namespace);
		setAbstract(isAbstract);
		setIsInterface(interfacee);
		setNamespace(packageName);
	}

	private void setNamespace(String packageName) {
		this.namespace = packageName;
	}
	

	public String getNamespace() {
		return namespace;
	}

	private void setIsInterface(boolean interfacee2) {
		this.interfacee = interfacee2;
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
	
	public List<Method> getMethods() {
		return methods;
	}
	
	/**
	 * True se a classe é uma interface. Caso contrário False.
	 * 
	 * Uma classe é considerada uma interface se a mesma contém o estreótipo << interface >>
	 * 
	 * @return boolean
	 */
	public boolean isInterface() {
		return interfacee;
	}

	
	
}