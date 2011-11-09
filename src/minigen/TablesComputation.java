package minigen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import minigen.model.Class;
import minigen.model.ObjectClass;

import minigen.model.Model;

public class TablesComputation implements Comparator<Class> {

	private Model model;
	private List<Class> globalLinearExt;
	private Map<Class, List<Class>> linearExts;
	
	private List<Class> core = new ArrayList<Class>();
	private List<Class> crown = new ArrayList<Class>();
	private List<Class> border = new ArrayList<Class>();

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
		computeCoreCrownBorder();
		
		// Compute conflict graph on core classes
		computeConflicts();
	}

	private void computeConflicts() {
		// Sort core by linear order
		Collections.sort(core, this);
		
		for(Class c : core) {
			if(c.getSuperClasses().size() == 2) {
				
				HashSet<Class> d = new HashSet<Class>();
				
				List<Class> linC1 = linearExts.get(c.getSuperClasses().get(0));
				List<Class> linC2 = linearExts.get(c.getSuperClasses().get(1));
				
				// linC1 - linC2
				for(Class p : linC2) {
					if(!linC1.contains(p)) {
						d.add(p);
					}
				}
				
				// linC2 - linC1
				for(Class p : linC1) {
					if(!linC2.contains(p)) {
						d.add(p);
					}
				}
				
				
			}
		}
		
	}

	/*
	 * Compute core, crown and border classes
	 */
	private void computeCoreCrownBorder() {
		HashSet<Class> core = new HashSet<Class>();
		HashSet<Class> crown = new HashSet<Class>();
		HashSet<Class> border = new HashSet<Class>();
		
		for(Class c : model.getClasses()) {
			// Check crown classes
			if(c.getSuperClasses().size() == 1 && !c.getSubClasses().isEmpty()) {
				boolean flag = true;
				for(Class s : c.getSubClasses()) {
					if(s.getSuperClasses().size() != 1) {
						flag = false;
					}
				}
				if(flag) {
					crown.add(c);
				}
				
			// Check core classes
			} else if(c.getSuperClasses().size() > 1 && !c.getSubClasses().isEmpty()) {
				core.add(c);
			}
		}
		
		for(Class c : core) {
			boolean flag = false;
			for(Class s : c.getSubClasses()) {
				if(crown.contains(s)) {
					flag = true;
				}	
			}
			
			if(flag) {
				border.add(c);
			}
			
		}
		
		this.core.addAll(core);
		this.crown.addAll(crown);
		this.border.addAll(border);
		
		System.out.println("crown : " + crown);
		System.out.println("core : " + core);
		System.out.println("border : " + border);
		
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
		if(toTag.getDepth() > depth) {
			depth = toTag.getDepth();
		}
		
		// Tag class
		toTag.setDepth(depth);
		depth++;
		
		// Explore childs
		for(Class child : toTag.getSubClasses()) {
			computeClassDepth(child, depth);
		}
	}
	
	private void computeClassLinearExt(Class c, List<Class> prev) {
		
		// Merge parent linExt with current
		HashSet<Class> currentLinExt = new HashSet<Class>();
		currentLinExt.addAll(prev);
		for(Class p : c.getParents()) {
			currentLinExt.add(p);
		}
		
		// Sort by decreasing linear order and store it
		List<Class> orderedLinExt = new ArrayList<Class>();
		orderedLinExt.addAll(currentLinExt);
		Collections.sort(orderedLinExt, this);
		this.linearExts.put(c, orderedLinExt);
		
		// Propagate to children
		for(Class child : c.getSubClasses()) {
			computeClassLinearExt(child, orderedLinExt);
		}
	}
	
	
	@Override
	public int compare(Class o1, Class o2) {
		if( o2.isSubClassOf(o1) ) {
			return -1;
		}
		if( o1.isSubClassOf(o2) ) {
			return 1;
		}
		
		if(o2.getDepth() - o1.getDepth() != 0) {
			return o1.getDepth() - o2.getDepth(); 
		}
		
		return o1.getName().compareTo(o2.getName()); 
	}
	
	
	
	
		
}
