package arquitetura.xmi2uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;

import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import arquitetura.helpers.Uml2Helper;
import arquitetura.helpers.Uml2HelperFactory;

public class Class {

	private static Classifier klass;
	private static  List<Attribute> attributes = new ArrayList<Attribute>();
	private static List<Operation> methods = new ArrayList<Operation>();
	
	protected static Uml2Helper uml2Helper;

	static {
		uml2Helper = Uml2HelperFactory.getUml2Helper();
	}

	public static Class createOnModel(Package model) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
		attributes.clear();
		methods.clear();
		klass = uml2Helper.createClass(model, "", false);
		return new Class();
	}

	public Class withName(String name) {
		klass.setName(name);
		return this;
	}
	
	/**
	 * PROTECTED_LITERAL
	 * PRIVATE_LETERAL
	 * PACKAGE_LITERAL
	 * PUBLIC_LITERAL
	 * 
	 * @param visibility
	 * @return
	 */
	public Class withVisibility(String visibility) {
		klass.setVisibility(VisibilityKind.get(visibility.toUpperCase()));
		return this;
	}

	public Classifier getKlass() {
		return klass;
	}

	public String getName() {
		String name = ( (klass.getName().isEmpty()) || (klass.getName() == null)) ? "DefaultName" : klass.getName();
		return name;
	}

	public String getVisibility() {
		return klass.getVisibility().getName();
	}

	public Class withAttributes(String ... attr) throws ModelNotFoundException {
		
		for(int i=0; i < attr.length; i++){
			String[] a = attr[i].split(":");
			String attributeName = a[0];
			String attributeType = a[1];
			attributes.add(new Attribute().createOnClass(klass, attributeName, attributeType));
		}

		return this;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Operation> getAllMethods() {
		return methods;
	}

	public Class withMethod(String methodName, Map<String, String> params, String returnType) throws ModelNotFoundException {
		EList<String> parameterNames = new BasicEList<String>();
		EList<Type> parameterTypes = new BasicEList<Type>();
		
		
		
		Iterator<String> paramsNames = params.keySet().iterator();
		Collection<String> paramsTypes = params.values();
		
		while(paramsNames.hasNext()){
			parameterNames.add(paramsNames.next());
		}
		
		for (String type : paramsTypes) {
			Type paramType = uml2Helper.getPrimitiveType(type);
			parameterTypes.add(paramType);
		}
		
		methods.add(uml2Helper.createOperation(klass, methodName, parameterNames, parameterTypes,  uml2Helper.getPrimitiveType(returnType)));
		return this;
	}
	

}