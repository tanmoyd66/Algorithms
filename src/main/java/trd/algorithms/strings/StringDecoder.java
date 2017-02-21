package trd.algorithms.strings;

import java.security.InvalidParameterException;
import java.util.Stack;

public class StringDecoder {
	public static class StackElem {
		int count;
		String run;
		public StackElem(int count, String run) {
			this.count = count; this.run = run;
		}
		public String toString() {
			return count + ":" + run;
		}
	}
	
	// Strategy:
	//		We will maintain a stack where the store what we have seen so far
	//		On a pop we update the next stack element
	public static String Decode(String encoded) {
		int i = 0; 
		String bufferCount = "", bufferRun = "";
		Stack<StackElem> stack = new Stack<StackElem>();
		
		// Loop till EOS
		while (i < encoded.length()) {
			// Assumes the happy case
			if (Character.isDigit(encoded.charAt(i))) {
				// Start of a run of the form 2[xxx
				int j = i + 1; 
				for (;j < encoded.length() && Character.isDigit(encoded.charAt(j)); j++);
				bufferCount += (encoded.substring(i, j)); 
				i = j;
			} else if (encoded.charAt(i) == '[') {
				// The [ should be followed by a string or a number
				int j = i + 1; 
				for (;j < encoded.length() && Character.isLetter(encoded.charAt(j)); j++);
				bufferRun += (encoded.substring(i + 1, j));
				i = j;
				stack.push(new StackElem(Integer.parseInt(bufferCount), bufferRun));
				bufferCount = ""; bufferRun = "";
			} else if (encoded.charAt(i) == ']') {
				// While Popping update the next stack element
				StackElem se = stack.pop();
				StringBuilder decoded = new StringBuilder();
				for (int j = 0; j < se.count; j++)
					decoded.append(se.run);
				if (stack.size() > 0) {
					StackElem se2 = stack.peek();
					se2.run += decoded.toString();
				} else {
					return decoded.toString();
				}
				i++;
			} else {
				new InvalidParameterException("Invalid Sequencing");
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
		String encoded = "3[3[x]2[ab3[c2[]]]]";
		String decoded = Decode(encoded);
		System.out.printf("Encoded: [%s], Decoded: [%s]\n", encoded, decoded);
	}
}
