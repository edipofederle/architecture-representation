package mestrado.arquitetura.representation;

/**
 * Representa parametro de um método.
 * 
 * @author edipofederle
 *
 */
public class ParameterMethod {
	
	private String name;
	private String type;
	
	public ParameterMethod(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
