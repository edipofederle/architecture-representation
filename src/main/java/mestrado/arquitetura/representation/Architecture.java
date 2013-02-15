package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Architecture {
	
	private List<Package> packages = new ArrayList<Package>();
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

	public List<Package> getPackages() {
		return packages;
	}
	
	public Concern getOrCreateConcernByName(String name){
		Concern concern = concerns.get(name.toLowerCase());
		if (concern == null){
			concern = new Concern(name);
			concerns.put(name.toLowerCase(), concern);
		}
		return concern;
	}

}
