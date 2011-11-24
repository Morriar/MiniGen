package minigen.model;

import java.util.ArrayList;
import java.util.List;

public class Type {

	// Type name
	private String name;

	// Generic part of Type
	private List<Type> genericTypes;

	// Type first introduction
	private Class intro;

	private FormalType link;

	public Type(String name, Class intro) {
		this.name = name;
		this.intro = intro;
		this.link = null;

		this.genericTypes = new ArrayList<Type>();
	}

	/*
	 * Is the current type a generic type ?
	 */
	public boolean isGeneric() {
		return getArity() > 0;
	}

	/*
	 * Is the type definition linked to a formal type ?
	 */
	public boolean isLinkedToFormalType() {
		return link != null;
	}

	/*
	 * Get the arity of the current type
	 */
	public int getArity() {
		return genericTypes.size();
	}

	/*
	 * Formal type linked to generic type if any
	 */
	public FormalType getFormalType() {
		return link;
	}

	/*
	 * Check if current type isa a subtype of ot Base is used to formal type
	 * replacement with concrete type
	 */
	public boolean isa(Type ot, Type base) {
		
		// Check rapid subtyping using adaptation table size
		if(ot.getIntro().getColor() >= this.getIntro().getAdaptationsTable().length) {
			return false;
		}
		
		// Get adpatation for ot
		Adaptation a = this.getIntro().getAdaptationsTable()[ot.getIntro().getColor()];
		if (a == null) {
			return false;
		}
		
		// Check is expected adaptation
		if(!a.isFor(ot.getIntro())) {
			return false;
		}
		
		// Replace formal type form adaptation with concrete type from type
		Type toCheck = this.buildConcreteAdaptation(a, ot, base);
		
		for (int i = 0; i < toCheck.getGenericTypes().size(); i++) {
			if (!toCheck.getGenericTypes().get(i).isa(ot.getGenericTypes().get(i),toCheck.getGenericTypes().get(i))) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Build a concrete adaptation from formal adaptation Replace each formal
	 * type in adaptation by concrete type provided by type to check
	 */
	public Type buildConcreteAdaptation(Adaptation a, Type to, Type base) {

		Type result = new Type(to.getName(), to.getIntro());
		
		for (Type gt : a.getTypes()) {
			
			if (gt.isLinkedToFormalType()) {
				int pos = gt.getFormalType().getPosition();
				Type adapted = base.getGenericTypes().get(pos);
				result.addGenericType(adapted);
			} else if (gt.isGeneric()) {
				Type resultGt = new Type(gt.getName(), gt.getIntro());
				recursiveBuild(gt, resultGt, base);
				result.addGenericType(resultGt);
			} else {
				result.addGenericType(gt);
			}
		}

		return result;
	}

	/*
	 * Adapt generic part of new concrete adaptation recursively
	 */
	private void recursiveBuild(Type t, Type toAdapt, Type base) {

		for (Type gt : t.getGenericTypes()) {
			if (gt.isLinkedToFormalType()) {
				int pos = gt.getFormalType().getPosition();
				Type adapted = base.getGenericTypes().get(pos);
				toAdapt.addGenericType(adapted);
			}
			if (gt.isGeneric()) {
				Type resultGt = new Type(gt.getName(), gt.getIntro());
				recursiveBuild(gt, resultGt, base);
				toAdapt.addGenericType(resultGt);
			}
		}
	}

	/*
	 * Add a generic type to current type
	 */
	public void addGenericType(Type type) {
		this.genericTypes.add(type);
	}

	/*
	 * Getters, setters
	 */

	@Override
	public String toString() {
		String str = this.getName();

		int i = 0;
		if (this.isGeneric()) {
			str += "[";
			for (Type type : genericTypes) {
				str += type.toString();
				if (i < genericTypes.size() - 1) {
					str += ", ";
				}
				i++;
			}
			str += "]";
		}

		if (this.isLinkedToFormalType()) {
			str += "*";
		}

		return str;
	}

	public String getName() {
		return name;
	}

	public List<Type> getGenericTypes() {
		return genericTypes;
	}

	public Class getIntro() {
		return intro;
	}

	public void setFormalTypeLink(FormalType link) {
		this.link = link;
	}

}
