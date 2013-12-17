package com.logicsimulator.evl.token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import com.logicsimulator.evl.module.EvlModules;
import com.logicsimulator.evl.statement.EvlStatements;

public class EvlTokenFile {

	private String fileName;
	private BufferedReader input;

	public EvlTokenFile(String fileName) {
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException();
		}
		this.fileName = fileName;
	}

	public EvlModules readModules() {
		return new EvlModules(new EvlStatements(readTokens()));
	}

	public static EvlTokens readTokens(String fileName) {
		EvlTokenFile tf = new EvlTokenFile(fileName);
		List<EvlToken> evlTokens;
		evlTokens = tf.extractTokensFromFile();
		return new EvlTokens(fileName, evlTokens);
	}

	public static boolean extractHexTokensFromLine(String line, int lineNo,
			List<EvlToken> evlTokens) {
		for (int index = 0; index < line.length();) {
			// Skip spaces
			if (EvlTokenFile.isSpace(line.charAt(index))) {
				index++;
			}
			// Number token
			else if (EvlTokenFile.isDigit(line.charAt(index))) {
				int numberBegin = index;
				for (index++; index < line.length(); index++) {
					if (!EvlTokenFile.isDigit(line.charAt(index))) {
						break;
					}
				}
				evlTokens.add(new EvlToken(EvlToken.TokenType.NUMBER, line
						.substring(numberBegin, index), lineNo, evlTokens
						.size() + 1));
			}
			// Character not supported
			else {
				throw new RuntimeException();
			}
		}
		return true;
	}

	private EvlTokens readTokens() {
		List<EvlToken> evlTokens;
		evlTokens = extractTokensFromFile();
		storeTokensToFile(fileName, evlTokens);
		return new EvlTokens(fileName, evlTokens);
	}

	public static boolean isSpace(char c) {
		return c == ' ' || c == '\t';
	}

	public static boolean isSingle(char c) {
		return (c == '(' || c == ')' || c == '[' || c == ']' || c == ':'
				|| c == ';' || c == ',' || c == '=');
	}

	public static boolean isNameSymbol(char c) {
		return (c == '_' || c == '\\' || c == '.');
	}

	public static boolean isAlpha(char c) {
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
	}

	public static boolean isComment(char c) {
		return c == '/';
	}

	public static boolean isDigit(char c) {
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')
				|| (c >= 'A' && c <= 'F');
	}

	private boolean extractTokensFromLine(String line, int lineNo,
			List<EvlToken> evlTokens) {
		for (int index = 0; index < line.length();) {
			// Skip comments
			if (isComment(line.charAt(index))) {
				index++;
				if (index == line.length() || !isComment(line.charAt(index))) {
					throw new RuntimeException();
				} else {
					break;
				}
			}
			// Skip spaces
			else if (isSpace(line.charAt(index))) {
				index++;
			}
			// Single character token
			else if (isSingle(line.charAt(index))) {
				evlTokens.add(new EvlToken(EvlToken.TokenType.SINGLE, Character
						.toString(line.charAt(index)), lineNo,
						evlTokens.size() + 1));
				index++;
			}
			// Name token
			else if (isAlpha(line.charAt(index))
					|| isNameSymbol(line.charAt(index))) {
				int nameBegin = index;
				for (index++; index < line.length(); index++) {
					if (!(isAlpha(line.charAt(index))
							|| isNameSymbol(line.charAt(index)) || isDigit(line
								.charAt(index)))) {
						break;
					}
				}
				evlTokens.add(new EvlToken(EvlToken.TokenType.NAME, line
						.substring(nameBegin, index), lineNo,
						evlTokens.size() + 1));
			}
			// Number token
			else if (isDigit(line.charAt(index))) {
				int numberBegin = index;
				for (index++; index < line.length(); index++) {
					if (!isDigit(line.charAt(index))) {
						break;
					}
				}
				evlTokens.add(new EvlToken(EvlToken.TokenType.NUMBER, line
						.substring(numberBegin, index), lineNo, evlTokens
						.size() + 1));
			}
			// Character not supported
			else {
				throw new RuntimeException("Exception: character '"
						+ line.charAt(index) + "' not supported in line "
						+ lineNo + " of " + fileName);
			}
		}
		return true;
	}

	private List<EvlToken> extractTokensFromFile() {

		List<EvlToken> evlTokens = new LinkedList<EvlToken>();
		String line;
		int lineNo;
		try {
			for (lineNo = 1, line = input.readLine(); line != null; lineNo++, line = input
					.readLine()) {
				if (!extractTokensFromLine(line, lineNo++, evlTokens)) {
					input.close();
					throw new RuntimeException();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return evlTokens;
	}

	private void displayTokens(PrintStream out, List<EvlToken> evlTokens) {
		if (out == null) {
			out = System.out;
		}
		for (EvlToken evlToken : evlTokens) {
			out.println(evlToken);
		}
	}

	private boolean storeTokensToFile(String fileName, List<EvlToken> evlTokens) {
		try {
			fileName = fileName + ".tokens";
			File file = new File(fileName);
			file.delete();
			PrintStream out = new PrintStream(file);
			displayTokens(out, evlTokens);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
