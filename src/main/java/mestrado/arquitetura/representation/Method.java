package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

public class Method extends Element{
	
	private String returnType;
	private final List<ParameterMethod> parameters = new ArrayList<ParameterMethod>();
	private boolean isAbstract;

	public Method(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod) {
		super(architecture, name, isVariationPoint, variantType);
		setReturnType(returnType);
		setAbstract(isAbstract);
		setParams(paramsMethod);
	}
	
	private void setParams(List<ParameterMethod> paramsMethod) {
		parameters.addAll(paramsMethod);
	}

	public Method(Architecture architecture, String name, Boolean isVariationPoint, VariantType variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod) {
		this(architecture, name, false, VariantType.NONE, returnType, isAbstract, paramsMethod);
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
