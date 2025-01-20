package ticket.exception;

public class PasswordInvalidException extends CertException {
	
	public PasswordInvalidException() {
		super("密碼錯誤");
	}
	
	public PasswordInvalidException(String message) {
		super(message);
	}

}
