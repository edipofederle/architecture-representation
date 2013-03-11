package mestrado.arquitetura.representation;

/**
 * 
 * @author edipofederle
 *
 */
public class Multiplicity {
	
	private String lowerValue;
	private String upperValue;
	
	public Multiplicity(String string, String string2) {
		this.lowerValue = string;
		this.upperValue = string2;
	}
	
	public String getLowerValue() {
		return lowerValue;
	}
	public void setLowerValue(String lowerValue) {
		this.lowerValue = lowerValue;
	}
	public String getUpperValue() {
		return upperValue;
	}
	public void setUpperValue(String upperValue) {
		this.upperValue = upperValue;
	}
	
	@Override
	public String toString(){
		return this.lowerValue + ".."  + this.upperValue;
	}

}