package minigen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import minigen.model.Class;
import minigen.model.Model;
import minigen.model.Scope;
import minigen.model.Type;
import minigen.model.Var;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AAssignInstr;
import minigen.syntax3.node.AClassDecl;
import minigen.syntax3.node.AClassnameInstr;
import minigen.syntax3.node.ADeclInstr;
import minigen.syntax3.node.AExecInstr;
import minigen.syntax3.node.AGenericPart;
import minigen.syntax3.node.AGenericTypes;
import minigen.syntax3.node.AIsaInstr;
import minigen.syntax3.node.AType;
import minigen.syntax3.node.ATypeInstr;
import minigen.syntax3.node.AVarExp;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.PAdditionalTypes;
import minigen.syntax3.node.PInstr;

public class Interpreter extends DepthFirstAdapter {

	private Model model;
	private Scope currentScope;
	private Type currentType;
	private Class currentClass;
	private Type currentInstanceType;
	private HashMap<Class, Scope> classScopes;
	private HashMap<Class, List<PInstr>> instrsByClasses;

	public Interpreter(Model model) {
		this.model = model;
		this.currentScope = new Scope();
		this.classScopes = new HashMap<Class, Scope>();
		this.instrsByClasses = new HashMap<Class, List<PInstr>>();
	}

	private Type computeType(Node node) {
		this.currentType = null;
		visit(node);
		Type resultType = this.currentType;
		this.currentType = null;
		return resultType;
	}

	private void visit(Node node) {
		if (node != null) {
			node.apply(this);
		}
	}

	/*
	 * Write result of type comparison on console
	 */
	@Override
	public void caseAIsaInstr(AIsaInstr node) {

		// Compute types
		Type leftType = computeType(node.getExp());
		Type rightType = computeType(node.getType());

		// Check isa and display results
		System.out.println(leftType + " isa " + rightType + " => "
				+ leftType.isa(rightType, leftType));

	}
	
	@Override
	public void caseAClassDecl(AClassDecl node) {
		Class currentClass = model.getClassByNode(node.getKclass(), node, "");
		this.classScopes.put(currentClass, new Scope());
		List<PInstr> instrs = new ArrayList<PInstr>();
		for (PInstr instr : node.getInstrs()) {
			instrs.add(instr);
		}
		this.instrsByClasses.put(currentClass, instrs);
	}
	
	@Override
	public void caseAType(AType node) {
		String name = node.getName().getText().trim();
		
		if(this.currentClass == null) {
			this.currentType = new Type(name, model.getClassByName(node.getName(),
					name));
			visit(node.getGenericPart());
		} else {
			if(this.currentClass.isFormalTypeDeclared(name)) {
				Integer ftPos = this.currentClass.getFormalType(name).getPosition();
				this.currentType = this.currentInstanceType.getGenericTypes().get(ftPos);
				visit(node.getGenericPart());
			} else {
				this.currentType = new Type(name, model.getClassByName(node.getName(),
						name));
				visit(node.getGenericPart());
			}
		}
	}

	@Override
	public void caseAGenericPart(AGenericPart node) {
		visit(node.getGenericTypes());
	}

	@Override
	public void caseAGenericTypes(AGenericTypes node) {
		Type savedType = this.currentType;

		Type type = computeType(node.getType());
		savedType.addGenericType(type);
		type = null;

		for (PAdditionalTypes t : node.getAdditionalTypes()) {
			type = computeType(t);
			savedType.addGenericType(type);
			type = null;
		}

		this.currentType = savedType;
	}
	
	@Override
	public void caseADeclInstr(ADeclInstr node) {
		String name = node.getId().getText();
		Type type = computeType(node.getExp());
		this.currentScope.declareVar(node.getKvar(), name, type);
	}
	
	@Override
	public void caseAAssignInstr(AAssignInstr node) {
		String name = node.getId().getText();
		Var var = this.currentScope.getVar(node.getId(), name);
		var.setType(computeType(node.getExp()));
	}

	@Override
	public void caseATypeInstr(ATypeInstr node) {
		System.out.println(computeType(node.getExp()));
	}
	
	@Override
	public void caseAVarExp(AVarExp node) {
		String name = node.getId().getText();
		this.currentType = this.currentScope.getVar(node.getId(), name)
				.getType();
	}
	
	@Override
	public void caseAClassnameInstr(AClassnameInstr node) {
		System.out.println(computeType(node.getExp()).getIntro().getName());
	}
	
	@Override
	public void caseAExecInstr(AExecInstr node) {
		Var var = this.currentScope.getVar(node.getId(), node.getId().getText());
		this.currentClass = var.getType().getIntro();
		this.currentInstanceType = var.getType();
		Scope savedScope = this.currentScope;
		this.currentScope = this.classScopes.get(currentClass);
		for(PInstr instr: this.instrsByClasses.get(currentClass)) {
			instr.apply(this);
		}
		this.currentInstanceType = null;
		this.currentClass = null;
		this.currentScope = savedScope;
	}

}
