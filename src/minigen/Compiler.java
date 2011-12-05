package minigen;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minigen.icode.ICode.ClassElement;
import minigen.icode.ICode.ClassInit;
import minigen.icode.ICode.ExecStatement;
import minigen.icode.ICode.Function;
import minigen.icode.ICode.InitStatement;
import minigen.icode.ICode.Program;
import minigen.icode.ICode.Statement;
import minigen.icode.ICode.Symbol;
import minigen.icode.ICode.TypeInitStatement;
import minigen.icode.ICode.TypeSymbol;
import minigen.icode.ICodeCompiler;
import minigen.icode.ICodeFactory;
import minigen.icode.TypeTag;
import minigen.model.Adaptation;
import minigen.model.Class;
import minigen.model.Model;
import minigen.model.Scope;
import minigen.model.Type;
import minigen.model.Var;
import minigen.syntax3.analysis.DepthFirstAdapter;
import minigen.syntax3.node.AAssignInstr;
import minigen.syntax3.node.AClassDecl;
import minigen.syntax3.node.AClassnameInstr;
import minigen.syntax3.node.ADeclInstr;
import minigen.syntax3.node.AExecInstr;
import minigen.syntax3.node.AGenericPart;
import minigen.syntax3.node.AGenericTypes;
import minigen.syntax3.node.AIsaInstr;
import minigen.syntax3.node.ANewExp;
import minigen.syntax3.node.ANewInstr;
import minigen.syntax3.node.AProgram;
import minigen.syntax3.node.AType;
import minigen.syntax3.node.ATypeInstr;
import minigen.syntax3.node.AVarExp;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.PAdditionalTypes;
import minigen.syntax3.node.PClassDecl;
import minigen.syntax3.node.PInstr;

public class Compiler extends DepthFirstAdapter {

	private Model model;

	private ICodeFactory factory;
	private ICodeCompiler builder;

	private Scope currentScope;
	private Program currentProgram;
	private Symbol currentSymbol;
	private Var currentVar;
	private Class currentClass;
	private Function currentFunction;
	private List<Statement> currentStatements;

	private boolean newFlag = false;

	private Map<String, Symbol> typeSymbols;
	private Map<Var, Symbol> symbolsByVars;

	private int objCount;
	private int clsCount;

	public Compiler(Model model, FileWriter out) {
		this.model = model;
		this.currentScope = new Scope();
		this.symbolsByVars = new HashMap<Var, Symbol>();
		this.typeSymbols = new HashMap<String, Symbol>();

		this.factory = new ICodeFactory();
		this.builder = new ICodeCompiler(out);

		this.objCount = 0;
		this.clsCount = 0;
	}

	private Type currentType;

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
	public void caseAProgram(AProgram node) {
		this.currentProgram = factory.program();
		this.currentStatements = this.currentProgram.getStmts();

		// Generate Object class
		minigen.model.Class cls = model.getClassByName(null, "Object");

		buildClsInit(cls);

		for (PClassDecl clss : node.getClasses()) {
			clss.apply(this);
		}

		for (PInstr instrs : node.getInstrs()) {
			instrs.apply(this);
		}

		builder.visit(this.currentProgram);
	}

	@Override
	public void caseAClassDecl(AClassDecl node) {
		this.currentClass = model.getClassByNode(node.getKclass(), node, "");

		// Switch scopes
		Scope savedScope = this.currentScope;
		this.currentScope = new Scope();

		// this.classScopes.put(this.currentClass, new Scope());
		buildClsInit(this.currentClass);

		// Build exec fct if class decl contains instrs
		if (!node.getInstrs().isEmpty()) {
			this.currentFunction = factory.function(factory
					.symbol(this.currentClass.getName()));
			this.currentProgram.getFcts().add(this.currentFunction);

			// Switch statements scopes
			List<Statement> savedStatements = this.currentStatements;
			this.currentStatements = this.currentFunction.getStmts();

			// Switch type scopes
			Map<String, Symbol> savedTypes = this.typeSymbols;
			this.typeSymbols = new HashMap<String, Symbol>();

			// Visit class stmts
			for (PInstr instr : node.getInstrs()) {
				instr.apply(this);
			}

			// Switch statements and type scopes
			this.currentStatements = savedStatements;
			this.typeSymbols = savedTypes;
		}
		this.currentClass = null;
		this.currentFunction = null;

		// Switch scopes
		this.currentScope = savedScope;
	}

	@Override
	public void caseAIsaInstr(AIsaInstr node) {

		this.currentSymbol = null;
		node.getExp().apply(this);
		Symbol leftSymbol = this.currentSymbol;

		this.currentSymbol = null;
		node.getType().apply(this);
		Symbol rightSymbol = this.currentSymbol;

		if (this.currentFunction == null) {
			this.currentStatements
					.add(factory.isaStmt(leftSymbol, rightSymbol));
		} else {
			this.currentFunction.getStmts().add(
					factory.isaStmt(leftSymbol, rightSymbol));
		}

		this.currentSymbol = null;
	}

