package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.AssociationClassInterClassRelationship;
import mestrado.arquitetura.representation.InterClassRelationship;
import mestrado.arquitetura.representation.Class;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Type;

public class AssociationClassInterClassRelationshipBuilder {
	
	private static ModelHelper modelHelper;
	private ClassBuilder classBuilder;
	
	static {
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}

	public AssociationClassInterClassRelationshipBuilder(ClassBuilder classBuilder) { this.classBuilder = classBuilder; }

	public InterClassRelationship create(AssociationClass associationClass) {
		List<Class> membersEnd = new ArrayList<Class>();
		
		for (Type t : associationClass.getEndTypes()) 
			membersEnd.add(classBuilder.getElementByXMIID(modelHelper.getXmiId(t)));
		
		Type ownedEnd = associationClass.getOwnedEnds().get(0).getType();
		mestrado.arquitetura.representation.Class onewd = classBuilder.getElementByXMIID(modelHelper.getXmiId(ownedEnd));
		
		return new AssociationClassInterClassRelationship(associationClass.getName(), membersEnd, onewd);
	}
	
}