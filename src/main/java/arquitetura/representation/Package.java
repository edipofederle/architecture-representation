package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import arquitetura.exceptions.ConcernCannotBeAppliedToPackagesException;
import arquitetura.helpers.UtilResources;


/**
 * 
 * {@link Package} representa um elemento Pacote dentro da arquitetura.
 * 
 * Pacotes podem conter {@link Class}'s e outros {@link Package}'s
 * 
 * 
 * @author edipofederle <edipofederle@gmail.com>
 *
 */
public class Package extends Element {

	private List<Element> elements = new ArrayList<Element>();
	
	private final List<Interface> implementedInterfaces = new ArrayList<Interface>();
	private final List<Interface> requiredInterfaces = new ArrayList<Interface>();
	private List<String> idsClasses = new ArrayList<String>();

	private String widht;
	private String height;
	private String x;
	private String y;
	
	/**
	 * Construtor Para um Elemento do Tipo Pacote
	 * 
	 * @param architecture - A qual arquitetura pertence
	 * @param name - Nome do Pacote
	 * @param isVariationPoint - Se o mesmo é um ponto de variação
	 * @param variantType - Qual o tipo ( {@link VariantType} ) da variante
	 * @param parent - Qual o {@link Element} pai
	 */
	public Package(Architecture architecture, String name, Variant variantType,  String namespace, String id) {
		super(architecture, name,  variantType, "package", namespace, id);
		
//		//Posição Original
//		this.x = XmiHelper.getXValueForElement(id);
//		this.y = XmiHelper.getYValueForElement(id);
//		
//		this.widht = XmiHelper.getWidhtForPackage(id);
//		this.height =	XmiHelper.getHeightForPackage(id);
	}
	
	public Package(Architecture architecture, String name, String id) {
		this(architecture, name, null, UtilResources.createNamespace(architecture.getName(), name) , id); //receber id
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
	 * @return the widht
	 */
	public String getWidht() {
		return widht;
	}

	/**
	 * @return the x
	 */
	public String getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public String getY() {
		return y;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * Retorna todas as classes ({@link Class}) que pertencem ao pacote. 
	 * 
	 * @return List<{@link Class}>
	 */
	public List<Class> getClasses(){
		List<Class> classes = new ArrayList<Class>();
		
		for (Element element : getArchitecture().getElements())
			if(UtilResources.extractPackageName(element.getNamespace()).equalsIgnoreCase(this.getName()))
				classes.add((Class) element);

		return classes;
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

	public void addImplementedInterface(Element interfacee) {
		implementedInterfaces.add((Interface) interfacee);
	}
	
	/**
	 * @return the implementedInterfaces
	 */
	public List<Interface> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void addRequiredInterface(Interface interfacee) {
		requiredInterfaces.add(interfacee);
	}

	public List<String> getAllClassIdsForThisPackage() {
		return idsClasses;
	}
	
	/**
	 * @return the requiredInterfaces
	 */
	public List<Interface> getRequiredInterfaces() {
		return requiredInterfaces;
	}

	public Class createClass(String className) throws Exception {
		String idClass = UtilResources.getRandonUUID();
		Class c = new Class(getArchitecture(), className, idClass);
		idsClasses.add(idClass);
		return c;
	}
	/**
	 * Eu sei, eu sei,  princípio da substituição de Liskov.
	 * Desculpe :D 
	 */
	@Override
	public Collection<Concern> getAllConcerns() {
		 new ConcernCannotBeAppliedToPackagesException();
		return null;
	}

	public void setElements(List<? extends Element> elements) {
		for (Element element : elements) {
			if (isPackageClassOrInterface(element)){
				getElements().add(element);
			}
		}
	}

	private boolean isPackageClassOrInterface(Element element) {
		if ((element instanceof Package) || (element instanceof Interface) || (element instanceof Class))
			return true;
		return false;
	}

	public void moveClassToPackage(Class klass, Package packageToMove) {
		if (!idsClasses.contains(klass.getId())) return;
		
		removeClass(klass);
		packageToMove.addExternalClass(klass);
	}

	private void addExternalClass(Class klass) {
		idsClasses.add(klass.getId());
	}
	
	public void removeClass(Class klass) {
		idsClasses.remove(klass.getId());
		getArchitecture().getAllClasses().remove(klass);
	}
	
	public boolean removeImplementedInterface(Interface interface_) {
		if (!implementedInterfaces.contains(interface_)) return false;
		
		implementedInterfaces.remove(interface_);
		return true;
	}

	
	

}
