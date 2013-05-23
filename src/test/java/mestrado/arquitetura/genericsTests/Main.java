package mestrado.arquitetura.genericsTests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.api.touml.BindingTime;
import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.api.touml.Operations;
import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.io.ReaderConfig;
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

public class Main {

	/**
	 * @param args
	 * @throws ModelIncompleteException 
	 * @throws ModelNotFoundException 
	 */
	public static void main(String[] args) throws ModelNotFoundException, ModelIncompleteException {
		
		System.out.println("Start....");
		
		Architecture a;
		DocumentManager doc = givenADocument("TesteComEstereotipo");
		String path = new File("src/test/java/resources/edipo.uml").getAbsolutePath(); 
		Operations op = new Operations(doc);
		
		
		try {
			a = new ArchitectureBuilder().create(path);
			
			List<Class> classes = a.getAllClasses();
			
			for (Class class1 : classes) {
				
				List<mestrado.arquitetura.parser.method.Attribute> attributes = new ArrayList<mestrado.arquitetura.parser.method.Attribute>();
				List<Attribute> attrs = class1.getAllAttributes();
				for (Attribute attribute : attrs) {
					mestrado.arquitetura.parser.method.Attribute attr = mestrado.arquitetura.parser.method.Attribute.create()
							 .withName(attribute.getName())
							 .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
							 .withType(Types.getByName(attribute.getType()));
					
					attributes.add(attr);
				}
				if(!attributes.isEmpty()){
					String id = op.forClass().createClass(class1.getName()).withAttribute(attributes).build().get("id");
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
				 * Isso é necesário pois quando usamos os métodos para gerar os elementos esses geram um id novo,	
				 * diante disso ambos ids são diferebtes e não é possível localizar os elementos nos XMIS para criar os relacionamentos.
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
			
			//Remove classes qeu não tenham relacionamentos
			for(GeneralizationRelationship g : a.getAllGeneralizations()){
				op.forGeneralization().createRelation("Ge").between(g.getChild().getId()).and(g.getParent().getId()).build();
			}
			
			
		} catch (Exception e) {
			System.out.println("Ops!. Error, I am sorry: " + e.getMessage());
		}
		
		System.out.println("\nDone. Architecture save into: " + ReaderConfig.getDirExportTarget()+doc.getNewModelName());
		
	}
	
	private static DocumentManager givenADocument(String outputModelName) {
		String pathToFiles = "/Users/edipofederle/sourcesMestrado/arquitetura/src/main/java/mestrado/arquitetura/api/touml/1/";// model padrao vazio que o programa usa para construir o novo
		DocumentManager documentManager = new DocumentManager(outputModelName, pathToFiles);
		
		return documentManager;
	}

}
