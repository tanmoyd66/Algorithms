package trd.algorithms.strings;

public class BoyerMooreTextMatch {
	
	// We will use an array to demonstrate. 
	// If you have a large alphabet, using a map is better.
	public static String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public static int	 maxchars = 256;
	
	private Integer[] 	Right;
	private Integer[]	Prefix;
	private String 		Pattern;
	
	public BoyerMooreTextMatch(String Pattern) {
		this.Pattern = Pattern;
	}
	
	public static <S> String StringToString(String A) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("   ["));
		for (int i = 0; i < A.length(); i++)
			sb.append(String.format("%s ", A.charAt(i)));
		sb.append(String.format("]"));
		return sb.toString();
	}

	public static <S> String ArrayToString(String tag, S[] A) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s:[", tag));
		for (int i = 0; i < A.length; i++)
			sb.append(String.format("%s ", A[i] == null ? " " : A[i]));
		sb.append(String.format("]", tag));
		return sb.toString();
	}

	public void CompilePattern() {
		
		// The Right array contains the rightmost location of the character
		Right = new Integer[maxchars];
		for (int i = 0; i < Right.length; i++) 
			Right[i] = -1;
		for (int i = 0; i < Pattern.length(); i++) 
			Right[(int)Pattern.charAt(i)] = i;
		
		this.Prefix  = new Integer[Pattern.length()];

		// For every position q, compute the largest prefix k such that P[1...k] is a suffix of P[1...q] 
		int k = 0;
		Prefix[0] = 0;
		
		System.out.printf("%s\n", StringToString(Pattern));
		
		// Clearly, we cannot have a prefix [1..k] when q is 0
		// The beauty is that we can do this in O(m) time
		// Strategy:
		//    The loop variable is called q to mimic the state machine of DFA Search
		// 	  We will match the pattern against itself in linear time
		// 	  Setting the Prefix array is the only difference between Compilation and Matching
		System.out.printf("%s  %s\n", ArrayToString(String.format("%2s", Pattern.charAt(0)), Prefix), "Initialize");
		for (int q = 1; q < Pattern.length();) {

			if (Pattern.charAt(q) == Pattern.charAt(k)) {
				// if the values of P[q] and P[k] match, that means
				// P[1...k-1]=P[q-k+1...q-1] => P[1...k]=P[q-k+1...q]
				// Forward both pointers
				Prefix[q] = k + 1;
				System.out.printf("%s  %s\n", ArrayToString(String.format("%2s", Pattern.charAt(q)), Prefix), 
								  String.format("Match for %s(%d) with %s(%d)", Pattern.charAt(q), q, Pattern.charAt(k), k));
				k++; q++;
			} else {
				// No match.
				// P[k] != P[q] => P[k-1] will tell you where to restart the match 
				// We have to reset the k pointer so that we reset the prefix.
				// Go back to where the Prefix of the previous location is.
				if (k != 0) {
					System.out.printf("%s  %s\n", ArrayToString(String.format("%2s", Pattern.charAt(q)), Prefix), 
							String.format("Jump back from %s(%d) to %s(%d)", Pattern.charAt(k), k, Pattern.charAt(Prefix[k - 1]), Prefix[k - 1]));
					k = Prefix[k - 1];
				} else {
					// We cannot go any farther back than 0.
					Prefix[q] = 0;
					System.out.printf("%s  %s\n", ArrayToString(String.format("%2s", Pattern.charAt(q)), Prefix), 
							String.format("No more jumps possible from %s(%d)", Pattern.charAt(q), Prefix[q]));
					q++;
				}
			}
		}

	}
	
	public int MatchPattern(String Text) {
		int patLen = Pattern.length();
		int txtLen = Text.length();		
		int shift  = 0;
		
		while(shift <= (txtLen - patLen)) {
			int j = patLen - 1;
			
			// Match from the right until the characters match
			while (j >= 0 && Pattern.charAt(j) == Text.charAt(shift + j))
				j--;
        
			// If we are past the left end of the pattern, indicate the match
			if (j < 0)
				return shift;
			else {
				// We have not fallen off the left end and thence the text and pattern does not match
				// Align with the right-most occurrence of the bad character
				shift += Math.max(1, j - Right[(int)Text.charAt(shift + j)]);
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		BoyerMooreTextMatch bm = new BoyerMooreTextMatch("ababaca");
		bm.CompilePattern();
		String[] texts = new String[] { "zzababaaazababacaz" };
		for (String text : texts) {
			int result = bm.MatchPattern(text);
			System.out.printf("Match in text: %s at: %d\n", text, result);
		}
	}
}
