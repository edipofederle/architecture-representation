package mestrado.arquitetura.genericsTests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.parser.method.Types;
import mestrado.arquitetura.parser.method.VisibilityKind;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;

public class Main {

	/**
	 * @param args
	 * @throws ModelIncompleteException 
	 * @throws ModelNotFoundException 
	 */
	public static void main(String[] args) throws ModelNotFoundException, ModelIncompleteException {
		
		Architecture a;
		DocumentManager doc = givenADocument("TesteMain3", "simples");
		String path = new File("src/test/java/resources/testeLeitura2.uml").getAbsolutePath(); 
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
				/**
				 * Deve atualizar o id do elemento em memória com o id do elemente gerado.
				 * Isso é necesário pois quando usamos os métodos para gerar os elementos esses geram um id novo,	
				 * diante disso ambos ids são diferen	tes e não é possível localizar os elementos nos XMIS para criar os relacionamentos.
				 */
				
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
			for (Class class1 : classes) {
				if(class1.dontHaveAnyRelationship()){
					//op.forClass().removeClassById(class1.getId());
					op.forGeneralization().createRelation("Ge").between(class1.getId()).and(classes.get(0).getId()).build();
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static DocumentManager givenADocument(String outputModelName, String originalModelName) {
		String pathToFiles = "src/main/java/mestrado/arquitetura/parser/1/";
		DocumentManager documentManager = new DocumentManager(outputModelName, pathToFiles, originalModelName);
		
		return documentManager;
	}

}
