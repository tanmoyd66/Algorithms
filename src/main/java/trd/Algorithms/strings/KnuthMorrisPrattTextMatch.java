package trd.Algorithms.strings;

// DFA matching matches a pattern in O(n) time.
// What is the difference between DFA and KMP?
//		DFA requires O((PatternLength + 1) * alphabet) storage
//		KMP uses O(PatternLength) space by avoiding storing the transition function
//			It instead uses a Prefix function, from which the transition function can be calculated
//			The Prefix pattern will tell us how much more than 1 should we shift for the next match
//
// Stated differently:
//		Given that P[1..q] matches T[s+1...s+q] what is the least shift s1 such that for some k
//			P[1...k] = T[s1+1...s1+k] where s1+k = s+q
//      Given that P[1...q] is a suffix of T[s+1...s+q], 
//			what is the longest P[1...k] such that P[1...k] is a suffix of T[s+1...s+q]
//		That is ironically equivalent to saying that: 
//			What is the largest k, such that P[1...k] is a suffix of P[1...q]
//		We compute the function pi(q) = k and we can do that just by looking at P
//			and using the same technique we used in the case of DFA based matching
public class KnuthMorrisPrattTextMatch {

	char[] 		PatternArray;
	String 		Pattern;
	Integer[] 	Prefix;

	public KnuthMorrisPrattTextMatch(String Pattern) {
		this.PatternArray = Pattern.toCharArray();
		this.Pattern = Pattern;
		Prefix = new Integer[Pattern.length()];
	}

	public void CompilePattern() {
		
		// For every position q, compute the largest prefix k such that P[1...k] is a suffix of P[1...q] 
		int k = 0;
		Prefix[0] = 0;
		
		// Clearly, we cannot have a prefix [1..k] when q is 0
		// The beauty is that we can do this in O(m) time
		// Strategy:
		//    The loop variable is called q to mimic the state machine of DFA Search
		// 	  We will match the pattern against itself in linear time
		// 	  Setting the Prefix array is the only difference between Compilation and Matching
		for (int q = 1; q < Pattern.length();) {
			
			if (PatternArray[q] == PatternArray[k]) {
				// if the values of P[q] and P[k] match, that means
				// P[1...k-1]=P[q-k+1...q-1] => P[1...k]=P[q-k+1...q]
				// Forward both pointers
				Prefix[q] = k + 1;
				k++; q++;
			} else {
				// No match.
				// P[k] != P[q] => P[k-1] will tell you where to restart the match 
				// We have to reset the k pointer so that we reset the prefix.
				// Go back to where the Prefix of the previous location is.
				if (k != 0) {
					k = Prefix[k - 1];
				} else {
					// We cannot go any farther back than 0.
					Prefix[q] = 0;
					q++;
				}
			}
		}
	}
	
	public int MatchPattern(String Text) {
		int i = 0; // Tracks the text
		int j = 0; // Tracks the pattern

		// The i pointer never goes back.
		while (i < Text.length() && j < Pattern.length()) {
			if (Text.charAt(i) == Pattern.charAt(j)) {
				// If the characters match at the current position in the text and pattern
				// move forward both pointers
				i++; j++;
			} else {
				// There is a mismatch at current position
				if (j != 0) {
					// If this is not the first character of the pattern
					// find out how far back we need to go 
					// (based on how many characters before the current location we can use the matches for) 
					j = Prefix[j - 1];
				}
				else {
					// The first character of the pattern does not match the current position in the text
					// There is no question of a prefix match (prefix of 0 is nothing)
					// Move the text pointer forward and start at the first character of the pattern
					i++;
				}
			}
		}
		
		// We are broken out of the while loop. Either we have reached the text end or the pattern end
		// If we have reached the pattern end, we should be good to go.
		if (j == Pattern.length()) {
			return i - j;
		}
		// No Match
		return -1;
	}

	public static void main(String[] args) {
		KnuthMorrisPrattTextMatch kmp = new KnuthMorrisPrattTextMatch("ababaca");
		kmp.CompilePattern();
		String[] texts = new String[] { "zzababzababacaz" };
		for (String text : texts) {
			int result = kmp.MatchPattern(text);
			System.out.printf("Match in text: %s at: %d\n", text, result);
		}
	}
}
