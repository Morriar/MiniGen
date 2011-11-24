package minigen.exception;

public class InternalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InternalException(String message) {
		super("[Internal Error] " + message + "\n Please contact developpement team.");
	}

}
