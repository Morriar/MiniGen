package minigen.icode;

import java.io.FileWriter;
import java.io.IOException;

import minigen.icode.ICode.Arg;
import minigen.icode.ICode.Class;
import minigen.icode.ICode.ClassElement;
import minigen.icode.ICode.ClassInit;
import minigen.icode.ICode.ClassTable;
import minigen.icode.ICode.ClassnameStatement;
import minigen.icode.ICode.ExecStatement;
import minigen.icode.ICode.Function;
import minigen.icode.ICode.InitStatement;
import minigen.icode.ICode.IsaStatement;
import minigen.icode.ICode.ObjectTable;
import minigen.icode.ICode.Program;
import minigen.icode.ICode.Statement;
import minigen.icode.ICode.Symbol;
import minigen.icode.ICode.Type;
import minigen.icode.ICode.TypeInitStatement;
import minigen.icode.ICode.TypeStatement;
import minigen.icode.ICode.TypeSymbol;
import minigen.icode.ICode.TypeTable;

public class ICodeCompiler extends ICodeStubVisitor {

	private FileWriter out;

	public ICodeCompiler(FileWriter out) {
		this.out = out;
	}

	@Override
	public void visit(Program program) {
		println("import java.util.ArrayList;");
		println("import java.util.List;");
		println("import java.util.Map;");
		println("import java.util.HashMap;");
		println();

		println("public class Main {");
		println();
		indent();

		// Tables
		for (Class cls : program.getClasses()) {
			cls.accept(this);
		}

		// Table initialisation
		println();
		println("/* Inits Classes */");
		for (ClassInit init : program.getInitClasses()) {
			init.accept(this);
		}
		println();

		// Type storage
		println();
		println("public static Map<String, TypeTable> types = new HashMap<String, TypeTable>(); ");
		println();

		// Execs
		for (Function fct : program.getFcts()) {
			fct.accept(this);
		}

		// Main method
		println("public static void main(String[] args) {");
		indent();

		// Statements
		println();
		println("/* Statements */");
		println();
		for (Statement stmt : program.getStmts()) {
			stmt.accept(this);
		}

		unindent();
		println("}");
		println();

		unindent();
		println("}");

	}

	@Override
	public void visit(Symbol symbol) {
		print(symbol.toString());
	}

	@Override
	public void visit(Type type) {
		switch (type.getTag()) {
		case CLASS:
			print("ClassTable");
			break;
		case OBJ:
			print("ObjectTable");
			break;
		case TYPE:
			print("TypeTable");
			break;
		default:
			throw new InternalError("Unknonw type tag " + type.getTag());
		}
	}

	@Override
	public void visit(Statement statement) {
		statement.accept(this);
	}

	@Override
	public void visit(ClassInit stmt) {
		printIndent();
		print("public static ");
		stmt.getType().accept(this);
		print(" ");
		stmt.getName().accept(this);
		print(" = new ");
		stmt.getType().accept(this);
		print("(");

		for (int i = 0; i < stmt.getArgs().size(); i++) {
			stmt.getArgs().get(i).accept(this);
			if (i < stmt.getArgs().size() - 1) {
				print(", ");
			}
		}

		print(");");
		println();
	}

	@Override
	public void visit(InitStatement stmt) {
		printIndent();
		stmt.getType().accept(this);
		print(" ");
		stmt.getName().accept(this);
		print(" = new ");
		stmt.getType().accept(this);
		print("(");

		for (int i = 0; i < stmt.getArgs().size(); i++) {
			stmt.getArgs().get(i).accept(this);
			if (i < stmt.getArgs().size() - 1) {
				print(", ");
			}
		}

		print(");");
		println();
	}

	@Override
	public void visit(TypeInitStatement stmt) {
		printIndent();
		print("TypeTable ");
		stmt.getName().accept(this);
		print(" = new TypeTable(");

		for (int i = 0; i < stmt.getArgs().size(); i++) {
			stmt.getArgs().get(i).accept(this);
			if (i < stmt.getArgs().size() - 1) {
				print(", ");
			}
		}

		print(");");
		println();

		printIndent();
		print("types.put(\"");
		stmt.getName().accept(this);
		print("\", ");
		stmt.getName().accept(this);
		print(");");
		println();
	}

