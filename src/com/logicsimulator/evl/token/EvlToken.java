package com.logicsimulator.evl.token;

public class EvlToken {
	public static enum TokenType {
		NAME, SINGLE, NUMBER
	}

	private TokenType type;
	private String str;
	private int lineNo;
	private int tokenNo;

	public EvlToken(TokenType type, String str, int lineNo, int tokenNo) {
		this.type = type;
		this.str = str;
		this.lineNo = lineNo;
		this.tokenNo = tokenNo;
	}

	public TokenType getType() {
		return type;
	}

	public String getName() {
		return str;
	}

	@Override
	public String toString() {
		if (type == TokenType.NAME) {
			return "NAME " + str;
		} else if (type == TokenType.NUMBER) {
			return "NUMBER " + str;
		} else if (type == TokenType.SINGLE) {
			return "SINGLE " + str;
		} else {
			return str;
		}
	}

	public int getLineNo() {
		return lineNo;
	}

	public int getTokenNo() {
		return tokenNo;
	}

}
