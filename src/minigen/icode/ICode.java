package minigen.icode;

import java.util.ArrayList;
import java.util.List;

public abstract class ICode {

	public abstract void accept(ICodeVisitor visitor);

	public static interface ICodeVisitor {
		void visit(Program program);

		void visit(Statement statement);

		void visit(Symbol symbol);

		void visit(Arg arg);

		void visit(Type type);

		void visit(ClassElement element);

		void visit(InitStatement stmt);

		void visit(IsaStatement stmt);

		void visit(ClassnameStatement stmt);

		void visit(TypeStatement stmt);

		void visit(Class cls);

		void visit(ClassTable cls);

		void visit(ObjectTable cls);

		void visit(TypeTable cls);
	}

	/**
	 * ICode Program
	 */
	public static class Program {
		private List<Class> classes;
		private List<Statement> initClasses;
		private List<Statement> initAdapts;
		private List<Statement> initTypes;
		private List<Statement> initObjs;
		private List<Statement> initElems;
		private List<Statement> stmts;

		public Program() {
			this.classes = new ArrayList<Class>();
			this.initClasses = new ArrayList<Statement>();
			this.initAdapts = new ArrayList<Statement>();
			this.initTypes = new ArrayList<Statement>();
			this.initObjs = new ArrayList<Statement>();
			this.initElems = new ArrayList<Statement>();
			this.stmts = new ArrayList<Statement>();
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public List<Class> getClasses() {
			return this.classes;
		}

		public void setClasses(List<Class> classes) {
			this.classes = classes;
		}

		public List<Statement> getStmts() {
			return stmts;
		}

		public void setStmts(List<Statement> stmts) {
			this.stmts = stmts;
		}

		public List<Statement> getInitClasses() {
			return initClasses;
		}

		public List<Statement> getInitAdapts() {
			return initAdapts;
		}

		public List<Statement> getInitTypes() {
			return initTypes;
		}

		public List<Statement> getInitObjs() {
			return initObjs;
		}

		public List<Statement> getInitElems() {
			return initElems;
		}
	}

	/**
	 * ICode Symbol
	 */
	public static class Symbol extends ICode {
		private String symbol;

		public Symbol(String symbol) {
			this.symbol = symbol;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public String toString() {
			return symbol;
		}
	}

	/**
	 * ICode Type
	 */
	public static class Type extends ICode {
		private TypeTag tag;

		public Type(TypeTag tag) {
			this.tag = tag;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public TypeTag getTag() {
			return tag;
		}
	}

	/**
	 * ICode Param Can be given to a InitStatement
	 */
	public static class Arg extends ICode {
		private String arg;

		public Arg(String arg) {
			this.arg = arg;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public String getArg() {
			return this.arg;
		}
	}

	/**
	 * ICode Statement Can be added to a program
	 */
	public static abstract class Statement extends ICode {
	}

	/**
	 * ICode InitStatement Init a table representation
	 */
	public static class InitStatement extends Statement {
		private Type type;
		private Symbol name;
		private List<Arg> args;

		public InitStatement(Type type, Symbol name) {
			this.type = type;
			this.name = name;
			this.args = new ArrayList<Arg>();
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public Type getType() {
			return type;
		}

		public Symbol getName() {
			return name;
		}

		public List<Arg> getArgs() {
			return args;
		}
	}

	/**
	 * ICode ClassElement Elements of Class like adapts, types...
	 */
	public static class ClassElement extends Statement {
		private Symbol id;
		private Symbol field;
		private String value;

		public ClassElement(Symbol id, Symbol field, String value) {
			this.id = id;
			this.field = field;
			this.value = value;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public Symbol getId() {
			return id;
		}

		public Symbol getField() {
			return field;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * ICode IsaStatement
	 */
	public static class IsaStatement extends Statement {
		private Symbol left;
		private Symbol right;

		public IsaStatement(Symbol left, Symbol right) {
			this.left = left;
			this.right = right;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public Symbol getLeft() {
			return left;
		}

		public Symbol getRight() {
			return right;
		}
	}

	public static class TypeStatement extends Statement {
		private Symbol exp;

		public TypeStatement(Symbol exp) {
			this.exp = exp;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public Symbol getExp() {
			return exp;
		}
	}

	public static class ClassnameStatement extends Statement {
		private Symbol exp;

		public ClassnameStatement(Symbol exp) {
			this.exp = exp;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public Symbol getExp() {
			return exp;
		}
	}

	public static class Class extends ICode {
		private Symbol name;

		public Class(Symbol name) {
			super();
			this.name = name;
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}

		public Symbol getName() {
			return name;
		}
	}

	public static class ClassTable extends Class {
		public ClassTable() {
			super(new Symbol("ClassTable"));
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ObjectTable extends Class {
		public ObjectTable() {
			super(new Symbol("ObjectTable"));
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class TypeTable extends Class {
		public TypeTable() {
			super(new Symbol("TypeTable"));
		}

		public void accept(ICodeVisitor visitor) {
			visitor.visit(this);
		}
	}

}
