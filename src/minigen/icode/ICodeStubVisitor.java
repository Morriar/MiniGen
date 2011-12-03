package minigen.icode;

import minigen.icode.ICode.Arg;
import minigen.icode.ICode.Class;
import minigen.icode.ICode.ClassElement;
import minigen.icode.ICode.ClassTable;
import minigen.icode.ICode.ClassnameStatement;
import minigen.icode.ICode.InitStatement;
import minigen.icode.ICode.IsaStatement;
import minigen.icode.ICode.ObjectTable;
import minigen.icode.ICode.Program;
import minigen.icode.ICode.Statement;
import minigen.icode.ICode.Symbol;
import minigen.icode.ICode.Type;
import minigen.icode.ICode.TypeStatement;
import minigen.icode.ICode.TypeTable;

public class ICodeStubVisitor implements ICode.ICodeVisitor {

	@Override
	public void visit(Program program) {
	}

	@Override
	public void visit(Statement statement) {
	}

	@Override
	public void visit(Symbol symbol) {
	}

	@Override
	public void visit(Type type) {
	}

	@Override
	public void visit(IsaStatement stmt) {
	}

	@Override
	public void visit(TypeStatement stmt) {
	}

	@Override
	public void visit(ClassnameStatement stmt) {
	}

	@Override
	public void visit(Class cls) {
	}

	@Override
	public void visit(ClassTable cls) {
	}

	@Override
	public void visit(ObjectTable cls) {
	}

	@Override
	public void visit(TypeTable cls) {
	}

	@Override
	public void visit(Arg arg) {
	}

	@Override
	public void visit(InitStatement stmt) {
	}

	@Override
	public void visit(ClassElement element) {
	}
}
