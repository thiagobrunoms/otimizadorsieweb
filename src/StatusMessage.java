import java.util.Hashtable;


public class StatusMessage {

	private String message;
	private Hashtable<String, String> status; //key - nome do arquivo, value - Sucesso/Falha
	
	public StatusMessage() {
		this.status = new Hashtable<String, String>();
	}
	
	public void setStatus(String object, String status) {
		this.status.put(object, status);
	}
	
	public Hashtable<String, String> getStatus() {
		return this.status;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
