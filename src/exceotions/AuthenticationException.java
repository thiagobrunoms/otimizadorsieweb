package exceotions;

public class AuthenticationException extends Exception {

	public AuthenticationException() {
		super("CPF ou Senha Inválidos!");
	}
	
	public AuthenticationException(String msg) {
		super(msg);
	}
	
}
