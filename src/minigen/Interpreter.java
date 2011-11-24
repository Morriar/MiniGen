package minigen;

import minigen.exception.SemanticException;
import minigen.model.Adaptation;
import minigen.model.Model;
import minigen.model.Type;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AGenericPart;
import minigen.syntax3.node.AGenericTypes;
import minigen.syntax3.node.AIsaInstr;
import minigen.syntax3.node.AType;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.PAdditionalTypes;

public class Interpreter extends DepthFirstAdapter {

	private Model model;
	private Type currentType;

	public Interpreter(Model model) {
		this.model = model;

		System.out.println("------- Statistics -------");
		System.out.println();

		System.out.println("Classes found: " + model.getClasses().size());
		System.out.println();

		System.out.println("Max color: " + model.getMaxColor());
		System.out.println();

		// System.out.println("Inheritance relations found:");
		// for (Class c : model.getClasses()) {
		// System.out.println();
		// System.out.println(" - class " + c.toStringWithParents() + " get " +
		// c.getSubClasses().size() + " subclasses (depth : "+ c.getDepth()
		// +")");
		// }
		// System.out.println();

		System.out.println("Adaptations tables:");
		for (minigen.model.Class c : model.getClasses()) {
			System.out.println(" - For class " + c + "(" + c.getColor()
					+ ") : " + c.toStringWithAdaptationsTable());
		}
		System.out.println();

		int nbCases = 0;
		int nbNull = 0;
		for (minigen.model.Class c : model.getClasses()) {
			nbCases += c.getAdaptationsTable().length;
			for (Adaptation a : c.getAdaptationsTable()) {
				if (a == null) {
					nbNull++;
				}
			}
		}
		double percent = nbNull * 100 / nbCases;
		System.out.println("Taux de trous: " + nbNull + "/" + nbCases + " ("
				+ percent + "%)");

		System.out.println("-------------------------------");

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
	 * Write result of type comparaison on console
	 */
	@Override
	public void caseAIsaInstr(AIsaInstr node) {

		// Compute types
		Type leftType = computeType(node.getLeft());
		Type rightType = computeType(node.getRight());

		// Check isa and display results
		System.out.println(" - " + leftType + " isa " + rightType + " => "
				+ leftType.isa(rightType, leftType));

	}

	@Override
	public void caseAType(AType node) {

		String name = node.getName().getText().trim();
		if (!model.containsClassDeclaration(name)) {
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

}
