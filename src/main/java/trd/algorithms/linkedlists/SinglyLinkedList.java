package trd.algorithms.linkedlists;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class  SinglyLinkedList<T extends Comparable<T>>  {
	public static class Node <T extends Comparable<T>> {
		public Node<T>  next;
		public T		value;
		public Node(T node) {
			next = null;
			value = node;
		}
		public String toString() {
			return value.toString();
		}
	}
	
	Node<T>  head, tail;

	public Node<T> getHead() {
		return head;
	}
	public Node<T> getTail() {
		return tail;
	}
	
	public SinglyLinkedList() {
		head = tail = null;
	}
	public SinglyLinkedList(Node<T> head, Node<T> tail) {
		this.head = head; this.tail = tail;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (Node<T> scanX = head; scanX != null; scanX = scanX.next) {
			sb.append(scanX == head ? "" : ", ");
			sb.append(scanX.toString());
		}
		sb.append("]");
		return sb.toString();
	}
	
	public T getNth(int n) {
		if (head == null)
			return null;
		Node<T> scanX = head, scanY = head;
		
		// Position 2 pointers at a distance of n
		for (int i = 0; scanY != null && i < n; i++)
			scanY = scanY.next;
		if (scanY == null)
			return null;
		
		// Now move each in lock step until Y reaches end. Return X
		while (scanY != null) {
			scanY = scanY.next;
			scanX = scanX.next;
		}
		return scanX.value;
	}
	
	public void truncate() {
		head = tail = null;
	}
	
	public void insertHead(T iVal) {
		Node<T> newNode = new Node<T>(iVal);
		newNode.next = head;
		head = newNode;
		if (tail == null)
			tail = head;
	}

	public void insertTail(T iVal) {
		Node<T> newNode = new Node<T>(iVal);
		if (head == null) {
			head = tail = newNode;
			return;
		}
		tail.next = newNode;
		tail = newNode;
	}

	public void insertAfter(Node<T> last, SinglyLinkedList<T> other) {
		if (other == null || other.head == null) 
			return;
		if (last == null) {
			other.tail.next = head;
			head = other.head;
		} else {
			Node<T> temp = last.next;
			last.next = other.head;
			other.tail.next = temp;
		}
	}
	
	public void insertHead(SinglyLinkedList<T> other) {
		insertAfter(null, other);
	}

	public void insertTail(SinglyLinkedList<T> other) {
		insertAfter(tail, other);
	}

	public List<T> getArray() {
		List<T> ret = new ArrayList<T>();
		for (Node<T> node = head; node.next != null; node = node.next)
			ret.add(node.value);
		return ret;
	}
	
	public Node<T> findMiddle() {
		// Start 2 pointers - one scan at 2X speed another at X
		// When the 2X pointer reaches the end, return the position of X
		Node<T> scanX  = head;
		Node<T> scan2X = head;
		while (scanX != null && scan2X != null) {
			scanX  = scanX.next;
			scan2X = scan2X.next == null ? null : scan2X.next.next;
		}
		return scanX;
	}
	
	public SinglyLinkedList<T> splitIntoTwo(Node<T> node) throws InvalidParameterException {
		if (node == null)
			throw new InvalidParameterException("");

		// Find Node that points to the node that will be the start node of the second list
		Node<T> scanX = head;
		for (; scanX != null; scanX = scanX.next) {
			if (scanX.next == node)
				break;
		}
		if (scanX == null)
			throw new InvalidParameterException("");
		
		// Set up the other list and return
		SinglyLinkedList<T> otherList = new SinglyLinkedList<T>();
		otherList.head = node; otherList.tail = tail;
		tail = scanX; tail.next = null;
		return otherList;
	}

	public void deleteN(Node<T> node, int N) throws InvalidParameterException {
		if (node == null)
			throw new InvalidParameterException("");
		Node<T> scanX  = head;
		while (scanX != null) {
			if (scanX.next == node) {
				int i = 0; Node<T> scanY = node;
				for (; scanY != null && ++i < N; scanY = scanY.next);
				scanX.next = (scanY == null ? null : scanY.next);
				return;
			}
			scanX = scanX.next;
		}
		throw new InvalidParameterException("");
	}
	
	// Iterative Reverse - sliding window
	public void iterativeReverse() {
		Node<T> oldH = head;
        Node<T> prev = null;
        Node<T> curr = head;
        Node<T> next = null;
        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        head = prev; tail = oldH;
 	}
	
	// Recursively reverses a linked list
	public void recursiveReverse() {
		Node<T> newTail = recursiveReverse(head);
		head = tail;
		tail = newTail; 
	}
	private Node<T> recursiveReverse(Node<T> node) {
		if (node == null)
			return null;
		if (node.next == null)
			return node;
		Node<T> partialReverse = recursiveReverse(node.next);
		partialReverse.next = node;
		node.next = null;
		return node;
	}

	// Merges 2 lists item by item
	public void mergeAlternate(SinglyLinkedList<T> mergee) {
		Node<T> scanX = head, scanY = mergee.head, nextX, nextY;
		while (scanX != null && scanY != null) {
			nextX = scanX.next; nextY = scanY.next;
			scanX.next = scanY;
			scanY.next = nextX;
			
			// Keep the tail invariant
			if (tail == scanX) 
				tail = scanY;

			// Move forward
			scanX = nextX; scanY = nextY;
		}
	}
	
	// Swizzles the linked list
	public void swizzle() {
		Node<T> middle = findMiddle();
		SinglyLinkedList<T> secondList = splitIntoTwo(middle);
		secondList.iterativeReverse();
		mergeAlternate(secondList);
	}
	
	// Check if Palindrome
	public boolean isPalindrome() {
		Node<T> middle = findMiddle();
		SinglyLinkedList<T> secondList = splitIntoTwo(middle);
		secondList.recursiveReverse();
		
		boolean fContinue = true;
		Node<T> scanX = head, scanY = secondList.head;
		while (fContinue && scanX != null && scanY != null) {
			fContinue = scanX.value.equals(scanY.value);
			scanX = scanX.next; scanY = scanY.next;
		}
		
		secondList.iterativeReverse();
		insertTail(secondList);
		
		return fContinue;
	}
	
	public void reverseEveryK(int k) {
		Node<T> scanX = head, lastReversePoint = null; int numThisScan = 0;
		while (true) {
			if (++numThisScan == k || scanX.next == null) {
				
				// We are here because we saw k-elements or the last block of less than k
				
				// Position the sublist to be the rest of the list without this part
				Node<T> headX = lastReversePoint == null ? head : lastReversePoint.next, tailX = scanX;
				scanX = scanX.next; 
				if (lastReversePoint != null) {
					lastReversePoint.next = scanX;
				} else {
					head = scanX;
				}
				tailX.next = null;
				
				// Create a new list (no copying) [headX, scanX]. Reverse, append to the beginning of this.
				SinglyLinkedList<T> temp = new SinglyLinkedList<T>(headX, tailX);
				temp.iterativeReverse();
				this.insertAfter(lastReversePoint, temp);
				
				if (scanX == null) {
					tail = temp.tail;
					return;
				}
				
				lastReversePoint = temp.tail;
				numThisScan = 0;
			} else {
				// Progress accumulation of the list to reverse
				scanX = scanX.next;
			}
		}
	}
	
	public void swapEveryOther() {
		reverseEveryK(2);
	}
	
	public void merge(SinglyLinkedList<T> mergee) {
//		System.out.printf("%s%s =", this.toString(), mergee.toString());
		Node<T> scanX = head, scanY = mergee.head;
		Node<T> prevX = null;
		
		while (scanX != null && scanY != null) {
			int iComp = scanX.value.compareTo(scanY.value);
			if (iComp <= 0) {
				// X is greater than Y
				if (prevX == null) {
					head = scanX;
				} else {
					prevX.next = scanX;
				}
				prevX = scanX;
				Node<T> nextX = scanX.next;
				scanX.next = scanY;
				scanX = nextX;
			} else {
				// Y is greater than X
				if (prevX == null) {
					head = scanY;
				} else {
					prevX.next = scanY;
				}
				prevX = scanY;
				Node<T> nextY = scanY.next;
				scanY.next = scanX;
				scanY = nextY;
			}
		}
		if (scanY != null) {
			if (prevX == null)
				this.head = mergee.head;
			this.tail = mergee.tail; 
		}
//		System.out.printf("%s\n", this.toString());
	}
	
	public void mergeSort() {
		if (head == null || head == tail)
			return;
		
		Node<T> middle = this.findMiddle();
		SinglyLinkedList<T> right = this.splitIntoTwo(middle);
		
		mergeSort();
		right.mergeSort();
		merge(right);		
	}
	
	public static void main(String[] args) {
		SinglyLinkedList<Integer> linkedList = new SinglyLinkedList<Integer>();

		// Test Swizzling
		linkedList.truncate();
		Random rand = new Random();
		for (int i = 0; i < 3; i++)
			linkedList.insertHead(i);
		System.out.println(linkedList);
		linkedList.swizzle();
		System.out.println(linkedList);
		
		// Test Palindrome
		linkedList.truncate();
		linkedList.insertHead(1);
		linkedList.insertHead(2);
		linkedList.insertHead(2);
		linkedList.insertHead(1);
		boolean fIsPalindrome = linkedList.isPalindrome();
		System.out.printf("%s: palindrome = %s\n", linkedList, fIsPalindrome ? "true" : "false");

		// Reverse every k
		linkedList.truncate();
		for (int i = 0; i < 20; i++)
			linkedList.insertHead(i);
		System.out.println(linkedList);
		linkedList.swapEveryOther();
		System.out.println(linkedList);

		// Get Nth
		linkedList.truncate();
		for (int i = 0; i < 20; i++)
			linkedList.insertHead(rand.nextInt(100));
		int n = 5;
		Integer nth = linkedList.getNth(n);
		System.out.printf("%s: %d(th) = %d\n", linkedList, n, nth);

		// Mergesort
		linkedList.truncate();
		//Integer[] list = new Integer[] {61, 74, 46, 36, 20, 65, 54, 38, 22, 89, 11, 26, 69, 34, 38, 30, 13, 55, 36, 73 };
		for (int i = 0; i < 40; i++)
			linkedList.insertHead(rand.nextInt(100));
		System.out.println(linkedList);
		linkedList.mergeSort();
		System.out.println(linkedList);
	}
}
