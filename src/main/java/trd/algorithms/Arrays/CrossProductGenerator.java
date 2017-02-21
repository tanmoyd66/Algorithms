package trd.algorithms.Arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import trd.algorithms.utilities.Tuples;
import trd.algorithms.utilities.Utilities;

public class CrossProductGenerator {
	public static <T> List<T> ComposeOutput(
								Stack<Tuples.Pair<Integer,Integer>> stack, 
								List<List<T>> bases) {
		List<T> ret = new ArrayList<T>();
		for (int i = 0; i < stack.size(); i++) {
			Tuples.Pair<Integer,Integer> e = stack.get(i);
			ret.add(bases.get(e.elem1).get(e.elem2));
		}
		return ret;
	}
	
	// Ideally we would have a for loop for each base.
	// But the number of for loops itself is a variable.
	// So we will use a stack.
	public static <T> List<List<T>> Generate_Stack(List<List<T>> bases) {
		List<List<T>> ret = new ArrayList<List<T>>();
		Stack<Tuples.Pair<Integer,Integer>> stack = new Stack<Tuples.Pair<Integer,Integer>>();
		int numBases = bases.size();
		
		// Bootstrap with Layer 0 and 0th index in that layer
		stack.push(new Tuples.Pair<Integer, Integer>(0, 0));
		
		// Done in layers. Imagine each layer holds the index of the for loop
		while (!stack.isEmpty()) {
			Integer whichBase = stack.peek().elem1; 
			Integer idxInBase = stack.peek().elem2; 

			if (whichBase == numBases - 1) {
				// The stack is full. Print the stack contents &
				// increase the index count on the top of the stack
				if (idxInBase < bases.get(whichBase).size()) {
					ret.add(ComposeOutput(stack, bases));
					stack.peek().elem2++;
				} else {
					// We are done with the current top
					// keep popping until we reach a top that can be incremented
					while (!stack.isEmpty() &&
							stack.peek().elem2 >= bases.get(stack.peek().elem1).size() - 1)
						stack.pop();
					if (!stack.isEmpty())
						stack.peek().elem2++;
				}
			} else {
				// We need to augment the current layer & push in the next layer
				stack.push(new Tuples.Pair<>(whichBase + 1, 0));
			}
		}		
		return ret;
	}

	// Recursive formulation:
	// 	Generate for [idx+1...N]
	//  Then Append in front each of the elements of idx
	public static <T> List<List<T>> Generate_Recursive(List<List<T>> bases, int idx) {
		List<List<T>> ret = new LinkedList<List<T>>();
		
		if (idx == bases.size() - 1) {
			for (T t : bases.get(idx)) {
				List<T> thisList = new LinkedList<T>();
				thisList.add(t);
				ret.add(thisList);
			}
		} else {
			List<List<T>> retFromSub = Generate_Recursive(bases, idx + 1);
			for (T t : bases.get(idx)) {
				for (List<T> listFromSub : retFromSub) {
					LinkedList<T> clonedList = Utilities.CloneLinkedList(listFromSub);
					clonedList.addFirst(t);
					ret.add(clonedList);
				}
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		List<Integer> idx1 = Arrays.asList(new Integer[] { 1, 2, 3 });
		List<Integer> idx2 = Arrays.asList(new Integer[] { 1, 2 });
		List<Integer> idx3 = Arrays.asList(new Integer[] { 1 });
		List<Integer> idx4 = Arrays.asList(new Integer[] { 2, 3 });
		List<List<Integer>> bases = new ArrayList<List<Integer>>();
		bases.add(idx1); bases.add(idx2); bases.add(idx3); bases.add(idx4);
		List<List<Integer>> ret1 = Generate_Stack(bases);
		List<List<Integer>> ret2 = Generate_Recursive(bases, 0);
		System.out.printf("Stack Based (%d): %s\n", ret1.size(), ret1);
		System.out.printf("Recursive   (%d): %s\n", ret2.size(), ret2);
	}
}
