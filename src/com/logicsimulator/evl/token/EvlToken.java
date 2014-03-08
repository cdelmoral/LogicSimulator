package com.logicsimulator.evl.token;

/**
 * Represents a single token.
 */
public class EvlToken {
	
	/**
	 * Type of the token. It can be <code>NAME</code>, <code>SINGLE</code> or <code>NUMBER</code>.
	 */
	public static enum TokenType {
		NAME, SINGLE, NUMBER
	}

	private TokenType type;
	private String str;
	private int lineNo;
	private int tokenNo;

	/**
	 * Constructs a new <code>EvlToken</code>.
	 * @param type	the type of token.
	 * @param str	the token as a <code>String</code>.
	 * @param lineNo	the line where the token appears.
	 * @param tokenNo	the number of token.
	 */
	public EvlToken(TokenType type, String str, int lineNo, int tokenNo) {
		this.type = type;
		this.str = str;
		this.lineNo = lineNo;
		this.tokenNo = tokenNo;
	}

	/**
	 * @return	the type of token.
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * @return	the token as a <code>String</code>.
	 */
	public String getName() {
		return str;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * @return	the line where the token appears.
	 */
	public int getLineNo() {
		return lineNo;
	}

	/**
	 * @return	the token number.
	 */
	public int getTokenNo() {
		return tokenNo;
	}

}
