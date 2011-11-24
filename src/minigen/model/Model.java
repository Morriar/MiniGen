package minigen.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import minigen.exception.SemanticException;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.Token;

public class Model {

	private Map<String, Class> classesByNames;
	private Map<Node, Class> classesByNodes;
	private Map<Node, Type> typesByNodes;

	public Model() {
		this.classesByNodes = new HashMap<Node, Class>();
		this.classesByNames = new HashMap<String, Class>();
		this.typesByNodes = new HashMap<Node, Type>();
	}

	public boolean containsClassDeclaration(Node node) {
		return this.classesByNodes.containsKey(node);
	}

	public boolean containsTypeDeclaration(Node node) {
		return this.typesByNodes.containsKey(node);
	}

	public boolean containsClassDeclaration(String name) {
		return this.classesByNames.containsKey(name);
	}

	public void declareClass(Node n, Class c) {
		if (containsClassDeclaration(c.getName())) {
			if (c.getName().equals("Object")) {
				throw new SemanticException(c.getLocation(),
						"You cannot redeclare class Object");
			}
			throw new SemanticException(c.getLocation(), c
					+ " is already declared in "
					+ classesByNames.get(c.getName()).getLocation().getLine()
					+ ":"
					+ classesByNames.get(c.getName()).getLocation().getPos());
		}
		this.classesByNodes.put(n, c);
		this.classesByNames.put(c.getName(), c);
	}

	public void declareType(Node n, Type t) {
		this.typesByNodes.put(n, t);
	}

	public Class getClassByNode(Token location, Node n, String name) {
		if (!containsClassDeclaration(n)) {
			throw new SemanticException(location, "class " + name
					+ " is not declared");
		}
		return this.classesByNodes.get(n);
	}

	public Type getTypeByNode(Node n) {
		return this.typesByNodes.get(n);
	}

	public Class getClassByName(Token location, String name) {
		if (!containsClassDeclaration(name)) {
			throw new SemanticException(location, "class " + name
					+ " is not declared");
		}
		return this.classesByNames.get(name);
	}

	public Collection<Class> getClasses() {
		return this.classesByNodes.values();
	}

}
