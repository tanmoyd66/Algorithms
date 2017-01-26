package trd.algorithms.binarytree;

import java.security.InvalidParameterException;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

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
	public void LevelOrderZigZag() {
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
	// Trick is that you will have to pass the parent node
	public boolean isBST() {
		return root == null ? true : isBST(root, null);
	}
	private boolean isBST(Node<T> node, T parentVal) {
		return (((node.left == null ? true : isBST(node.left, node.value)) &&
			     (parentVal == null ? true : node.value.compareTo(parentVal) <= 0) &&
			     (node.left == null ? true : isBST(node.left, node.value))));
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

		BinaryTree<Integer> tree3a = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
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
		System.out.printf("LevelOrderZigZag:"); tree3a.LevelOrderZigZag();
		System.out.println("----------------------------------------------------");
		System.out.printf("ReverseOrder:    "); tree3a.SBTReverse(null, (Node<Integer> n)-> { System.out.printf("%s ", n.value); return true;  }, null); System.out.println();
		System.out.println("----------------------------------------------------");

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

		if (true) {
			BinaryTree<Integer> tree4a, tree4b;
			tree4a = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });
			System.out.printf("Tree: %s", tree4a);
			tree4a.Mirror(tree4a.root);
			String thatTree = tree4a.toString();
			System.out.printf("Mirrored: %s ", tree4a);
			tree4a = new BinaryTree<Integer>("[[[[2]3[4]]8[9]]11[[13]17[[19]23]]]", (String s)-> { return Integer.parseInt(s); });			
			tree4b = new BinaryTree<Integer>(thatTree, (String s)-> { return Integer.parseInt(s); });
			System.out.printf("Are %s mirrors ", tree4a.IsMirror(tree4b) ? "true" :"false");
		}
	}
}
