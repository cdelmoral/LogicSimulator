package com.logicsimulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GoldenComparator {
	public static boolean compare(String fileName) {

		String line, lineGolden;
		BufferedReader reader, readerGolden;
		int lineNo;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			int lastIndex = fileName.lastIndexOf("/");
			readerGolden = new BufferedReader(new FileReader(
					fileName.substring(0, lastIndex + 1) + "golden."
							+ fileName.substring(lastIndex + 1)));
			for (lineNo = 1, line = reader.readLine(), lineGolden = readerGolden
					.readLine(); line != null && lineGolden != null; lineNo++, line = reader
					.readLine(), lineGolden = readerGolden.readLine()) {
				if (!line.equals(lineGolden)) {
					reader.close();
					readerGolden.close();
					throw new RuntimeException("Error comparing " + fileName
							+ ": line " + lineNo);
				}
			}
			reader.close();
			readerGolden.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException();
		} catch (IOException e) {
			throw new RuntimeException();
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());
			throw new RuntimeException();
		}
		return true;
	}
}
