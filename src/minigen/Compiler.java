package minigen;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import minigen.icode.ICode.ClassElement;
import minigen.icode.ICode.InitStatement;
import minigen.icode.ICode.Program;
import minigen.icode.ICode.Symbol;
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
import minigen.syntax3.node.AIsaInstr;
import minigen.syntax3.node.ANewExp;
import minigen.syntax3.node.ANewInstr;
import minigen.syntax3.node.AProgram;
import minigen.syntax3.node.AType;
import minigen.syntax3.node.ATypeInstr;
import minigen.syntax3.node.AVarExp;
import minigen.syntax3.node.Node;
import minigen.syntax3.node.PClassDecl;
import minigen.syntax3.node.PInstr;

public class Compiler extends DepthFirstAdapter {

	private Model model;
	private Scope scope;

	private ICodeFactory factory;
	private ICodeCompiler builder;

	private Program currentProgram;
	private Symbol currentSymbol;
	private Var currentVar;
	
	private boolean newFlag = false;

	private HashMap<String, Symbol> typeSymbols;
	private Map<Var, Symbol> symbolsByVars;

	private int objCount;
	private int clsCount;

	public Compiler(Model model, Scope scope, FileWriter out) {
		this.model = model;
		this.scope = scope;

		this.factory = new ICodeFactory();
		this.builder = new ICodeCompiler(out);

		this.symbolsByVars = new HashMap<Var, Symbol>();
		this.typeSymbols = new HashMap<String, Symbol>();

		this.objCount = 0;
		this.clsCount = 0;

		// System.out.println("------- Compilation Statistics -------");
		// System.out.println();
		//
		// System.out.println("-------------------------------");
		// System.out.println();

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

		// Generate Object class
		minigen.model.Class cls = model.getClassByName(null, "Object");

		InitStatement objInit = buildClsInit(cls);
		this.currentProgram.getInitClasses().add(objInit);

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
		minigen.model.Class cls = model.getClassByNode(node.getKclass(), node,
				"");

		InitStatement clsInit = buildClsInit(cls);
		this.currentProgram.getInitClasses().add(clsInit);
	}

	@Override
	public void caseAIsaInstr(AIsaInstr node) {

		this.currentSymbol = null;
		node.getExp().apply(this);
		Symbol leftSymbol = this.currentSymbol;

		this.currentSymbol = null;
		node.getType().apply(this);
		Symbol rightSymbol = this.currentSymbol;

		this.currentProgram.getStmts().add(
				factory.isaStmt(leftSymbol, rightSymbol));

		this.currentSymbol = null;
	}

	@Override
	public void caseAVarExp(AVarExp node) {
		String name = node.getId().getText();
		this.currentType = this.scope.getVar(node.getId(), name).getType();
		this.currentSymbol = this.symbolsByVars.get(this.scope.getVar(
				node.getId(), name));
	}

