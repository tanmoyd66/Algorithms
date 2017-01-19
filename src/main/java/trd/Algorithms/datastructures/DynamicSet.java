package trd.Algorithms.datastructures;

import java.util.HashMap;

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
	
	HashMap<T, Node<T>>  element2NodeMap = new HashMap<T, Node<T>>();
	
	public void MakeSet(T val) {
		Node<T>	elem = element2NodeMap.get(val);
		if (elem != null)
			return;
		elem = new Node<T>(val);
		element2NodeMap.put(val, elem);
	}
	
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
	
	public void Link(Node<T> elemX, Node<T>	elemY) {
		if (elemX.rank > elemY.rank)
			elemY.parent = elemX;
		else {
			elemX.parent = elemY;
			if (elemX.rank == elemY.rank)
				elemY.rank++;
		}
	}

	public void Union(T valX, T valY) {
		Link(FindSet(valX), FindSet(valY));
	}
}
