package trd.algorithms.strings;

public class SimplePatternMatching {
	
	// Simple pattern matcher with Kleene Closure, wildcard-character match 
	public static boolean PatternHasFollow(char[] Pattern, int iPattern) {
		boolean fRet = false;
		for (int j = iPattern; j < Pattern.length; j++) {
			if (iPattern < Pattern.length - 1 && Pattern[iPattern + 1] == '*') {
				j++; fRet = true;
			} else 
				fRet = false;
		}
		return fRet;
	}
	public static boolean SimpleMatch(char[] Text, char[] Pattern, int iText, int iPattern) {
		
		// Base of Recursion, if we are past the text limit
		if (iText > Text.length - 1 && iPattern > Pattern.length - 1) {
			return true;
		} else if (iText > Text.length - 1) {
			// if there is more pattern left does not match
			if (PatternHasFollow(Pattern, iPattern))
				return false;
			return true ;
		} else if (iPattern > Pattern.length - 1) {
			// if there is more pattern left does not match
			if (iText < Text.length)
				return false;
			return true ;
		}
		
		// In the case of iPattern pointing to X*, we will keep iPattern pointing to X
		if (Pattern[iPattern] == '.') {
			// Inductive step: case 1: '.' 
			return SimpleMatch(Text, Pattern, iText + 1, iPattern + 1);
		} else if ((iPattern < Pattern.length - 1) && Pattern[iPattern + 1] == '*') {
			// Inductive step: case 2: 'X*'
			while (iText < Text.length && Text[iText] == Pattern[iPattern])
				iText++;
			return SimpleMatch(Text, Pattern, iText, iPattern + 2);
		} else {
			// Inductive step: case 3: 'direct match'
			if (Text[iText] != Pattern[iPattern])
				return false;
			return SimpleMatch(Text, Pattern, iText + 1, iPattern + 1);
		}
		
		// Termination Proof:
		//	 (a) Base case returns true or false
		//	 (b) Inductive step: We either move iText or move both iText and iPattern
	}
	
	public static void main(String[] args) {
		if (true) {
			char[] text = "ac".toCharArray(); char[] pattern = "ab*c".toCharArray();
			System.out.printf("Text: [%s] %s match Pattern:[%s]\n", new String(text), SimpleMatch(text, pattern, 0, 0) ? "does" : "does not", new String(pattern));
		}
	}
}
