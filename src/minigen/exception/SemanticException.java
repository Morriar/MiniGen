
package minigen.exception;

import minigen.syntax3.node.*;

public class SemanticException
        extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SemanticException(
            Token token,
            String message) {

        super("[" + token.getLine() + "," + token.getPos() + "] " + message);
    }
    
}
