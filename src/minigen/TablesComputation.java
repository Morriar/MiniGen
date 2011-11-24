package minigen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import minigen.model.Adaptation;
import minigen.model.Class;
import minigen.model.Model;
import minigen.model.ObjectClass;

public class TablesComputation implements Comparator<Class> {

	private Model model;
	public Integer maxColor = 0;
	private List<Class> globalLinearExt;

	private Map<Class, List<Class>> linearExts;
	private Map<Class, List<Class>> conflictGraph;

	private HashSet<Class> core;
	private HashSet<Class> crown;
	private HashSet<Class> border;

	public TablesComputation(Model model) {
		this.model = model;

		// Compute classes depths
		computeClassDepth(ObjectClass.getInstance(), 0);

		// Compute linear extension of model
		computeModelLinearExt();

		// Compute linear ext for each class
		this.linearExts = new HashMap<Class, List<Class>>();
		computeClassLinearExt(ObjectClass.getInstance(), new ArrayList<Class>());

		// Compute core, crown and border
		computeCoreClasses();
		computeBorderClasses();
		computeCrownClasses();
		// computeCoreCrownBorder();

		// System.out.println("crown : " + crown);
		// System.out.println("core : " + core);
		// System.out.println("border : " + border);

		// Compute conflict graph on core classes
		computeConflicts();

		colorizeCore(0);
		buildCoreAdaptations();

		colorizeCrown();
		// buildCrownAdaptations();
	}

	private void buildCoreAdaptations() {
		for (Class c : this.core) {
			// Computation is based on classe's parents discovery
			model.computeAdaptations(c);
		}
	}

	private void buildCrownAdaptations() {

		for (Class c : this.crown) {
			Class parent = c.getSuperClasses().get(0);

			// Computation is based on classe's parents discovery
			model.computeAdaptations(c);
		}
	}

