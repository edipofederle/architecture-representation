package mestrado.arquitetura.representation;


import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Variability {

	private String name;
	private String minSelection;
	private String maxSelection;
	private boolean allowsAddingVar;
	
	private String variabilityOwner; 
	
	private final List<VariationPoint> variationPoints = new ArrayList<VariationPoint>();
	private final Map<String, String> attributes = new HashMap<String, String>();
	
	public Variability(String name, String minSelection, String maxSelection, boolean allowsAddingVar, Map<String, String> attributes, String elementOwner) {
		setName(name);
		setMinSelection(minSelection);
		setMaxSelection(maxSelection);
		setAllowsAddingVar(allowsAddingVar);
		setElementOwner(elementOwner);
		this.getAttributes().putAll(attributes);
	}

	private void setElementOwner(String elementOwner) {
		this.variabilityOwner = elementOwner;
	}

	public void addVariationPoint(VariationPoint variationPoint) {
		if (variationPoint != null)
			getVariationPoints().add(variationPoint);
	}

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		if (name == null || name.length() == 0)
			throw new InvalidParameterException("Variability name can't be empty");
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (Entry<String, String> attribute : getAttributes().entrySet())
			builder.append(attribute.getKey() + ": " + attribute.getValue() + "\n");

		builder.append("MaxSelection: " + getMaxSelection() + "\n");
		builder.append("MinSelection: " + getMinSelection() + "\n");
		builder.append("AllowsAddingVar: " + allowAddingVar() + "\n");
		
		return builder.toString();
	}

	public String generateNote(VariationPoint variationPoint) {
		return String.format("%1s%2s", toString(), variationPoint.toString());
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public String getOwnerClass() {
		return variabilityOwner;
	}
}
