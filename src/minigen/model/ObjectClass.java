package minigen.model;

import minigen.exception.*;

public class ObjectClass extends Class {
	
	private static Class instance = null;

	private ObjectClass() {
		super("Object", null, 0);
	}

	public static final Class getInstance() {
		if (instance == null) {
			instance = new ObjectClass();
		}
		return instance;
	}
	
	@Override
	public boolean isObjectIntro() {
		return true;
	}

	@Override
	public boolean isSubClassOf(Class c) {
		if(c.isObjectIntro()) {
			return true;
		}
		return false;
	}
	

	@Override
	public boolean isFormalTypeDeclared(String name) {
		return false;
	}

	@Override
	public FormalType getFormalType(String name) {
		throw new SemanticException(null, name
				+ "formal type not declared in type " + this.getName());
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public String toStringWithParents() {
		return this.toString();
	}

}
