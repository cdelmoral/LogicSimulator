package com.logicsimulator.evl.statement;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.logicsimulator.evl.token.EvlToken;
import com.logicsimulator.evl.token.EvlTokens;

public class EvlStatements {

	String fileName;
	List<EvlStatement> evlStats;

	public EvlStatements(EvlTokens evlTokens) {
		evlStats = groupIntoStatements(evlTokens);
		fileName = evlTokens.getFileName();
	}

	public String getFileName() {
		return fileName;
	}

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
			EvlStatement.StatementType type;

			if (token.getType() != EvlToken.TokenType.NAME) {
				throw new RuntimeException();
			} else if (token.getName().equals("wire")) {
				type = EvlStatement.StatementType.WIRE;
			} else if (token.getName().equals("assign")) {
				type = EvlStatement.StatementType.ASSIGN;
			} else if (token.getName().equals("module")) {
				type = EvlStatement.StatementType.MODULE;
			} else if (token.getName().equals("endmodule")) {
				type = EvlStatement.StatementType.ENDMODULE;
			} else {
				type = EvlStatement.StatementType.COMPONENT;
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