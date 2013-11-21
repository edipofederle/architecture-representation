package arquitetura.representation.relationship;

import java.io.Serializable;

import arquitetura.representation.Element;

/**
 * MemberEnd usada nas associações do tipo AssociationClass
 * 
 * Each end represents participation of instances of the classifier connected to the end in links of the association (Papyrus info)
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class MemberEnd implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6273724849151075349L;
	private String aggregation;
	private Multiplicity multiplicity;
	private String visibility;
	private Element type;
	
	public MemberEnd(String aggregation, Multiplicity multiplicity,	String visibility, Element type) {
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
	public Element getType() {
		return type;
	}
	public void setType(Element type) {
		this.type = type;
	}
	
}