	@Override
	public void visit(TypeSymbol symbol) {
		print("types.get(\"" + symbol.toString() + "\")");
	}

	@Override
	public void visit(Function fct) {
		printIndent();
		print("public static void ");
		fct.getName().accept(this);
		print("_exec(ObjectTable rec) {");
		println();
		indent();

		for (Statement stmt : fct.getStmts()) {
			stmt.accept(this);
		}

		unindent();
		println("}");
		println();
	}

	@Override
	public void visit(ClassElement element) {
		printIndent();
		element.getId().accept(this);
		print(".");
		element.getField().accept(this);
		print(".add(");
		element.getValue().accept(this);
		print(");");
		println();
	}

	@Override
	public void visit(IsaStatement stmt) {
		Symbol l = stmt.getLeft();
		Symbol r = stmt.getRight();

		printIndent();
		print("System.out.println(");
		l.accept(this);
		print(".isa(");
		r.accept(this);
		print("));");
		println();
	}

	@Override
	public void visit(ClassnameStatement stmt) {
		printIndent();
		print("System.out.println(");
		stmt.getExp().accept(this);
		print(".getClassName());");
		println();
	}

	@Override
	public void visit(TypeStatement stmt) {
		printIndent();
		print("System.out.println(");
		stmt.getExp().accept(this);
		print(".getType());");
		println();
	}

	@Override
	public void visit(Class cls) {
		cls.accept(this);
	}

	@Override
	public void visit(Arg arg) {
		arg.getArg().accept(this);
	}

	@Override
	public void visit(ClassTable cls) {
		println("public static class ClassTable {");
		indent();
		println("public int id;");
		println("public int color;");
		println("public String name;");
		println("public List<TypeTable> adapts;");
		println();
		println("public ClassTable(int id, int color, String name) {");
		indent();
		println("this.id = id;");
		println("this.color = color;");
		println("this.name = name;");
		println("this.adapts = new ArrayList<TypeTable>();");
		unindent();
		println("}");
		unindent();
		println("}");
	}

	@Override
	public void visit(ExecStatement stmt) {
		printIndent();
		stmt.getId().accept(this);
		print("_exec(");
		stmt.getRec().accept(this);
		print(");");
		println();
	}

	@Override
	public void visit(ObjectTable cls) {
		println("public static class ObjectTable {");
		indent();
		println("public int id;");
		println("public ClassTable cls;");
		println("public TypeTable type;");
		println();
		println("public ObjectTable(int id, ClassTable cls, TypeTable type) {");
		indent();
		println("this.id = id;");
		println("this.cls = cls;");
		println("this.type = type;");
		unindent();
		println("}");
		println();
		println("public String getType() {");
		indent();
		println("return this.type.getType();");
		unindent();
		println("}");
		println();
		println("public String getClassName() {");
		indent();
		println("return this.cls.name;");
		unindent();
		println("}");
		println();

		println("public boolean isa(TypeTable ot) {");
		indent();
		println("return this.type.isa(ot, this.type);");
		unindent();
		println("}");

		unindent();
		println("}");
	}

