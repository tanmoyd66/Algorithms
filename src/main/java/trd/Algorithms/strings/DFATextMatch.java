package trd.Algorithms.strings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import trd.Algorithms.utilities.Tuples;
import trd.Algorithms.utilities.Utilities;

public class DFATextMatch {

	char[] Pattern;
	String PatternString;
	HashMap<Long,Integer> Transition = new HashMap<Long,Integer>();

	public DFATextMatch(String Pattern) {
		this.Pattern = Pattern.toCharArray();
		this.PatternString = Pattern;
	}
	
	public void CompilePattern() {
		// We will build a finite state machine with the following characteristics:
		//    (0) There are (Pattern.length + 1) states in the state machine.
		//    (1) The spine edges are those for each character in the alphabet for each character in the pattern.
		//    (2) At any point, D(state,char) is the state that is the length of the largest prefix that is a suffix of
		//        what the spie edges are till this (state || char).
		//        That is D(state, char) = k such that P(1:k) is the largest suffix of (P(1:state + 1)||char)
		int patLen 	 = Pattern.length;
		Set<Character> alphabet = Arrays.asList(Tuples.CharArrayToCharacterArray(Pattern)).stream().collect(Collectors.toSet());
		
		// Number of States: Pattern length + 1
		// Number of Transitions: one of each character in the alphabet
		for (int state = 0; state < patLen + 1; state++) {
			for (Character trans : alphabet) {

				int transition  = (int)(char)trans;
				String prefix, suffix;				
				
				// Start from the minimum of the number of (states + 1) or the pattern-length
				int 	k = Math.min(patLen + 1, state + 2);
				boolean tryASmallerK;
				do {
					// Start from the largest value of k and keep reducing it
					k = k - 1;
					
					// Calculate the prefix and the suffix 
					//	  Prefix: P[1..k]
					//    Suffix: P[1..state]||char
					prefix = PatternString.substring(0, k);
					suffix = (state == 0 ? "" : PatternString.substring(0, state)) + new String(new char[] {(char)trans});
					
					// check if P[1..k] is the largest prefix which is also a suffix
					tryASmallerK = !suffix.endsWith(prefix);
				} while (tryASmallerK);
				int targetState = k >= 0 ? k : 0;

				// By convention, we will store the transition function values for only those that do not take us to state 0
				if (targetState != 0) {
					Long key = Utilities.LongFromTwoIntegers(state, transition);
					System.out.printf("d(%d,%1s) = %d\n", state, new String(new char[] {trans}), targetState);
					Transition.put(key, targetState);
				}
			}
		}
	}
	
	// Run through the DFA in a linear scan.
	public int MatchPattern(String Text) {
		int DFAState = 0;
		for (int i = 0; i < Text.length(); i++) {
			
			// Get next character
			char 	curr = Text.charAt(i);
			
			// Find next state
			Integer	nextState = Transition.get(Utilities.LongFromTwoIntegers(DFAState, (int)curr));
			DFAState = nextState == null ? 0 : nextState;
			
			if (DFAState == PatternString.length()) {
				return i - Pattern.length + 1;
			}
		}
		return -1;
	}
	public static void main(String[] args) {
		DFATextMatch dfa = new DFATextMatch("ababaca");
		dfa.CompilePattern();
		String[] texts = new String[] { "zzababacaz" };
		for (String text : texts) {
			int result = dfa.MatchPattern(text);
			System.out.printf("Match in text: %s at: %d\n", text, result);
		}
	}
}
