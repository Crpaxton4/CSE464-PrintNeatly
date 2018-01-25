package edu.miamioh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//A Dynamic programming solution for
//Word Wrap Problem in Java
public class WordWrap {

	final int MAX = Integer.MAX_VALUE;

	// A utility function to print the solution
	int printSolution(int p[], int n) {
		int k;
		if (p[n] == 1)
			k = 1;
		else
			k = printSolution(p, p[n] - 1) + 1;
		System.out.println("Line number" + " " + k + ": " + "From word no." + " " + p[n] + " " + "to" + " " + n);
		return k;
	}

	// l[] represents lengths of different words in input sequence. For example,
	// l[] = {3, 2, 2, 5} is for a sentence like "aaa bb cc ddddd". n is size of
	// l[] and M is line width (maximum no. of characters that can fit in a line)
	void solveWordWrap(int l[], int n, int M) {
		// For simplicity, 1 extra space is used in all below arrays

		// extras[i][j] will have number of extra spaces if words from i
		// to j are put in a single line
		int extras[][] = new int[n + 1][n + 1];

		// lc[i][j] will have cost of a line which has words from
		// i to j
		int lc[][] = new int[n + 1][n + 1];

		// c[i] will have total cost of optimal arrangement of words
		// from 1 to i
		int c[] = new int[n + 1];

		// p[] is used to print the solution.
		int p[] = new int[n + 1];

		// calculate extra spaces in a single line. The value extra[i][j]
		// indicates extra spaces if words from word number i to j are
		// placed in a single line
		/**
		 * ALSO LINE COST PART
		 */
		for (int i = 1; i <= n; i++) {
			extras[i][i] = M - l[i - 1];
			for (int j = i + 1; j <= n; j++)
				extras[i][j] = extras[i][j - 1] - l[j - 1] - 1;
		}

		// Calculate line cost corresponding to the above calculated extra
		// spaces. The value lc[i][j] indicates cost of putting words from
		// word number i to j in a single line
		
		/**
		 * LINE COST PART
		 */
		for (int i = 1; i <= n; i++) {
			for (int j = i; j <= n; j++) {
				if (extras[i][j] < 0)
					lc[i][j] = MAX;
				else if (j == n && extras[i][j] >= 0)
					lc[i][j] = 0;
				else
					lc[i][j] = extras[i][j] * extras[i][j] * extras[i][j];
			}
		}

		// Calculate minimum cost and find minimum cost arrangement.
		// The value c[j] indicates optimized cost to arrange words
		// from word number 1 to j.
		
		/**
		 * DYNAMIC PROGRAMMING PART
		 */
		c[0] = 0;
		for (int j = 1; j <= n; j++) {
			c[j] = MAX;
			for (int i = 1; i <= j; i++) {
				if (c[i - 1] != MAX && lc[i][j] != MAX && (c[i - 1] + lc[i][j] < c[j])) {
					c[j] = c[i - 1] + lc[i][j];
					p[j] = i;
				}
			}
		}

		System.out.println("Total Cost: " + c[n]);

		printSolution(p, n);
	}

	public static void main(String args[]) {
		WordWrap w = new WordWrap();
		
		try {
			ret dat = w.readWordsFromFile(args[0]);
			int[] l = dat.l;
			System.out.println(Arrays.toString(l));
			int n = l.length;
			int M = 80;
			w.solveWordWrap(l, n, M);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class ret{
		int[] l;
		String[] w;
		
		public ret(String[] w, Integer[] l) {
			this.w = w;
			this.l = new int[l.length];
		    for (int i=0; i < this.l.length; i++)
		    {
		        this.l[i] = l[i].intValue();
		    }
		}
		
	}

	public ret readWordsFromFile(String path) throws IOException {
		Scanner scanner = new Scanner(new File(path));
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<Integer> lengths = new ArrayList<Integer>();

		while (scanner.hasNext()) {
			String word = scanner.next();
			words.add(word);
			lengths.add(word.length());
		}

		scanner.close();
		
		
		return new ret(words.toArray(new String[words.size()]), lengths.toArray(new Integer[lengths.size()]));
	}
}

// This code is contributed by Saket Kumar