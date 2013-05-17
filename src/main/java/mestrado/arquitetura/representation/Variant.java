package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.Arrays;
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
public class Variant implements Stereotype{
	
	private String name;
	private String rootVP;
	List<String> variabilities = new ArrayList<String>();
	/**
	 * @return the variantType
	 */
	public String getVariantName() {
		return name;
	}
	/**
	 * @param variantType the variantType to set
	 */
	private void setVariantName(String name) {
		this.name = name;
	}
	/**
	 * @return the rootVP
	 */
	public String getRootVP() {
		return this.rootVP;
	}
	/**
	 * @param rootVP the rootVP to set
	 */
	private void setRootVP(String rootVP) {
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
	
	public Variant withName(String name) {
		setVariantName(name);
		return this;
	}
	public static Variant createVariant() {
		return new Variant();
	}
	
	/**
	 * rootVP, representa o ponto de variação ao qual está associado 
	 * 
	 * @param rootVP
	 * @return
	 */
	public Variant andRootVp(String rootVP) {
		setRootVP(rootVP);
		return this;
	}
	
	/**
	 * 
	 * @param variabilities
	 * @return
	 */
	public Variant andVariabilities(String ... variabilities) {
		this.setVariabilities(Arrays.asList(variabilities));
		return this;
	}
	public Variant build() {
		return this;
	}
	
}