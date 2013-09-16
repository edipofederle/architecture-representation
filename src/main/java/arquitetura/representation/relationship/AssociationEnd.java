package arquitetura.representation.relationship;

import arquitetura.representation.Class;
import arquitetura.representation.Element;


/**
 * An association end specifies the role that the object at one end of a relationship performs. 
 * Each end of a relationship has properties that specify the role of the association end,
 * its multiplicity, visibility, navigability, and constraints.
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationEnd {

	private Element klass;
	private boolean isNavigable;
	//TODO Tipo da associação mudar nome da variavel
	private String aggregation;
	private Multiplicity multiplicity;

	public AssociationEnd(Element klass, boolean isNavigable, String aggregation, Multiplicity multiplicity) {
		setCLSClass(klass);
		setNavigable(isNavigable);
		setAggregation(aggregation);
		setMultiplicity(multiplicity);
	}

	private void setMultiplicity(Multiplicity multiplicity) {
		this.multiplicity = multiplicity;
	}

	public AssociationEnd(Element klass) {
		this(klass, false, "", null);
	}

	public Element getCLSClass() {
		return klass;
	}

	public void setCLSClass(Element c) {
		this.klass = c;
	}

	public boolean isNavigable() {
		return isNavigable;
	}

	private void setNavigable(boolean isNavigable) {
		this.isNavigable = isNavigable;
	}

	/**
	 * Retorna o tipo da associação.<br />
	 * Tipos:
	 * 	
	 * 	<ul>
	 * 		<li>Composiçãao - Composite</li>
	 * 		<li>Agregação - Shared</li>
	 *  </ul>
	 *  
	 * @return
	 */
	public String getAggregation() {
		if("shared".equalsIgnoreCase(aggregation))
			return "Aggregation";
		else if("composite".equalsIgnoreCase(aggregation))
			return "Composition";
		else
			return "none";
		
	}

	private void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}
	
	public void replaceCLSClass(Class c) {
		setCLSClass(c);
	}

	public Multiplicity getMultiplicity() {
		return multiplicity;
	}

	public boolean isComposite() {
		return getAggregation().equals("Composition");
	}

	public boolean isAggregation() {
		return getAggregation().equals("Aggregation");
	}
	
}