	@Override
	public void caseAType(AType node) {
		if (!this.model.containsTypeDeclaration(node)) {
			throw new InternalError("No type declaration for node " + node);
		}
		this.currentType = this.model.getTypeByNode(node);

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

	@Override
	public void caseAAssignInstr(AAssignInstr node) {
		Var var = this.scope.getVar(node.getId(), node.getId().getText());
		this.currentVar = var;
		node.getExp().apply(this);
		this.currentVar = null;
		Symbol ref = this.currentSymbol;
		this.symbolsByVars.put(var, ref);
	}

	@Override
	public void caseADeclInstr(ADeclInstr node) {
		Var var = this.scope.getVar(node.getId(), node.getId().getText());
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
		InitStatement objInit = factory.initStmt(factory.type(TypeTag.OBJ), tObjName);
		objInit.getArgs().add(factory.arg(objId.toString()));
		objInit.getArgs().add(factory.arg(tClsName.toString()));
		objInit.getArgs().add(factory.arg(typeInit.toString()));
		
		this.currentSymbol = tObjName;
		this.currentProgram.getInitObjs().add(objInit);
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
		InitStatement objInit = factory.initStmt(factory.type(TypeTag.OBJ), tObjName);
		objInit.getArgs().add(factory.arg(objId.toString()));
		objInit.getArgs().add(factory.arg(tClsName.toString()));
		objInit.getArgs().add(factory.arg(typeInit.toString()));
		
		this.currentSymbol = tObjName;
		this.currentProgram.getInitObjs().add(objInit);
	}

	/* Utility methods */

	public Symbol buildTypeTable(Type type, Symbol tObjName, int index) {
		Symbol tGenName = factory.symbol("type_" + type.toSymbol());
		
		if(this.typeSymbols.containsKey(tGenName.toString())) {
			this.typeSymbols.get(tGenName.toString());
			this.currentSymbol = tGenName;
		} else {
			InitStatement tInit = factory.initStmt(factory.type(TypeTag.TYPE), tGenName);
			tInit.getArgs().add(factory.arg("cls_" + type.getName()));
	
			// Add generic concretes types
			int i = 0;
			for (Type genType : type.getGenericTypes()) {
				buildTypeTable(genType, tGenName, i);
				ClassElement tElem = factory.typeElement(tInit.getName(), this.currentSymbol);
				this.currentProgram.getInitElems().add(tElem);
				i++;
			}
			this.typeSymbols.put(tInit.getName().toString(), tInit.getName());
			this.currentProgram.getInitTypes().add(tInit);
			this.currentSymbol = tInit.getName();
		}
		return tGenName;
	}
	
	public InitStatement buildGenInit(Type t, Symbol adName) {
		Symbol atypeName = factory.symbol("gen_" + adName.toString() + "_" + t.getName());
		
		// Init the gen table
		InitStatement genInit = factory.initStmt(factory.type(TypeTag.TYPE), atypeName);
		
		// Set link to real type or formal type index
		if(t.isLinkedToFormalType()) {
			genInit.getArgs().add(factory.arg(t.getFormalType().getPosition().toString()));
		} else {
			genInit.getArgs().add(factory.arg("cls_" + t.getName()));
		}
		
		// Build gen part of the type
		for(Type st: t.getGenericTypes()) {
			// Build gen table
			InitStatement genTable = buildGenInit(st, atypeName);
			
			// Add it to gen types list
			ClassElement clsElem = factory.typeElement(genInit.getName(), genTable.getName());
			this.currentProgram.getInitElems().add(clsElem);
		}
		
		this.currentProgram.getInitAdapts().add(genInit);
		return genInit;
	}

	public InitStatement buildAdaptInit(Adaptation a, Class c) {
		Symbol tAdName = factory.symbol("adapt_" + c.getName() + a.getName());
		InitStatement adInit = factory.initStmt(factory.type(TypeTag.TYPE), tAdName);
		adInit.getArgs().add(factory.arg("cls_" + a.getName()));
		
		// For each gen type of adaptation
		for (Type t: a.getTypes()) {
			// Build gen table
			InitStatement genTable = buildGenInit(t, tAdName);
			
			// Add gen table to adaptation gen list
			ClassElement clsElem = factory.typeElement(adInit.getName(), genTable.getName());
			this.currentProgram.getInitElems().add(clsElem);
		}
		
		this.currentProgram.getInitAdapts().add(adInit);
		return adInit;
	}

	public InitStatement buildClsInit(Class cls) {

		Symbol tClsName = factory.symbol("cls_" + cls.getName());
		Integer clsId = this.clsCount;
		this.clsCount++;

		InitStatement clsInit = factory.initStmt(factory.type(TypeTag.CLASS), tClsName);

		// Add class infos
		clsInit.getArgs().add(factory.arg(clsId.toString())); // id
		clsInit.getArgs().add(factory.arg(cls.getColor().toString())); // color
		clsInit.getArgs().add(factory.arg("\"" + cls.getName() + "\"")); // name
		
		for (int color = 0; color < cls.getAdaptationsTable().length; color++) {
			if (cls.getAdaptationsTable()[color] != null) {
				InitStatement tAd = buildAdaptInit(cls.getAdaptationsTable()[color], cls);
				ClassElement clsElem = factory.classElement(clsInit.getName(), tAd.getName().toString());
				this.currentProgram.getInitElems().add(clsElem);
			} else {
				ClassElement clsElem = factory.classElement(clsInit.getName(), "null");
				this.currentProgram.getInitElems().add(clsElem);
			}
		}

		return clsInit;
	}

	@Override
	public void caseAClassnameInstr(AClassnameInstr node) {
		node.getExp().apply(this);
		this.currentProgram.getStmts().add(
				factory.classnameStatement(this.currentSymbol));
	}

	@Override
	public void caseATypeInstr(ATypeInstr node) {
		node.getExp().apply(this);
		this.currentProgram.getStmts().add(
				factory.typeStatement(this.currentSymbol));
	}
}