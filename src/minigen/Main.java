package minigen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

import minigen.analysis.ClassAnalysis;
import minigen.analysis.FormalTypeAnalysis;
import minigen.analysis.InheritanceAnalysis;
import minigen.analysis.TypeAnalysis;
import minigen.exception.InternalException;
import minigen.exception.SemanticException;
import minigen.model.Model;
import minigen.model.Tables;
import minigen.syntax3.lexer.Lexer;
import minigen.syntax3.lexer.LexerException;
import minigen.syntax3.node.Node;
import minigen.syntax3.parser.Parser;
import minigen.syntax3.parser.ParserException;

public class Main {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("usage:");
			System.out.println(" java miniproc.Main filename");
			return;
		}

		try {
			FileReader in = new FileReader(args[0]);
			Lexer lexer = new Lexer(new PushbackReader(new BufferedReader(in),
					1020));
			Parser parser = new Parser(lexer);

			Node tree = parser.parse();

			in.close();

			// Prepare class scope
			Model model = new Model();

			// Check class declarations
			tree.apply(new ClassAnalysis(model));

			// Check formal type declarations
			tree.apply(new FormalTypeAnalysis(model));

			// Check inheritance declarations
			tree.apply(new InheritanceAnalysis(model));
			
			// Check type declarations
			tree.apply(new TypeAnalysis(model));
			
			// Compute tables
			new Tables(model);
			
			// Run interpreter
			tree.apply(new Interpreter(model));
		} catch (IOException e) {
			System.out.flush();
			System.err.println("IO ERROR: while reading " + args[0] + ": "
					+ e.getMessage());
			return;
		} catch (LexerException e) {
			System.out.flush();
			System.err.println("LEXICAL ERROR: " + e.getMessage());
			return;
		} catch (ParserException e) {
			System.out.flush();
			System.err.println("SYNTAX ERROR: " + e.getMessage());
			return;
		} catch (SemanticException e) {
			System.out.flush();
			System.err.println("SEMANTIC ERROR: " + e.getMessage());
			return;
		} catch (InternalException e) {
			System.out.flush();
			System.err.println("INTERPRETER ERROR: " + e.getMessage());
			return;
		}
	}

}