	@Override
	public void caseAVarExp(AVarExp node) {
		String name = node.getId().getText();
		this.currentType = this.currentScope.getVar(node.getId(), name)
				.getType();
		this.currentSymbol = this.symbolsByVars.get(this.currentScope.getVar(
				node.getId(), name));
	}

	@Override
	public void caseAType(AType node) {
		String name = node.getName().getText().trim();

		if (this.currentClass != null
				&& this.currentClass.isFormalTypeDeclared(name)) {
			this.currentType = new Type(name, null);

			visit(node.getGenericPart());

			if (this.newFlag) {
				this.currentSymbol = factory.symbol("rec.type.subTypes.get("
						+ this.currentClass.getFormalType(name).getPosition()
						+ ").cls");
			} else {
				// Build a type table
				this.currentSymbol = factory.symbol("type_"
						+ this.currentType.toSymbol());
				this.objCount++;
				buildTypeTable(this.currentType, this.currentSymbol, 0);
			}
		} else {
			Class cls = model.getClassByName(node.getName(), name);
			this.currentType = new Type(name, cls);

			visit(node.getGenericPart());

			if (this.newFlag) {
				this.currentSymbol = factory.symbol("cls_"
						+ this.currentType.getName());
			} else {
				// Build a type table
				this.currentSymbol = factory.symbol("type_"
						+ this.currentType.toSymbol());
				this.objCount++;
				buildTypeTable(this.currentType, this.currentSymbol, 0);
			}
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

	@Override
	public void caseAAssignInstr(AAssignInstr node) {
		Var var = this.currentScope
				.getVar(node.getId(), node.getId().getText());
		this.currentVar = var;
		node.getExp().apply(this);
		var.setType(this.currentType);
		this.currentVar = null;
		Symbol ref = this.currentSymbol;
		this.symbolsByVars.put(var, ref);
	}

	@Override
	public void caseADeclInstr(ADeclInstr node) {
		String name = node.getId().getText();
		Type type = computeType(node.getExp());
		this.currentScope.declareVar(node.getKvar(), name, type);

		Var var = this.currentScope
				.getVar(node.getId(), node.getId().getText());
		this.currentVar = var;
		node.getExp().apply(this);
		this.currentVar = null;
		Symbol ref = this.currentSymbol;
		this.symbolsByVars.put(var, ref);
	}

	@Override
	public void caseANewExp(ANewExp node) {

		// Compute type
		this.newFlag = true;
		Type type = computeType(node.getType());
		this.newFlag = false;

		Class cls = type.getIntro();

		Integer objId = this.objCount;
		Symbol tObjName;
		if (this.currentVar == null) {
			tObjName = factory.symbol("anon_" + objId);
		} else {
			tObjName = factory.symbol(this.currentVar.getName() + "_" + objId);
		}
		Symbol tClsName = factory.symbol("cls_" + cls.getName());
		Symbol tTypeName = factory.symbol("type_" + type.toSymbol());
		this.objCount++;

		Symbol typeInit = buildTypeTable(type, tTypeName, 0);
		InitStatement objInit = factory.initStmt(factory.type(TypeTag.OBJ),
				tObjName);
		objInit.getArgs().add(factory.arg(objId.toString()));
		objInit.getArgs().add(factory.arg(tClsName.toString()));
		objInit.getArgs().add(factory.arg(typeInit));

		this.currentSymbol = tObjName;
		this.currentStatements.add(objInit);
		this.currentType = type;
	}

	@Override
	public void caseANewInstr(ANewInstr node) {
		// Compute type
		this.newFlag = true;
		Type type = computeType(node.getType());
		this.newFlag = false;

		Class cls = type.getIntro();

		Integer objId = this.objCount;
		Symbol tObjName;
		if (this.currentVar == null) {
			tObjName = factory.symbol("anon_" + objId);
		} else {
			tObjName = factory.symbol(this.currentVar.getName() + "_" + objId);
		}
		Symbol tClsName = factory.symbol("cls_" + cls.getName());
		Symbol tTypeName = factory.symbol("type_" + tObjName.toString());
		this.objCount++;

		Symbol typeInit = buildTypeTable(type, tTypeName, 0);
		InitStatement objInit = factory.initStmt(factory.type(TypeTag.OBJ),
				tObjName);
		objInit.getArgs().add(factory.arg(objId.toString()));
		objInit.getArgs().add(factory.arg(tClsName.toString()));
		objInit.getArgs().add(factory.arg(typeInit));

		this.currentSymbol = tObjName;
		this.currentStatements.add(objInit);
	}

	@Override
	public void caseAClassnameInstr(AClassnameInstr node) {
		node.getExp().apply(this);

		if (this.currentFunction == null) {
			this.currentStatements.add(factory
					.classnameStatement(this.currentSymbol));
		} else {
			this.currentFunction.getStmts().add(
					factory.classnameStatement(this.currentSymbol));
		}
	}

	@Override
	public void caseATypeInstr(ATypeInstr node) {
		node.getExp().apply(this);

		if (this.currentFunction == null) {
			this.currentStatements.add(factory
					.typeStatement(this.currentSymbol));
		} else {
			this.currentFunction.getStmts().add(
					factory.typeStatement(this.currentSymbol));
		}
	}

	@Override
	public void caseAExecInstr(AExecInstr node) {
		Class calledClass = this.currentScope
				.getVar(node.getId(), node.getId().getText()).getType()
				.getIntro();

		Var var = this.currentScope
				.getVar(node.getId(), node.getId().getText());
		Symbol rec = this.symbolsByVars.get(var);
		ExecStatement fct = factory.execStmt(
				factory.symbol(calledClass.getName()), rec);

		if (this.currentFunction == null) {
			this.currentStatements.add(fct);
		} else {
			this.currentFunction.getStmts().add(fct);
		}
	}

	/* Utility methods */

	public TypeSymbol buildTypeTable(Type type, Symbol tObjName, int index) {
		Symbol tGenName = factory.symbol("type_" + type.toSymbol());

		if (this.typeSymbols.containsKey(tGenName.toString())) {
			this.typeSymbols.get(tGenName.toString());
			this.currentSymbol = factory.typeSymbol(tGenName);
		} else {
			TypeInitStatement tInit = factory.typeInitStmt(tGenName);

			// Special construct for FT
			if (type.getIntro() == null) {
				tInit.getArgs().add(
						factory.arg("rec.type.subTypes.get(" + index + ")"));
			} else {
				tInit.getArgs().add(factory.arg("cls_" + type.getName()));
			}
			this.currentStatements.add(tInit);

			// Add generic concretes types
			int i = 0;
			for (Type genType : type.getGenericTypes()) {
				buildTypeTable(genType, tGenName, i);
				ClassElement tElem = factory.typeElement(
						factory.typeSymbol(tInit.getName()),
						factory.typeSymbol(this.currentSymbol));
				this.currentStatements.add(tElem);
				i++;
			}
			this.typeSymbols.put(tInit.getName().toString(), tInit.getName());
			this.currentSymbol = factory.typeSymbol(tInit.getName());
		}
		return factory.typeSymbol(tGenName);
	}

	public InitStatement buildGenInit(Type t, Symbol adName) {
		Symbol atypeName = factory.symbol("gen_" + adName.toString() + "_"
				+ t.getName());

		// Init the gen table
		InitStatement genInit = factory.initStmt(factory.type(TypeTag.TYPE),
				atypeName);
		this.currentStatements.add(genInit);

		// Set link to real type or formal type index
		if (t.isLinkedToFormalType()) {
			genInit.getArgs().add(
					factory.arg(t.getFormalType().getPosition().toString()));
		} else {
			genInit.getArgs().add(factory.arg("cls_" + t.getName()));
		}

		// Build gen part of the type
		int i = 0;
		for (Type st : t.getGenericTypes()) {
			// Build gen table
			InitStatement genTable = buildGenInit(st,
					factory.symbol(atypeName.toString() + "_" + i));

			// Add it to gen types list
			ClassElement clsElem = factory.typeElement(genInit.getName(),
					genTable.getName());
			this.currentStatements.add(clsElem);
			i++;
		}

		return genInit;
	}

	public InitStatement buildAdaptInit(Adaptation a, Class c) {
		Symbol tAdName = factory.symbol("adapt_" + c.getName() + a.getName());
		InitStatement adInit = factory.initStmt(factory.type(TypeTag.TYPE),
				tAdName);
		this.currentStatements.add(adInit);

		adInit.getArgs().add(factory.arg("cls_" + a.getName()));

		// For each gen type of adaptation
		int i = 0;
		for (Type t : a.getTypes()) {
			// Build gen table
			InitStatement genTable = buildGenInit(t,
					factory.symbol(tAdName.toString() + "_" + i));

			// Add gen table to adaptation gen list
			ClassElement clsElem = factory.typeElement(adInit.getName(),
					genTable.getName());
			this.currentStatements.add(clsElem);
			i++;
		}

		return adInit;
	}

	public ClassInit buildClsInit(Class cls) {

		Symbol tClsName = factory.symbol("cls_" + cls.getName());
		Integer clsId = this.clsCount;
		this.clsCount++;

		ClassInit clsInit = factory.classInit(factory.type(TypeTag.CLASS),
				tClsName);
		this.currentProgram.getInitClasses().add(clsInit);

		// Add class infos
		clsInit.getArgs().add(factory.arg(clsId.toString())); // id
		clsInit.getArgs().add(factory.arg(cls.getColor().toString())); // color
		clsInit.getArgs().add(factory.arg("\"" + cls.getName() + "\"")); // name

		for (int color = 0; color < cls.getAdaptationsTable().length; color++) {
			if (cls.getAdaptationsTable()[color] != null) {
				InitStatement tAd = buildAdaptInit(
						cls.getAdaptationsTable()[color], cls);
				ClassElement clsElem = factory.classElement(clsInit.getName(),
						tAd.getName());
				this.currentStatements.add(clsElem);
			} else {
				ClassElement clsElem = factory.classElement(clsInit.getName(),
						factory.symbol("null"));
				this.currentStatements.add(clsElem);
			}
		}

		return clsInit;
	}
}