	/**
	 * Build the conflict graph of model
	 */
	private void computeConflicts() {

		this.conflictGraph = new HashMap<Class, List<Class>>();

		// Sort core by linear order
		List<Class> ordered = new ArrayList<Class>(core);
		Collections.sort(ordered, this);

		for (Class c : core) {
			// TODO conflicts for c > 2
			if (c.getSuperClasses().size() == 2) {

				List<Class> linC1 = linearExts.get(c.getSuperClasses().get(0));
				linC1.add(c.getSuperClasses().get(0));

				List<Class> linC2 = linearExts.get(c.getSuperClasses().get(1));
				linC2.add(c.getSuperClasses().get(1));

				// D12 = linC1 - linC2
				HashSet<Class> d12 = new HashSet<Class>();
				for (Class p : linC2) {
					if (!linC1.contains(p)) {
						d12.add(p);
					}
				}

				// D21 = linC2 - linC1
				HashSet<Class> d21 = new HashSet<Class>();
				for (Class p : linC1) {
					if (!linC2.contains(p)) {
						d21.add(p);
					}
				}

				// Conflicts = D12 x D21
				for (Class c1 : d12) {
					List<Class> conflicts = new ArrayList<Class>();
					if (this.conflictGraph.containsKey(c1)) {
						conflicts = this.conflictGraph.get(c1);
					}

					for (Class c2 : d21) {
						conflicts.add(c2);
					}
					this.conflictGraph.put(c1, conflicts);
				}

				// Conflicts = D21 x D12
				for (Class c1 : d21) {
					List<Class> conflicts = new ArrayList<Class>();
					if (this.conflictGraph.containsKey(c1)) {
						conflicts = this.conflictGraph.get(c1);
					}

					for (Class c2 : d12) {
						conflicts.add(c2);
					}
					this.conflictGraph.put(c1, conflicts);
				}

			} else if (c.getSuperClasses().size() > 2) {

				List<List<Class>> linExts = new ArrayList<List<Class>>();

				for (int i = 0; i < c.getSuperClasses().size(); i++) {
					linExts.add(linearExts.get(c.getSuperClasses().get(i)));
					linExts.get(i).add(c.getSuperClasses().get(i));
				}

				List<HashSet<Class>> diffs = new ArrayList<HashSet<Class>>();

				// Compute lini - linj for all i != j
				for (List<Class> linExt : linExts) {
					for (List<Class> linExt2 : linExts) {
						if (linExt != linExt2) {
							HashSet<Class> diff = new HashSet<Class>();
							for (Class p : linExt2) {
								if (!linExt.contains(p)) {
									diff.add(p);
								}
							}
							diffs.add(diff);

							diff = new HashSet<Class>();
							for (Class p : linExt) {
								if (!linExt2.contains(p)) {
									diff.add(p);
								}
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
								if (this.conflictGraph.containsKey(c1)) {
									conflicts = this.conflictGraph.get(c1);
								}

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
	 * Colorize the core of model
	 * 
	 * @param startColor
	 */
	private void colorizeCore(Integer startColor) {

		// Sort by linear order
		List<Class> ordered = new ArrayList<Class>(this.core);
		Collections.sort(ordered, this);

		for (Class c : ordered) {

			int color = startColor;

			// Get conflict graph
			HashSet<Class> relatedTo = c.getRelatedTo();
			if (this.conflictGraph.containsKey(c)) {
				for (Class rel : this.conflictGraph.get(c)) {
					relatedTo.add(rel);
				}
			}

			boolean colored = false;
			while (!colored) {

				boolean colorBusy = false;
				for (Class rel : relatedTo) {
					if (rel.getColor() != null && rel.getColor() == color) {
						colorBusy = true;
					}
				}

				if (!colorBusy) {
					c.setColor(color);
					colored = true;
				} else {
					color++;
					if (color > this.maxColor) {
						this.maxColor = color;
					}
				}
			}
		}
	}

	private void colorizeCrown() {
		// Sort by linear order
		List<Class> ordered = new ArrayList<Class>(this.crown);
		Collections.sort(ordered, this);

		for (Class c : ordered) {
			if (c.getColor() != null) {
				continue;
			}

			Adaptation[] superAdapt = c.getSuperClasses().get(0)
					.getAdaptationsTable();

			boolean colorized = false;
			for (int i = 0; i < superAdapt.length; i++) {
				if (superAdapt[i] == null) {
					c.setColor(i);
					colorized = true;
				}
			}
			if (!colorized) {
				c.setColor(c.getSuperClasses().get(0).getColor() + 1);
				if (c.getColor() > this.maxColor) {
					this.maxColor = c.getColor();
				}
			}

			// Computation is based on classe's parents discovery
			model.computeAdaptations(c);
		}
	}

	/**
	 * Compute core classes of class hierarchy Core classes are classes of class
	 * hierarchy which have more than one direct super class and their indirect
	 * super classes
	 */
	private void computeCoreClasses() {
		core = new HashSet<Class>();
		for (Class c : model.getClasses()) {
			if (c.getSuperClasses().size() > 1) {
				core.add(c);
				buildCoreRecursively(c, core);
			}
		}
	}

	/**
	 * @see computeCoreClasses
	 * @param c
	 * @param core
	 */
	private void buildCoreRecursively(Class c, HashSet<Class> core) {
		for (Class p : c.getSuperClasses()) {
			core.add(p);
			buildCoreRecursively(p, core);
		}
	}

	/**
	 * Compute border classes of class hierarchy Border classes are minimal
	 * classes of core
	 */
	private void computeBorderClasses() {
		border = new HashSet<Class>();

		for (Class c : core) {
			if (c.getSubClasses().isEmpty()) {
				border.add(c);
			}
		}
	}

	/**
	 * Compute crown classes of class hierarchy Crown classes are classes which
	 * have one super class and which all sub classes have only one super class
	 */
	private void computeCrownClasses() {
		crown = new HashSet<Class>();

		for (Class c : model.getClasses()) {
			if (c.getSuperClasses().size() == 1) {
				boolean allSubClassesSimpleInheritance = true;

				for (Class s : c.getSubClasses()) {
					if (s.getSuperClasses().size() != 1) {
						allSubClassesSimpleInheritance = false;
					}
				}

				if (allSubClassesSimpleInheritance) {
					crown.add(c);
				}

			}
		}
	}

	/*
	 * Compute the linear extension of global model
	 */
	private void computeModelLinearExt() {
		this.globalLinearExt = new ArrayList<Class>();
		this.globalLinearExt.addAll(this.model.getClasses());

		// Sort classes by total order
		Collections.sort(this.globalLinearExt, this);

	}

	/*
	 * Compute depth of class in global class tree
	 */
	private void computeClassDepth(Class toTag, int depth) {

		// Check transitive relations
		if (toTag.getDepth() > depth) {
			depth = toTag.getDepth();
		}

		// Tag class
		toTag.setDepth(depth);
		depth++;

		// Explore childs
		for (Class child : toTag.getSubClasses()) {
			computeClassDepth(child, depth);
		}
	}

	private void computeClassLinearExt(Class c, List<Class> prev) {

		// Merge parent linExt with current
		HashSet<Class> currentLinExt = new HashSet<Class>();
		currentLinExt.addAll(prev);
		for (Class p : c.getParents()) {
			currentLinExt.add(p);
		}

		// Sort by decreasing linear order and store it
		List<Class> orderedLinExt = new ArrayList<Class>();
		orderedLinExt.addAll(currentLinExt);
		Collections.sort(orderedLinExt, this);
		this.linearExts.put(c, orderedLinExt);

		// Propagate to children
		for (Class child : c.getSubClasses()) {
			computeClassLinearExt(child, orderedLinExt);
		}
	}

	@Override
	public int compare(Class o1, Class o2) {
		if (o2.isSubClassOf(o1)) {
			return -1;
		}
		if (o1.isSubClassOf(o2)) {
			return 1;
		}

		if (o2.getDepth() - o1.getDepth() != 0) {
			return o1.getDepth() - o2.getDepth();
		}

		return o1.getName().compareTo(o2.getName());
	}

}
