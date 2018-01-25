package edu.miamioh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

/**
 * Consider the problem of neatly printing a paragraph with a monospaced font
 * (all characters having the same width) on a printer. The input text is a
 * sequence of n words of lengths l[0], l[1], ..., l[n-1] , measured in
 * characters. We want to print this paragraph neatly on a number of lines that
 * hold a maximum of M characters each. Our criterion of "neatness" is as
 * follows. If a given line contains words i through j, where i <= j , and we
 * leave exactly one space between words, the number of extra space characters
 * at the end of the line is [EDIT: you are to figure this out], which must be
 * nonnegative so that the words fit on the line. We wish to minimize the sum,
 * over all lines except the last, of the cubes of the numbers of extra space
 * characters at the ends of lines. Give a dynamic-programming algorithm to
 * print a paragraph of n words neatly on a printer. Analyze the running time
 * and space requirements of your algorithm.
 *
 * NOTE: I have provided two files (magna_carta.txt and kubla_kahn.txt). I know
 * what the correct cost is for each, and I know the approximate running time
 * (if you mess up DP then you will be off by several orders of magnitude).
 */
public class Main {

	static int INFINITY = Integer.MAX_VALUE;
	static int MAXLINE = 80;

	final static class Line {
		final int i;
		final int j;
		final int cost;

		public Line(int i, int j, int cost) {
			this.i = i;
			this.j = j;
			this.cost = cost;
		}
	}

	static String[] readWordsFromFile(String path) throws IOException {
		Scanner scanner = new Scanner(new File(path));
		ArrayList<String> words = new ArrayList<String>();

		while (scanner.hasNext())
			words.add(scanner.next());

		scanner.close();
		return words.toArray(new String[words.size()]);
	}

	static int lineCost(int i, int j, int[] cumSumOfWordLengths, int maxline) {
		// If S[k] is the cumulative sum of word lengths up to word k, or 0 if k < 0
		// Then this should be the value M - (S[j] - S[i-1] + j - i)
		if (i > j) {
			return INFINITY;
		}

		int extraSpaces = maxline - (cumSumOfWordLengths[j] - cumSumOfWordLengths[i - 1] + j - i);

		if (extraSpaces < 0) {
			return INFINITY;
		} else if (j == cumSumOfWordLengths.length - 1 && extraSpaces >= 0) {
			return 0;
		} else {
			return (int) Math.pow(extraSpaces, 3);
		}
	}

	static int printNeatly(String[] words, int maxLine, OutputStream out) throws IOException {
		// Minimize the cost of printing the words.
		// The cost is the amount of unused space on each line (cubed),
		// or INFINITY of the words don't fit.
		// The last line has no cost.
		// (e.g. there is no penalty for unused spaces on the last line).
		Stack<Line> solution = dynamicSolution(words, maxLine);
		StringBuffer str = new StringBuffer();
		Line l = null;

		while (!solution.empty()) {
			l = solution.pop();
			str.append(getLineAsStringBuffer(words, l));
			str.append("\n");
		}

		out.flush();
		out.write(str.toString().getBytes());

		return l.cost; // The total cost (sum of unusedSpace^3 for
						// all but last line)
	}

	public static StringBuffer getLineAsStringBuffer(String[] words, Line l) {
		StringBuffer str = new StringBuffer();
		int i = l.i;
		int j = l.j;

		str.append(words[i - 1]);
		for (int k = i; k < j; k++) {
			str.append(" ");
			str.append(words[k]);

		}

		return str;
	}

	static Stack<Line> dynamicSolution(String[] words, int maxLine) {

		int[] cumSumOfWordLengths = cumSumOfWordLengths(words);
		int[] solutions = new int[cumSumOfWordLengths.length];
		int[] startOflines = new int[cumSumOfWordLengths.length];

		startOflines[0] = 0;
		solutions[0] = 0;

		for (int j = 1; j < cumSumOfWordLengths.length; j++) {

			int min = INFINITY;
			int index = -1;
			for (int i = 1; i <= j; i++) {

				int totalCost;
				if (lineCost(i, j, cumSumOfWordLengths, maxLine) == INFINITY) {
					totalCost = INFINITY;
				} else {
					totalCost = solutions[i - 1] + lineCost(i, j, cumSumOfWordLengths, maxLine);
				}

				if (totalCost < min) {
					min = totalCost;
					index = i;
				}

			}
			
			solutions[j] = min;
			startOflines[j] = index;

		}

		Stack<Line> lines = new Stack<Line>();

		int end = words.length;

		while (end != 0) {
			Line l = new Line(startOflines[end], end, solutions[end]);
			lines.push(l);
			end = startOflines[end] - 1;
		}

		return lines;
	}

	static int[] cumSumOfWordLengths(String[] words) {
		int[] cumSumOfWordLengths = new int[words.length + 1];
		cumSumOfWordLengths[0] = 0;

		for (int i = 1; i <= words.length; i++) {
			cumSumOfWordLengths[i] = cumSumOfWordLengths[i - 1] + words[i - 1].length();
		}

		return cumSumOfWordLengths;

	}

	static boolean checkThatNoLinesStartOrEndWithSpaces(String text) {
		String[] lines = text.split("\n");
		for (String line : lines) {
			if (line.charAt(line.length()-1) == ' ' || line.charAt(0) == ' ') {
				// false if line starts or ends with space
				return false;
			}
		}
		return true; 
	}

	static boolean checkThatNoMoreThanOneSpaceBetweenWords(String text) {
		String[] words = text.split(" ");
		for (String word : words) {
			if (word.isEmpty()) {
				return false; // false means multiple spaces in a row were found
			}
		}

		return true; 
	}

	static boolean checkThatAllLinesAreLessThanMax(String text, int maxline) {
		String[] lines = text.split("\n");
		for (String line : lines) {
			if (line.length() > maxline) {
				return false; // false means you output a line that was too long
			}
		}
		return true; 
	}

	static int computeCostFromOutput(String text, int maxline) {
		String[] lines = text.split("\n");
		int totalCost = 0;

		for(int i = 0; i < lines.length-1; i++) { // -1 so that extra space in last line is ignored
			totalCost += Math.pow((maxline - lines[i].length()), 3);
		}

		return totalCost; // Compute the cost to verify that
						  // it matches what we get from printNeatly
	}

	public static void main(String[] args) {

		for (String arg : args) {
			try {
				String[] words = readWordsFromFile(arg);

				ByteArrayOutputStream result = new ByteArrayOutputStream();
				long trials = 0;
				long total = 0;
				long stop = 0;
				long start = System.currentTimeMillis();
				long cost = 0;
				while (total < 30) {
					result.reset();
					cost = printNeatly(words, MAXLINE, result);
					stop = System.currentTimeMillis();
					total = stop - start;
					trials += 1;
				}
				float average = total / (float) trials;

				String text = result.toString();
				assert checkThatAllLinesAreLessThanMax(text, MAXLINE);				
				assert checkThatNoLinesStartOrEndWithSpaces(text);				
				assert checkThatNoMoreThanOneSpaceBetweenWords(text);				
				assert computeCostFromOutput(text, MAXLINE) == cost;

				FileWriter f = new FileWriter("output.txt");
				f.write(text);
				f.close();

				System.out.print(arg);
				System.out.print(", average running time: ");
				System.out.println(average);
				System.out.print(", cost: ");
				System.out.println(cost);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
