package minigen.analysis;

import minigen.exception.SemanticException;
import minigen.model.Class;
import minigen.model.Model;
import minigen.model.Type;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AClassDecl;
import minigen.syntax3.node.AGenericPart;
import minigen.syntax3.node.AGenericTypes;
import minigen.syntax3.node.ANewExp;
import minigen.syntax3.node.ANewInstr;
import minigen.syntax3.node.AType;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.PAdditionalTypes;
import minigen.syntax3.node.PInstr;

public class TypeAnalysis extends DepthFirstAdapter {

	private Model model;
	private Type currentType;
	private Class currentClass;

	public TypeAnalysis(Model model) {
		this.model = model;
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
	public void caseANewExp(ANewExp node) {
		computeType(node.getType());
	}

	@Override
	public void caseANewInstr(ANewInstr node) {
		computeType(node.getType());
	}

	@Override
	public void caseAType(AType node) {

		String name = node.getName().getText().trim();
		if (!model.containsClassDeclaration(name)) {
			if (this.currentClass != null
					&& this.currentClass.isFormalTypeDeclared(name)) {
				return;
			}
			throw new SemanticException(node.getName(), "class " + name
					+ " not declared");
		}
		this.currentType = new Type(name, model.getClassByName(node.getName(),
				name));

		visit(node.getGenericPart());

		if (this.currentType.getIntro().getArity() != this.currentType
				.getArity()) {
			throw new SemanticException(node.getName(), this.currentType
					.getIntro().getName()
					+ " expects "
					+ this.currentType.getIntro().getArity()
					+ " parameter(s) ("
					+ this.currentType.getArity()
					+ " are provided)");
		}

		// Register type in model
		this.model.declareType(node, this.currentType);
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
	public void caseAClassDecl(AClassDecl node) {
		this.currentClass = model.getClassByNode(node.getKclass(), node, "");
		for (PInstr instr : node.getInstrs()) {
			instr.apply(this);
		}
		this.currentClass = null;
	}

}
