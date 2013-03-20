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
	
	public List<Method> getMethods() {
		return methods;
	}
	
}