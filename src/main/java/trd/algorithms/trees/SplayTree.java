package trd.algorithms.trees;

import java.util.Stack;

public class SplayTree<T extends Comparable<T>> extends BinarySearchTree<T> {

	public enum SplayMode { None, Zig, Zag, ZigZig, ZagZag, ZigZag, ZagZig };
	
	public SplayTree(trd.algorithms.trees.BinaryTree.Node<T> root) {
		super(root);
	}

	private SplayMode GetSplayMode(SearchPathNode<T> spn, SearchPathNode<T> spp, SearchPathNode<T> spg) {
		if (spg == null) {
			if (spn.orientation == Orientation.Left)
				return SplayMode.Zig;
			else
				return SplayMode.Zag;
		} else {
			if (spp.orientation == Orientation.Left) {
				if (spn.orientation == Orientation.Left)
					return SplayMode.ZigZig;
				else 
					return SplayMode.ZigZag;
			} else {
				if (spn.orientation == Orientation.Left)
					return SplayMode.ZagZig;
				else 
					return SplayMode.ZagZag;
			}
		}
	}
	
	public Node<T> Zig(SearchPathNode<T> spn, SearchPathNode<T> spp) {
		spp.node.left  = spn.node.right;
		spn.node.right = spp.node;
		return spn.node;
	}	
	public Node<T> Zag(SearchPathNode<T> spn, SearchPathNode<T> spp) {
		spp.node.right = spn.node.left;
		spn.node.left  = spp.node;
		return spn.node;
	}

	public Node<T> ZigZig(SearchPathNode<T> spn, SearchPathNode<T> spp, SearchPathNode<T> spg) {
		spp.node.left  = spn.node.right;
		spg.node.left  = spp.node.right;
		spp.node.right = spg.node;
		spn.node.right = spp.node;
		return spn.node;
	}
	public Node<T> ZagZag(SearchPathNode<T> spn, SearchPathNode<T> spp, SearchPathNode<T> spg) {
		spg.node.right = spp.node.left;
		spp.node.right = spn.node.left;
		spp.node.left  = spg.node;
		spn.node.left  = spp.node;
		return spn.node;
	}

	public Node<T> ZigZag(SearchPathNode<T> spn, SearchPathNode<T> spp, SearchPathNode<T> spg) {
		spp.node.right = spn.node.left;
		spg.node.left  = spn.node.right;
		spn.node.left  = spp.node;
		spn.node.right = spg.node;
		return spn.node;
	}
	public Node<T> ZagZig(SearchPathNode<T> spn, SearchPathNode<T> spp, SearchPathNode<T> spg) {
		spg.node.right = spn.node.left;
		spp.node.left  = spn.node.right;
		spn.node.right = spp.node;
		spn.node.left  = spg.node;
		return spn.node;
	}
	
	public Node<T> Splay(Stack<SearchPathNode<T>> searchPath) {
		SearchPathNode<T> spn = null, spp = null, spg = null;
		while (!searchPath.isEmpty()) {
			spn = searchPath.pop();
			if (!searchPath.isEmpty()) {
				spp = searchPath.pop();
				if (!searchPath.isEmpty()) {
					spg = searchPath.pop();
				}
			}

			// Find the Splay Mode and call the appropriate Splay
			SplayMode splayMode = GetSplayMode(spn, spp, spg);
			switch(splayMode) {
			case Zig: 		Zig(spn, spp); 			break;
			case Zag: 		Zag(spn, spp); 			break;
			case ZigZig: 	ZigZig(spn, spp, spg); 	break;
			case ZagZag: 	ZagZag(spn, spp, spg); 	break;
			case ZigZag: 	ZigZag(spn, spp, spg); 	break;
			case ZagZig: 	ZagZig(spn, spp, spg); 	break;
			default:								break;
			}
			
			if (spg != null && !searchPath.isEmpty()) {
				searchPath.push(new SearchPathNode<T>(spn.node, spg.orientation));
				spn = spp = spg = null;
			}
		}
		root = spn.node;
		return spn.node;
	}
	
	@Override
	public Node<T> search(T key) {
		Stack<SearchPathNode<T>> searchPath = getSearchPath(key);
		if (searchPath.size() == 0 || searchPath.peek().node.value.compareTo(key) != 0)
			return null;
		
		return Splay(searchPath);
	}

	public static void main(String[] args) {
		try {
			// Insert 
			SplayTree<Integer> bst = new SplayTree<>(null);
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
			bst = new SplayTree<>(null);
			for (int i = 0; i < keysToInsert.length; i++) 
				bst.insert(keysToInsert[i]);
			System.out.printf("%s is a %s bst\n", bst, bst.isBST() ? "true" : "false");
			
			// Search for a key
			if (true) {
				Node<Integer> node;
				Integer key = 20;
				node = bst.search(key);
				System.out.printf("[After Search of %4s] %s is a %s bst\n", key, bst, bst.isBST() ? "true" : "false");
				key = 30;
				node = bst.search(key);
				System.out.printf("[After Search of %4s] %s is a %s bst\n", key, bst, bst.isBST() ? "true" : "false");
				key = 110;
				node = bst.search(key);
				System.out.printf("[After Search of %4s] %s is a %s bst\n", key, bst, bst.isBST() ? "true" : "false");
			}
			
			// Find the successor
			Integer succOf = 145, succ = bst.nextLargest(succOf);
			System.out.printf("Successor of %d is %d\n", succOf, succ);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
