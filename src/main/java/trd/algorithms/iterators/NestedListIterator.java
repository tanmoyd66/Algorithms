package trd.algorithms.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import trd.algorithms.utilities.Tuples;

public class NestedListIterator<T> implements Iterator<T> {

	private Stack<Tuples.Pair<List<Object>,Integer>> stack = 
				new Stack<Tuples.Pair<List<Object>,Integer>>();
	T curr = null;
	
	public NestedListIterator(List<Object> nestedList) {
		if (nestedList != null)
			stack.push(new Tuples.Pair<List<Object>,Integer>(nestedList, 0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasNext() {
		if (curr == null) {
			while (!stack.isEmpty()) {
				
				// Get the top element from the stack
				Tuples.Pair<List<Object>,Integer> top = stack.peek();
				List<Object> currList = top.elem1; Integer currIdx = top.elem2;

				// See if you can return something from the current list on the top of stack
				boolean fContinue = false;
				while (currList.size() > currIdx) {
					Object currElement = currList.get(currIdx);
					
					// if the current element to be returned is also a list, then push new level
					if (List.class.isAssignableFrom(currElement.getClass())) {
						List<Object> currElemList = (List<Object>)currElement;
						
						// If the current list has potential candidates push it onto the stack
						// And continue the iteration to pick from stack top and return next
						if (currElemList != null) {
							stack.push(new Tuples.Pair<List<Object>,Integer>(currElemList, 0));
							fContinue = true;
						}
						break;
					} else {
						// We found an element to return. Return it.
						curr = (T)currElement;
						top.elem2++;
						return true;
					}
				}
				
				// Nothing to return from the top of the stack.
				// Pop a level out and see if you can return anything from that level.
				if (!fContinue) {
					stack.pop();
					if (!stack.empty())
						stack.peek().elem2++;
				}
			}
		}
		return curr == null ? false : true;
	}

	@Override
	public T next() {
		T retVal = curr;
		curr = null;
		return retVal;
	}
	
	public static void main(String[] args) {
		List<Object> l1 = new ArrayList<Object>();
		l1.add(Arrays.asList(new Integer[] {11, 12, 13}));
		l1.add((Integer)2); l1.add((Integer)3);
		l1.add(Arrays.asList(new Integer[] {41, 42}));
		List<Object> l51 = new ArrayList<Object>();
		List<Object> l511 = new ArrayList<Object>();
		List<Object> l5111 = Arrays.asList(new Integer[] {5111, 5112});
		l511.add(l5111);
		l51.add(l511);
		l1.add(l51);
		NestedListIterator<Integer> nli = new NestedListIterator<Integer>(l1);
		while (nli.hasNext())
			System.out.printf("%d ", nli.next());
		System.out.println();
	}
}
