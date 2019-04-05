package exceotions;

public class CPFNotValidException extends Exception {
	
	public CPFNotValidException() {
		super("CPF Inválido!");
	}
	
	public CPFNotValidException(String msg) {
		super(msg);
	}

}
