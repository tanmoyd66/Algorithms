package trd.algorithms.datastructures;

import java.util.HashMap;

// Data structure to do Union/Find operations fast
public class DynamicSet<T> {
	public static class Node<T> {
		T			value;
		Node<T>		parent;
		int			rank;
		
		public Node(T value) {
			this.value = value;
			this.rank = 0;
			this.parent = this;
		}
	}
	
	// Hash table that captures every element in the set
	HashMap<T, Node<T>>  element2NodeMap = new HashMap<T, Node<T>>();

	// Initialization. 
	// In the beginning every set element is its own parent
	// They have a rank of 0
	public void MakeSet(T val) {
		Node<T>	elem = element2NodeMap.get(val);
		if (elem != null)
			return;
		elem = new Node<T>(val);
		element2NodeMap.put(val, elem);
	}
	
	// Traverse the parentage till you reach the top most element
	// The Top most element is the representative of that subset.
	public Node<T> FindSet(T val) {
		Node<T>	elem = element2NodeMap.get(val);
		if (elem == null) {
			return null;
		}
		
		while (elem.parent != elem) {
			elem = elem.parent;
		}
		return elem;
	}
	
	// Links elemX and elemY into the same set
	// We check, which one has a lower rank and make that the child
	public void Link(Node<T> elemX, Node<T>	elemY) {
		if (elemX.rank > elemY.rank)
			elemY.parent = elemX;
		else {
			elemX.parent = elemY;
			if (elemX.rank == elemY.rank)
				elemY.rank++;
		}
	}

	// To union 2 elements is to bring them into the same subset
	// We do that by linking the representative element of each subset
	public void Union(T valX, T valY) {
		Link(FindSet(valX), FindSet(valY));
	}
}
