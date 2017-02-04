package trd.algorithms.trees;

import java.util.Stack;

import trd.algorithms.utilities.Tuples;

public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

	public static enum Orientation {Left, Right, Root};
	public static class SearchPathNode<T extends Comparable<T>> {
		Node<T>	 	 node;
		Orientation	 orientation;
		public SearchPathNode(Node<T> node, Orientation orientation) {
			this.node = node;
			this.orientation = orientation;
		}
	}
	
	public BinarySearchTree(Node<T> root) {
		super(root);
	}
	
	public Node<T> search(Node<T> node, T key, boolean fForUpdate) {
		if (root == null)
			return null;
		int cmp = node.value.compareTo(key);
		if (cmp == 0)
			return node;
		else if (cmp > 0) {
			if (node.left != null)
				return search(node.left, key, fForUpdate);
			else
				return fForUpdate ? node : null;
		} else {
			if (node.right != null)
				return search(node.right, key, fForUpdate);
			else
				return fForUpdate ? node : null;
		}
	}
	
	public Node<T> search(T key) {
		Stack<SearchPathNode<T>> searchPath = getSearchPath(key);
		if (searchPath.size() == 0 || searchPath.peek().node.value.compareTo(key) != 0)
			return null;
		return searchPath.peek().node;
	}
	
	public Node<T> insert(T key) {
		Stack<SearchPathNode<T>> searchPath = getSearchPath(key);
		if (searchPath.size() == 0) {
			return root = new Node<>(null, key, null);
		}
		Node<T> inserted;
		int cmp = searchPath.peek().node.value.compareTo(key);
		if (cmp == 0) {
			inserted = searchPath.pop().node;
		} else if (cmp > 0) {
			inserted = new Node<>(null, key, null);
			searchPath.pop().node.left = inserted;
		} else {
			inserted = new Node<>(null, key, null);
			searchPath.pop().node.right = inserted;
		}
		searchPath.empty();
		return inserted;
	}

	Stack<SearchPathNode<T>> getSearchPath(T key) {
		Stack<SearchPathNode<T>> searchPath = new Stack<SearchPathNode<T>>();
		if (root == null)
			return searchPath;
		
		searchPath.push(new SearchPathNode<>(root, Orientation.Root));
		while (true) {
			Node<T> topNode = searchPath.peek().node;
			int cmp = topNode.value.compareTo(key);
			if (cmp == 0) {
				return searchPath;
			} else if (cmp > 0) {
				if (topNode.left != null)
					searchPath.push(new SearchPathNode<>(topNode.left, Orientation.Left));
				else
					return searchPath;
			} else {
				if (topNode.right != null)
					searchPath.push(new SearchPathNode<>(topNode.right, Orientation.Right));
				else
					return searchPath;
			}
		}
	}
	
	public void delete(T key) throws Exception {
		if (root == null)
			return;
		Stack<SearchPathNode<T>> searchPath = getSearchPath(key);
		Node<T> top;
		if (searchPath.isEmpty())
			return;
		top = searchPath.pop().node;
		if (top.value.compareTo(key) != 0)
			return;

		// Case 1: Simple Leaf Node
		if (top.left == null && top.right == null) {
			if (searchPath.isEmpty()) {
				root = null;
				return;
			}
			Node<T> nextToTop = searchPath.pop().node;
			if (nextToTop.left == top) {
				nextToTop.left = null;
				return;
			} else {
				nextToTop.right = null;
				return;
			}
		}
		
		// Case 2: Internal Node
		if (top.right != null) {
			// Case 2a: Left Child is empty, right child is not
			// Case 2c: Both Right and Left children are full 
			//          Find the successor node as the leftmost node in the right subtree
			//          That node now replaces this node
			Node<T> prev = top, next = top.right;
			while (next.left != null) {
				prev = next;
				next = next.left;
			}
			if (prev == top)
				prev.right = null;
			else
				prev.left = null;
			top.value = next.value;
		} else {
			// Case 2b: Right Child is empty, left child is not
			//          Find the predecessor node as the rightmost node in the left subtree
			//          That node replaces this node
			
			// Set the left pointer or right pointer of the parent to record the deletion of this guy
			Node<T> prev = top, next = top.left;
			while (next.right != null) {
				prev = next;
				next = next.right;
			}
			if (prev == top)
				prev.left = null;
			else
				prev.right = null;
			top.value = next.value;
		}
		
		if (!isBST())
			throw new Exception("Bad");
		return;
	}

	// Find the next largest node in the BST
	public T nextLargest(T key) {
		Stack<Tuples.Pair<Color,Node<T>>> sp = new Stack<Tuples.Pair<Color, Node<T>>>();
		sp.push(new Tuples.Pair<>(Color.white, root));
		while (!sp.isEmpty()) {
			Tuples.Pair<Color,Node<T>> curr = sp.peek();
			Node<T>	currNode = curr.elem2; Color currCol = curr.elem1;
			
			if (currCol == Color.gray) {
				return currNode.value;
			}
			
			int	cmp = currNode.value.compareTo(key);
			if (cmp == 0) {
				// We are at a node whose value is equal. Just return the successor.
				if (currNode.right == null)
					return null;
				else {
					Node<T> ret = currNode.right;
					while (ret.left != null)
						ret = ret.left;
					return ret.value;
				}
			} else if (cmp > 0) {
				// Current Node is greater than the key. Go left. If nowhere to go this is our guy.
				if (currNode.left == null)
					return currNode.value;
				else {
					curr.elem1 = Color.gray;
					sp.push(new Tuples.Pair<Color,Node<T>>(Color.white, currNode.left));
				}
			} else {
				// Current Node is less than the key. Go right. If nowhere to go pop.
				if (currNode.right == null)
					sp.pop();
				else {
					curr.elem1 = Color.gray;
					sp.push(new Tuples.Pair<Color,Node<T>>(Color.white, currNode.right));
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			// Insert 
			BinarySearchTree<Integer> bst = new BinarySearchTree<>(null);
			Integer[] keysToInsert = new Integer[] { 110, 80, 30, 20, 40, 90, 170, 130, 230, 190 };
			for (int i = 0; i < keysToInsert.length; i++) 
				bst.insert(keysToInsert[i]);
			System.out.printf("%s is a %s bst\n", bst, bst.isBST() ? "true" : "false");
	
			// Delete
			Integer[] keysToDelete = new Integer[] { 80, 30, 230, 20, 90, 40, 130, 190, 110, 170 };
			for (int i = 0; i < keysToDelete.length; i++) 
				bst.delete(keysToDelete[i]);
			System.out.printf("%s is a %s bst\n", bst, bst.isBST() ? "true" : "false");

			// Insert Again
			bst = new BinarySearchTree<>(null);
			for (int i = 0; i < keysToInsert.length; i++) 
				bst.insert(keysToInsert[i]);
			System.out.printf("%s is a %s bst\n", bst, bst.isBST() ? "true" : "false");
			
			// Find the successor
			Integer succOf = 145, succ = bst.nextLargest(succOf);
			System.out.printf("Successor of %d is %d\n", succOf, succ);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
