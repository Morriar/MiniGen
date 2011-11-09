package minigen.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minigen.exception.SemanticException;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.Token;

public class Model implements Comparator<Class> {

	private Map<String, Class> classesByNames;
	private Map<Node, Class> classesByNodes;
	private Map<Class, Integer> colorsByClasses;
	private int maxColor;

	public Model() {
		this.classesByNodes = new HashMap<Node, Class>();
		this.classesByNames = new HashMap<String, Class>();
	}

	/*
	 * For each class in model, we pre-calculate all possibles adaptations
	 */
	public void buildAdaptations() {
		
		for (Class c : this.getClasses()) {

			// Allocate table adaptation in class
			c.allocateAdaptationTable(this.getClasses().size());

			// Computation is based on classe's parents discovery
			computeAdaptations(c);

		}

	}
	
	/*
	 * Compute adaptations for each class in the model
	 */
	public void computeAdaptations(Class c) {

		// Build adaptation for self
		c.addAdaptation(c, this.getBaseAdaptation(c));
		
		// Explore parents
		for (Class p : c.getParents()) {

			Adaptation a = new Adaptation(p);
			
			// Explore generic part
			if (p.isGeneric()) {
				computeGenericPart(a, c.getParentsBounds().get(p));
			}

			// Add adaptation in table
			c.addAdaptation(p, a);

			// Start recursion for parent exploration
			computeParents(c, p, a);

		}
	}

	/*
	 * Copy bound definition to adaptation
	 */
	public void computeGenericPart(Adaptation a, List<Type> bounds) {
		for (Type bound : bounds) {
			a.add(bound);
		}
	}

	/*
	 * Compute parents adaptation to add to child table
	 */
	public void computeParents(Class child, Class parent, Adaptation previous) {

		// Compute parents of parent
		for (Class pp : parent.getParents()) {

			Adaptation a = new Adaptation(pp);

			// Adapt generic part (recursively)
			if (pp.isGeneric()) {

				List<Type> bounds = parent.getParentBounds(pp);

				for (Type bound : bounds) {
					Type adaptedBound = adaptBoundRecursively(bound, previous);
					a.add(adaptedBound);
				}

			}

			child.addAdaptation(pp, a);

			// Explore parents of parent (recursively)
			computeParents(child, pp, a);
		}

	}

	/*
	 * Replace current formal type by previous adaptation
	 */
	private Type adaptBoundRecursively(Type bound, Adaptation previous) {
		if (bound.isLinkedToFormalType()) {
			int pos = bound.getFormalType().getPosition();
			return previous.get(pos);
		}

		if (bound.isGeneric()) {

			// build a new type definition
			Type adaptedBound = new Type(bound.getName(), bound.getIntro());

			for (Type gt : bound.getGenericTypes()) {
				// Adapt generic type and replace formal types by previous
				// adaptation
				Type adaptedGt = adaptBoundRecursively(gt, previous);
				adaptedBound.addGenericType(adaptedGt);
			}

			return adaptedBound;
		}

		return bound;

	}

	/*
	 * Compute adaptation of a class to itself
	 */
	public Adaptation getBaseAdaptation(Class child) {

		Adaptation a = new Adaptation(child);

		// For each bounds to this parent
		if (child.isGeneric()) {
			for (FormalType ft : child.getOrderedFormalTypes()) {
				Type adapted = new Type(ft.getName(), child);
				adapted.setFormalTypeLink(ft);
				a.add(adapted);
			}
		}

		return a;

	}

	public boolean containsClassDeclaration(Node node) {
		if (this.classesByNodes.containsKey(node)) {
			return true;
		}
		return false;
	}

	public boolean containsClassDeclaration(String name) {
		if (this.classesByNames.containsKey(name)) {
			return true;
		}
		return false;
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

	public Class getClassByNode(Token location, Node n, String name) {
		if (!containsClassDeclaration(n)) {
			throw new SemanticException(location, "class " + name
					+ " is not declared");
		}
		return this.classesByNodes.get(n);
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

	@Override
	public int compare(Class arg0, Class arg1) {
		return arg1.getDegree() - arg0.getDegree();
	}

	public Map<Class, Integer> getColorsByClasses() {
		return colorsByClasses;
	}

	public void setColorsByClasses(Map<Class, Integer> colorsByClasses) {
		this.colorsByClasses = colorsByClasses;
	}

	public int getMaxColor() {
		return maxColor;
	}

}
