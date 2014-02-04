package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public abstract class Relationship {
	
	private String id;
	private String type;
	private String name;
	
	/**
	 * Estereótipos para os relacionamentos
	 * 
	 * São somente o nome do estereótipo, e devem estar presentes no arquivo de perfil (relationships.profile.uml).
	 * 
	 */
	private List<String> stereotypes = new ArrayList<String>();

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStereotypes(List<String> stereotypes){
		this.stereotypes = stereotypes;
	}
	
	public List<String> getStereotypes(){
		return this.stereotypes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relationship other = (Relationship) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}