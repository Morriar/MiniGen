package minigen.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import minigen.exception.SemanticException;
import minigen.syntax3.node.Token;

public class Class {

	private int classId;
	private Integer color;
	private int depth;
	private String name;
	private Token location;

	private Map<String, FormalType> formalTypes = new HashMap<String, FormalType>();

	private List<FormalType> orderedFormalTypes = new ArrayList<FormalType>();

	private Map<String, Class> parents = new HashMap<String, Class>();
	private List<Class> subClasses = new ArrayList<Class>();
	private List<Class> superClasses = new ArrayList<Class>();

	private Map<Class, List<Type>> parentsBounds = new HashMap<Class, List<Type>>();

	private Adaptation[] adaptationsTable;

	public Class(String name, Token location, int classId) {
		super();
		this.classId = classId;
		this.name = name;
		this.location = location;
		this.depth = -1;
	}

	/*
	 * Add a parent class to the current class
	 */
	public void addParent(Class parent, List<Type> bounds) {
		this.parents.put(parent.getName(), parent);
		this.parentsBounds.put(parent, bounds);
		this.superClasses.add(parent);
		parent.getSubClasses().add(this);
	}

	/*
	 * Add a formal type to the current class
	 */
	public void addFormalType(FormalType ftype) {
		this.formalTypes.put(ftype.getName(), ftype);
		this.orderedFormalTypes.add(ftype);
	}

	/*
	 * Check if a formal type is already declared Checks are based on the formal
	 * type name
	 */
	public boolean isFormalTypeDeclared(String name) {
		return formalTypes.containsKey(name);
	}

	/*
	 * Retrieve a formal type by is name
	 */
	public FormalType getFormalType(String name) {
		if (!this.formalTypes.containsKey(name)) {
			throw new SemanticException(null, name
					+ "formal type not declared in type " + this.getName());
		}
		return this.formalTypes.get(name);
	}

	public boolean isGeneric() {
		return this.orderedFormalTypes.size() != 0;
	}

	/*
	 * Return the arity of the generic class
	 */
	public int getArity() {
		return this.orderedFormalTypes.size();
	}

	@Override
	public String toString() {
		String str = this.getName();
		int i = 0;
		if (orderedFormalTypes.size() > 0) {
			str += '[';
			for (FormalType ft : this.orderedFormalTypes) {
				str += i < this.orderedFormalTypes.size() - 1 ? ft + ", " : ft;
				i++;
			}
			str += ']';
		}

		return str;
	}

	/*
	 * Rapid isa, constant time execution
	 */
	public boolean isa(Class c) {

		if (c.getColor() >= this.adaptationsTable.length) {
			return false;
		}

		Adaptation adaptation = this.adaptationsTable[c.getColor()];

		return adaptation != null && adaptation.isFor(c);
	}

	/*
	 * Get the adaptation to specified parent
	 */
	public Adaptation getAdaptation(Class to) {
		return this.adaptationsTable[to.getColor()];
	}

	/*
	 * Register adaptation to super class Throw Exception if formal type
	 * conflict is found
	 */
	public void addAdaptation(Class to, Adaptation adaptation) {
		if (this.adaptationsTable == null) {
			this.adaptationsTable = new Adaptation[to.getColor() + 1];
		} else if (to.getColor() >= this.adaptationsTable.length) {
			// Need to enlarge the array
			this.adaptationsTable = Arrays.copyOf(this.adaptationsTable,
					to.getColor() + 1);
		}
		this.adaptationsTable[to.getColor()] = adaptation;
	}

