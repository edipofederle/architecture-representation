package mestrado.arquitetura.representation;


/**
 * An association end specifies the role that the object at one end of a relationship performs. 
 * Each end of a relationship has properties that specify the role of the association end,
 * its multiplicity, visibility, navigability, and constraints.
 * @author edipofederle
 *
 */
public class AssociationEnd {

	private Class class_;
	private boolean isNavigable;
	private String aggregation;

	public AssociationEnd(Class class_, boolean isNavigable, String aggregation) {
		setCLSClass(class_);
		setNavigable(isNavigable);
		setAggregation(aggregation);
	}

	public AssociationEnd(Class class_) {
		this(class_, false, "");
	}

	public Class getCLSClass() {
		return class_;
	}

	public void setCLSClass(Class c) {
		this.class_ = c;
	}

	public boolean isNavigable() {
		return isNavigable;
	}

	private void setNavigable(boolean isNavigable) {
		this.isNavigable = isNavigable;
	}

	public String getAggregation() {
		return aggregation;
	}

	private void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}
	
	public void replaceCLSClass(Class c) {
		setCLSClass(c);
	}
}
