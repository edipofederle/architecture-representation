package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Architecture {
	
	private List<Element> elements = new ArrayList<Element>();
	private HashMap<String, Concern> concerns = new HashMap<String, Concern>();
	
	private String name;
	
	public Architecture(String name){
		setName(name);
	}
	
	public void setName(String name) {
		this.name = name;
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
		for (Element element : elements) {
			if(element.getTypeElement().equals("package"))
				paks.add(((Package)element));
		}
		return paks;
	}

	public List<Class> getClasses(){
		List<Class> paks = new ArrayList<Class>();
		for (Element element : elements) {
			if(element.getTypeElement().equals("klass"))
				paks.add(((Class)element));
		}
		return paks;
	}
}