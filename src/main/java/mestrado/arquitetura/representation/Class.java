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
	
	/**
	 * 
	 * @param architecture
	 * @param name
	 * @param isVariationPoint
	 * @param variantType
	 * @param isAbstract 
	 * @param parent null if not exist
	 * @param interfacee Is Class a interface?
	 */
	public Class(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, boolean isAbstract, Element parent, boolean interfacee) {
		super(architecture, name, isVariationPoint, variantType, "klass", parent);
		setAbstract(isAbstract);
		setIsInterface(interfacee);
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
	 * True se a classe é uma interface, caso contrário False.
	 * 
	 * Uma classe é considerada uma interface se a mesma contém o estreótipo << interface >>
	 * 
	 * @return boolean
	 */
	public boolean isInterface() {
		return interfacee;
	}

	
}