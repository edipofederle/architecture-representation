package mestrado.arquitetura.builders;

import java.util.List;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Interface;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;

public class InterfaceBuilder extends ElementBuilder<mestrado.arquitetura.representation.Interface>  {

	private MethodBuilder methodBuilder;
	

	public InterfaceBuilder(Architecture architecture) {
		super(architecture);
		this.methodBuilder = new MethodBuilder(architecture);
		//Operations
	}

	@Override
	protected Interface buildElement(NamedElement modelElement, Element parent) {
		
		Interface interfacee = new Interface(architecture, name, isVariationPoint, variantType, parent, modelElement.getNamespace().getQualifiedName());
		
		List<Operation> elements = ((org.eclipse.uml2.uml.Class)modelElement).getAllOperations();
		for (Operation operation : elements) {
			interfacee.getOperations().add(methodBuilder.create(operation, interfacee));
		}
		
		return interfacee;
	}

}
