package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Method;
import mestrado.arquitetura.representation.ParameterMethod;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;

public class MethodBuilder extends ElementBuilder<Method> {

	public MethodBuilder(Architecture architecture) {
		super(architecture);
	}

	@Override
	protected Method buildElement(NamedElement modelElement) {
		Operation method =  ((Operation) modelElement);
		Type methodType = method.getType();
		String type = methodType != null ? methodType.getName() : "";
		boolean isAbstract = false;
		
		List<ParameterMethod> paramsMethod = new ArrayList<ParameterMethod>();
		
		isAbstract = method.isAbstract();
		EList<Parameter> params = method.getOwnedParameters();
		for (Parameter parameter : params)
			paramsMethod.add(new ParameterMethod(parameter.getName(), parameter.getType().getName()));
			
		Method m = new Method(architecture, name, isVariationPoint, variantType, type, isAbstract, paramsMethod);
		return m;
	}

}
