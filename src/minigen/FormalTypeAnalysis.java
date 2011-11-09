package minigen;

import minigen.model.Class;
import minigen.model.FormalType;
import minigen.model.Model;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AAdditionalFormalTypes;
import minigen.syntax3.node.AClassDecl;
import minigen.syntax3.node.AFormalDecl;
import minigen.syntax3.node.AFormalDecls;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.PAdditionalFormalTypes;
import minigen.syntax3.node.PFormalDecl;
import minigen.syntax3.node.Token;
import minigen.exception.*;

public class FormalTypeAnalysis extends DepthFirstAdapter {

	private Model model;
	private Class currentClass;
	private int currentPos;

	public FormalTypeAnalysis(Model classScope) {
		this.model = classScope;
	}

	private void visit(Node node) {
		if (node != null) {
			node.apply(this);
		}
	}

	@Override
	public void caseAClassDecl(AClassDecl node) {
		String name = node.getName().getText().trim();

		this.currentClass = model.getClassByNode(node.getName(), node, name);

		visit(node.getFormalDecls());

		this.currentClass = null;
	}

	@Override
	public void caseAFormalDecls(AFormalDecls node) {
		this.currentPos = -1;
		for (PFormalDecl f : node.getFormalDecl()) {
			visit(f);
		}
		this.currentPos = -1;
	}

	@Override
	public void caseAFormalDecl(AFormalDecl node) {
		String name = node.getName().getText().trim();

		this.currentPos++;
		FormalType ftype = computeFormalType(node.getName(), name);
		this.currentClass.addFormalType(ftype);

		for (PAdditionalFormalTypes f : node.getAdditionalFormalTypes()) {
			visit(f);
		}
	}

	@Override
	public void caseAAdditionalFormalTypes(AAdditionalFormalTypes node) {
		String name = node.getName().getText().trim();

		this.currentPos++;
		FormalType ftype = computeFormalType(node.getName(), name);
		this.currentClass.addFormalType(ftype);
	}

	/*
	 * Check formal type validity
	 */
	private FormalType computeFormalType(Token token, String name) {

		// Not already declared in local class
		if (this.currentClass.isFormalTypeDeclared(name)) {
			throw new SemanticException(token, "formal type " + name
					+ " already declared");
		}

		return new FormalType(name, this.currentPos);
	}

}
