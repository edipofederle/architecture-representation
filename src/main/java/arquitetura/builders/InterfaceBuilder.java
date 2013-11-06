package arquitetura.builders;

import java.util.List;


import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;

import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class InterfaceBuilder extends ElementBuilder<arquitetura.representation.Interface>  {

	private MethodBuilder methodBuilder;
	
	public InterfaceBuilder(Architecture architecture) {
		super(architecture);
		this.methodBuilder = new MethodBuilder(architecture);
		//Operations
	}

	@Override
	protected Interface buildElement(NamedElement modelElement) {
		
		Interface interfacee = new Interface(architecture, name, variantType, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement));
		
		List<Operation> elements = ((org.eclipse.uml2.uml.Class)modelElement).getOperations();
		
		for (Operation operation : elements)
			interfacee.getOperations().add(methodBuilder.create(operation));
		
		return interfacee;
	}

}
