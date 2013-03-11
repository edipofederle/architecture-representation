package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * {@link Package} representa um elemento Pacote dentro da arquitetura.
 * 
 * Pacotes podem conter {@link Class}'s e outros {@link Package}'s
 * 
 * 
 * @author edipofederle
 *
 */
public class Package extends Element {

	private List<Element> elements = new ArrayList<Element>();
	private final List<Class> implementedInterfaces = new ArrayList<Class>();
	private final List<Class> requiredInterfaces = new ArrayList<Class>();
	
	/**
	 * Construtor Para um Elemento do Tipo Pacote
	 * 
	 * @param architecture - A qual arquitetura pertence
	 * @param name - Nome do Pacote
	 * @param isVariationPoint - Se o mesmo é um ponto de variação
	 * @param variantType - Qual o tipo ( {@link VariantType} ) da variante
	 * @param parent - Qual o {@link Element} pai
	 */
	public Package(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, Element parent) {
		super(architecture, name, isVariationPoint, variantType, "package", parent);
	}
	
	/**
	 * 
	 * Retorna todos os elementos que pertencem a um Pacote.
	 * 
	 * Esses elementos podem ser do tipo {@link Class} e/ou {@link Package}.
	 * 
	 * @return List<{@link Element}>
	 */
	public List<Element> getElements() {
		return elements;
	}
	
	/**
	 * Retorna todas as classes ({@link Class}) que pertencem ao pacote. 
	 * 
	 * @return List<{@link Class}>
	 */
	public List<Class> getClasses(){
		List<Class> paks = new ArrayList<Class>();
		
		for (Element element : elements)
			if(element.getTypeElement().equals("klass"))
				paks.add(((Class)element));

		return paks;
	}
	
	/**
	 * Retorna todos pacotes dentro do pacote em questão.
	 * 
	 * @return List<{@link Package}>
	 */
	public List<Package> getNestedPackages(){
		List<Package> paks = new ArrayList<Package>();
		
		for (Element element : elements)
			if(element.getTypeElement().equals("package"))
				paks.add(((Package)element));
		
		return paks;
	}

	public void addImplementedInterface(Class interfacee) {
		implementedInterfaces.add(interfacee);
	}

	public void addRequiredInterface(Class interfacee) {
		requiredInterfaces.add(interfacee);
	}
}
