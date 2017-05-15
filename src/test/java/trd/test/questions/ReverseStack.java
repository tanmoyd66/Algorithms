package trd.test.questions;

import java.util.Stack;

public class ReverseStack {
	Stack<Integer> theStack = new Stack<Integer>();
	// Recursively insert to the bottom
	public void InsertBottom(Integer val) {
		if (theStack.isEmpty())
			theStack.push(val);
		else {
			// Take the top and remember it
			Integer temp = theStack.pop();
			
			// Recursively insert bottom
			InsertBottom(val);
			
			// Insert the top
			theStack.push(temp);
		}
	}
	// Recursively reverse
	public void Reverse() {
		if (!theStack.isEmpty()) {
			// Keep the stack in the call stack
			Integer temp = theStack.pop();
			
			// Recursively reverse
			Reverse();
			
			// Now insert the top to the bottom
			InsertBottom(temp);
		}
	}
	public static void main(String[] args) {
		ReverseStack rs = new ReverseStack();
		for (int elem : new Integer[] { 1, 2, 3, 4 })
			rs.theStack.push(elem);
		System.out.printf("%s\n", rs.theStack);
		rs.Reverse();
		System.out.printf("%s\n", rs.theStack);
	}
}