	/*
	 * Check if current class is a sub class of c Comparisons are based on
	 * parents names WARNING : Low efficiency, highly recursive, used only as
	 * bootstrap for type check. Use isa method instead.
	 */
	public boolean isSubClassOf(Class c) {
		if (c.getName().equals("Object")) {
			return true;
		}

		if (this.isSameClass(c)) {
			return true;
		}

		for (Class parent : parents.values()) {
			if (parent.isSubClassOf(c)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Display inheritance relations of current class
	 */
	public String toStringWithParents() {
		String str = this.getName();
		int i = 0;
		if (orderedFormalTypes.size() > 0) {
			str += '[';
			for (FormalType ft : this.orderedFormalTypes) {
				str += i < this.orderedFormalTypes.size() - 1 ? ft + ", " : ft;
				i++;
			}
			str += ']';
		}
		if (parents.size() > 0) {
			str += " <: ";
			int j = 0;
			for (Class c : this.parents.values()) {
				String classStr = c.getName();

				// Disp bounds super relations
				if (parentsBounds.get(c).size() > 0) {
					classStr += "[";
					int k = 0;
					for (Type parent : parentsBounds.get(c)) {
						classStr += k < this.parentsBounds.get(c).size() - 1 ? parent
								+ ", "
								: parent;
						k++;
					}
					classStr += "]";
				}
				str += j < this.parents.size() - 1 ? classStr + ", " : classStr;
				j++;
			}
		}

		return str;
	}

	public String toStringWithAdaptationsTable() {
		String print = "{";
		for (int i = 0; i < this.adaptationsTable.length; i++) {
			print += "[" + i + "]" + " = " + this.adaptationsTable[i];

			if (i < this.adaptationsTable.length - 1) {
				print += ", ";
			}
		}
		print += "}";
		return print;
	}

	/*
	 * Check if current class is same class than c Comparisons are based on name
	 * and formal type arity
	 */
	public boolean isSameClass(Class c) {
		return this.getName().equals(c.getName());
	}

	/*
	 * Is the local class introduction for Object ?
	 */
	public boolean isObjectIntro() {
		return false;
	}

	/*
	 * Getters, setters
	 */

	public String getName() {
		return name;
	}

	public Token getLocation() {
		return location;
	}

	public Map<String, FormalType> getFormalTypes() {
		return formalTypes;
	}

	public Collection<Class> getParents() {
		return parents.values();
	}

	public Class getParentByName(String name) {
		return parents.get(name);
	}

	public Map<Class, List<Type>> getParentsBounds() {
		return parentsBounds;
	}

	public List<Type> getParentBounds(Class c) {
		return parentsBounds.get(c);
	}

	public int getClassId() {
		return classId;
	}

	public Adaptation[] getAdaptationsTable() {
		return adaptationsTable;
	}

	public void setAdaptationsTable(Adaptation[] adaptationsTable) {
		this.adaptationsTable = adaptationsTable;
	}

	public List<FormalType> getOrderedFormalTypes() {
		return orderedFormalTypes;
	}

	public List<Class> getSubClasses() {
		return subClasses;
	}

	public void setSubClasses(List<Class> subClasses) {
		this.subClasses = subClasses;
	}

	public int getDegree() {
		return this.subClasses.size() + this.parents.size();
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public HashSet<Class> getRelatedTo() {

		HashSet<Class> result = new HashSet<Class>();

		buildRecursiveParentsRelation(this.getSuperClasses(), result);
		buildRecursiveChildrenRelation(this.getSubClasses(), result);

		return result;
	}

	private void buildRecursiveParentsRelation(Collection<Class> toExplore,
			Collection<Class> relatedTo) {
		for (Class c : toExplore) {
			relatedTo.add(c);
			buildRecursiveParentsRelation(c.getSuperClasses(), relatedTo);
		}
	}

	private void buildRecursiveChildrenRelation(Collection<Class> toExplore,
			Collection<Class> relatedTo) {
		for (Class c : toExplore) {
			relatedTo.add(c);
			buildRecursiveChildrenRelation(c.getSubClasses(), relatedTo);
		}
	}

	public boolean isRelatedTo(Class c) {
		for (Class p : this.getParents()) {
			if (p.isSameClass(c)) {
				return true;
			}
		}
		return false;
	}

	public List<Class> getSuperClasses() {
		return superClasses;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}
