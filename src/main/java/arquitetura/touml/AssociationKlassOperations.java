package arquitetura.touml;

import java.util.ArrayList;
import java.util.List;

import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.ParameterMethod;
import arquitetura.representation.relationship.AssociationClassRelationship;


public class AssociationKlassOperations {
	
	private DocumentManager documentManager;
	private String ownedEnd;
	private String associationEnd2;
	private String id;
	private ElementXmiGenerator elementXmiGenerator;
	private List<arquitetura.touml.Attribute> attrs;
	private List<arquitetura.touml.Method> methods;
	private Architecture architecture;
	private Class associationClass;
	
	public AssociationKlassOperations(DocumentManager doc, Architecture a) {
		this.documentManager = doc;
		this.architecture = a;
	}

	public AssociationKlassOperations createAssociationClass(AssociationClassRelationship asr) {
		this.id = asr.getId();
		this.ownedEnd = asr.getMemebersEnd().get(0).getType().getId();
		this.associationEnd2 = asr.getMemebersEnd().get(1).getType().getId();
		this.elementXmiGenerator = new ElementXmiGenerator(documentManager, this.architecture);
		this.attrs = buildAttributes(asr);
		this.methods = createMethods(asr);
		this.associationClass = asr.getAssociationClass();
		return this;
	}

	private List<arquitetura.touml.Attribute> buildAttributes(AssociationClassRelationship asr) {
		List<arquitetura.touml.Attribute> attrs = new ArrayList<arquitetura.touml.Attribute>();
		for(Attribute attribute : asr.getAssociationClass().getAllAttributes()){
			arquitetura.touml.Attribute attr = arquitetura.touml.Attribute.create()
					 .withName(attribute.getName())
					 .grafics(attribute.isGeneratVisualAttribute())
					 .withConcerns(attribute.getOwnConcerns())
					 .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
					 .withType(Types.getByName(attribute.getType()));
			
			attrs.add(attr);
		}
		
		return attrs;
	}
	
	//TODO refatorar método do main - mover
	private static List<arquitetura.touml.Method> createMethods(AssociationClassRelationship klass) {
		List<arquitetura.touml.Method> methods = new ArrayList<arquitetura.touml.Method>();
		List<arquitetura.representation.Method> methodsClass = new ArrayList<arquitetura.representation.Method>();
		
		methodsClass = klass.getAllMethods();
		for (arquitetura.representation.Method method : methodsClass) {
			
			List<ParameterMethod> paramsMethod = method.getParameters();
			List<Argument> currentMethodParams = new ArrayList<Argument>();
			
			for (ParameterMethod param : paramsMethod) {
				currentMethodParams.add(Argument.create(param.getName(), Types.getByName(param.getType()), param.getDirection()));
			}
			
			if(method.isAbstract()){
				arquitetura.touml.Method m = arquitetura.touml.Method.create()
					  .withId(method.getId())
					  .withName(method.getName()).abstractMethod()
					  .withArguments(currentMethodParams)
					  .withConcerns(method.getOwnConcerns())
					  .withReturn(Types.getByName(method.getReturnType())).build();
				methods.add(m);
			}else{
				arquitetura.touml.Method m = arquitetura.touml.Method.create()
						  .withId(method.getId())
						  .withName(method.getName())
						  .withArguments(currentMethodParams)
						  .withConcerns(method.getOwnConcerns())
						  .withReturn(Types.getByName(method.getReturnType())).build();
				methods.add(m);
			}
				 
		}
		
		return methods;
	}


	public void build() {
		final AssociationClassNode associationClassNode = new AssociationClassNode(this.documentManager,null);
		
		arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				associationClassNode.createAssociationClass(id, ownedEnd, associationEnd2);
			}
		});
		
		for (final arquitetura.touml.Attribute attribute : this.attrs) {
			arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
				public void useTransformation() {
					elementXmiGenerator.generateAttribute(attribute, id);
				}
			});
		}
		
		for (final arquitetura.touml.Method method : this.methods) {
			arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
				public void useTransformation() {
					elementXmiGenerator.generateMethod(method, id);
				}
			});
		}
		
		//Adiciona Interesses nos atributos
		for (arquitetura.touml.Attribute attribute : attrs) {
			for (Concern c : attribute.getConcerns()) {
				elementXmiGenerator.generateConcern(c, attribute.getId());
			}
		}
		
		//Adiciona Interesses nos métodos
		for (arquitetura.touml.Method method : methods) {
			for (Concern c : method.getConcerns()) {
				elementXmiGenerator.generateConcern(c, method.getId());
			}
		}
		
		//Adiciona Interesses nos métodos
		for (arquitetura.touml.Method method : methods) {
			for (Concern c : method.getConcerns()) {
				elementXmiGenerator.generateConcern(c, method.getId());
			}
		}
		
		//Adiciona Interesses na associationClass
		for (Concern c : associationClass.getOwnConcerns()) {
			elementXmiGenerator.generateConcern(c, associationClass.getId());
		}
		
	}

}
