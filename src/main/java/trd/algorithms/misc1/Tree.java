package trd.algorithms.misc1;

import java.util.Stack;

public class Tree {
	public static class Node {
		int value;
		Node left, right;
		public Node(int value, Node left, Node right) {
			this.value = value;
			this.left = left;
			this.right = right;
		}
	}
	public Tree(Node root) {
		this.root = root;
	}
	private Node root;
	
	public void Periphery() {
		Stack<Node> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node curr = stack.pop();
			
		}
	}
	
	public static void main(String[] args) {
		
	}
}
