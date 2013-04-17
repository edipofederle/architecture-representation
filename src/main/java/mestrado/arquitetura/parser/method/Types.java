package mestrado.arquitetura.parser.method;

public enum Types {
	
	INTEGER(1, "Integer"), STRING(2, "String"), DOUBLE(3, "Double"), LONG(4, "Long");
	
    private int value;
    private String name;
    
    private Types(int value, String name) {
    	this.value = value;
        this.name = name;
    }

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

}