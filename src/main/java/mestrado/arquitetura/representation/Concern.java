package mestrado.arquitetura.representation;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Concern {

	private final String name;

	public Concern(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}
}