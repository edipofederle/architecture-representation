package arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Type;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.AssociationClassRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationClassRelationshipBuilder extends ArchitectureHelper {

	private Architecture architecture;
	private ClassBuilder classBuilder;

	public AssociationClassRelationshipBuilder(Architecture architecture) {
		this.architecture = architecture;
		classBuilder = new ClassBuilder(architecture);
	}

	public AssociationClassRelationship create(AssociationClass associationClass) {
		List<Element> membersEnd = new ArrayList<Element>();
		
		Class classAssociation = classBuilder.create(associationClass);
		
		for (Type t : associationClass.getEndTypes())
			membersEnd.add(architecture.getElementByXMIID(getModelHelper().getXmiId(t)));

		Type ownedEnd = associationClass.getOwnedEnds().get(0).getType();
		
		Element onewd = architecture.getElementByXMIID(getModelHelper().getXmiId(ownedEnd));

		architecture.getAllIds().add(getModelHelper().getXmiId(associationClass));
		
		String idOwner = null;
		if(!associationClass.getPackage().getName().equalsIgnoreCase("model"))
			idOwner = getModelHelper().getXmiId(associationClass.getOwner());
		
		EList<org.eclipse.uml2.uml.Element> relatedElements = associationClass.getRelatedElements();
		
		for (org.eclipse.uml2.uml.Element element : relatedElements) {
			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(element));
			e.getIdsRelationships().add((getModelHelper().getXmiId(associationClass)));
		}

		//TODO passar oBJ
		return new AssociationClassRelationship(architecture, associationClass.getName(),
											    membersEnd,
											    onewd,
											    getModelHelper().getXmiId(associationClass),
											    idOwner,
											    classAssociation);
	}

}