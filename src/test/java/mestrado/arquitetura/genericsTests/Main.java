package mestrado.arquitetura.genericsTests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.api.touml.BindingTime;
import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.api.touml.Operations;
import mestrado.arquitetura.base.ArchitectureBase;
import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.io.ReaderConfig;
import mestrado.arquitetura.parser.method.Method;
import mestrado.arquitetura.parser.method.Types;
import mestrado.arquitetura.parser.method.VisibilityKind;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Variability;
import mestrado.arquitetura.representation.Variant;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;
import mestrado.arquitetura.writer.VariabilityStereotype;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main extends ArchitectureBase {

	/**
	 * @param args
	 * @throws ModelIncompleteException 
	 * @throws ModelNotFoundException 
	 * @throws SMartyProfileNotAppliedToModelExcepetion 
	 */
	public static void main(String[] args) {
		
		Logger LOGGER = LogManager.getLogger(Main.class.getName());
		
		
		if((args.length != 2) || ("".equals(args[0].trim()))){
			System.out.println("\tUsage:");
			System.out.println("\t\t java -jar arq.java <path_to_model.uml> <model_output_name>");
			System.exit(0);
		}
		
		long init = System.currentTimeMillis();
		
		System.out.println("Working....");

		
		String pathToModel = args[0];
		
		Architecture a;
		DocumentManager doc = null;
		try {
			doc = givenADocument(args[1]);
		} catch (ModelNotFoundException e1) {
			LOGGER.warn("Cannot find model: " + e1.getMessage());
		} catch (ModelIncompleteException e1) {
			LOGGER.warn("Model Incomplete" + e1.getMessage());
		} catch (SMartyProfileNotAppliedToModelExcepetion e1) {
			LOGGER.warn("Smarty Profile note Applied: "+e1.getMessage());
		}
		
		String path = new File(pathToModel).getAbsolutePath(); 
		Operations op = null;
		
		op = new Operations(doc);
		
		
		try {
			a = new ArchitectureBuilder().create(path);
			
			
			//Alguma manipulação sobre a arquitetura
			List<Class> classes = a.getAllClasses();
			
			for (Class klass : classes) {		
				klass.createAttribute("attr", Types.STRING, VisibilityKind.PUBLIC_LITERAL); // ver resto dos parametros
				klass.createMethod("fooBar", "String", false); // ver resto dos parametros
				klass.createMethod("fooBar1", "String", false);
				klass.createMethod("fooBar2", "String", true);
				klass.createMethod("fooBar3", "String", false);
				klass.createMethod("fooBar4", "String", true);
			}
			
			//Fim manipulação
			
			
			for (Class class1 : classes) {
				
				List<mestrado.arquitetura.parser.method.Attribute> attributes = new ArrayList<mestrado.arquitetura.parser.method.Attribute>();
				List<Method> methods = new ArrayList<Method>();
				
				List<mestrado.arquitetura.representation.Method> methodsClass = class1.getAllMethods();
				for (mestrado.arquitetura.representation.Method method : methodsClass) {
					
					if(method.isAbstract()){
						Method m = Method.create()
							  .withName(method.getName()).abstractMethod()
							  .withReturn(Types.getByName(method.getReturnType())).build();
						methods.add(m);
					}else{
						Method m = Method.create()
								  .withName(method.getName())
								  .withReturn(Types.getByName(method.getReturnType())).build();
						methods.add(m);
					}
						
						 
				}
				
				List<Attribute> attrs = class1.getAllAttributes();
				for (Attribute attribute : attrs) {
					mestrado.arquitetura.parser.method.Attribute attr = mestrado.arquitetura.parser.method.Attribute.create()
							 .withName(attribute.getName())
							 .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
							 .withType(Types.getByName(attribute.getType()));
					
					attributes.add(attr);
				}
				if(!attributes.isEmpty()){
					String id = op.forClass().createClass(class1.getName()).withAttribute(attributes).withMethod(methods).build().get("id");
					class1.updateId(id);
				}else{
					String id = op.forClass().createClass(class1.getName()).build().get("id");
					class1.updateId(id);
				}
				
				Variant v = null;
				try{
					Variant variant = class1.getVariantType();
					Element elementRootVp = a.findElementByName(variant.getRootVP());
					v = Variant.createVariant().withName(variant.getVariantName()).andRootVp(elementRootVp.getId()).build();
				}catch(Exception e){}
				
				//Se tem variant adicionar na classe
				if(v != null){
					op.forClass().addStereotype(class1.getId(), v);
				}
				
				/**
				 * Deve atualizar o id do elemento em memória com o id do elemente gerado.
				 * Isso é necesário pois quando usamos os métodos para gerar os elementos esses geram ids novos,	
				 * diante disso ambos ids são diferentes e não é possível localizar os elementos nos XMIS para criar os relacionamentos.
				 */
				
			}
			
			//Variabilidades - Notes
			List<Variability> variabilities = a.getAllVariabilities();
			for (Variability variability : variabilities) {
				String ownerClass = a.findElementByName(variability.getOwnerClass()).getId();
				
				String idNote = op.forNote().createNote().build();
				VariabilityStereotype var = new VariabilityStereotype(variability.getMinSelection(),
																	 variability.getMaxSelection(), false, BindingTime.DESIGN_TIME,
																	 variability.getVariants());
				op.forNote().addVariability(idNote, var).build();
				op.forClass().withId(ownerClass).linkToNote(idNote);
			}
			
			for (AssociationRelationship r : a.getAllAssociations()) {
				op.forAssociation().createAssociation()
				  .betweenClass(r.getParticipants().get(0).getCLSClass().getId())
				  .andClass(r.getParticipants().get(1).getCLSClass().getId()).build();
			}
			
			for(DependencyRelationship d : a.getAllDependencies()){
				op.forDependency().createRelation("")
								  .between(d.getClient().getId())
								  .and(d.getSupplier().getId()).build();
			}
			
			for(GeneralizationRelationship g : a.getAllGeneralizations()){
				op.forGeneralization().createRelation("Ge").between(g.getChild().getId()).and(g.getParent().getId()).build();
			}
			
			
		} catch (Exception e) {
			System.out.println("Ops!. Error, I am sorry: " + e.getMessage());
			System.exit(0);
		}
		long end = System.currentTimeMillis();
		System.out.println("\nDone. Architecture save into: " + ReaderConfig.getDirExportTarget()+doc.getNewModelName());
		System.out.println("Time:" + (end - init) + "Millis");
		
	}
	

}
