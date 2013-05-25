package mestrado.arquitetura.builders;

import java.util.List;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Interface;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class InterfaceBuilder extends ElementBuilder<mestrado.arquitetura.representation.Interface>  {

	private MethodBuilder methodBuilder;
	
	public InterfaceBuilder(Architecture architecture) {
		super(architecture);
		this.methodBuilder = new MethodBuilder(architecture);
		//Operations
	}

	@Override
	protected Interface buildElement(NamedElement modelElement) {
		
		Interface interfacee = new Interface(architecture, name, isVariationPoint, variantType, modelElement.getNamespace().getQualifiedName(), getXmiId(modelElement));
		
		List<Operation> elements = ((org.eclipse.uml2.uml.Class)modelElement).getAllOperations();
		for (Operation operation : elements) {
			interfacee.getOperations().add(methodBuilder.create(operation));
		}
		return interfacee;
	}

}
