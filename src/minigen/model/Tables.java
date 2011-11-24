package minigen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Tables {

	private Model model;

	private Map<Class, List<Class>> linearExts;
	private Map<Class, List<Class>> conflictGraph;

	private List<Class> core;
	private List<Class> crown;

	public Tables(Model model) {
		this.model = model;

		// Compute classes depths
		computeClassDepth(ObjectClass.getInstance(), 0);

		// Compute linear ext for each class
		this.linearExts = new HashMap<Class, List<Class>>();
		computeClassLinearExt(ObjectClass.getInstance(), new ArrayList<Class>());

		// Compute core, crown and conflicts
		computeCoreCrown();
		computeConflicts();

		// System.out.println("crown : " + crown);
		// System.out.println("core : " + core);

		// Colorize
		colorizeCore();
		colorizeCrown();
	}

	/**
	 * Build the conflict graph of model
	 */
	private void computeConflicts() {
		this.conflictGraph = new HashMap<Class, List<Class>>();

		for (Class c : this.core) {
			if (c.getSuperClasses().size() >= 2) {

				List<List<Class>> linExts = new ArrayList<List<Class>>();
				for (int i = 0; i < c.getSuperClasses().size(); i++) {
					linExts.add(this.linearExts.get(c.getSuperClasses().get(i)));
					linExts.get(i).add(c.getSuperClasses().get(i));
				}

				List<HashSet<Class>> diffs = new ArrayList<HashSet<Class>>();

				// Compute lini - linj for all i != j
				for (List<Class> linExt : linExts) {
					for (List<Class> linExt2 : linExts) {
						if (linExt != linExt2) {
							HashSet<Class> diff = new HashSet<Class>();
							for (Class p : linExt2) {
								if (!linExt.contains(p))
									diff.add(p);
							}
							diffs.add(diff);

							diff = new HashSet<Class>();
							for (Class p : linExt) {
								if (!linExt2.contains(p))
									diff.add(p);
							}
							diffs.add(diff);
						}
					}
				}

				// Compute conflicts di * dj for all j != i
				for (HashSet<Class> diff : diffs) {
					for (HashSet<Class> diff2 : diffs) {
						if (diff != diff2) {
							for (Class c1 : diff) {
								List<Class> conflicts = new ArrayList<Class>();
								if (this.conflictGraph.containsKey(c1))
									conflicts = this.conflictGraph.get(c1);

								for (Class c2 : diff2) {
									conflicts.add(c2);
								}
								this.conflictGraph.put(c1, conflicts);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Colorize the core of model and compute adaptation
	 * 
	 * @param startColor
	 */
	private void colorizeCore() {

		for (Class c : this.core) {

			int color = 0;
			boolean colored = false;

			// Get conflict graph
			HashSet<Class> relatedTo = c.getRelatedTo();
			if (this.conflictGraph.containsKey(c)) {
				for (Class rel : this.conflictGraph.get(c)) {
					relatedTo.add(rel);
				}
			}

			// Lookup for a free color
			while (!colored) {

				boolean colorBusy = false;
				for (Class rel : relatedTo) {
					if (rel.getColor() != null && rel.getColor() == color)
						colorBusy = true;
				}

				if (!colorBusy) {
					c.setColor(color);
					colored = true;
				} else {
					color++;
				}
			}
			computeAdaptations(c);
		}
	}

	/**
	 * Colorize the crown and compute adaptation
	 */
	private void colorizeCrown() {
		for (Class c : this.crown) {
			if (c.getColor() != null) {
				continue;
			}
			
			boolean colorized = false;

			Adaptation[] superAdapt = new Adaptation[0];
			if(c.getSuperClasses().size() > 0) {
				superAdapt = c.getSuperClasses().get(0)
					.getAdaptationsTable();
			} else {
				c.setColor(0);
				colorized = true;
			}
			

			
			for (int i = 0; i < superAdapt.length; i++) {
				if (superAdapt[i] == null) {
					c.setColor(i);
					colorized = true;
				}
			}
			if (!colorized) {
				c.setColor(c.getSuperClasses().get(0).getColor() + 1);
			}

			// Computation is based on classe's parents discovery
			computeAdaptations(c);
		}
	}

	/**
	 * Compute core classes of class hierarchy Core classes are classes of class
	 * hierarchy which have more than one direct super class and their indirect
	 * super classes Compute crown classes of class hierarchy Crown classes are
	 * classes which have one super class and which all sub classes have only
	 * one super class
	 */
	private void computeCoreCrown() {
		Set<Class> core = new HashSet<Class>();
		Set<Class> crown = new HashSet<Class>();

		for (Class c : this.model.getClasses()) {
			if (c.getSuperClasses().size() > 1) {
				core.add(c);
				buildCoreRecursively(c, core);
			}
			if (c.getSuperClasses().size() <= 1) {
				boolean allSubClassesSimpleInheritance = true;
				for (Class s : c.getSubClasses()) {
					if (s.getSuperClasses().size() > 1) {
						allSubClassesSimpleInheritance = false;
					}
				}
				if (allSubClassesSimpleInheritance) {
					crown.add(c);
				}
			}
		}
		this.core = new ArrayList<Class>(core);
		Collections.sort(this.core);

		this.crown = new ArrayList<Class>(crown);
		Collections.sort(this.crown);
	}

	/**
	 * @see computeCoreClasses
	 * @param c
	 * @param core
	 */
	private void buildCoreRecursively(Class c, Set<Class> core) {
		for (Class p : c.getSuperClasses()) {
			core.add(p);
			buildCoreRecursively(p, core);
		}
	}

	/**
	 * Compute depth of class in global class tree recursively
	 * 
	 * @param toTag
	 * @param depth
	 *            current depth
	 */
	private void computeClassDepth(Class toTag, int depth) {
		// Check transitive relations
		if (toTag.getDepth() > depth)
			depth = toTag.getDepth();

		// Tag class
		toTag.setDepth(depth);
		depth++;

		// Explore childs
		for (Class child : toTag.getSubClasses()) {
			computeClassDepth(child, depth);
		}
	}

	/**
	 * Compute linear extension for a class
	 * 
	 * @param c
	 *            class to analyse
	 * @param parentLinExt
	 */
	private void computeClassLinearExt(Class c, List<Class> parentLinExt) {

		// Merge parent linExt with current
		HashSet<Class> currentLinExt = new HashSet<Class>();
		currentLinExt.addAll(parentLinExt);
		for (Class p : c.getParents()) {
			currentLinExt.add(p);
		}

		// Sort by decreasing linear order and store it
		List<Class> orderedLinExt = new ArrayList<Class>();
		orderedLinExt.addAll(currentLinExt);
		Collections.sort(orderedLinExt);
		this.linearExts.put(c, orderedLinExt);

		// Propagate to children
		for (Class child : c.getSubClasses()) {
			computeClassLinearExt(child, orderedLinExt);
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

			// Adapt generic part (recursively)
			Adaptation a = new Adaptation(pp);
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
		// For each bounds to this parent
		Adaptation a = new Adaptation(child);
		if (child.isGeneric()) {
			for (FormalType ft : child.getOrderedFormalTypes()) {
				Type adapted = new Type(ft.getName(), child);
				adapted.setFormalTypeLink(ft);
				a.add(adapted);
			}
		}
		return a;
	}

}
