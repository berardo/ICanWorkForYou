package au.com.yourcompany;

public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
