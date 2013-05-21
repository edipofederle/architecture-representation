package mestrado.arquitetura.writer;

/**
 * Classe usada para definir o estereótipo Variability para ser setado em uma note.(Comment)
 * 
 * @author edipofederle
 *
 */
public class VariabilityStereotype  implements Stereotype{
	
	private String name;
	private int minSelection;
	private int maxSelection;
	private boolean allowAddingVar;
	private String bindingTime;

	public VariabilityStereotype(int minSelection, int maxSelecion, boolean allowAddingVar, String bindingTime) {
		setName("variability");
		setMinSelection(minSelection);
		setMaxSelection(maxSelecion);
		setAllowAddingVar(allowAddingVar);
		setBindingTime(bindingTime);
	}

	private void setBindingTime(String bindingTime) {
		this.bindingTime = bindingTime;
	}

	private void setAllowAddingVar(boolean allowAddingVar) {
		this.allowAddingVar = allowAddingVar;
	}

	private void setMaxSelection(int maxSelecion) {
		this.maxSelection = maxSelecion;
	}

	private void setMinSelection(int minSelection) {
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
	public int getMinSelection() {
		return minSelection;
	}

	/**
	 * @return the maxSelection
	 */
	public int getMaxSelection() {
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
	
	
}
