package mestrado.arquitetura.xmi2uml2;

import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;

import org.eclipse.uml2.uml.Classifier;

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