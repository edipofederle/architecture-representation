package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Class;

public class AssociationClassRelationship extends	Relationship {

	public String name;
	public List<Class> memebersEnd = new ArrayList<Class>();
	private Class ownedEnd;

	public AssociationClassRelationship(String name, List<Class> ends, Class ownedEnd) {
		super();
		this.name = name;
		this.memebersEnd = ends;
		this.ownedEnd = ownedEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Class> getMemebersEnd() {
		return memebersEnd;
	}

	public void setMemebersEnd(List<Class> memebersEnd) {
		this.memebersEnd = memebersEnd;
	}

	/**
	 * Retorna a {@link Class } dona da AssociationClass.
	 * 
	 * @return
	 */
	public Class getOwnedEnd() {
		return ownedEnd;
	}

}