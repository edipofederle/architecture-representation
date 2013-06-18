package arquitetura.representation;


import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class VariationPoint {

	private final Element variationPointElement;
	private final List<Variant> variants;
	private final List<Variability> variabilities = new ArrayList<Variability>();
	private final String bindingTime;
	
	/**
	 * 
	 * @param variationPointElement {@link Element}
	 * @param variants {@link List}<{@link Element}>
	 * @param bindingTime 
	 * @param type 
	 */
	public VariationPoint(Element variationPointElement, List<Variant> variants, String bindingTime) { //TODO VER SOBRE TIPO
		if (variationPointElement == null)
			throw new InvalidParameterException("A variation point must have an element");
		this.variationPointElement = variationPointElement;
		this.variationPointElement.setVariationPoint(this);
		this.variants = variants;
		this.bindingTime = bindingTime;
	}

	public Element getVariationPointElement() {
		return variationPointElement;
	}

	public List<Variant> getVariants() {
		return variants;
	}
	
	public int getNumberOfVariants(){
		return this.variants.size();
	}
	
	/**
	 * @return the variabilities
	 */
	public List<Variability> getVariabilities() {
		return variabilities;
	}

	/**
	 * @return the bindingTime
	 */
	public String getBindingTime() {
		return bindingTime;
	}

//	@Override
//	public String toString() {
//		StringBuilder builder = new StringBuilder();
//				
//		if (!getVariants().isEmpty()) {
//			builder.append("Variants: ");
//			Element[] variantsArray = getVariants().toArray(new Element[0]);
//			for (int i = 0; i < variantsArray.length; i++) {
//				if (i > 0)
//					builder.append(", ");
//				builder.append(variantsArray[i].getName());
//			}
//		}
//		return builder.toString();
//	}
}
