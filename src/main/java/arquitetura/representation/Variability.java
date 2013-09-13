package arquitetura.representation;


import java.util.ArrayList;
import java.util.Iterator;
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
	private String ownerClass; // Classe na qual a variabilidade esta ligada
	
	private VariationPoint variationPoint; 
	private List<Variant> variants = new ArrayList<Variant>();
	
	public Variability(String name, String minSelection, String maxSelection, String bindingTime, boolean allowsAddingVar, String ownerClass) {
		setName(name);
		setMinSelection(minSelection);
		setMaxSelection(maxSelection);
		setAllowsAddingVar(allowsAddingVar);
		setBindingTime(bindingTime);
		setOwner(ownerClass);
	}
	

	private void setOwner(String ownerClass) {
		this.ownerClass = ownerClass;
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
			this.variationPoint = variationPoint;
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
	 * Retorna o {@link VariationPoint} para a {@link Variability}.
	 * 
	 * @return {@link VariationPoint}
	 */
	public VariationPoint getVariationPoint() {
		return variationPoint;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Variability [name=" + name + ", minSelection=" + minSelection
				+ ", maxSelection=" + maxSelection + ", allowsAddingVar="
				+ allowsAddingVar + ", variationPoint=" + variationPoint
				+ ", variants=" + variants + "]";
	}

	public String generateNote(VariationPoint variationPoint) {
		return String.format("%1s%2s", toString(), variationPoint.toString());
	}


	public List<Variant> getVariants() {
		List<Variant> variantsNames = variants;
		Iterator<Variant> i = variantsNames.iterator();
		
		while (i.hasNext()) {
			Variant s = i.next();
			if(!s.getVariantType().equalsIgnoreCase("optional"))
				if (s.getName().equalsIgnoreCase(this.getOwnerClass())){ i.remove(); }
		}
		return variants;
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


	/**
	 * Retorna a classe na qual a Variabilidade pertence.
	 * 
	 * @return String - nome da classe
	 */
	public String getOwnerClass() {
		return ownerClass;
	}
	
}