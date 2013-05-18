package mestrado.arquitetura.representation;


/**
 * Essa classe representa uma variant, contem os atributos referentes
 * as variants:<br/>
 * <ul>
 * 	<li>mandatory</li>
 *  <li>optional</li>
 *  <li>alternative_OR</li>
 *  </li>alternative_XOR</li>
 * </ul> 
 * @author edipofederle
 *
 */
public class Variant{
	
	private String name;
	private String rootVP;
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
	
	
	public Variant build() {
		return this;
	}
	
}