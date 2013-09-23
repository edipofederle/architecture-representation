package arquitetura.representation;

/**
 * Representa parametro de um método.
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class ParameterMethod {
	
	private String name;
	private String type;
	private String direction;
	
	public ParameterMethod(String name, String type, String direction) {
		this.name = name;
		this.type = type;
		this.direction = direction;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public String getDirection(){
		return direction;
	}

}
