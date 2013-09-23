package arquitetura.representation.relationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
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
		return lowerValue == null ? "1" : lowerValue;
	}
	public void setLowerValue(String lowerValue) {
		this.lowerValue = lowerValue;
	}
	public String getUpperValue() {
		return upperValue == null ? "1" : upperValue;
	}
	public void setUpperValue(String upperValue) {
		this.upperValue = upperValue;
	}
	
	@Override
	public String toString(){
		
		if(bothEqualsOne()){
			return "1";
		}else if(isCompleteMultiplicty()){
			return this.lowerValue + ".."  + this.upperValue;
		}
		
		return "";
			
	}

	private boolean bothEqualsOne() {
		if(("1".equals(this.upperValue)) && ("1".equals(this.lowerValue)))
			return true;
		return false;
	}

	private boolean isCompleteMultiplicty() {
		if ((this.lowerValue == null) || ("".equalsIgnoreCase(this.lowerValue)) && ((this.upperValue == null)) || ("".equalsIgnoreCase(this.upperValue))){
			return false;
		}else{
			return true;
		}
	}

}