package trd.test.questions;

import java.util.Stack;

public class WellFormedParenthesis {
	public static boolean check(String s) {
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case '(': stack.push(')'); break;
			case '[': stack.push(']'); break;
			case '{': stack.push('}'); break;
			default:
				if (stack.pop() != s.charAt(i))
					return false;
			}
		}
		return stack.isEmpty() ? true: false;
	}
	public static void main(String[] args) {
		String[] tests = new String[] {"()", "(", "(()(()))" };
		for (String t: tests)
			System.out.printf("%s:%s\n", t, check(t));
	}
}
