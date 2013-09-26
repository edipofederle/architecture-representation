package arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.MemberEnd;
import arquitetura.representation.relationship.Multiplicity;
import arquitetura.representation.relationship.Relationship;

/**
 * Classe usada para realizar operações sobre Associations. Seu uso é feito da seguinte forma:<br/><br/>
 * 
 *  {@code  a.forAssociation().createAssociationEnd() }<br/>
	{@code	.withKlass(class1)}<br/>
	{@code	.withMultiplicity("1..*")}<br/>
	{@code	.navigable()}<br/>
	{@code	.and()}<br/>
	{@code	.createAssociationEnd()}<br/>
	{@code		      .withKlass(class2)}<br/>
	{@code	  .withMultiplicity("1..1)}<br/>
	{@code	.navigable().build();}<br/>
 *  
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class OperationsOverAssociation {

	private AssociationEnd associationEnd1;
	private AssociationRelationship association;
	private List<Relationship> relationships;
	private Architecture architecture;
	
	public OperationsOverAssociation(Architecture architecture){
		this.architecture = architecture;
		String id = UtilResources.getRandonUUID();
		association = new AssociationRelationship(id);
		this.relationships = architecture.getAllRelationships();
		architecture.getAllIds().add(id);
	}
	
	public OperationsOverAssociation createAssociationEnd() {
		associationEnd1 = new AssociationEnd();
		return this;
	}

	public OperationsOverAssociation withName(String name) {
		this.associationEnd1.setName(name);
		return this;
	}

	public OperationsOverAssociation withKlass(Class idclass1) {
		associationEnd1.setCLSClass(idclass1);
		return this;
	}

	public OperationsOverAssociation withMultiplicity(String multiplicity) {
		this.associationEnd1.setMultiplicity(new Multiplicity(multiplicity.split("\\..")[0], multiplicity.split("\\..")[1]));
		return this;
	}

	public OperationsOverAssociation navigable() {
		this.associationEnd1.setNavigable(true);
		return this;
	}

	public void build() {
		this.association.getParticipants().add(associationEnd1);
	}

	public OperationsOverAssociation and() {
		this.association.getParticipants().add(associationEnd1);
		this.relationships.add(association);
		return this;
	}

	public OperationsOverAssociation asComposition() {
		this.associationEnd1.setAggregation("composite");
		return this;
	}

	public void createAssociationClass(List<Attribute> listAttrs, List<Method> listMethods, Class owner, Class klass) {
		Class asClass = new Class(this.architecture, "AssociationClass", UtilResources.getRandonUUID());
		
		asClass.getAllAttributes().addAll(listAttrs);
		asClass.getAllMethods().addAll(listMethods);
		
		List<MemberEnd> ends = new ArrayList<MemberEnd>();
		ends.add(new MemberEnd("none", null, "public", owner));
		ends.add(new MemberEnd("none", null, "public", klass));
		
	
		AssociationClassRelationship asc = new AssociationClassRelationship(this.architecture,
																			"nameAssociation",
																			ends,
																			owner,
																			asClass.getId(),
																			null,
																			asClass);
		owner.getIdsRelationships().add(asClass.getId());
		klass.getIdsRelationships().add(asClass.getId());
		
		this.architecture.getAllAssociationsClass().add(asc);
		
	}

}
