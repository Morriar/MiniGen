package minigen.model;


public class FormalType {

	private String name;
	private int position;

	public FormalType(String name, int position) {
		super();
		this.name = name;
		this.position = position;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
		return position;
	}
	
}
