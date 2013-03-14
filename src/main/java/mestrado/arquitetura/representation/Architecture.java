package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author edipofederle
 *
 */
public class Architecture {
	
	private List<Element> elements = new ArrayList<Element>();
	private HashMap<String, Concern> concerns = new HashMap<String, Concern>();
	private List<Variability> variabilities = new ArrayList<Variability>();
	
	private List<InterClassRelationship> interClassRelationships = new ArrayList<InterClassRelationship>();
	private List<InterElementRelationship> interElementRelationships = new ArrayList<InterElementRelationship>();
	
	private String name;
	
	public Architecture(String name){
		setName(name);
	}
	
	public void setName(String name) {
		this.name= name != null ? name : "";
	}
	
	public String getName(){
		return name;
	}

	public List<Element> getElements() {
		return elements;
	}
	
	public Concern getOrCreateConcernByName(String name){
		Concern concern = concerns.get(name.toLowerCase());
		if (concern == null){
			concern = new Concern(name);
			concerns.put(name.toLowerCase(), concern);
		}
		
		return concern;
	}

	public HashMap<String, Concern> getConcerns() {
		return concerns;
	}

	public List<Package> getPackages() {
		List<Package> paks = new ArrayList<Package>();
		
		for (Element element : elements) 
			if(element.getTypeElement().equals("package"))
				paks.add(((Package)element));
		
		if(paks.isEmpty()) return Collections.emptyList();
		return paks;
	}

	public List<Class> getClasses(){
		List<Class> klasses = new ArrayList<Class>();
		
		for (Element element : elements) 
			if(element.getTypeElement().equals("klass"))
				klasses.add(((Class)element));

		if(klasses.isEmpty()) return Collections.emptyList();
		return klasses;
	}
	
	public List<Variability> getVariabilities() {
		return variabilities;
	}
	
	public List<InterClassRelationship> getInterClassRelationships() {
		return interClassRelationships;
	}

	public void setInterClassRelationships(List<InterClassRelationship> interClassRelationships) {
		this.interClassRelationships = interClassRelationships;
	}

	//TODO refatorar para buscar todo tipo de elemento
	public Element findElementByName(String name) {
		List<Class> klasses = getClasses();
		List<Package> packages = getPackages();
		
		for (Class klass : klasses)
			if(foundElement(name, klass))
				return klass;
		
		for (Package package1 : packages)
			if(foundElement(name, package1))
				return package1;
			
		return null; //TODO Verifica Null
	}

	private <T> boolean foundElement(String name, Element element) {
		return name.equalsIgnoreCase(element.getName());
	}

	public List<InterElementRelationship> getInterElementRelationships() {
		return interElementRelationships;
	}

	public List<Class> getAllInterfaces() {
		List<Class> klasses = new ArrayList<Class>();
		
		for (Element element : elements) 
			if(element.getTypeElement().equals("klass"))
				if(((Class)element).isInterface())
					klasses.add(((Class)element));

		if(klasses.isEmpty()) return Collections.emptyList();
		return klasses;
		
	}

}