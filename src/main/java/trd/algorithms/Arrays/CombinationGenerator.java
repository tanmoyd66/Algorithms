package trd.algorithms.Arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import trd.algorithms.utilities.Tuples;

public class CombinationGenerator {
	
	public static <T> List<T> GetCombination(List<T> baseSet, Stack<Tuples.Pair<Integer, Boolean>> aux){
		List<T> thisComb = new ArrayList<T>();
		for (int i = 0; i < aux.size(); i++) {
			thisComb.add(baseSet.get(aux.get(i).elem1));
		}
		return thisComb;
	}
	
	public static <T> List<List<T>> GenerateAllCombinations(Collection<T> _baseSet, int R) {

		List<T> baseSet = new ArrayList<T>(_baseSet);
		
		List<List<T>> ret = new ArrayList<List<T>>();
		int bound = R == -1 ? baseSet.size() : R;
		
		// Add the null set
		if (R == -1) {
			ret.add(new ArrayList<T>());
		} else if (R == 0)
			return ret;
		
		// Create stack and bootstrap
		Stack<Tuples.Pair<Integer, Boolean>> combStack = new Stack<Tuples.Pair<Integer, Boolean>>();
		combStack.push(new Tuples.Pair<>(0,true));
		
		// For every entry in the stack, there are 2 states:
		//	 true:  the layers above it have not been seen
		//	 false: the layers above it has been seen
		while (!combStack.isEmpty()) {
			Tuples.Pair<Integer, Boolean> top = combStack.peek();
			
			if (top.elem2 == true) {
				// If the top is true then we will print the combinations which is in the stack
				// and push the next elements.
				if (R == -1 || combStack.size() == bound) {
					List<T> thisComb = GetCombination(baseSet, combStack);
					ret.add(thisComb);
				}
				top.elem2 = false;
			} else if (top.elem1 < baseSet.size() - 1) {
				// The top is false - that means we have printed it.
				// Now push the other levels above it
				combStack.push(new Tuples.Pair<>(top.elem1 + 1, combStack.size() < bound));
				top.elem2 = false;
			} else {
				// We need to pop the stack and go to the next elements in the current level
				combStack.pop();
				if (!combStack.isEmpty()) {
					top = combStack.peek();
					if (top.elem1 < baseSet.size() - 1) {
						top.elem1 += 1;
						top.elem2 = true;
					}
				}
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		String[] _elements = new String[] { "a", "b", "c", "d" };
		List<String> elements = Arrays.asList(_elements);		
		System.out.printf("%s\n", GenerateAllCombinations(elements, -1));
		System.out.printf("%s\n", GenerateAllCombinations(elements, 1));
		System.out.printf("%s\n", GenerateAllCombinations(elements, 2));
		System.out.printf("%s\n", GenerateAllCombinations(elements, 3));
		System.out.printf("%s\n", GenerateAllCombinations(elements, 4));
	}
}
