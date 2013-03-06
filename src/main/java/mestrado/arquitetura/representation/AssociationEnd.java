package mestrado.arquitetura.representation;


/**
 * An association end specifies the role that the object at one end of a relationship performs. 
 * Each end of a relationship has properties that specify the role of the association end,
 * its multiplicity, visibility, navigability, and constraints.
 * @author edipofederle
 *
 */
public class AssociationEnd {

	private Class klass;
	private boolean isNavigable;
	private String aggregation;
	private Multiplicity multiplicity;

	public AssociationEnd(Class klass, boolean isNavigable, String aggregation, Multiplicity multiplicity) {
		setCLSClass(klass);
		setNavigable(isNavigable);
		setAggregation(aggregation);
		setMultiplicity(multiplicity);
	}

	private void setMultiplicity(Multiplicity multiplicity) {
		this.multiplicity = multiplicity;
	}

	public AssociationEnd(Class klass) {
		this(klass, false, "", null);
	}

	public Class getCLSClass() {
		return klass;
	}

	public void setCLSClass(Class c) {
		this.klass = c;
	}

	public boolean isNavigable() {
		return isNavigable;
	}

	private void setNavigable(boolean isNavigable) {
		this.isNavigable = isNavigable;
	}

	public String getAggregation() {
		return "shared".equalsIgnoreCase(aggregation) ? "Aggregation" : aggregation;
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
	
}
