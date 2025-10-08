package maven;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

enum Opcode {
	NOP,
	PUSH,
	PUSHN,
	POP,
	POPN,
	DUP,
	COPY,
	SAVE,
	LD_NULL,
	LD_TRUE,
	LD_FALSE,
	LOAD,
	LOADA,
	LOADM,
	STORE,
	STOREA,
	STOREM,
	NEWA,
	NEG,
	INC,
	DEC,
	ADD,
	SUB,
	MUL,
	DIV,
	MOD,
	SHL,
	SHR,
	BOR,
	BAND,
	BXOR,
	BNOT,
	CEQ,
	CNE,
	CLT,
	CLE,
	CGT,
	CGE,
	OR,
	AND,
	NOT,
	JUMP,
	JIT,
	JIF,
	CALL,
	RET,
	POS,
	ASSERT,
	COUT,
	HALT
}

enum Token {
	EOT,
	DOT,
	COMMA,
	COLON,
	BICOLON,
	SEMICOLON,
	QUESTION,
	LPAREN,
	RPAREN,
	LBRACE,
	RBRACE,
	LBRACKET,
	RBRACKET,
	ASSIGN,
	ADDASS,
	SUBASS,
	PLUS,
	MINUS,
	MULT,
	DIV,
	MOD,
	SHL,
	SHR,
	BOR,
	BAND,
	BXOR,
	BNOT,
	CEQ,
	CNE,
	CLT,
	CLE,
	CGT,
	CGE,
	OR,
	AND,
	NOT,
	VAL,
	VAR,
	CLASS,
	THIS,
	FUNCTION,
	DO,
	END,
	IF,
	ELSE,
	WHILE,
	FOREACH,
	BREAK,
	RETURN,
	NULL,
	TRUE,
	FALSE
}

enum Expression {
	Literal("Value value"),
	Identifier("String name"),
	Negation("Expression *expression"),
	Addition("Expression *left;Expression *right"),
	Subtract("Expression *left;Expression *right"),
	Multiply("Expression *left;Expression *right"),
	Division("Expression *left;Expression *right"),
	Modulus("Expression *left;Expression *right"),
	ShiftLeft("Expression *left;Expression *right"),
	ShiftRight("Expression *left;Expression *right"),
	BitwiseOR("Expression *left;Expression *right"),
	BitwiseAND("Expression *left;Expression *right"),
	BitwiseXOR("Expression *left;Expression *right"),
	BitwiseNOT("Expression *expression"),
	Equal("Expression *left;Expression *right"),
	NotEqual("Expression *left;Expression *right"),
	LessThan("Expression *left;Expression *right"),
	LessEqual("Expression *left;Expression *right"),
	GreaterThan("Expression *left;Expression *right"),
	GreaterEqual("Expression *left;Expression *right"),
	LogicalOR("Expression *left;Expression *right"),
	LogicalAND("Expression *left;Expression *right"),
	LogicalNOT("Expression *expression"),
	Conditional("Expression *condition;Expression *left;Expression *right"),
	Subscript("Expression *left;Expression *right"),
	Dot("Expression *left;Expression *right"),
	Call("Expression *caller;List<Expression*> *arguments"),
	AddAssign("Expression *left;Expression *right"),
	SubAssign("Expression *left;Expression *right"),
	Assignment("Expression *left;Expression *right");
	
	final String data;
	Expression(String data) {this.data = data;}
	public String getData() {return data;}
}

enum Statement {
	ClassStatement("List<Statement*> *variables;List<Statement*> *functions"),
	FunctionStatement("String name;List<String> *params;List<Statement*> *statements"),
	BlockStatement("List<Statement*> *statements"),
	VarStatement("String name;Expression *value"),
	ValStatement("String name;Expression *value"),
	IfStatement("Expression *condition;Statement *statement0;Statement *statement1"),
	WhileStatement("Expression *condition;Statement *statement"),
	ForeachStatement("String name;String target"),
	BreakStatement("uint pos"),
	ReturnStatement("Expression *value"),
	ExpressionStatement("Expression *expression");
	
	final String data;
	Statement(String data) {this.data = data;}
	public String getData() {return data;}
}

