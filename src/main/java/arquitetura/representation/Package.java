package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;


/**
 * 
 * {@link Package} representa um elemento Pacote dentro da arquitetura.
 * 
 * Pacotes podem conter {@link Class}'s {@link Interface} e outros {@link Package}'s
 * 
 * 
 * @author edipofederle <edipofederle@gmail.com>
 *
 */
public class Package extends Element {

	private static final long serialVersionUID = -3080328928563871488L;

	private Set<Element> elements = new HashSet<Element>();
	private final Set<Interface> implementedInterfaces = new HashSet<Interface>();
	private final Set<Interface> requiredInterfaces = new HashSet<Interface>();
	
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
	}
	
	public Package(Architecture architecture, String name) {
		this(architecture, name, null, UtilResources.createNamespace(architecture.getName(), name), UtilResources.getRandonUUID());
		architecture.addElement(this);
	}

	/**
	 * 
	 * Retorna todos os elementos que pertencem a um Pacote.
	 * 
	 * Esses elementos podem ser do tipo {@link Class}, {@link Package} e {@link Interface}.
	 * 
	 * @return List<{@link Element}>
	 */
	public Collection<Element> getElements() {
		return elements;
	}
	
	/**
	 * Retorna todas as {@link Interface}  do pacote.
	 * 
	 * @return
	 */
	public List<Interface> getAllInterfaces(){
		List<Interface> allInterfaces = new ArrayList<Interface>();
		for (Element element : this.elements) {
			if(element instanceof Interface)
				allInterfaces.add((Interface)element);
		}
		
		return allInterfaces;
	}
	/**
	 * Retorna todas {@link Class} que pertencem ao pacote. 
	 * 
	 * @return List<{@link Class}>
	 */
	public Set<Class> getClasses(){
		Set<Class> classes = new HashSet<Class>();
		
		for (Element element : this.getElements())
			if(element instanceof Class)
				classes.add((Class) element);

		return classes;
	}
	
	/**
	 * Retorna todos {@link Package} dentro do pacote em questão.
	 * 
	 * @return List<{@link Package}>
	 */
	public List<Package> getNestedPackages(){
		List<Package> paks = new ArrayList<Package>();
		
		for (Element element : elements)
			if(element.getTypeElement().equals("package")){
				if(UtilResources.extractPackageName(((Package)element).getNamespace()).equalsIgnoreCase(this.getName()))
					paks.add(((Package)element));	
			}
		
		return paks;
	}

	public void addImplementedInterface(Element interfacee) {
		implementedInterfaces.add((Interface) interfacee);
	}
	
	public Collection<Interface> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void addRequiredInterface(Interface interfacee) {
		requiredInterfaces.add(interfacee);
	}

	public Collection<Interface> getRequiredInterfaces() {
		return requiredInterfaces;
	}

	/**
	 * Cria uma classe e adiciona no pacote em questão.
	 * 
	 * @param className - Nome da classe
	 * @param isAbstract - abstrata ou não
	 * @return {@link Class}
	 * @throws Exception
	 */
	public Class createClass(String className, boolean isAbstract) throws Exception {
		Class c = new Class(getArchitecture(), className, isAbstract, this.getName());
		this.elements.add(c);
		return c;
	}

	@Override
	public List<Concern> getAllConcerns() {
		List<Concern> concerns = new ArrayList<Concern>();
		
		for (Class class_ : getClasses()) 
			concerns.addAll(class_.getAllConcerns());
		for (Interface interfc:getImplementedInterfaces())
			concerns.addAll(interfc.getAllConcerns());
		
		return concerns;
	}

	public void setElements(List<? extends Element> elements) {
		for (Element element : elements)
			getElements().add(element);
	}

	public void moveClassToPackage(Element klass, Package packageToMove) {
		if (!elements.contains(klass)) return;
		
		removeClass(klass);
		packageToMove.addExternalClass(klass);
	}

	public void addExternalClass(Element klass) {
		elements.add(klass);
	}
	
	public void removeClass(Element klass) {
		this.elements.remove(klass);
		
		for (Iterator<Relationship> i = getArchitecture().getAllRelationships().iterator(); i.hasNext();) {
			Relationship r = i.next();
			if(r instanceof GeneralizationRelationship){
				if(((GeneralizationRelationship) r).getParent().equals(klass) || ((GeneralizationRelationship) r).getChild().equals(klass)){
					i.remove();
				}
			}
			if(r instanceof RealizationRelationship){
				if(((RealizationRelationship) r).getClient().equals(klass) || ((RealizationRelationship) r).getSupplier().equals(klass)){
					i.remove();
				}
			}
			if(r instanceof DependencyRelationship){
				if(((DependencyRelationship) r).getClient().equals(klass) || ((DependencyRelationship) r).getSupplier().equals(klass)){
					i.remove();
				}
			}
			if(r instanceof AbstractionRelationship){
				if(((AbstractionRelationship) r).getClient().equals(klass) || ((AbstractionRelationship) r).getSupplier().equals(klass)){
					i.remove();
				}
			}
			if(r instanceof AssociationRelationship){
				for(AssociationEnd a : ((AssociationRelationship)r).getParticipants()){
					if(a.getCLSClass().equals(klass))
						i.remove();
				}
			}
		}
	
	}
	
	public boolean removeImplementedInterface(Interface interface_) {
		if (!implementedInterfaces.contains(interface_)) return false;
		implementedInterfaces.remove(interface_);
		return true;
	}

}
