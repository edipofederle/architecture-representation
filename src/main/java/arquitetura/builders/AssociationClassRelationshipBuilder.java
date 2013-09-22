package arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Element;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AssociationClassRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationClassRelationshipBuilder extends ArchitectureHelper {

	private Architecture architecture;
	private AttributeBuilder attributeBuilder;
	private MethodBuilder methodBuilder;

	public AssociationClassRelationshipBuilder(Architecture architecture) {
		this.architecture = architecture;
		attributeBuilder = new AttributeBuilder(architecture);
		methodBuilder = new MethodBuilder(architecture);
	}

	public AssociationClassRelationship create(AssociationClass associationClass) {
		List<Element> membersEnd = new ArrayList<Element>();
		
		for (Type t : associationClass.getEndTypes())
			membersEnd.add(architecture.getElementByXMIID(getModelHelper().getXmiId(t)));

		Type ownedEnd = associationClass.getOwnedEnds().get(0).getType();
		
		Element onewd = architecture.getElementByXMIID(getModelHelper().getXmiId(ownedEnd));

		architecture.getAllIds().add(getModelHelper().getXmiId(associationClass));
		
		String idOwner = null;
		if(!associationClass.getPackage().getName().equalsIgnoreCase("model"))
			idOwner = getModelHelper().getXmiId(associationClass.getOwner());

		//TODO passar oBJ
		return new AssociationClassRelationship(associationClass.getName(),
											    membersEnd,
											    onewd,
											    getModelHelper().getXmiId(associationClass),
											    idOwner,
											    getOnlyAttributes(associationClass),
											    getMethods(associationClass));
	}

	
	//TODO praticamente igual ao método do classBuilderr - Mover
	private List<Method> getMethods(AssociationClass associationClass) {
		List<Method> methods = new ArrayList<Method>();
		List<Operation> elements = associationClass.getAllOperations();
		
		for (Operation classifier : elements)
			methods.add(methodBuilder.create(classifier));
			
		return methods;
	}

	//TODO praticamente igual ao método do classBuilderr - Mover
	private List<Attribute> getOnlyAttributes(AssociationClass associationClass) {
		
		List<Attribute> attrs = new ArrayList<Attribute>();
		
		List<Property> attributes = associationClass.getAllAttributes();
		for (Property property : attributes){
			if(property.getAssociation() == null)
				attrs.add(attributeBuilder.create(property));
		}
	
		
		return attrs;
		
	}

}