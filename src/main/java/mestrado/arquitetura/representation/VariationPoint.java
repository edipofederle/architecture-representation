package mestrado.arquitetura.representation;


import java.security.InvalidParameterException;
import java.util.List;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class VariationPoint {

	private final Element variationPointElement;
	private final List<Element> variants;

	/**
	 * 
	 * @param variationPointElement {@link Element}
	 * @param variants {@link List}<{@link Element}>
	 */
	public VariationPoint(Element variationPointElement, List<Element> variants) {
		if (variationPointElement == null)
			throw new InvalidParameterException("A variation point must have an element");
		this.variationPointElement = variationPointElement;
		this.variants = variants;
	}

	public Element getVariationPointElement() {
		return variationPointElement;
	}

	public List<Element> getVariants() {
		return variants;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
				
		if (!getVariants().isEmpty()) {
			builder.append("Variants: ");
			Element[] variantsArray = getVariants().toArray(new Element[0]);
			for (int i = 0; i < variantsArray.length; i++) {
				if (i > 0)
					builder.append(", ");
				builder.append(variantsArray[i].getName());
			}
		}
		return builder.toString();
	}
}
