package minigen.exception;

public class InterpreterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InterpreterException(String message) {

		super("[Internal Error] " + message);
	}

}
