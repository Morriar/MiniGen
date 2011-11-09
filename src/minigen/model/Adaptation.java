package minigen.model;

import java.util.ArrayList;
import java.util.List;

public class Adaptation {

	private List<Type> types;
	private Class parent;
	
	public Adaptation(Class parent) {
		this.types = new ArrayList<Type>();
		this.parent = parent;
	}
	
	public boolean isCompatibleTo(Adaptation a) {
		if(this.getTypes().size() != a.getTypes().size()) {
			return false;
		}
		
		for(int i = 0; i < this.getTypes().size(); i++) {
			Class local = this.get(i).getIntro();
			Class other = a.get(i).getIntro();
			
			if(!local.isSameClass(other)) {
				return false;
			}
		}
		
		
		return true;
	}
	
	public void add(Type t) {
		this.types.add(t);
	}
	
	public Type get(int index) {
		return types.get(index);
	}

	public Type get(String name) {
		for(Type t : types) {
			if( t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	public List<Type> getTypes() {
		return types;
	}
	
	public boolean contains(Type ot) {
		for(Type t : types) {
			if( ot.getName().equals(t.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String str = this.parent.getName();
		
		if(!this.types.isEmpty()) {
			str += "[";
		}
		
		int i = 0;
		for(Type t : this.types) {
			str += t.toString();
			
			if(i < this.types.size() - 1) {
				str += ", ";
			}
			
			i++;
		}
		
		if(!this.types.isEmpty()) {
			str += "]";
		}
		
		return str;
	}
	
	public boolean isFor(Class c) {
		return this.parent.isSameClass(c);
	}
	
}
