package trd.algorithms.Arrays;

import java.util.Stack;

public class LargestRectangleInHistogram {
	public static int Find(Integer[] A) {
		
		int len = A.length;
		Stack<Integer> s = new Stack<Integer>();
		int maxArea = 0;
		
		// A[n] = 0 - simulated end to pop the stack
		for (int i = 0; i <= len; i++) {

			// Rightmost element is 0
			// We need this to induce calculation
			int h = (i == len ? 0 : A[i]);

			if (s.isEmpty() || h >= A[s.peek()]) {
				
				// For monotonically increasing just keep pushing
				s.push(i);
				
			} else {
				
				// Otherwise Pop and calculate the maximal area
				// top of the stack (tp) is the monotonically highest thus far
				int tp = s.pop();
				
				int height = A[tp];
				int width;
				
				// If the stack is empty at this point, the only width we will get
				// is all the way till the first block (i - 0)
				if (s.isEmpty())
					width = i;
				else
					width = i - 1 - s.peek();
				
				// Find the area
				maxArea = Math.max(maxArea, height * width);
				
				// Move i back. It will hold the i++ at the end of the loop
				i--;
			}
		}
		return maxArea;
	}

	public static void main(String[] args) {
		Integer[] A = new Integer[] { 1, 2, 3, 2 };
		Find(A);
	}
}