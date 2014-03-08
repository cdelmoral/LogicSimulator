package com.logicsimulator.evl.statement;

import java.util.List;
import java.util.ListIterator;

import com.logicsimulator.evl.token.EvlToken;

/**
 * Represents a single Easy Verilog statement.
 * <p>
 * Contains all the information of a single Easy Verilog statement.
 */
public class EvlStatement {
	private StatementType type;
	private List<EvlToken> tokens;

	/**
	 * Constructs a new <code>EvlStatement</code>.
	 * @param type	the type of the statement.
	 * @param tokens	the list of <code>EvlToken</code>s contained in the statement.
	 */
	EvlStatement(StatementType type, List<EvlToken> tokens) {
		this.type = type;
		this.tokens = tokens;
	}

	/**
	 * @return	the type of the statement.
	 */
	public StatementType getType() {
		return type;
	}

	/**
	 * @return	the number of <code>EvlToken</code>s contained in the statement.
	 */
	public int getNumberOfTokens() {
		return tokens.size();
	}

	/**
	 * @param index	the index of the token.
	 * @return	the name of the token at the specified <code>index</code>.
	 */
	public String getNameOfToken(int index) {
		return tokens.get(index).getName();
	}

	/**
	 * @return	the <code>ListIterator</code> of <code>EvlToken</code>s.
	 */
	public ListIterator<EvlToken> getTokenIterator() {
		return tokens.listIterator();
	}
}