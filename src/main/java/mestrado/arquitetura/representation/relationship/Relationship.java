package mestrado.arquitetura.representation.relationship;


/**
 * 
 * @author edipofederle
 *
 */
public abstract class Relationship {
	
	private String id;
	private String type;

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
	
	public void setTypeRelationship(String type){
		this.type = type;
	}
	
	public String getTypeRelationship(){
		return this.type;
	}
	
}