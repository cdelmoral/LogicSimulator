package com.logicsimulator.evl.statement;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.logicsimulator.evl.token.EvlToken;
import com.logicsimulator.evl.token.EvlTokens;

/**
 * Class to group different Easy Verilog statements.
 */
public class EvlStatements {
	private String fileName;
	private List<EvlStatement> evlStats;

	/**
	 * Constructs a <code>EvlStatements</code> from a <code>EvlTokens</code>. It groups the tokens into separate
	 * statements.
	 * @param evlTokens	the tokens to construct the <code>EvlStatements</code> from
	 */
	public EvlStatements(EvlTokens evlTokens) {
		evlStats = groupIntoStatements(evlTokens);
		fileName = evlTokens.getFileName();
	}

	/**
	 * @return	the name of the file where tokens come from
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return	the list iterator for the <code>EvlStatments</code> object
	 */
	public ListIterator<EvlStatement> getListIterator() {
		return evlStats.listIterator();
	}

	private List<EvlStatement> groupIntoStatements(EvlTokens evlTokens) {
		List<EvlStatement> statements = new LinkedList<EvlStatement>();
		EvlStatement newStat;
		EvlToken token = null;

		for (ListIterator<EvlToken> it = evlTokens.getListIterator(); it
				.hasNext();) {
			token = it.next();
			List<EvlToken> tokens;
			StatementType type;

			if (token.getType() != EvlToken.TokenType.NAME) {
				throw new RuntimeException();
			} else if (token.getName().equals("wire")) {
				type = StatementType.WIRE;
			} else if (token.getName().equals("assign")) {
				type = StatementType.ASSIGN;
			} else if (token.getName().equals("module")) {
				type = StatementType.MODULE;
			} else if (token.getName().equals("endmodule")) {
				type = StatementType.ENDMODULE;
			} else {
				type = StatementType.COMPONENT;
			}
			tokens = new LinkedList<EvlToken>();
			tokens.add(token);
			while (it.hasNext() && !token.getName().equals("endmodule")) {
				token = it.next();
				tokens.add(token);
				if (token.getName().equals(";")) {
					break;
				}
			}
			newStat = new EvlStatement(type, tokens);
			statements.add(newStat);
		}
		if (token != null && !token.getName().equals("endmodule")) {
			throw new RuntimeException();
		}
		return statements;
	}

}