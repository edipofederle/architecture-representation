package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * Essa classe representa uma variant, contem os atributos referentes
 * as variants:<br/>
 * <ul>
 * 	<li>Mandatory</li>
 * </ul> 
 * @author edipofederle
 *
 */
public class Variant {
	
	private VariantType variantType;
	private String rootVP;
	List<String> variabilities = new ArrayList<String>();
	/**
	 * @return the variantType
	 */
	public VariantType getVariantType() {
		return variantType;
	}
	/**
	 * @param variantType the variantType to set
	 */
	public void setVariantType(VariantType variantType) {
		this.variantType = variantType;
	}
	/**
	 * @return the rootVP
	 */
	public String getRootVP() {
		return rootVP;
	}
	/**
	 * @param rootVP the rootVP to set
	 */
	public void setRootVP(String rootVP) {
		this.rootVP = rootVP;
	}
	/**
	 * @return the variabilities
	 */
	public List<String> getVariabilities() {
		return variabilities;
	}
	/**
	 * @param variabilities the variabilities to set
	 */
	public void setVariabilities(List<String> variabilities) {
		this.variabilities = variabilities;
	}
	
}