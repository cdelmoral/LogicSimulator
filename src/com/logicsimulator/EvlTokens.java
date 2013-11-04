package com.logicsimulator;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EvlTokens implements Iterable<EvlToken> {
	private String fileName;
	private List<EvlToken> tokens;

	EvlTokens(String fileName, List<EvlToken> tokens) {
		this.fileName = fileName;
		this.tokens = tokens;
	}

	ListIterator<EvlToken> getListIterator() {
		return tokens.listIterator();
	}

	String getFileName() {
		return fileName;
	}

	@Override
	public Iterator<EvlToken> iterator() {
		return tokens.iterator();
	}
}
