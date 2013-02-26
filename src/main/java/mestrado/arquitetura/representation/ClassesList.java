package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

public class ClassesList {
	
	private final List<Class> classes = new ArrayList<Class>();

	public List<Class> getClasses() {
		return classes;
	}

	public void addAll(List<Class> classes2) {
		this.classes.addAll(classes2);
	}

	public Class get(int i) {
		return classes.get(i);
	}

	public Object size() {
		return classes.size();
	}
}