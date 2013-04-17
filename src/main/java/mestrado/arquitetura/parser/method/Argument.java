package mestrado.arquitetura.parser.method;


public class Argument {
	
	String name;
	Types type;	
	
	public Argument(String name, Types type){
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type.getName();
	}
	
}