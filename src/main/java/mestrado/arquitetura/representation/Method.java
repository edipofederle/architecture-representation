package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author edipofederle
 *
 */
public class Method extends Element{
	
	private String returnType;
	private final List<ParameterMethod> parameters = new ArrayList<ParameterMethod>();
	private boolean isAbstract;

	public Method(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod, Element parent, String namespace, String id) {
		super(architecture, name, isVariationPoint, variantType, "method", parent, namespace, id);
		setReturnType(returnType);
		setAbstract(isAbstract);
		setParams(paramsMethod);
	}
	
	private void setParams(List<ParameterMethod> paramsMethod) {
		if(paramsMethod != null)
			parameters.addAll(paramsMethod);
	}
	
	public Method(Architecture architecture, String name, Boolean isVariationPoint, VariantType variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod, Element parent, String namespace, String id) {
		this(architecture, name, false, VariantType.NONE, returnType, isAbstract, paramsMethod, parent, namespace, id);
	}

	public Method(Architecture architecture, String name, String type, Element parent, boolean isAbstract) {
		super(architecture, name, false, VariantType.NONE, "method", parent, parent.getNamespace(), "id"); //TODO verificar id unico
		setReturnType(type);
		setAbstract(isAbstract);
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract; 
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}

	public String getReturnType() {
		return returnType;
	}

	private void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<ParameterMethod> getParameters() {
		return parameters;
	}

}
