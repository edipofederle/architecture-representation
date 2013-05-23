package mestrado.arquitetura.writer;

import java.util.List;

import mestrado.arquitetura.api.touml.Stereotype;

/**
 * Classe usada para definir o estereótipo Variability para ser setado em uma note.(Comment)
 * 
 * @author edipofederle
 *
 */
public class VariabilityStereotype  implements Stereotype{
	
	private String name;
	private String minSelection;
	private String maxSelection;
	private boolean allowAddingVar;
	private String bindingTime;
	private List<String> variants;

	public VariabilityStereotype(String minSelection, String maxSelecion, boolean allowAddingVar, String bindingTime, List<String> variants) {
		setName("variability");
		setMinSelection(minSelection);
		setMaxSelection(maxSelecion);
		setAllowAddingVar(allowAddingVar);
		setBindingTime(bindingTime);
		setVariants(variants);
	}

	private void setBindingTime(String bindingTime) {
		this.bindingTime = bindingTime;
	}

	private void setAllowAddingVar(boolean allowAddingVar) {
		this.allowAddingVar = allowAddingVar;
	}

	private void setMaxSelection(String maxSelecion) {
		this.maxSelection = maxSelecion;
	}

	private void setMinSelection(String minSelection) {
		this.minSelection = minSelection;
	}

	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the minSelection
	 */
	public String getMinSelection() {
		return minSelection;
	}

	/**
	 * @return the maxSelection
	 */
	public String getMaxSelection() {
		return maxSelection;
	}

	/**
	 * @return the allowAddingVar
	 */
	public boolean isAllowAddingVar() {
		return allowAddingVar;
	}

	/**
	 * @return the bindingTime
	 */
	public String getBindingTime() {
		return bindingTime;
	}

	/**
	 * @return the variants
	 */
	public List<String> getVariants() {
		return variants;
	}

	/**
	 * @param variants the variants to set
	 */
	public void setVariants(List<String> variants) {
		this.variants = variants;
	}
	
}