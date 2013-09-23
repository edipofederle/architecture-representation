package arquitetura.xmi2uml2;


import org.eclipse.uml2.uml.Classifier;

import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.helpers.Uml2Helper;
import arquitetura.helpers.Uml2HelperFactory;

public class Attribute {
	
	private String name;
	private String type;

	private static Uml2Helper uml2Helper;

	static {
			uml2Helper = Uml2HelperFactory.getUml2Helper();
	}

	public Attribute createOnClass(Classifier klass, String attributeName, String attributeType) throws ModelNotFoundException {
		name = attributeName;
		type = attributeType;
		uml2Helper.createAttribute((org.eclipse.uml2.uml.Class) klass, attributeName, uml2Helper.getPrimitiveType(attributeType), 1, 1);
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}

}