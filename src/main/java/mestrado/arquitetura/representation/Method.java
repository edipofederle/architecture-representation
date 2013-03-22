package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.UtilResources;

/**
 * 
 * @author edipofederle
 *
 */
public class Method extends Element{
	
	private String returnType;
	private final List<ParameterMethod> parameters = new ArrayList<ParameterMethod>();
	private boolean isAbstract;

	public Method(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod, String namespace, String id) {
		super(architecture, name, isVariationPoint, variantType, "method", namespace, id);
		setReturnType(returnType);
		setAbstract(isAbstract);
		setParams(paramsMethod);
	}
	
	private void setParams(List<ParameterMethod> paramsMethod) {
		if(paramsMethod != null)
			parameters.addAll(paramsMethod);
	}
	
	public Method(Architecture architecture, String name, Boolean isVariationPoint, VariantType variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod, String namespace, String id) {
		this(architecture, name, false, VariantType.NONE, returnType, isAbstract, paramsMethod,  namespace, id);
	}

	public Method(Architecture architecture, String name, String type, String className, boolean isAbstract, String id) {
		super(architecture, name, false, VariantType.NONE, "method", UtilResources.createNamespace(architecture.getName(), className), id);
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
