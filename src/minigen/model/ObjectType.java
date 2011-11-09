package minigen.model;

public class ObjectType extends Type {

	private static ObjectType instance = null;

	private ObjectType() {
		super("Object", ObjectClass.getInstance());
	}
	
	public static final ObjectType getInstance() {
		if (instance == null) {
			instance = new ObjectType();
		}
		return instance;
	}

	@Override
	public boolean isGeneric() {
		return false;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

}
