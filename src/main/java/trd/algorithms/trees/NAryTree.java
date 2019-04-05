package trd.algorithms.trees;

import java.util.ArrayList;
import java.util.List;

public class NAryTree<T> {
	public static class Node<T> {
		T value;
		List<Node<T>> children;
		public Node(T value, List<Node<T>> children) {
			this.value = value;
			this.children = children;
		}
		public Node(T value) {
			this.value = value;
			this.children = new ArrayList<>();
		}
	}
	
	Node<T> root;
	public NAryTree(Node<T> root) {
		this.root = root;
	}
	
	public String ser() {
		StringBuilder sb = new StringBuilder();
		ser(root, sb);
		return sb.toString();
	}
	private void ser(Node<T> node, StringBuilder sb) {
		if (node == null)
			return;
		sb.append(node.value.toString());
		sb.append(" ");
		for (Node<T> child : node.children) {
			
		}
		sb.append("$");
	}
	
}
