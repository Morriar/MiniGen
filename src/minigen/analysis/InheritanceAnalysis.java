package minigen.analysis;

import java.util.ArrayList;
import java.util.List;

import minigen.exception.SemanticException;
import minigen.model.Class;
import minigen.model.FormalType;
import minigen.model.Model;
import minigen.model.ObjectClass;
import minigen.model.ObjectType;
import minigen.model.Type;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AClassDecl;
import minigen.syntax3.node.ASuperDecls;
import minigen.syntax3.node.ASuperGenericPart;
import minigen.syntax3.node.ASuperGenericTypes;
import minigen.syntax3.node.ASuperType;
import minigen.syntax3.node.EOF;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.PAdditionalSupers;
import minigen.syntax3.node.PSuperAdditionalTypes;

public class InheritanceAnalysis extends DepthFirstAdapter {

	private Model model;
	private Class currentClass = null;
	private Type currentType;

	private List<Class> currentSuperClasses;

	public InheritanceAnalysis(Model classScope) {
		this.model = classScope;
	}

	private void visit(Node node) {

		if (node != null) {
			node.apply(this);
		}
	}

	private Type computeType(Node node) {
		this.currentType = null;
		visit(node);
		Type resultType = this.currentType;
		this.currentType = null;
		return resultType;
	}

	@Override
	public void caseAClassDecl(AClassDecl node) {
		String name = node.getName().getText().trim();

		this.currentClass = model.getClassByNode(node.getName(), node, name);
		this.currentSuperClasses = new ArrayList<Class>();
		visit(node.getSuperDecls());

		this.currentClass = null;
		this.currentSuperClasses = null;
	}

	@Override
	public void caseASuperDecls(ASuperDecls node) {
		Type type = computeType(node.getSuperType());

		Class parent = computeClassParent(node, type);

		this.currentClass.addParent(parent, type.getGenericTypes());
		this.currentSuperClasses.add(parent);

		for (PAdditionalSupers t : node.getAdditionalSupers()) {
			type = computeType(t);

			if (!model.containsClassDeclaration(type.getName())) {
				throw new SemanticException(node.getKsuper(), "class "
						+ type.getName() + " not declared");
			}

			parent = computeClassParent(node, type);

			this.currentClass.addParent(parent, type.getGenericTypes());
			this.currentSuperClasses.add(parent);
		}
	}

	@Override
	public void caseASuperType(ASuperType node) {
		String name = node.getName().getText().trim();

		if (name.equals(ObjectType.getInstance().getName())) {
			this.currentType = ObjectType.getInstance();
		} else {
			Class intro;
			FormalType link = null;
			if (!model.containsClassDeclaration(name)) {
				if (!this.currentClass.isFormalTypeDeclared(name)) {
					throw new SemanticException(node.getName(), "class " + name
							+ " not declared");
				} else {
					intro = this.currentClass;
					link = this.currentClass.getFormalType(name);
				}
			} else {
				intro = model.getClassByName(node.getName(), name);
			}

			this.currentType = new Type(name, intro);
			this.currentType.setFormalTypeLink(link);
		}

		visit(node.getSuperGenericPart());
	}

	@Override
	public void caseASuperGenericPart(ASuperGenericPart node) {
		visit(node.getSuperGenericTypes());
	}

	@Override
	public void caseASuperGenericTypes(ASuperGenericTypes node) {

		Type savedType = this.currentType;
		Type type = computeType(node.getSuperType());
		savedType.addGenericType(type);

		for (PSuperAdditionalTypes t : node.getSuperAdditionalTypes()) {
			type = computeType(t);
			savedType.addGenericType(type);
		}

		this.currentType = savedType;
	}

	/**
	 * Check if a super definition is repeated like: class A super B, B, ...
	 */
	public boolean checkAlreadyInSuper(Class c) {
		return this.currentSuperClasses.contains(c);
	}

	/**
	 * Check if a super definition is not the current class definition like:
	 * class A super A
	 */
	public boolean checkIsItSelf(Class c) {
		if (this.currentClass.getName().equals(c.getName())) {
			return true;
		}
		return false;
	}

	public boolean checkGenericPartArity(Class parent, Type type) {
		if (parent.getFormalTypes().size() != type.getArity()) {
			return false;
		}
		return true;
	}

	/**
	 * Check inheritance loops like: A <: B and B <: A
	 * 
	 * @param type
	 * @return
	 */
	public boolean checkInheritanceLoop(Class parent) {
		if (parent.isSubClassOf(this.currentClass)) {
			return true;
		}
		return false;
	}

	public Class computeClassParent(ASuperDecls node, Type type) {
		if (!model.containsClassDeclaration(type.getName())) {
			throw new SemanticException(node.getKsuper(), "class "
					+ type.getName() + " not declared");
		}

		Class parent = model.getClassByName(node.getKsuper(), type.getName());

		// Check parent is not class it self
		if (checkIsItSelf(parent)) {
			throw new SemanticException(node.getKsuper(), "Class "
					+ parent.getName() + " cannot import itself");
		}

		// Check parent not already in current super clause
		if (checkAlreadyInSuper(parent)) {
			throw new SemanticException(node.getKsuper(), parent.getName()
					+ " already declared as parent in this super clause");
		}

		// Check inheritance loops
		if (checkInheritanceLoop(parent)) {
			throw new SemanticException(node.getKsuper(),
					"Loop inheritance between class "
							+ this.currentClass.getName() + " and parent "
							+ parent.getName());
		}

		// Check generic arity
		if (!checkGenericPartArity(parent, type)) {
			throw new SemanticException(node.getKsuper(), parent.getName()
					+ " expects " + parent.getFormalTypes().size()
					+ " parameter(s) (" + type.getArity() + " are provided)");
		}

		// Check generic types are declared
		for (Type t : type.getGenericTypes()) {
			if (this.currentClass.isFormalTypeDeclared(t.getName())) {
				continue;
			}
			if (!model.containsClassDeclaration(t.getName())) {
				System.out.println(type.getName());
				throw new SemanticException(node.getKsuper(), "class "
						+ t.getName() + " not declared");
			}
		}

		// Check bound conformity
		/*
		 * for( int i = 0; i < type.getArity(); i ++ ) {
		 * 
		 * String name = type.getGenericTypes().get(i).getName(); Type
		 * localType;
		 * 
		 * if(model.containsClassDeclaration(name)) { localType =
		 * model.getClassByName(node.getKsuper(), name).getType(); } else {
		 * localType = this.currentClass.getFormalType(name).getBound(); }
		 * 
		 * Type parentType = parent.getOrderedFormalTypes().get(i).getBound();
		 * 
		 * if( !localType.isSubTypeOf(parentType)) { throw new
		 * SemanticException(node.getKsuper(), "generic type " +
		 * localType.getName() + " must be a subtype of " + parentType.getName()
		 * + " formal type bound"); } }
		 */

		return parent;
	}

	/*
	 * Add Object as parent for all classes that haven't it yet (except Object
	 * itself)
	 */
	@Override
	public void caseEOF(EOF node) {
		for (Class c : model.getClasses()) {
			if (c.getParents().isEmpty() && !c.isObjectIntro()) {
				c.addParent(ObjectClass.getInstance(), new ArrayList<Type>());
			}
		}
	}

}