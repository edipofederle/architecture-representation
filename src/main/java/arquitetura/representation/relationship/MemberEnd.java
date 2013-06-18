package arquitetura.representation.relationship;

import arquitetura.representation.Class;

/**
 * MemberEnd usada nas associações do tipo AssociationClass
 * 
 * Each end represents participation of instances of the classifier connected to the end in links of the association (Papyrus info)
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class MemberEnd {
	
	private String aggregation;
	private Multiplicity multiplicity;
	private String visibility;
	private Class type;
	
	public MemberEnd(String aggregation, Multiplicity multiplicity,	String visibility, Class type) {
		super();
		this.aggregation = aggregation;
		this.multiplicity = multiplicity;
		this.visibility = visibility;
		this.type = type;
	}
	
	public String getAggregation() {
		return aggregation;
	}
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}
	public Multiplicity getMultiplicity() {
		return multiplicity;
	}
	public void setMultiplicity(Multiplicity multiplicity) {
		this.multiplicity = multiplicity;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	
}
