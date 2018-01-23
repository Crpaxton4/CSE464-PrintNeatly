package edu.miamioh;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrintNeatlyTest {
	int maxLineLength = 10;

	@Test
	void checkThatAllLinesAreLessThanMaxTest() {
		String testPass = "CJM2Wxfx\nFpLQDyCJqe\n3t1GbQraiK\nrl2NsTbg52\nyqYM4nibhE\nt25RNz50Kh\nQ3ZIFyJIR4\nnKVZdN6HP7\nmpiJwQWcsU\nL5IAAzfh8Y\npUHmcYp2uB\nlR0lRAwdl9\nXKrlrnaBIf\nOqusYRxXTv\n8omwf4uEsi\nlIxARkfFJS\nwLoFVzyuqK\n2MztIBCGRT\njv9dJjjB1B\nzyiQ8az7EY";
		String testFail = "CJM2Q3Wxfx\nFpLQDyCJqe\n3t1GbQraiK\nrl2NsTbg52\nyqYM4nibhE\nt25RNz50Kh\nQ3ZIFyJIR4\nnKVZdN6HP7\nmpiJwQWcsU\nL5IAAzfh8Y\npUHmcYp2uB\nlR0lRAwdl9\nXKrlrnaBIf\nOqusYRxXTv\n8omwf4uEsi\nlIxARkfFJS\nwLoFVzyuqK\n2MztI_BCGRT\njv9dJjjB1B\nzyiQ8az7EY";
		Main.MAXLINE = maxLineLength;
		
		assertTrue(Main.checkThatAllLinesAreLessThanMax(testPass, Main.MAXLINE));
		assertFalse(Main.checkThatAllLinesAreLessThanMax(testFail, Main.MAXLINE));
		
	}
	
	@Test
	void checkThatNoMoreThanOneSpaceBetweenWordsTest() {
		String testPass = "jndfkjndfbkjnf jndfkj kjnd kjndfkjn\nkbjfjb kjbdf kjbn jfkjb kjbfkjbfb\nndfbjndfbkjn kjnfb kjnfb kjnf \n kjbdfkjf kj ljjn kjn kjn";
		String testFail = "ljndfbln lkndblndfb kljn kljndbljnb jkndb kjndb \nkjbdfkjdfb kjnfdb kjnfdb  kjbdfbkjf\n jndfbkjfb ljn kn jnfbkjn";
	
		assertTrue(Main.checkThatNoMoreThanOneSpaceBetweenWords(testPass));
		assertFalse(Main.checkThatNoMoreThanOneSpaceBetweenWords(testFail));
	}
	
	@Test
	void checkThatNoLinesStartOrEndWithSpacesTest() {
		String testPass = "CJM2Wxfx\nFpLQDyCJqe\n3t1GbQraiK\nrl2NsTbg52\nyqYM4nibhE\nt25RNz50Kh\nQ3ZIFyJIR4\nnKVZdN6HP7\nmpiJwQWcsU\nL5IAAzfh8Y\npUHmcYp2uB\nlR0lRAwdl9\nXKrlrnaBIf\nOqusYRxXTv\n8omwf4uEsi\nlIxARkfFJS\nwLoFVzyuqK\n2MztIBCGRT\njv9dJjjB1B\nzyiQ8az7EY";
		String testFail = "jndfkjndfbkjnf jndfkj kjnd kjndfkjn\nkbjfjb kjbdf kjbn jfkjb kjbfkjbfb\nndfbjndfbkjn kjnfb kjnfb kjnf \n kjbdfkjf kj ljjn kjn kjn";
	
		assertTrue(Main.checkThatNoLinesStartOrEndWithSpaces(testPass));
		assertFalse(Main.checkThatNoLinesStartOrEndWithSpaces(testFail));
	}
	
	@Test
	void computeCostFromOutputTest() {
		String test0 = "jfnv fnvur\njfkdjfnv r\nldldmv fkf\njdjdjsjsks\ndkdkd dkdk\ndkd dkd dk";
		String test27 = "jfjfjfjfjf\njf jfjfjfj\njfjfjfj\nkfkf fkd d\njfj jfj jf";
		Main.MAXLINE = maxLineLength;
		
		assertTrue(Main.computeCostFromOutput(test0, Main.MAXLINE) == 0);
		assertTrue(Main.computeCostFromOutput(test27, Main.MAXLINE) == 27);
		
	}

}
