package com.logicsimulator.evl.module;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.logicsimulator.evl.statement.EvlStatement;
import com.logicsimulator.evl.statement.EvlStatements;
import com.logicsimulator.evl.statement.StatementType;

public class EvlModules implements Iterable<EvlModule> {
	List<EvlModule> modules;

	public EvlModules(EvlStatements evlStats) {
		groupIntoModules(evlStats);
	}

	private void groupIntoModules(EvlStatements evlStats) {
		EvlModule module = null;
		EvlStatement stat = null;
		modules = new LinkedList<EvlModule>();
		for (ListIterator<EvlStatement> it = evlStats.getListIterator(); it
				.hasNext();) {
			stat = it.next();
			if (stat.getType() == StatementType.MODULE
					&& module == null) {
				if (stat.getNumberOfTokens() > 1) {
					module = new EvlModule(stat.getNameOfToken(1),
							evlStats.getFileName());
				} else {
					throw new RuntimeException();
				}
			} else if (stat.getType() == StatementType.WIRE
					&& module != null) {
				module.extractWiresFromStat(stat);
			} else if (stat.getType() == StatementType.COMPONENT
					&& module != null) {
				module.extractComponentsFromStat(stat);
			} else if (stat.getType() == StatementType.ASSIGN
					&& module != null) {
				module.extractAssignsFromStat(stat);
			} else if (stat.getType() == StatementType.ENDMODULE
					&& module != null) {
				if (stat.getNumberOfTokens() != 1) {
					throw new RuntimeException();
				} else {
					module.correctPins();
					module.storeModuleToFile(module.getFileName());
					modules.add(module);
					module = null;
				}
			}
		}
		if (stat != null
				&& stat.getType() != StatementType.ENDMODULE) {
			throw new RuntimeException();
		}
	}

	@Override
	public Iterator<EvlModule> iterator() {
		return modules.iterator();
	}
}
