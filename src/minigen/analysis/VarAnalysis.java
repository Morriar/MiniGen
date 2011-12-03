package minigen.analysis;

import minigen.model.Model;
import minigen.model.Scope;
import minigen.model.Type;
import minigen.model.Var;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AAssignInstr;
import minigen.syntax3.node.ADeclInstr;
import minigen.syntax3.node.AType;
import minigen.syntax3.node.AVarExp;
import minigen.syntax3.node.Node;

public class VarAnalysis extends DepthFirstAdapter {

	private Model model;
	private Scope scope;
	private Type currentType;

	public VarAnalysis(Model model, Scope scope) {
		this.model = model;
		this.scope = scope;
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

	@Override
	public void caseADeclInstr(ADeclInstr node) {
		String name = node.getId().getText();
		Type type = computeType(node.getExp());
		scope.declareVar(node.getKvar(), name, type);
	}

	@Override
	public void caseAAssignInstr(AAssignInstr node) {
		String name = node.getId().getText();
		Var var = this.scope.getVar(node.getId(), name);
		var.setType(computeType(node.getExp()));
	}

	@Override
	public void caseAVarExp(AVarExp node) {
		String name = node.getId().getText();
		this.currentType = this.scope.getVar(node.getId(), name).getType();
	}

	@Override
	public void caseAType(AType node) {
		if (!this.model.containsTypeDeclaration(node)) {
			throw new InternalError("No type declaration for node " + node);
		}
		this.currentType = this.model.getTypeByNode(node);
	}

}
