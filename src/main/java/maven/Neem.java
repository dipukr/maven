package maven;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
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
	
	private final String data;
	
	Expression(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
}

enum Statement {
	ClassDecl("List<Statement> variables;List<Statement> functions;"),
	Function("String name;List<String> params;List<Statement> statements;"),
	Block("List<Statement> statements;"),
	Var("String name;Expression value;"),
	Val("String name;Expression value;"),
	If("Expression codition;Statement statement0;Statement statement1;"),
	While("Expression codition;Statement statement;"),
	Foreach("String name;String target"),
	Break(""),
	Return("Expression value"),
	Expression("Expression expression");
	
	private final String data;
	
	Statement(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
}

public class Neem {
	public static void writeExpression(FileWriter writer) throws Exception {
		writer.append("struct ExpressionVisitor;\n");
		writer.append("struct Expression {\n");
		writer.append("\tuint pos;\n");
		writer.append("\tExpression(int pos) : pos(pos) {}\n");
		writer.append("virtual void accept(ExpressionVisitor *visitor) = 0;\n};\n");
		for (Expression expression: Expression.values()) {
			String name = expression.name();
			String data = expression.getData();
			String[] fields = data.split(";");
			writer.append("class ").append(name).append(": public")
				.append(" Expression").append("\n{\n");
			for (String field: fields) {
				String[] strs = field.split(" ");
				writer.append('\t').append(strs[0]).append(' ');
				if (strs[1].charAt(0) == '*')
					  writer.append("*m_").append(strs[1].substring(1)).append(";\n");
				else writer.append("m_").append(strs[1]).append(";\n");
			}
			writer.append("public:\n");
			writer.append('\t').append(name).append("(");
			for (int i = 0; i < fields.length; i++) {
				if (i > 0) writer.append(", ");
				writer.append(fields[i]).append(", uint pos");
			}
			writer.append(");\n");
			for (String field: fields) {
				String[] strs = field.split(" ");
				writer.append('\t').append(strs[0]).append(' ')
					.append(strs[1]).append("() const;\n");
			}
			writer.append("\tvoid accept(ExpressionVisitor *visitor);\n");
			writer.append("};\n");
		}
		writer.append("struct ExpressionVisitor {\n");
		for (Expression expression: Expression.values()) {
			writer.append("\tvirtual void visit(");
			writer.append(expression.name());
			writer.append(" *expression) = 0;\n");
		}
		writer.append("};\n");
		for (Expression expression: Expression.values()) {
			String name = expression.name();
			String data = expression.getData();
			String[] fields = data.split(";");
			writer.append(name).append("::").append(name).append("(");
			for (int i = 0; i < fields.length; i++) {
				if (i > 0) writer.append(", ");
				writer.append(fields[i]);
			}
			writer.append(", uint pos) : ");
			for (int i = 0; i < fields.length; i++) {
				String field = fields[i];
				String[] strs = field.split(" ");
				if (i > 0) writer.append(", ");
				if (strs[1].charAt(0) == '*')
					writer.append("m_").append(strs[1].substring(1)).append('(')
						.append(strs[1].substring(1)).append(')');
				else writer.append("m_").append(strs[1]).append('(')
					.append(strs[1]).append(')');
			}
			writer.append(", Expression(pos) {}\n");
		}
		for (Expression expression: Expression.values()) {
			String name = expression.name();
			writer.append("void ").append(name).append("::accept(ExpressionVisitor *visitor)")
				.append(" {visitor->visit(this);}").append("\n");
		}
		for (Expression expression: Expression.values()) {
			String name = expression.name();
			String data = expression.getData();
			String[] fields = data.split(";");
			for (String field: fields) {
				String[] strs = field.split(" ");
				if (strs[1].charAt(0) == '*')
					writer.append(strs[0]).append("* ").append(name).append("::")
						.append(strs[1].substring(1)).append("() const {")
						.append("return m_").append(strs[1].substring(1)).append(";}\n");
				else writer.append(strs[0]).append(" ").append(name).append("::")
						.append(strs[1]).append("() const {")
						.append("return m_").append(strs[1]).append(";}\n");
			}
		}
	}
	public static void writeStatement(FileWriter writer) throws Exception {
		for (Statement statement: Statement.values()) {
			String name = statement.name();
			String data = statement.getData();
			String[] fields = data.split(";");
			writer.append("class ").append(name).append("{\n");
			for (String field: fields) {
				writer.append('\t').append(field).append(';');
				writer.append('\n');
				//String[] strs = field.split(" ");
			}
			writer.append("public");
			writer.append("};\n");
		}
	}
	public static void writeVisitor(FileWriter writer, List<String> names, String arg) throws Exception {
		writer.append("struct Visitor {\n");
		for (String name: names) {
			writer.append("\tvirtual void visit(");
			writer.append(name);
			writer.append(" *");
			writer.append(arg);
			writer.append(") = 0;\n");
		}
		writer.append("};\n");
	}
	public static void main(String[] args) throws Exception {
		Token[] tokens = Token.values();
		Opcode[] opcodes = Opcode.values();
		Expression[] expressions = Expression.values();
		Statement[] statements = Statement.values();
		File file = new File("output.data");
		FileWriter writer = new FileWriter(file);
		//writeVisitor(writer, Stream.of(expressions).map(elem -> elem.name()).toList(), "expression");
		//writeVisitor(writer, Stream.of(statements).map(elem -> elem.name()).toList(), "statement");
		writeExpression(writer);
		writer.flush();
		writer.close();
	}
}
