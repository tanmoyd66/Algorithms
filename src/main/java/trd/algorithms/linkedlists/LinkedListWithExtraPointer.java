package trd.algorithms.linkedlists;

public class LinkedListWithExtraPointer<T extends Comparable<T>> {
	public static class Node <T extends Comparable<T>> {
		public Node<T>  next;
		public Node<T>  random;
		public T		value;
		public Node(T node) {
			next = null;
			value = node;
		}
		public String toString() {
			return value.toString();
		}
	}
	Node<T> head;
	public LinkedListWithExtraPointer<T> clone() {
		
		// First clone list with pointer to new node
		for (Node<T> curr = head; curr != null; curr = curr.next) {
			Node<T> newNode = new Node<>(curr.value);
			newNode.next = curr.next;	// newNode is placed between curr and curr.next
			curr.next = newNode; 
			curr = newNode;
		}
		
		// Now set the random pointers
		for (Node<T> curr = head; curr != null;) {
			curr.next.random = curr.random.next;
			curr = curr.next.next;
		}
		
		// Now decouple the 2 lists
		LinkedListWithExtraPointer<T> ll = new LinkedListWithExtraPointer<T>();
		ll.head = head.next;
		for (Node<T> curr = head; curr != null;) {
			Node<T> cloneOfCurr = curr.next;
			cloneOfCurr.next = curr.next.next;
			curr.next = cloneOfCurr.next;
			curr = curr.next;
		}
		
		return ll;
	}
}