	@Override
	public void visit(TypeTable cls) {
		println("public static class TypeTable {");
		indent();
		println("public ClassTable cls;");
		println("public Integer index;");
		println("public List<TypeTable> subTypes;");
		println();
		println("public TypeTable(ClassTable cls) {");
		indent();
		println("this.cls = cls;");
		println("this.index = null;");
		println("this.subTypes = new ArrayList<TypeTable>();");
		unindent();
		println("}");
		println();
		println("public TypeTable(Integer index) {");
		indent();
		println("this.cls = null;");
		println("this.index = index;");
		println("this.subTypes = new ArrayList<TypeTable>();");
		unindent();
		println("}");
		println();

		println("public TypeTable(TypeTable type) {");
		indent();
		println("this.cls = type.cls;");
		println("this.index = type.index;");
		println("this.subTypes = type.subTypes;");
		unindent();
		println("}");
		println();

		println("public String getType() {");
		indent();
		println("String typeStr = this.cls.name;");
		println();
		println("if(this.subTypes.size() > 0) {");
		indent();
		println("int i = 0;");
		println("typeStr += \"[\";");
		println("for(TypeTable subType: this.subTypes) {");
		indent();
		println("typeStr += subType.getType();");
		println("if(i < this.subTypes.size() - 1) {");
		indent();
		println("typeStr += \",\";");
		unindent();
		println("}");
		println("i++;");
		unindent();
		println("}");
		println("typeStr += \"]\";");
		unindent();
		println("}");
		println("return typeStr;");
		unindent();
		println("}");
		println();

		println("public boolean isa(TypeTable ot, TypeTable base) {");
		indent();
		println("// Check color");
		println("if(ot.cls.color >= this.cls.adapts.size()) {");
		indent();
		println("return false;");
		unindent();
		println("}");

		println("// Check adaptation");
		println("TypeTable a = this.cls.adapts.get(ot.cls.color);");
		println("if(a == null || a.cls.id != ot.cls.id) {");
		indent();
		println("return false;");
		unindent();
		println("}");

		println("// Replace formal type form adaptation with concrete type from type");
		println("TypeTable toCheck = this.buildAdaptation(a, ot, base);");

		println("for (int i = 0; i < toCheck.subTypes.size(); i++) {");
		indent();
		println("if (!toCheck.subTypes.get(i).isa(ot.subTypes.get(i), toCheck.subTypes.get(i))) {");
		indent();
		println("return false;");
		unindent();
		println("}");
		unindent();
		println("}");
		println("return true;");
		unindent();
		println("}");

		println("public TypeTable buildAdaptation(TypeTable a, TypeTable to, TypeTable base) {");
		indent();

		println("TypeTable result = new TypeTable(to.cls);");

		println("for (TypeTable gt : a.subTypes) {");
		indent();

		println("if (gt.cls == null) {");
		indent();
		println("result.subTypes.add(base.subTypes.get(gt.index));");
		unindent();
		println("} else if (!gt.subTypes.isEmpty()) {");
		indent();
		println("TypeTable resultGt = new TypeTable(gt.cls);");
		println("this.recursiveBuild(gt, resultGt, base);");
		println("result.subTypes.add(resultGt);");
		unindent();
		println("} else {");
		indent();
		println("result.subTypes.add(gt);");
		unindent();
		println("}");
		unindent();
		println("}");
		println("return result;");
		unindent();
		println("}");

		println("private void recursiveBuild(TypeTable t, TypeTable toAdapt, TypeTable base) {");
		indent();

		println("for (TypeTable gt : t.subTypes) {");
		indent();
		println("if (gt.cls == null) {");
		indent();
		println("toAdapt.subTypes.add(base.subTypes.get(gt.index));");
		unindent();
		println("}");
		println("if (!gt.subTypes.isEmpty()) {");
		indent();
		println("TypeTable resultGt = new TypeTable(gt.cls);");
		println("recursiveBuild(gt, resultGt, base);");
		println("toAdapt.subTypes.add(resultGt);");
		unindent();
		println("}");
		unindent();
		println("}");
		unindent();
		println("}");

		unindent();
		println("}");
	}

	/*
	 * Indentation facilities
	 */

	public void println(String str) {
		System.out.println(getIndent() + str);
		try {
			out.write(getIndent() + str + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void println() {
		System.out.println();
		try {
			out.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(String str) {
		System.out.print(str);
		try {
			out.write(str);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void printIndent() {
		System.out.print(getIndent());
		try {
			out.write(getIndent());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	int currentIndent = 0;
	static int INDENTSIZE = 4;

	public void indent() {
		this.currentIndent += INDENTSIZE;
	}

	public void unindent() {
		this.currentIndent -= INDENTSIZE;
	}

	public String getIndent() {
		String str = "";
		for (int i = 0; i < currentIndent; i++) {
			str += " ";
		}
		return str;
	}

}