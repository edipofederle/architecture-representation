package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

public class Class {
	
	private final List<String> attributes = new ArrayList<String>();

	
	public void setAttribute(String attr){
		this.attributes.add(attr);
	}
	
	public List<String> getAttributes() {
		return attributes;
	}
	
}