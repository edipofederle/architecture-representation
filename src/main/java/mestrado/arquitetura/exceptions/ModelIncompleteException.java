package mestrado.arquitetura.exceptions;

public class ModelIncompleteException extends Exception {

	private static final long serialVersionUID = -607431717512976439L;
	
	public ModelIncompleteException(String message){
		super(message);
	}

}