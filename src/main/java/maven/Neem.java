package maven;

import java.io.FileWriter;
import java.util.List;

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
	
}

enum Expression {
	Negation,
	Addition,
	Subtract,
	Multiply,
	Division,
	Modulus,
	ShiftLeft,
	ShiftRight,
	BitwiseOR,
	BitwiseAND,
	BitwiseXOR,
	BitwiseNOT,
	Equal,
	NotEqual,
	LessThan,
	LessEqual,
	GreaterThan,
	GreaterEqual,
	LogicalOR,
	LogicalAND,
	LogicalNOT,
	Assignment,
	AddAssign,
	SubAssign,
	Subscript,
	Dot,
	Call,
	Literal,
	Identifier
}

enum Statement {
	ClassDecl,
	Function,
	Block,
	If,
	While,
	For,
	Foreach,
	Break,
	Continue,
	Logger,
	Assert,
	Expression,
}






public class Neem {
	
	public void writeVisitor(String baseName, List<String> names, FileWriter writer) throws Exception {
		writer.append("struct Visitor {");
		for (String name: names) {
			writer.append("\tvoid visit(");
			if (baseName != null)
				writer.append(baseName);
			writer.append(name);
			writer.append(");\n");
		}
		writer.append("};");
	}
	public static void main(String[] args) throws Exception {
		
	}
}
