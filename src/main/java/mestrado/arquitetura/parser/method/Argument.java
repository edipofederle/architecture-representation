package mestrado.arquitetura.parser.method;

public class Argument {

	private final String name;
	private final Types.Type type;

	private Argument(String name, Types.Type type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Types.Type getType() {
		return type;
	}

	public static Argument create(String name, Types.Type type) {
		return new Argument(name, type);
	}
}