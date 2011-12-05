package minigen.icode;

import minigen.icode.ICode.Arg;
import minigen.icode.ICode.ClassElement;
import minigen.icode.ICode.ClassInit;
import minigen.icode.ICode.ClassTable;
import minigen.icode.ICode.ClassnameStatement;
import minigen.icode.ICode.InitStatement;
import minigen.icode.ICode.ObjectTable;
import minigen.icode.ICode.Program;
import minigen.icode.ICode.Function;
import minigen.icode.ICode.Symbol;
import minigen.icode.ICode.ExecStatement;
import minigen.icode.ICode.IsaStatement;
import minigen.icode.ICode.Type;
import minigen.icode.ICode.TypeInitStatement;
import minigen.icode.ICode.TypeStatement;
import minigen.icode.ICode.TypeSymbol;
import minigen.icode.ICode.TypeTable;

public class ICodeFactory {

	public Program program() {
		Program program = new Program();

		program.getClasses().add(new ClassTable());
		program.getClasses().add(new ObjectTable());
		program.getClasses().add(new TypeTable());

		return program;
	}
	
	public Function function(Symbol name) {
		return new Function(name);
	}

	public ClassElement classElement(Symbol id, Symbol value) {
		return new ClassElement(id, new Symbol("adapts"), value);
	}
	
	public ClassInit classInit(Type type, Symbol name) {
		return new ClassInit(type, name);
	}
	
	public ClassElement typeElement(Symbol id, Symbol value) {
		return new ClassElement(id, new Symbol("subTypes"), value);
	}
	
	public TypeInitStatement typeInitStmt(Symbol name) {
		return new TypeInitStatement(name);
	}
	
	public InitStatement initStmt(Type type, Symbol name) {
		return new InitStatement(type, name);
	}

	public IsaStatement isaStmt(Symbol left, Symbol right) {
		return new IsaStatement(left, right);
	}
	
	public ExecStatement execStmt(Symbol id, Symbol rec) {
		return new ExecStatement(id, rec);
	}

	public Symbol symbol(String str) {
		return new Symbol(str);
	}
	
	public TypeSymbol typeSymbol(Symbol symbol) {
		return new TypeSymbol(symbol.toString());
	}
	
	public TypeSymbol typeSymbol(String symbol) {
		return new TypeSymbol(symbol);
	}
	
	public Type type(TypeTag tag) {
		return new Type(tag);
	}
	
	public Arg arg(String arg) {
		return new Arg(symbol(arg));
	}
	
	public Arg arg(Symbol arg) {
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
