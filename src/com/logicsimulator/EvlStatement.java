package com.logicsimulator;

import java.util.List;
import java.util.ListIterator;

public class EvlStatement {
	public static enum StatementType {
		MODULE, WIRE, COMPONENT, ASSIGN, ENDMODULE
	}

	private StatementType type;
	private List<EvlToken> tokens;

	EvlStatement(StatementType type, List<EvlToken> tokens) {
		this.type = type;
		this.tokens = tokens;
	}

	StatementType getType() {
		return type;
	}

	int getNumberOfTokens() {
		return tokens.size();
	}

	String getNameOfToken(int i) {
		return tokens.get(i).getName();
	}

	ListIterator<EvlToken> getTokenIterator() {
		return tokens.listIterator();
	}
}