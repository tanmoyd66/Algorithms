package trd.algorithms.Arrays;

import java.util.Stack;

public class ValidParenthesization {
	public static boolean Validate(String s) {
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(')
				stack.push(')');
			else if (s.charAt(i) == '{')
				stack.push('}');
			else if (s.charAt(i) == '[')
				stack.push(']');
			else if (s.charAt(i) == ')' || 
					 s.charAt(i) == '}' || 
					 s.charAt(i) == ']') {
				Character top = stack.pop();
				if (top != s.charAt(i))
					return false;
			}
		}
		return true;
	}
}
