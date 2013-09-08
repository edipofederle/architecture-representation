package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.ParameterMethod;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.touml.ArchitectureBase;
import arquitetura.touml.Argument;
import arquitetura.touml.BindingTime;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Method;
import arquitetura.touml.Operations;
import arquitetura.touml.Types;
import arquitetura.touml.VariabilityStereotype;
import arquitetura.touml.VisibilityKind;

import com.google.common.base.Joiner;
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
			
//			
//			//Alguma manipulação sobre a arquitetura
//			List<Class> classes = a.getAllClasses();
//			
//			for (Class klass : classes) {		
//				klass.createAttribute("attr", Types.STRING, VisibilityKind.PUBLIC_LITERAL);
//				
//				List<ParameterMethod> parameters = new ArrayList<ParameterMethod>();
//				ParameterMethod p1 = new ParameterMethod("name", "String");
//				parameters.add(p1);
//				
//				klass.createMethod("fooBar1", "String", false, parameters);
//				Class class1 = a.createClass(klass.getName()+"AlgumaCoias");
//				Class class2 = a.createClass(klass.getName()+"AlgumaCoias_2");
//				a.createAssociation(class1, class2);
//			}
//			
//			//Fim manipulação
			
			List<Class> classesReloaded = a.getAllClasses(); //TODO VER ISSO?Nao sei mais o que é :(
			for (Class class1 : classesReloaded) {
				String idClass = null;
				List<arquitetura.touml.Attribute> attributes = new ArrayList<arquitetura.touml.Attribute>();
				List<Method> methods = new ArrayList<Method>();
				
				List<arquitetura.representation.Method> methodsClass = class1.getAllMethods();
				for (arquitetura.representation.Method method : methodsClass) {
					
					List<ParameterMethod> paramsMethod = method.getParameters();
					List<Argument> currentMethodParams = new ArrayList<Argument>();
					
					for (ParameterMethod param : paramsMethod) {
						currentMethodParams.add(Argument.create(param.getName(), Types.getByName(param.getType())));
					}
					
					if(method.isAbstract()){
						Method m = Method.create()
							  .withName(method.getName()).abstractMethod()
							  .withArguments(currentMethodParams)
							  .withReturn(Types.getByName(method.getReturnType())).build();
						methods.add(m);
					}else{
						Method m = Method.create()
								  .withName(method.getName())
								  .withArguments(currentMethodParams)
								  .withReturn(Types.getByName(method.getReturnType())).build();
						methods.add(m);
					}
						
						 
				}
				
				List<Attribute> attrs = class1.getAllAttributes();
				for (Attribute attribute : attrs) {
					arquitetura.touml.Attribute attr = arquitetura.touml.Attribute.create()
							 .withName(attribute.getName())
							 .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
							 .withType(Types.getByName(attribute.getType()));
					
					attributes.add(attr);
				}
				if(!attributes.isEmpty()){
					idClass = op.forClass()
							      .createClass(class1.getName())
							      .withAttribute(attributes)
							      .withMethod(methods)
							      .build()
							      .get("id");
					class1.updateId(idClass);
				}else{
					idClass = op.forClass().createClass(class1.getName()).build().get("id");
					class1.updateId(idClass);
				}
				
				VariationPoint vp = class1.getVariationPoint();
				if(vp != null)
					op.forClass().withId(idClass).isVariationPoint(spliterVariants(vp.getVariants()), spliterVariabilities(vp.getVariabilities()), BindingTime.DESIGN_TIME);
				
				Variant v = null;
					
					Variant variant = class1.getVariant();
					if(variant != null){
						try{
							Element elementRootVp = a.findElementByName(variant.getRootVP(), "class");
							String rootVp = null;
							
							if(elementRootVp != null)
								rootVp = elementRootVp.getId();
							else
								rootVp = ""; //TODO Ver essa questao
							v = Variant.createVariant()
									   .withName(variant.getVariantName())
									   .andRootVp(rootVp)
									   .withVariantType(variant.getVariantType()).build();
							
							//Se tem variant adicionar na classe
							if(v != null){
								op.forClass().addStereotype(class1.getId(), v);
							}
						
						}catch(Exception e){
							System.out.println("Error when try create Variant."+ e.getMessage());
							System.exit(0);
						}
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
				VariationPoint variationPointForVariability = variability.getVariationPoint();
				
				String idNote = op.forNote().createNote().build();
				VariabilityStereotype var = new VariabilityStereotype(variability.getName(), variability.getMinSelection(), variability.getMaxSelection()
						,variability.allowAddingVar(), variability.getBindingTime(), spliterVariants(variability.getVariants()));
					op.forNote().addVariability(idNote, var).build();
					op.forClass().withId(variationPointForVariability.getVariationPointElement().getId()).linkToNote(idNote);
				
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

	private static String spliterVariants(List<Variant> list) {
		List<String> names = new ArrayList<String>();
		
		for (Variant variant : list)
			names.add(variant.getName());
		
		return Joiner.on(", ").skipNulls().join(names);
	}
	
	private static String spliterVariabilities(List<Variability> list) {
		List<String> names = new ArrayList<String>();
		
		for(Variability variability : list)
			names.add(variability.getName());
		
		return Joiner.on(", ").skipNulls().join(names);
	}
	

}
