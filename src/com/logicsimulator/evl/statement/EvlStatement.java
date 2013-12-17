package com.logicsimulator.evl.statement;

import java.util.List;
import java.util.ListIterator;

import com.logicsimulator.evl.token.EvlToken;

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

	public StatementType getType() {
		return type;
	}

	public int getNumberOfTokens() {
		return tokens.size();
	}

	public String getNameOfToken(int i) {
		return tokens.get(i).getName();
	}

	public ListIterator<EvlToken> getTokenIterator() {
		return tokens.listIterator();
	}
}