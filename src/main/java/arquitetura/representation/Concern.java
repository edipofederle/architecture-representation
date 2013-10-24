package arquitetura.representation;

import java.io.Serializable;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Concern implements Serializable {

	private static final long serialVersionUID = -1282322275180618366L;
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