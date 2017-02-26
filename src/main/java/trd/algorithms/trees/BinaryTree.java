package trd.algorithms.trees;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Tuples;

public class BinaryTree<T extends Comparable<T>> {
	public static class Node<T extends Comparable<T>> {
		Node<T>	left;
		Node<T> right;
		T		value;
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			if (left != null)
				sb.append(left.toString());
			sb.append(value.toString());
			if (right != null)
				sb.append(right.toString());
			sb.append("]");
			return sb.toString();
		}
		
		public Node(Node<T> left, T value, Node<T> right) {
			this.left = left; this.value = value; this.right = right;
		}
		
		private Node() {
		}
	}
	
	protected Node<T> root;
	public String toString() {
		if (root != null)
			return root.toString();
		return "";
	}
	
	public BinaryTree(Node<T> root) {
		this.root = root;
	}
	
	// Build from serialized format
	public BinaryTree(String source, Function<String, T> convertToT) throws InvalidParameterException {
		char[] sourceAsChars = source.toCharArray();
		Stack<Tuples.Pair<Boolean, Node<T>>> nodeStack = new Stack<Tuples.Pair<Boolean, Node<T>>>();
		int i = 0;
		while (i < sourceAsChars.length) {
			switch(sourceAsChars[i]) {
			case '[': {
					Node<T> aNode = new Node<T>();
					if (!nodeStack.isEmpty()) {
						Tuples.Pair<Boolean, Node<T>> stackTop = nodeStack.peek();
						if (stackTop.elem1() == false) {
							stackTop.elem2.left = aNode;
						} else {
							stackTop.elem2.right = aNode;
						}
					}
					nodeStack.push(new Tuples.Pair<Boolean, Node<T>>(false, aNode));
					i++;
				}
				break;
			case ']': {
					Tuples.Pair<Boolean, Node<T>> stackTop = nodeStack.pop();
					root = stackTop.elem2;
					i++;
				}
				break;
			default: {
					StringBuilder sb = new StringBuilder();
					while (i < sourceAsChars.length && (sourceAsChars[i] != '[' && sourceAsChars[i] != ']')) {
						sb.append(sourceAsChars[i]);
						i++;
					}
					String digitString = new String(sb.toString());
					T val = convertToT.apply(digitString);
					Tuples.Pair<Boolean, Node<T>> stackTop = nodeStack.peek();
					stackTop.elem2.value = val;
					stackTop.elem1 = true;
				}
			}
		}
	}

	// Build from InOrder and PreOrder traversal
	private Node<T> BuildTreeInPre(T[] InOrder, T[] PreOrder, 
								int inStart,  int inEnd, int preStart,
								Map<T, Integer> idxMap) {
		if (inStart > inEnd)
			return null;
		else if (inStart == inEnd)
			return new Node<T>(null, InOrder[inStart], null);
		else {
			// Find the root for this segment
			T thisRoot = PreOrder[preStart];
			Integer thisRootIdx = idxMap.get(thisRoot);
			
			Node<T> left  = BuildTreeInPre(InOrder, PreOrder, inStart, thisRootIdx - 1, preStart + 1,  idxMap);
			Node<T> right = BuildTreeInPre(InOrder, PreOrder, thisRootIdx + 1, inEnd, thisRootIdx + 1, idxMap);
			return new Node<T>(left, thisRoot, right);
		}
	}
	public void BuildTreeInPre(T[] InOrder, T[] PreOrder) {
		
		// To avoid multiple scans, build a hash table for indexes of In-order entries
		Map<T, Integer> idxMap = new HashMap<T, Integer>();
		for (int i = 0; i < InOrder.length; i++)
			idxMap.put(InOrder[i], i);		
		
		// Build Recursively
		root = BuildTreeInPre(InOrder, PreOrder, 0, InOrder.length - 1, 0, idxMap);
	}

	// Build from InOrder and PostOrder traversal
	private Node<T> BuildTreeInPost(T[] InOrder, T[] PostOrder, 
								int inStart,  int inEnd, int postEnd,
								Map<T, Integer> idxMap) {
		if (inStart > inEnd)
			return null;
		else if (inStart == inEnd)
			return new Node<T>(null, InOrder[inStart], null);
		else {
			// Find the root for this segment
			T thisRoot = PostOrder[postEnd];
			Integer thisRootIdx = idxMap.get(thisRoot);
			
			Node<T> left  = BuildTreeInPost(InOrder, PostOrder, inStart, thisRootIdx - 1, thisRootIdx - 1,  idxMap);
			Node<T> right = BuildTreeInPost(InOrder, PostOrder, thisRootIdx + 1, inEnd, postEnd - 1, idxMap);
			return new Node<T>(left, thisRoot, right);
		}
	}
	public void BuildTreeInPost(T[] InOrder, T[] PostOrder) {
		
		// To avoid multiple scans, build a hash table for indexes of In-order entries
		Map<T, Integer> idxMap = new HashMap<T, Integer>();
		for (int i = 0; i < InOrder.length; i++)
			idxMap.put(InOrder[i], i);		
		
		// Build Recursively
		root = BuildTreeInPost(InOrder, PostOrder, 0, InOrder.length - 1, PostOrder.length - 1, idxMap);
	}
	
	// Traversals
	public void InOrder() {
		InOrder(root); System.out.println();
	}
	private void InOrder(Node<T> node) {
		if (node.left != null) InOrder(node.left);
		System.out.printf("%s ", node.value);
		if (node.right != null) InOrder(node.right);
	}

	public void PreOrder() {
		PreOrder(root); System.out.println();
	}
	private void PreOrder(Node<T> node) {
		System.out.printf("%s ", node.value);
		if (node.left != null) PreOrder(node.left);
		if (node.right != null) PreOrder(node.right);
	}

	public void PostOrder() {
		PostOrder(root); System.out.println();
	}
	private void PostOrder(Node<T> node) {
		if (node.left != null) PostOrder(node.left);
		if (node.right != null) PostOrder(node.right);
		System.out.printf("%s ", node.value);
	}

	// Level Order
	public void LevelOrder() {
		Queue<Node<T>> nodeQ = new ConcurrentLinkedQueue<Node<T>>();
		nodeQ.add(root);
		while (!nodeQ.isEmpty()) {
			Node<T> node = nodeQ.poll();
			System.out.printf("%s ", node.value);
			if (node.left != null)
				nodeQ.add(node.left);
			if (node.right != null)
				nodeQ.add(node.right);
		}
		System.out.println();
	}

	// Level Order ZigZag
	public void LevelOrderZigZag_Tuples() {
		Queue<Tuples.Pair<Integer, Node<T>>> nodeQ = new ConcurrentLinkedQueue<Tuples.Pair<Integer, Node<T>>>();
		Stack<Node<T>> stack = new Stack<Node<T>>();
		
		// Breadth first search with the root as the first node
		nodeQ.add(new Tuples.Pair<Integer, Node<T>>(1, root));
		int lastLevel = 1;
		while (!nodeQ.isEmpty()) {
			
			// Pop the queue
			Tuples.Pair<Integer, Node<T>> qElement = nodeQ.poll();
			int level = qElement.elem1; Node<T> node = qElement.elem2();
			
			// If we see a change in level, we are increasing the frontier.
			if (lastLevel != level && !stack.empty()) {
				
				// If the last level was even, pop the contents of the stack
				while (!stack.isEmpty()) {
					System.out.printf("%s ", stack.pop().value);
				}
			}
		
			// For even levels, we just do not print one at a time.
			if (level % 2 == 0)
				stack.push(node);
			else 
				System.out.printf("%s ", node.value);
			lastLevel = level;
			
			// The bread-first logic remains the same
			if (node.left != null)
				nodeQ.add(new Tuples.Pair<Integer, Node<T>>(level + 1, node.left));
			if (node.right != null)
				nodeQ.add(new Tuples.Pair<Integer, Node<T>>(level + 1, node.right));
		}
		
		// In case the last level was an even level, flush the last level
		while (!stack.empty()) {
			System.out.printf("%s ", stack.pop().value);
		}
		System.out.println();
	}
	
	// Level Order ZigZag
	public void LevelOrderZigZag_JustNodes() {
		Queue<Node<T>> nodeQ = new ConcurrentLinkedQueue<Node<T>>();
		Stack<Node<T>> stack = new Stack<Node<T>>();
		
		// Breadth first search with the root as the first node
		int level = 0;
		nodeQ.add(root);
		while (!nodeQ.isEmpty()) {
		
			// size will be the size of the frontier
			// loop over all nodes in the frontier
			int size = nodeQ.size();
			for (int i = 0; i < size; i++) {

				// Pop the queue and print if necessary
				Node<T> node = nodeQ.poll();
				if (level % 2 == 1) {
					stack.push(node);
				} else {
					System.out.printf("%s ", node.value);
				}
				
				// Grow the frontier
				if (node.left != null)
					nodeQ.add(node.left);
				if (node.right != null)
					nodeQ.add(node.right);
			}
			while (!stack.isEmpty())
				System.out.printf("%s ", stack.pop().value);
			level++;
		}
		
		// In case the last level was an even level, flush the last level
		while (!stack.empty()) {
			System.out.printf("%s ", stack.pop().value);
		}
		System.out.println();
	}
	
	// Maximal Path Sum
	public T MaximalPathSum(Node<T> node, T maxValue, BiFunction<T, T, T> summer) {
		T ret = node.value;
		if (node.left != null) {			
			ret = summer.apply(ret, MaximalPathSum(node.left, maxValue, summer));
		}
		if (node.right != null) {			
			ret = summer.apply(ret, MaximalPathSum(node.right, maxValue, summer));
		}
		if (maxValue.compareTo(ret) > 0)
			return maxValue;
		return ret;
	}
	// DFS traversal of tree
	public enum Color { white, gray, black };
	public static class DFSNode<T extends Comparable<T>, C> {
		Color			color;
		DFSNode<T,C>	parent;
		int				start;
		int				end;
		Node<T> 		node;
		C				aux;
		
		public DFSNode(Node<T> node, int start, DFSNode<T, C> parent) {
			this.node = node; this.start = start; this.parent = parent;
			color = Color.white; end = 0;  
		}
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("(%s:[%d,%d])", node.value, start, end));
			return sb.toString();
		}
	}	
	public <C> void DepthFirstSearch(Consumer<Node<T>> discover, Consumer<Node<T>> explore, Consumer<Node<T>> complete) {
		DFSVisit(new DFSNode<T, C>(root, 0, null), 0);
	}
	private <C> void DFSVisit(DFSNode<T, C> dfsNode, int time) {
		
		// White Node has just been discovered
		time++;
		
		// set the color stating that we have started exploring
		dfsNode.color = Color.gray;
		dfsNode.start = ++time;

		// explore children recursively
		Node<T> treeNode = dfsNode.node;
		if (treeNode.left != null) {
			DFSNode<T,C> leftDFSNode = new DFSNode<T,C>(treeNode.left, time, dfsNode);
			leftDFSNode.parent = dfsNode;
			DFSVisit(leftDFSNode, time);
		}
		if (treeNode.right != null) {
			DFSNode<T,C> rightDFSNode = new DFSNode<T,C>(treeNode.right, time, dfsNode);
			rightDFSNode.parent = dfsNode;
			DFSVisit(rightDFSNode, time);
		}
		
		// set complete
		dfsNode.color = Color.black;
		dfsNode.end = ++time;
	}
	
	// Stack based traversal of Binary Tree
	public <C> void SBT(Function<Node<T>, Boolean> discover, Function<Node<T>, Boolean> explore, Function<Node<T>, Boolean> complete) {
		
		// Strategy here is to track what the state of the element in the stack is:
		// (1) White means: parent pushed and we haven't seen the left child
		// (2) Gray  means: we have seen the left child
		// (3) Black means: we have seen both left and right child
		int 				time = 0;
		Stack<DFSNode<T,C>> context = new Stack<DFSNode<T,C>>();
		context.push(new DFSNode<T,C>(root, time, null));
		
		while (!context.isEmpty()) {
			
			// increment time
			++time;
			DFSNode<T,C> curr = context.peek();

			// Get the exploration state of the current node
			Color 	col = curr.color;				
			Node<T> currNode = curr.node;
			if (col == Color.white) {
				if (discover != null)
					if (discover.apply(currNode) == false)
						return;
				curr.color = Color.gray;
				if (currNode.left != null)
					context.push(new DFSNode<T,C>(currNode.left, time, curr));
			} else if (col == Color.gray) {
				if (explore != null)
					if (explore.apply(currNode) == false)
						return;
				curr.color = Color.black;
				if (currNode.right != null)
					context.push(new DFSNode<T,C>(currNode.right, time, curr));
			} else if (col == Color.black) {
				if (complete != null)
					if (complete.apply(currNode) == false)
						return;
				context.pop();
			}
		}
	}

	// Stack based traversal of Binary Tree
	public <C> void SBTReverse(Function<Node<T>, Boolean> discover, Function<Node<T>, Boolean> explore, Function<Node<T>, Boolean> complete) {
		
		// Strategy here is to track what the state of the element in the stack is:
		// (1) White means: parent pushed and we haven't seen the right child
		// (2) Gray  means: we have seen the right child
		// (3) Black means: we have seen both right and left child
		int 				time = 0;
		Stack<DFSNode<T,C>> context = new Stack<DFSNode<T,C>>();
		context.push(new DFSNode<T,C>(root, time, null));
		
		while (!context.isEmpty()) {
			
			// increment time
			++time;
			DFSNode<T,C> curr = context.peek();

			// Get the exploration state of the current node
			Color 	col = curr.color;				
			Node<T> currNode = curr.node;
			if (col == Color.white) {
				if (discover != null)
					if (discover.apply(currNode) == false)
						return;
				curr.color = Color.gray;
				if (currNode.right != null)
					context.push(new DFSNode<T,C>(currNode.right, time, curr));
			} else if (col == Color.gray) {
				if (explore != null)
					if (explore.apply(currNode) == false)
						return;
				curr.color = Color.black;
				if (currNode.left != null)
					context.push(new DFSNode<T,C>(currNode.left, time, curr));
			} else if (col == Color.black) {
				if (complete != null)
					if (complete.apply(currNode) == false)
						return;
				context.pop();
			}
		}
	}
	
	// Iterative in-order traversal (Morris Traversal)
	public void MorrisOrder() {
		Node<T>  curr = root;
		
		// The strategy here is:
		//     Popping back the stack is simulated by setting the right-most pointer of the left child to this node 
		while (curr != null) {
			
			// If there is a left child. There are 2 possibilities:
			// (1) We have never seen the left child
			// (2) We have seen the left child and we are popping back
			if (curr.left != null) {
				
				// Follow the rightmost path of the left child.
				// If the left child has been seen before, the right-most pointer will point to curr
				// Otherwise it will be null
				Node<T> pre = curr.left;
				while (pre.right != null && pre.right != curr) {
					pre = pre.right;
				}

				// At this point, pre is the rightmost child of the left child of curr
				if (pre.right != null) {
					// This means the left child of curr was seen before.
					// We need to reset the pointer
					pre.right = null;
					System.out.printf("%d ", curr.value);
					curr = curr.right;
				} else {
					// If that is null, we will set curr as the successor of the left child of curr
					pre.right = curr;
					curr = curr.left;
				}
			} else {
				// curr does not have a left child. Print and move to the right-child. 
				System.out.printf("%d ", curr.value);
				curr = curr.right;
			}
		}
		System.out.println();
	}
	
	// Find Predecessors and Successors
	public void successor(T nodeVal) {
		AtomicBoolean trackOn = new AtomicBoolean(false);
		System.out.printf("Successor:[%d]:", nodeVal); 
		SBT(null, 
			(Node<T> n)-> { 
				if (trackOn.get() == false) {
					if (n.value.compareTo(nodeVal) == 0)
						trackOn.set(true);
				} else {
					System.out.printf("%s ", n.value);
					return false;
				}
				return true; 
			}, null); 
	}

	// Find Predecessors and Successors
	T prev;
	public void predecessor(T nodeVal) {
		prev = root.value;
		System.out.printf("Predecessor:[%d]:", nodeVal); 
		SBT(null, 
			(Node<T> n)-> {
				if (n.value.compareTo(nodeVal) == 0) {
					System.out.printf("%s ", prev);
					return false;
				}
				if (n.value.compareTo(prev) != 0)
					prev = n.value;
				return true;
			}, null); 
	}
	
	// Print path with sum. Keep Auxiliary stack capturing the current path
	private void pathWithSum(Node<T> node, T sumOnPath, T sum, BiFunction<T, T, T> summer, Stack<Node<T>> path) {		
		
		// If you have a node that has no left and right, try check if the sum matches.
		// Remember: node is on the stack at this point. The caller will pop it out.
		if (node.left == null && node.right == null) {
			T finalSum = summer.apply(sumOnPath, node.value);
			if (sum.compareTo(finalSum) == 0) {
				Stack<Node<T>>  inversePath = new Stack<Node<T>>();
				System.out.printf(" [");
				while (!path.empty()) {
					Node<T> thisNode = path.pop();
					System.out.printf("%s ", thisNode.value);
					inversePath.push(thisNode);
				}
				System.out.printf("] ");
				while (!inversePath.isEmpty()) {
					Node<T> thisNode = inversePath.pop();
					path.push(thisNode);
				}
			}
		}

		// Traverse down the tree adjusting the top of the stack
		if (node.left != null) {
			path.push(node.left);
			pathWithSum(node.left, summer.apply(sumOnPath, node.value), sum, summer, path);
			path.pop();
		} 
		if (node.right != null) {
			path.push(node.right);
			pathWithSum(node.right, summer.apply(sumOnPath, node.value), sum, summer, path);
			path.pop();
		}
	}
	public void pathWithSum(T sum, BiFunction<T, T, T> summer) {
		Stack<Node<T>> path = new Stack<Node<T>>();
		path.push(root);
		pathWithSum(root, null, sum, summer, path);
		path.pop();
		System.out.println();
	}

	// Mirror the binary tree
	public void Mirror(Node<T> node) {
		if (node == null || (node.left == null && node.right == null))
			return;
		Node<T> rightChild = node.right;
		node.right = node.left; node.left = rightChild;
		Mirror(node.left);
		Mirror(node.right);
	}
	
	// Check if one binary tree is a mirror of another
	private boolean IsMirror(Node<T> nodeThis, Node<T> nodeThat) {
		boolean fRetVal = (nodeThis == null) ^ (nodeThat == null);
		if (fRetVal)
			return false;
		else if (nodeThis == null && nodeThat == null)
			return true;
		else {
			return 	nodeThis.value.compareTo(nodeThat.value) == 0 &&
					IsMirror(nodeThis.left,  nodeThat.right) &&
					IsMirror(nodeThis.right, nodeThat.left);
		}
			
	}
	public boolean IsMirror(BinaryTree<T> that) {
		return IsMirror(this.root, that.root);
	}
	
	// Recursive check for IsBST.
	public boolean isBST() {
		return root == null ? true : null != isBST(root);
	}
	private Tuples.Pair<T,T> isBST(Node<T> node) {
		Tuples.Pair<T,T> ret = null; 
		boolean fContinue = true;

		if (node.left == null && node.right == null) {
			ret = new Tuples.Pair<T,T>(node.value, node.value);
		} else {
			Tuples.Pair<T,T> lRet = null, rRet = null;
			if (fContinue && node.left != null) {
				lRet = isBST(node.left);
				fContinue = lRet != null && lRet.elem1.compareTo(node.value) <= 0;
			}
			if (fContinue && node.right != null) {
				rRet = isBST(node.right);
				fContinue = rRet != null && rRet.elem1.compareTo(node.value) >= 0;
			}
			ret = !fContinue ? 	null : 
								new Tuples.Pair<T,T>(lRet != null ? lRet.elem1 : node.value,
													 rRet != null ? rRet.elem2 : node.value);
		}
		return ret;
	}

	// Recursive check for Depth Calculation
	public int MaxDepth(Node<T> node, int distFromRoot) {
		if (node == null)
			return distFromRoot;
		
		int leftHeight  = MaxDepth(node.left,  distFromRoot + 1);
		int rightHeight = MaxDepth(node.right, distFromRoot + 1);

		return leftHeight > rightHeight ? leftHeight : rightHeight;
	}

	// Recursive check for Height Balance
	public boolean IsHeightBalanced() {
		int leftDepth  = MaxDepth(root.left,  0);
		int rightDepth = MaxDepth(root.right, 0);
		return Math.abs(leftDepth - rightDepth) <= 1;
	}
	
	// If one of the nodes is 
	private Node<T> LowestCommonAncestor(Node<T> node, T val1, T val2) {
		if (node.value.compareTo(val1) == 0 || node.value.compareTo(val2) == 0)
			return node;

		// Recurse
		
		// if the left child returns non-null and the non-null is not val1 and val2, the left child is the lca
		Node<T> left = node.left == null ? null : LowestCommonAncestor(node.left, val1, val2);
		if (left != null && left.value.compareTo(val1) != 0 && left.value.compareTo(val2) != 0)
			return left;

		// if the right child returns non-null and the non-null is not val1 and val2, the right child is the lca
		Node<T> right = node.right == null ? null : LowestCommonAncestor(node.right, val1, val2);
		if (right != null && right.value.compareTo(val1) != 0 && right.value.compareTo(val2) != 0)
			return right;
		
		// if both child return non-null, then this guy is the lca
		if (left != null && right != null)
			return node;
		else if (left != null)
			return left;
		else if (right != null)
			return right;
		else
			return null;
	}
	
	public T LowestCommonAncestor(T val1, T val2) {
		Node<T> lcaNode = LowestCommonAncestor(root, val1, val2);
		return lcaNode == null ? null : lcaNode.value;
	}
	
	// Build a doubly linked list out of a binary tree
	// Strategy:
	//		We will do this in one In-Order traversal
	//		Remember the direction from the parent (whether node is left or right of parent) 
	public static enum Direction { Left, Right }
	private Node<T> buildDoublyLinkedList(Node<T> node, Direction dir) {
		Node<T> pred = null, succ = null;

		if (node.left == null && node.right == null) {
			if (root == null)
				root = node;
		} else {
			if (node.left != null) {
				pred = buildDoublyLinkedList(node.left, Direction.Left);
				if (pred != null)
					pred.right = node;
			}
			if (node.right != null) {
				succ = buildDoublyLinkedList(node.right, Direction.Right);
				if (succ != null)
					succ.left = node;
			}
			node.left = pred; node.right = succ;
		}
		return (pred == null && succ == null) ? node:
			(dir == Direction.Left) ? succ : pred;	
	}
	public Node<T> buildDoublyLinkedList() {
		Node<T> oldRoot = root; 
		root = null;
		buildDoublyLinkedList(oldRoot, Direction.Left);
		return root;
	}
	
	private Node<T> buildSinglyLinkedList(Node<T> node, Direction dir) {
		Node<T> pred = null, succ = null;
		
		if (node.left == null && node.right == null) {
			if (root == null)
				root = node;
		} else {
			if (node.left != null) {
				pred = buildSinglyLinkedList(node.left, Direction.Left);
				if (pred != null)
					pred.right = node;
			}
			
			if (node.right != null) {
				succ = buildSinglyLinkedList(node.right, Direction.Right);
				node.right = succ;
			}
		}
		return (pred == null && succ == null) ? node:
			(dir == Direction.Left) ? succ : pred;	
	}
	public void buildSinglyLinkedList() {
		Node<T> oldRoot = root; 
		root = null;
		buildSinglyLinkedList(oldRoot, Direction.Left);
	}

	// Convert a Tree (assuming BST) in-place to a min-heap
	// Strategy:
	//		1. First convert the BST into a Linked List (In-Order)
	//		2. Set the pointers to build the heap 
	public void ConvertToMinHeap() {
		
		// First Build a Singly Linked List
		this.buildSinglyLinkedList();
		
		if (root == null)
			return;
		
		// Then convert the singly linked list into a min-heap using a queue
		Queue<Node<T>> queue = new ConcurrentLinkedQueue<Node<T>>();
		queue.add(root); 
		Node<T> curr = root.right;
		root.left = root.right = null;
		while (!queue.isEmpty() && curr != null) {
			Node<T> top = queue.poll();
			top.left = curr;
			if (top.left != null) {
				queue.add(top.left); 
				curr = curr.right;
				top.left.left = top.left.right = null;
				top.right = curr;
				if (top.right != null) {
					queue.add(top.right);
					curr = curr.right;
					top.right.left = top.right.right = null;
				}
			}
		}
	}
	

	
	// Find the largest Binary Search Tree in a Binary Tree
	public static class BSTInfo<T extends Comparable<T>> {
		Node<T> root; 
		int 	size;
		T		minVal;
		T		maxVal;
		public BSTInfo(Node<T> node, int size, T minVal, T maxVal) {
			this.root = node; this.size = size; this.minVal = minVal; this.maxVal = maxVal;
		}
	}
	
	public BSTInfo<T> LargestBSTInBinaryTree(Node<T> node) {
		if (node.left == null && node.right == null)
			return new BSTInfo<T>(node, 1, node.value, node.value);
		else {
			BSTInfo<T> left = null, right = null;
			if (node.left != null)
				left = LargestBSTInBinaryTree(node.left);
			if (node.right != null)
				right = LargestBSTInBinaryTree(node.right);
			
			if (left != null && right != null) {
				if (left.maxVal.compareTo(node.value) <= 0 && right.minVal.compareTo(node.value) >= 0)
					return new BSTInfo<T>(node, left.size + 1 + right.size, left.minVal, right.maxVal);
				else 
					return left.size < right.size ? right : left;
			} else if (left == null && right == null) {
				return null;
			} else if (left == null && right != null) {
				return right.minVal.compareTo(node.value) >= 0 ?
						new BSTInfo<T>(node, right.size + 1, node.value, right.maxVal) :
						right;
			} else {
				return left.maxVal.compareTo(node.value) <= 0 ?
						new BSTInfo<T>(node, left.size + 1, left.minVal, node.value) :
						left;
			}
		}
	}
	
	// Right Rotate Whole Tree
	public Node<T> RightRotateWholeTree(Node<T> node, Node<T> prev) {
		if (node == null)
			return prev;
		prev = RightRotateWholeTree(node.right, prev);
		prev = RightRotateWholeTree(node.left, prev);
		node.right = prev;
		node.left = null;
		return node;
	}

	// Find Leaf Nodes from a pre-order traversal
	public static <T extends Comparable<T>> void FindLeafNodes(T[] A, int low, int high, List<T> leaves) {

		// Base cases
		if (A.length == 0 || low > high)
			return;
		else if (low == high || low == high - 1) {
			leaves.add(A[high]);
			return;
		} else {
			
			int j = low + 1;
			
			// increasing sequences always result in internal nodes 
			while (j <= high && A[j].compareTo(A[low]) < 0)
				j++;
			
			// recurse
			FindLeafNodes(A, low + 1, j - 1, leaves);
			FindLeafNodes(A, j, high, leaves);
		}
	}

	// Find First Non-matching leaves in 2 pre-order traversals
	public static <T extends Comparable<T>> T FindFirstNonMatchingLeaves(T[] A, T[] B) {
		
		List<T> leavesA = new ArrayList<T>(), leavesB = new ArrayList<T>(); 
		FindLeafNodes(A, 0, A.length - 1, leavesA);
		FindLeafNodes(B, 0, B.length - 1, leavesB);

		// Now find the first non-matching
		int iMax = Math.min(leavesA.size(), leavesB.size());
		int i = 0;
		for (; i < iMax; i++) {
			T leafA = leavesA.get(i);
			T leafB = leavesB.get(i);
			if (leafA.compareTo(leafB) != 0)
				return leafA;
		}
		return i > leavesA.size() ? 
					i > leavesB.size() ? leavesB.get(i) : null : 
					leavesA.get(i);
	}
		
	// Column Order Traversal
	public static class NodeEx<T extends Comparable<T>> extends Node<T> {
		Integer col;
		Integer row;
		Node<T> node;
		public NodeEx(Integer row, Integer col, Node<T> node) {
			this.row = row; this.col = col; this.node = node;
		}
		public String toString() {
			return node.value.toString();
		}
	}
	private void ColumnOrder(Node<T> node, Map<Integer, List<NodeEx<T>>> vertexMap, int row, int col) {
		if (node == null)
			return;
		else {
			// Add nodes to vertex map in pre-order
			List<NodeEx<T>> vMapEntry = vertexMap.get(col);
			if (vMapEntry == null) {
				vMapEntry = new ArrayList<NodeEx<T>>();
				vertexMap.put(col, vMapEntry);
			}
			vMapEntry.add(new NodeEx<>(row, col, node));
			
			// Traverse Children
			ColumnOrder(node.left,  vertexMap, row + 1, col - 1);
			ColumnOrder(node.right, vertexMap, row + 1, col + 1);
		}
	}
	public void ColumnOrderTraversal() {
		Map<Integer, List<NodeEx<T>>> vertexMap = new HashMap<Integer, List<NodeEx<T>>>();
		ColumnOrder(root, vertexMap, 0, 0);
		
		// Create a Matrix so that printing is easy. Then print.
		int row = 0, col = 0, colMin = Integer.MAX_VALUE, colMax = Integer.MIN_VALUE;
		for (Map.Entry<Integer, List<NodeEx<T>>> me : vertexMap.entrySet()) {
			List<NodeEx<T>> values = me.getValue();
			for (NodeEx<T> value : values) {
				row 	= Math.max(row, value.row);
				colMin 	= Math.min(colMin, value.col);
				colMax 	= Math.max(colMax, value.col);
			}
		}
		col = colMax - colMin;
		Object[][] printable = new Object[row + 1][col + 1];
		for (Map.Entry<Integer, List<NodeEx<T>>> me : vertexMap.entrySet()) {
			List<NodeEx<T>> values = me.getValue();
			for (NodeEx<T> value : values) {
				
				// Xlate the columns as they may be negative
				int XlatedRow = value.row, XlatedCol = value.col - colMin;
				printable[XlatedRow][XlatedCol] = value.node.value;
			}
		}
		for (int i = 0; i < row + 1; i++) {
			for (int j = 0; j < col + 1; j++)
				System.out.printf("%4s ", printable[i][j] == null ? "   " : printable[i][j]);
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		BinaryTree<Integer> tree1 = new BinaryTree<Integer>(
										new Node<Integer>(
												new Node<Integer>(
														new Node<Integer>(null, 6, null),
														2,
														null),
												1,
												new Node<Integer>(
													new Node<Integer>(null, 4, null), 
													3,
													new Node<Integer>(null, 5, null))));
		System.out.println(tree1);
		
		BinaryTree<String> tree2 = new BinaryTree<String>("[[[f]b]a[[d]c[e]]]", (String s)-> { return s; });
		System.out.println(tree2);

		BinaryTree<Integer> tree3a = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]18]]]", (String s)-> { return Integer.parseInt(s); });
		System.out.printf("%s: IsBST:%s\n", tree3a, tree3a.isBST() ? "true" : "false");

		System.out.println("----------------------------------------------------");
		System.out.printf("InOrder:         "); tree3a.InOrder();
		System.out.printf("InOrder(sbt):    "); tree3a.SBT(null, (Node<Integer> n)-> { System.out.printf("%s ", n.value); return true; }, null); System.out.println();
		System.out.printf("MorrisOrder:     "); tree3a.MorrisOrder();
		System.out.println("----------------------------------------------------");
		System.out.printf("PreOrder:        "); tree3a.PreOrder();
		System.out.printf("PreOrder(sbt):   "); tree3a.SBT((Node<Integer> n)-> { System.out.printf("%s ", n.value); return true;  }, null, null); System.out.println();
		System.out.println("----------------------------------------------------");
		System.out.printf("PostOrder:       "); tree3a.PostOrder();
		System.out.printf("PostOrder(sbt):  "); tree3a.SBT(null, null, (Node<Integer> n)-> { System.out.printf("%s ", n.value); return true; }); System.out.println();
		System.out.println("----------------------------------------------------");
		System.out.printf("LevelOrder:      "); tree3a.LevelOrder();
		System.out.printf("LevelOrderZigZag:"); tree3a.LevelOrderZigZag_Tuples();
		System.out.printf("LevelOrderZigZag:"); tree3a.LevelOrderZigZag_JustNodes();
		System.out.println("----------------------------------------------------");
		System.out.printf("ReverseOrder:    "); tree3a.SBTReverse(null, (Node<Integer> n)-> { System.out.printf("%s ", n.value); return true;  }, null); System.out.println();
		System.out.println("----------------------------------------------------");

		// Column Order Traversal
		tree3a.ColumnOrderTraversal();
		
		// Find Successor
		int nodeVal = 11; 
		tree3a.successor(nodeVal);
		System.out.println("\n----------------------------------------------------");

		// Find Predecessor
		tree3a.predecessor(nodeVal);
		System.out.println("\n----------------------------------------------------");

		// Path with sum
		int sum = 28;
		System.out.printf("Path With Sum [%d]:", sum); 
		tree3a.pathWithSum(sum, (s1,s2)-> (s1 == null ? 0 : s1) + (s2 == null ? 0 : s2));
		System.out.printf("Maximal Path Sum: %d\n", tree3a.MaximalPathSum(tree3a.root, Integer.MIN_VALUE, (a,b)-> (a + b)));
		
		if (true) {
			BinaryTree<Integer> tree4a, tree4b;
			tree4a = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
			System.out.printf("Tree: %s", tree4a);
			tree4a.Mirror(tree4a.root);
			String thatTree = tree4a.toString();
			System.out.printf("Mirrored: %s ", tree4a);
			tree4a = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });			
			tree4b = new BinaryTree<Integer>(thatTree, (String s)-> { return Integer.parseInt(s); });
			System.out.printf("Are %s mirrors \n", tree4a.IsMirror(tree4b) ? "true" :"false");
			System.out.printf("LCA of %s and %s is %s \n", 8, 4, tree4a.LowestCommonAncestor(8, 4));
		}

		if (true) {
			BinaryTree<Integer> tree5a;
			tree5a = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
			Node<Integer> start = tree5a.buildDoublyLinkedList();
			for (Node<Integer> s = start; s != null; s = s.right) 
				System.out.printf("%s ", s.value);
			System.out.println();
		}

		if (true) {
			BinaryTree<Integer> tree5b;
			tree5b = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
			tree5b.buildSinglyLinkedList();
			for (Node<Integer> s = tree5b.root; s != null; s = s.right) 
				System.out.printf("%s ", s.value);
			System.out.println();
		}

		if (true) {
			BinaryTree<Integer> tree5c;
			tree5c = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
			tree5c.ConvertToMinHeap();
			System.out.printf("Min-Heap: %s\n", tree5c.root);
		}
		if (true) {
			BinaryTree<Integer> tree6;
			tree6 = new BinaryTree<Integer>("[[[19[15]]18[[18]20[25]]]25[[[20[25]]35[40]]50[[55]60[70]]]]", (String s)-> { return Integer.parseInt(s); });
			BSTInfo<Integer> lBST = tree6.LargestBSTInBinaryTree(tree6.root);
			System.out.printf("Largest BST(%d): %s\n", lBST.size, lBST.root);
		}
		if (true) {
			BinaryTree<Integer> tree7;
			tree7 = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
			tree7.RightRotateWholeTree(tree7.root, null);
			System.out.printf("Right Rotated Tree: %s\n", tree7);
		}
		
		if (true) {
			BinaryTree<Integer> tree8;
			tree8 = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
			System.out.printf("Tree: %s if Height Balanced: %s\n", tree8, tree8.IsHeightBalanced());
		}
		
		if (true) {
			BinaryTree<Integer> tree8a = new BinaryTree<>(null);
			BinaryTree<Integer> tree8b = new BinaryTree<>(null);
			String deser = "[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]";
			Integer[] inOrder   = new Integer[] { 2, 3, 4, 8, 9, 11, 13, 17, 19, 23 };
			Integer[] preOrder  = new Integer[] { 11, 8, 3, 2, 4, 9, 17, 13, 23, 19 };
			Integer[] postOrder = new Integer[] { 2, 4, 3, 9, 8, 13, 19, 23, 17, 11 };
			
			tree8a.BuildTreeInPre(inOrder, preOrder);
			tree8b.BuildTreeInPost(inOrder, postOrder);
			String ser = tree8a.toString();
			System.out.printf("%s into %s: %s\n", deser, ser, ser.equals(deser));
			ser = tree8b.toString();
			System.out.printf("%s into %s: %s\n", deser, ser, ser.equals(deser));
		}

		if (true) {
			Integer[] A  = new Integer[] { 11, 8, 3, 2, 4, 9, 17, 13, 23, 19 };
			List<Integer> leavesA = new ArrayList<Integer>();
			FindLeafNodes(A, 0, A.length - 1, leavesA);
		}

		if (true) {
			Integer[] A  = new Integer[] { 5, 4, 2, 4, 8, 6, 9 };
			Integer[] B  = new Integer[] { 5, 3, 2, 4, 8, 7, 9 };
			Integer leaf = FindFirstNonMatchingLeaves(A, B);
			System.out.printf("%s, %s: %s\n", 
						ArrayPrint.ArrayToString("A", A), ArrayPrint.ArrayToString("B", B), leaf);
		}
	}
}
