package minigen;

import minigen.model.Class;
import minigen.model.ObjectClass;
import minigen.model.Model;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AClassDecl;

public class ClassAnalysis extends DepthFirstAdapter {

	private Model model;
	private int currentId; 

	public ClassAnalysis(Model classScope) {
		this.currentId = 0;
		this.model = classScope;
		
		//Reserve id 0 for Object class
		reserveCurrentId();
		
		// Declare Object class
		this.model.declareClass(null, ObjectClass.getInstance());
	}

	@Override
	public void caseAClassDecl(AClassDecl node) {
		
		//Get the class id
		int id = reserveCurrentId();
		
		//Declare class
		model.declareClass(node, new Class(node.getName().getText().trim(),
				node.getKclass(), id));
	}
	
	/*
	 * Returns and increments the current id
	 */
	private int reserveCurrentId() {
		int id = this.currentId;
		this.currentId++;
		return id;
	}

}
