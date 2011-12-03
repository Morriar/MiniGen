package minigen.model;

import java.util.HashMap;
import java.util.Map;

import minigen.exception.SemanticException;
import minigen.syntax3.node.Token;

/**
 * Scope of vars Register vars and their types
 */
public class Scope {

	private Map<String, Var> varsByName;

	public Scope() {
		this.varsByName = new HashMap<String, Var>();
	}

	public void declareVar(Token location, String name, Type type) {
		if (containsVar(name)) {
			throw new SemanticException(location, "var " + name
					+ " is already declared");
		}
		this.varsByName.put(name, new Var(name, type));
	}

	public boolean containsVar(String var) {
		return this.varsByName.containsKey(var);
	}
	
	public Var getVar(Token location, String name) {
		if (!containsVar(name)) {
			throw new SemanticException(location, "var " + name
					+ " not declared");
		}
		return this.varsByName.get(name);
	}

}
