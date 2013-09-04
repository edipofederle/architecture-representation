package arquitetura.representation;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Concern {

	private String name;

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

	public void updateConcernsList(String newName) {
		this.name += ","+newName;
	}
}