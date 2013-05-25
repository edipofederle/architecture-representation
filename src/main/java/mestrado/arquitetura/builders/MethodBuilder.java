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

/**
 * Builder resposável por criar element do tipo Método.
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class MethodBuilder extends ElementBuilder<Method> {

	public MethodBuilder(Architecture architecture) {
		super(architecture);
	}
	
	/**
	 * Cria um elemento do tipo {@link Method}
	 */
	@Override
	protected Method buildElement(NamedElement modelElement) {
		Operation method =  ((Operation) modelElement);
		Type methodReturnType = method.getType();
		String type = methodReturnType != null ? methodReturnType.getName() : "";
		boolean isAbstract = false;
		
		List<ParameterMethod> parameterMethodReceives = new ArrayList<ParameterMethod>();
		
		isAbstract = method.isAbstract();
		EList<Parameter> params = method.getOwnedParameters();
		for (Parameter parameter : params){
			if(parameter.getDirection().getName().equals("out"))
				type = parameter.getType().getName();
			parameterMethodReceives.add(new ParameterMethod(parameter.getName(), parameter.getType().getName()));
		}
		
		String namespace = modelElement.getNamespace().getQualifiedName();
			
		Method m = new Method(architecture, name, isVariationPoint, variantType, type, isAbstract, parameterMethodReceives, namespace, getXmiId(modelElement));
		return m;
	}

}