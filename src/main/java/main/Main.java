package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.exceptions.CustonTypeNotFound;
import arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.NodeNotFound;
import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import arquitetura.helpers.Strings;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;
import arquitetura.representation.ParameterMethod;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.UsageRelationship;
import arquitetura.touml.ArchitectureBase;
import arquitetura.touml.Argument;
import arquitetura.touml.BindingTime;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Method;
import arquitetura.touml.Operations;
import arquitetura.touml.Types;
import arquitetura.touml.VariabilityStereotype;
import arquitetura.touml.VisibilityKind;
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
		
		
		
		
		try {
			a = new ArchitectureBuilder().create(path);
			op = new Operations(doc,a);
			
			List<Package> packages = a.getAllPackages();

			for(Class klass : a.getAllClasses()){
				List<arquitetura.touml.Attribute> attributesForClass = createAttributes(op, klass);
				
				
				List<Method> methodsForClass = createMethods(klass);
				
				//Interesses para classe
				List<Concern> klassConcerns = klass.getOwnConcerns();
				
				//Variation Point
				VariationPoint variationPoint = klass.getVariationPoint();
				String variants = "";
				String variabilities = "";
				
				if(variationPoint != null){
					variants = Strings.spliterVariants(variationPoint.getVariants());
					variabilities = Strings.spliterVariabilities(variationPoint.getVariabilities());
				}
				//Variation Point
				
				if(attributesForClass.isEmpty() )
					op.forClass().createClass(klass).withMethods(methodsForClass).build();
				else{
					op.forClass()
				      .createClass(klass)
				      .withMethods(methodsForClass)
				      .withAttribute(attributesForClass)
				      .isVariationPoint(variants, variabilities, BindingTime.DESIGN_TIME)
				      .build();
				}
				
				//Adiciona Interesses nas classes
				op.forConcerns().withConcerns(klass.getOwnConcerns(), klass.getId());
				//Adiciona Interesses nas classes
				
				//Adiciona Interesses nos atributos
				for (arquitetura.touml.Attribute attr : attributesForClass) {
					op.forConcerns().withConcerns(attr.getConcerns(), attr.getId());
				}
				//Adiciona Interesses nos atributos
				
				//Adiciona Interesses nos métodos
				for(Method m : methodsForClass){
					op.forConcerns().withConcerns(m.getConcerns(), m.getId());
				}
				//Adiciona Interesses nas classes
				
				//Variant Type
				
				Variant v = null;
				
				Variant variant = klass.getVariant();
				if(variant != null){
					try{
						Element elementRootVp = a.findElementByName(variant.getRootVP(), "class");
						String rootVp = null;
						
						if(elementRootVp != null)
							rootVp = elementRootVp.getName();
						else
							rootVp = "";
						v = Variant.createVariant()
								   .withName(variant.getVariantName())
								   .andRootVp(rootVp)
								   .wihtVariabilities(variant.getVariabilities())
								   .withVariantType(variant.getVariantType()).build();
						
						//Se tem variant adicionar na classe
						if(v != null){
							op.forClass().addStereotype(klass.getId(), v);
						}
					
					}catch(Exception e){
						System.out.println("Error when try create Variant."+ e.getMessage());
						System.exit(0);
					}
				}
				//Variant Type
			}
			
			for(Interface _interface : a.getAllInterfaces()){
				List<Method> methodsForClass = createMethods(_interface);
				op.forClass()
				  .createClass(_interface)
				  .withMethods(methodsForClass)
				  .asInterface()
				  .build();
			}
			
			for (Package pack : packages) {
				//Todas as classes do pacote
				List<String> ids = pack.getAllClassIdsForThisPackage();
				op.forPackage().createPacakge(pack).withClass(ids).build();
				
			}
			
			
			//Relacionamentos
			for (AssociationRelationship r : a.getAllAssociations()) {
				generateComposition(op, r);
				generateSimpleAssociation(op, r);
				generateAggregation(op, r);
			}
			
			
			for(GeneralizationRelationship g : a.getAllGeneralizations()){
				op.forGeneralization().createRelation("Ge").between(g.getChild().getId()).and(g.getParent().getId()).build();
			}
			
			for(DependencyRelationship d : a.getAllDependencies()){
				op.forDependency().createRelation("")
							  .between(d.getClient().getId())
							  .and(d.getSupplier().getId()).build();
			}
			
			for(RealizationRelationship r : a.getAllRealizations()){
				op.forRealization().createRelation("").between(r.getClient().getId()).and(r.getSupplier().getId()).build();
			}
			
			for(UsageRelationship u : a.getAllUsage()){
				op.forUsage().createRelation("").between(u.getClient().getId()).and(u.getSupplier().getId()).build();
			}
			
			//Variabilidades - Notes
			List<Variability> variabilities = a.getAllVariabilities();
			String idOwner = "";
			for (Variability variability : variabilities) {
				VariationPoint variationPointForVariability = variability.getVariationPoint();
				/*
				 * Um Variabilidade pode estar ligada a uma classe que não seja um ponto de variação,
				 * neste caso a chama do método acima vai retornar null. Quando isso acontecer é usado o
				 * método getOwnerClass() que retorna a classe que é dona da variabilidade.
				 */
				if(variationPointForVariability == null){
					idOwner = a.findClassByName(variability.getOwnerClass()).get(0).getId();
				}else{
					idOwner = variationPointForVariability.getVariationPointElement().getId();
				}
				
				String idNote = op.forNote().createNote().build();
				VariabilityStereotype var = new VariabilityStereotype(variability.getName(), variability.getMinSelection(), variability.getMaxSelection()
						,variability.allowAddingVar(), variability.getBindingTime(), Strings.spliterVariants(variability.getVariants()));
					op.forNote().addVariability(idNote, var).build();
					op.forClass().withId(idOwner).linkToNote(idNote);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		long end = System.currentTimeMillis();
		System.out.println("\nDone. Architecture save into: " + ReaderConfig.getDirExportTarget()+doc.getNewModelName());
		System.out.println("Time:" + (end - init) + "Millis");
		
	}

	private static void generateAggregation(Operations op,	AssociationRelationship r) throws NotSuppportedOperation {
		AssociationEnd p1 = r.getParticipants().get(0);
		AssociationEnd p2 = r.getParticipants().get(1);
		
		if(p1.isAggregation()){
			op.forAggregation().createRelation("").between(p1.getCLSClass().getId()).and(p2.getCLSClass().getId()).build();
		}else if(p2.isAggregation()){
			op.forAggregation().createRelation("").between(p2.getCLSClass().getId()).and(p1.getCLSClass().getId()).build();
		}
	}

	private static void generateSimpleAssociation(Operations op, AssociationRelationship r) {
		AssociationEnd p1 = r.getParticipants().get(0);
		AssociationEnd p2 = r.getParticipants().get(1);
		if(p1.getAggregation().equalsIgnoreCase("none") && (p2.getAggregation().equalsIgnoreCase("none"))){
			op.forAssociation().createAssociation()
			  .betweenClass(r.getParticipants().get(0).getCLSClass().getId())
			  .andClass(r.getParticipants().get(1).getCLSClass().getId()).build();
		}
	}

	private static void generateComposition(Operations op, AssociationRelationship r) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		AssociationEnd p1 = r.getParticipants().get(0);
		AssociationEnd p2 = r.getParticipants().get(1);
		
		if(p1.isComposite()){
			op.forComposition().createComposition()
							   .between(p1.getCLSClass().getId())
							   .and(p2.getCLSClass().getId()).build();
		}else if(p2.isComposite()){
			op.forComposition().createComposition()
			   .between(p2.getCLSClass().getId())
			   .and(p1.getCLSClass().getId()).build();
		}
	}

	private static List<Method> createMethods(Element klass) {
		List<arquitetura.touml.Method> methods = new ArrayList<arquitetura.touml.Method>();
		List<arquitetura.representation.Method> methodsClass = new ArrayList<arquitetura.representation.Method>();
		
		if(klass instanceof Class){
			methodsClass = ((Class) klass).getAllMethods();
		}else{
			methodsClass = ((Interface) klass).getOperations();
		}
		for (arquitetura.representation.Method method : methodsClass) {
			
			List<ParameterMethod> paramsMethod = method.getParameters();
			List<Argument> currentMethodParams = new ArrayList<Argument>();
			
			for (ParameterMethod param : paramsMethod) {
				currentMethodParams.add(Argument.create(param.getName(), Types.getByName(param.getType()), param.getDirection()));
			}
			
			if(method.isAbstract()){
				arquitetura.touml.Method m = arquitetura.touml.Method.create()
					  .withName(method.getName()).abstractMethod()
					  .withArguments(currentMethodParams)
					  .withConcerns(method.getOwnConcerns())
					  .withReturn(Types.getByName(method.getReturnType())).build();
				methods.add(m);
			}else{
				arquitetura.touml.Method m = arquitetura.touml.Method.create()
						  .withName(method.getName())
						  .withArguments(currentMethodParams)
						  .withConcerns(method.getOwnConcerns())
						  .withReturn(Types.getByName(method.getReturnType())).build();
				methods.add(m);
			}
				
				 
		}
		
		return methods;
	}

	private static List<arquitetura.touml.Attribute> createAttributes(Operations op, Class klass) {
		List<arquitetura.touml.Attribute> attributes = new ArrayList<arquitetura.touml.Attribute>();
		
		for(Attribute attribute : klass.getAllAttributes()){
			arquitetura.touml.Attribute attr = arquitetura.touml.Attribute.create()
					 .withName(attribute.getName())
					 .withConcerns(attribute.getOwnConcerns())
					 .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
					 .withType(Types.getByName(attribute.getType()));
			
			attributes.add(attr);
		}
		
		return attributes;
	}

}
