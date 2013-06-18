package arquitetura.representation;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Variability {

	private String name;
	private String minSelection;
	private String maxSelection;
	private String bindingTime;
	private boolean allowsAddingVar;
	
	private List<VariationPoint> variationPoints = new ArrayList<VariationPoint>();
	private List<Variant> variants = new ArrayList<Variant>();
	
	/**
	 * @param name
	 * @param minSelection
	 * @param maxSelection
	 * @param bidingTime 
	 * @param allowsAddingVar
	 * @param attributes
	 * @param elementOwner
	 */
	public Variability(String name, String minSelection, String maxSelection, String bindingTime, boolean allowsAddingVar) {
		setName(name);
		setMinSelection(minSelection);
		setMaxSelection(maxSelection);
		setAllowsAddingVar(allowsAddingVar);
		setBindingTime(bindingTime);
	}
	

	private void setBindingTime(String bindingTime) {
		this.bindingTime = bindingTime;
	}
	


	/**
	 * @return the bindingTime
	 */
	public String getBindingTime() {
		return bindingTime;
	}


	public void addVariationPoint(VariationPoint variationPoint) {
		if (variationPoint != null)
			getVariationPoints().add(variationPoint);
	}

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}

	public String getMinSelection() {
		return minSelection;
	}

	private void setMinSelection(String minSelection) {
		this.minSelection = minSelection;
	}

	public String getMaxSelection() {
		return maxSelection;
	}

	private void setMaxSelection(String maxSelection) {
		this.maxSelection = maxSelection;
	}

	public boolean allowAddingVar() {
		return allowsAddingVar;
	}

	private void setAllowsAddingVar(boolean allowsAddingVar) {
		this.allowsAddingVar = allowsAddingVar;
	}
	
	/**
	 * Retorna uma lista de {@link VariationPoint} para a {@link Variability} em questão.
	 * 
	 * @return {@link List}<{@link VariationPoint}>
	 */
	public List<VariationPoint> getVariationPoints() {
		return variationPoints;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Variability [name=" + name + ", minSelection=" + minSelection
				+ ", maxSelection=" + maxSelection + ", allowsAddingVar="
				+ allowsAddingVar + ", variationPoints=" + variationPoints
				+ ", variants=" + variants + "]";
	}

	public String generateNote(VariationPoint variationPoint) {
		return String.format("%1s%2s", toString(), variationPoint.toString());
	}


	public List<Variant> getVariants() {
		return this.variants;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variability other = (Variability) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}