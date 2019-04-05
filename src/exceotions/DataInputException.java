package exceotions;

public class DataInputException extends Exception {
	
	public DataInputException() {
		super("Dados de entrada inválidos!");
	}
	
	public DataInputException(String msg) {
		super(msg);
	}
	
}