class Metadata {
	public String name;
	public String data;
	public static Metadata of(String name, String data) {
		Metadata metadata = new Metadata();
		metadata.name = name;
		metadata.data = data;
		return metadata;
	}
}

public class Neem {
	public void writeAstData(PrintWriter writer) throws Exception {
		Function<Expression, Metadata> emapper = (a) -> Metadata.of(a.name(), a.getData());
		Function<Statement, Metadata> smapper = (a) -> Metadata.of(a.name(), a.getData());
		List<Metadata> expressions = Stream.of(Expression.values()).map(emapper).toList();
		List<Metadata> statements = Stream.of(Statement.values()).map(smapper).toList();
		List<Metadata> ast = new ArrayList<>();
		ast.addAll(expressions);
		ast.addAll(statements);
		writer.printf("struct Visitor;\n\n");
		writeClassDecls(writer, expressions, "Expression");
		writeClassDecls(writer, statements, "Statement");
		
		
	}
	public void writeClassDecls(PrintWriter writer, List<Metadata> classDatas, String kind) throws Exception {
		writer.printf("struct %s {\n", kind);
		writer.printf("\tuint pos;\n");
		writer.printf("\t%s(uint pos) : pos(pos) {}\n", kind);
		writer.printf("\tvirtual void accept(%sVisitor *visitor) = 0;\n", kind);
		writer.printf("};\n\n");
		for (Metadata data: classDatas) {
			writer.printf("class %s: public %s\n{\n", data.name, kind);
			writeFields(writer, kind, data);
			writer.printf("public:\n");
			writeConstructorDecl(writer, data);
			writeGettersDecl(writer, data);
			writeAcceptDecl(writer, kind);
			writer.printf("};\n\n");
		}
	}
	public void writeAll(PrintWriter writer, String kind, List<Metadata> classDatas) throws Exception {
		writer.printf("struct %sVisitor;\n\n", kind);
		writer.printf("struct %s {\n", kind);
		writer.printf("\tuint pos;\n");
		writer.printf("\t%s(uint pos) : pos(pos) {}\n", kind);
		writer.printf("\tvirtual void accept(%sVisitor *visitor) = 0;\n", kind);
		writer.printf("};\n\n");
		for (Metadata classData: classDatas) {
			writeFields(writer, kind, classData);
			writer.printf("public:\n");
			writeConstructorDecl(writer, classData);
			writeGettersDecl(writer, classData);
			writeAcceptDecl(writer, kind);
			writer.printf("};\n\n");
		}
		writer.printf("\n");
		for (Metadata classData: classDatas)
			writeConstructor(writer, kind, classData);
		writer.printf("\n");
		for (Metadata classData: classDatas)
			writeGetters(writer, classData);
			writer.printf("\n");
		for (Metadata classData: classDatas)
			writeAccept(writer, kind, classData.name);
			writer.printf("\n");
	}
	public void writeClassDecl(PrintWriter writer, Metadata data) throws Exception {
		writer.printf("struct Visitor;\n\n", data.kind);
		writer.printf("struct %s {\n", kind);
		writer.printf("\tuint pos;\n");
		writer.printf("\t%s(uint pos) : pos(pos) {}\n", kind);
		writer.printf("\tvirtual void accept(%sVisitor *visitor) = 0;\n", kind);
		writer.printf("};\n\n");
		for (Metadata classData: classDatas) {
			writeFields(writer, kind, classData);
			writer.printf("public:\n");
			writeConstructorDecl(writer, classData);
			writeGettersDecl(writer, classData);
			writeAcceptDecl(writer, kind);
			writer.printf("};\n\n");
		}
	}
	public void writeFields(PrintWriter writer, String kind, Metadata data) throws Exception {
		String[] fields = data.data.split(";");
		for (String field: fields) {
			String[] strs = field.split(" ");
			writer.printf("\t%s ", strs[0]);
			if (strs[1].charAt(0) == '*')
				writer.printf("*m_%s;\n", strs[1].substring(1));	
			else writer.printf("m_%s;\n", strs[1]);
		}
	}	
	public void writeConstructorDecl(PrintWriter writer, Metadata data) throws Exception {
		writer.printf("\t%s(", data.name);
		String[] fields = data.data.split(";");
		for (int i = 0; i < fields.length; i++) {
			if (i > 0) writer.append(", ");
			writer.printf("%s", fields[i]);
		}
		writer.printf(", uint pos);\n");
	}
	public void writeConstructor(PrintWriter writer, String kind, Metadata data) throws Exception {
		String[] fields = data.data.split(";");
		writer.printf("%s::%s(", data.name, data.name);
		for (int i = 0; i < fields.length; i++) {
			if (i > 0) writer.printf(", ");
			writer.printf(fields[i]);
		}
		writer.printf(", uint pos) : ");
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			String[] strs = field.split(" ");
			if (i > 0) writer.printf(", ");
			String name = strs[1].charAt(0) == '*' ? strs[1].substring(1) : strs[1];
			writer.printf("m_%s(%s)", name, name);
		}
		writer.printf(", %s(pos) {}\n", kind);
	}
	public void writeGettersDecl(PrintWriter writer, Metadata data) throws Exception {
		String[] fields = data.data.split(";");
		for (String field: fields) {
			String[] strs = field.split(" ");
			if (strs[1].charAt(0) == '*')
				writer.printf("\t%s* %s() const;\n", strs[0], strs[1].substring(1));
			else writer.printf("\t%s %s() const;\n", strs[0], strs[1]);
		}
	}
	public void writeGetters(PrintWriter writer, Metadata data) throws Exception {
		String[] fields = data.data.split(";");
		for (String field: fields) {
			String[] strs = field.split(" ");
			if (strs[1].charAt(0) == '*')
				writer.printf("%s* %s::%s() const {return m_%s;}\n", strs[0], data.name, strs[1].substring(1), strs[1].substring(1));
			else writer.printf("%s %s::%s() const {return m_%s;}\n", strs[0], data.name, strs[1], strs[1]);
		}
	}
	public void writeAcceptDecl(PrintWriter writer, String kind) throws Exception {
		writer.printf("\tvoid accept(%sVisitor *visitor);\n", kind);
	}
	public void writeAccept(PrintWriter writer, String kind, String name) throws Exception {
		writer.printf("void %s::accept(%sVisitor *visitor) {visitor->visit(this);}\n", name, kind);
	}
	public void writeVisitor(PrintWriter writer, List<Metadata> classDatas) throws Exception {
		writer.printf("struct Visitor {\n");
		for (Metadata classData: classDatas)
			writer.printf("\tvirtual void visit(%s *%s) = 0;\n", classData.name, classData.kind.toLowerCase());
		writer.printf("};\n");
	}
	public void writeVisitorImpl(PrintWriter writer, List<Metadata> expressions, String name, List<Metadata> statements) throws Exception {
		writer.printf("class %s: public Visitor, StatementVisitor {\n", name);
		
	}
	public boolean notEmpty(String[] fields) {
		if (fields.length == 1 && fields[0].isEmpty()) return false;
		return true;
	}
	public static void main(String[] args) throws Exception {
		var neem = new Neem();
		Token[] tokens = Token.values();
		Opcode[] opcodes = Opcode.values();
		File file = new File("output.data");
		FileWriter fileWriter = new FileWriter(file);
		PrintWriter writer = new PrintWriter(fileWriter, true);
		Function<Expression, Metadata> emapper = (a) -> Metadata.of(a.name(), a.getData());
		Function<Statement, Metadata> smapper = (a) -> Metadata.of(a.name(), a.getData());
		List<Metadata> expressions = Stream.of(Expression.values()).map(emapper).toList();
		List<Metadata> statements = Stream.of(Statement.values()).map(smapper).toList();
		List<Metadata> ast = new ArrayList<>();
		ast.addAll(expressions);
		ast.addAll(statements);
		
		
		//neem.writeAll(writer, "Expression", expressions);
		neem.writeAll(writer, "Statement", statements);
		//neem.writeVisitorImpl(writer, expressions, statements);
		writer.flush();
		writer.close();
	}
}
