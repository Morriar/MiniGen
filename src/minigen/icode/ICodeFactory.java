package minigen.icode;

import minigen.icode.ICode.Arg;
import minigen.icode.ICode.ClassElement;
import minigen.icode.ICode.ClassTable;
import minigen.icode.ICode.ClassnameStatement;
import minigen.icode.ICode.InitStatement;
import minigen.icode.ICode.ObjectTable;
import minigen.icode.ICode.Program;
import minigen.icode.ICode.Symbol;
import minigen.icode.ICode.IsaStatement;
import minigen.icode.ICode.Type;
import minigen.icode.ICode.TypeStatement;
import minigen.icode.ICode.TypeTable;

public class ICodeFactory {

	public Program program() {
		Program program = new Program();

		program.getClasses().add(new ClassTable());
		program.getClasses().add(new ObjectTable());
		program.getClasses().add(new TypeTable());

		return program;
	}

	public ClassElement classElement(Symbol id, String value) {
		return new ClassElement(id, new Symbol("adapts"), value);
	}
	
	public ClassElement typeElement(Symbol id, Symbol value) {
		return new ClassElement(id, new Symbol("subTypes"), value.toString());
	}
	
	public InitStatement initStmt(Type type, Symbol name) {
		return new InitStatement(type, name);
	}

	public IsaStatement isaStmt(Symbol left, Symbol right) {
		return new IsaStatement(left, right);
	}

	public Symbol symbol(String str) {
		return new Symbol(str);
	}
	
	public Type type(TypeTag tag) {
		return new Type(tag);
	}
	
	public Arg arg(String arg) {
		return new Arg(arg);
	}

	public Symbol symbol(int i) {
		return new Symbol(Integer.toString(i));
	}

	public ClassnameStatement classnameStatement(Symbol exp) {
		return new ClassnameStatement(exp);
	}

	public TypeStatement typeStatement(Symbol exp) {
		return new TypeStatement(exp);
	}
}
