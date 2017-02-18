package trd.algorithms.datastructures;

import java.util.function.BiFunction;

import trd.algorithms.datastructures.KDTreeNode.LevelType;
import trd.algorithms.datastructures.KDTreeNode.Point;
import trd.algorithms.sorting.Sorting;
import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;

public class KDTree<T extends Comparable<T>> {
	BiFunction<Point<T>, Point<T>, Integer> compareX = (a,b) -> a.X.compareTo(b.X);  
	BiFunction<Point<T>, Point<T>, Integer> compareY = (a,b) -> a.Y.compareTo(b.Y);
	ISwapper<Point<T>> swapperX = new Swapper.SwapperImpl<Point<T>>(compareX);
	ISwapper<Point<T>> swapperY = new Swapper.SwapperImpl<Point<T>>(compareY);

	KDTreeNode<T> root = null;
	
	public String toString() {
		return root.toString(); 
	}
	
	public KDTree() { ; }
	
	public LevelType NextLevelType(LevelType lt) {
		return lt == LevelType.X ? LevelType.Y : LevelType.X;
	}
	public ISwapper<Point<T>> GetSwapperForLevel(LevelType lt) {
		return lt == LevelType.X ? swapperX : swapperY;
	}
	
	public KDTreeNode<T> BuildTree(Point<T>[] points, int start, int end, LevelType lt) {
		
		KDTreeNode<T> thisNode = null;
		if (points.length == 0)
			return null;
		else if (start == end) {
			thisNode = new KDTreeNode<T>(points[start], lt);
		} else {
			ISwapper<Point<T>> swapper = GetSwapperForLevel(lt);
			int midElement 	= (end + start) >> 1;
			int medianIdx 	= (end - start) / 2 + 1;
			
			// Find Median
			Point<T> median = Sorting.KthLargest_DPP(points, medianIdx, start, end, swapper);
			thisNode = new KDTreeNode<T>(median, lt);
			
			// Build Sub-trees Recursively
			if (midElement > start) {
				thisNode.left  = BuildTree(points, start, midElement - 1, NextLevelType(lt));
			}
			if (midElement + 1 <= end) {
				thisNode.right = BuildTree(points, midElement + 1, end, NextLevelType(lt));
			}
		}
		return thisNode; 
	}
	
	public void BulkImport(Point<T>[] points) {
		root = BuildTree(points, 0, points.length - 1, LevelType.X);
	}
	
	// inserts point p into the KD-tree
	public KDTreeNode<T> Insert(Point<T> p) {
		if (root == null) {
			root = new KDTreeNode<T>(p, KDTreeNode.LevelType.X);
			return root;
		} else {
			KDTreeNode<T> curr = root;
			while (curr != null) {
				int cmp = curr.lt == LevelType.X ? p.X.compareTo(curr.point.X) : p.Y.compareTo(curr.point.Y);
				if (cmp == 0)
					return curr;
				else if (cmp < 0) {
					if (curr.left == null) {
						curr.left = new KDTreeNode<T>(p, curr.lt == LevelType.X ? 
														LevelType.Y : LevelType.X);
						return curr.left;
					} else {
						curr = curr.left;
					}
				} else {
					if (curr.right == null) {
						curr.right = new KDTreeNode<T>(p, curr.lt == LevelType.X ?
														LevelType.Y : LevelType.X);
						return curr.right;
					} else {
						curr = curr.right;
					}
				}
			}
			return null;
		}
	}
	
	// Searches for point p in the KD-tree
	public KDTreeNode<T> Search(Point<T> p) {
		if (root == null)
			return null;
		else {
			KDTreeNode<T> curr = root;
			while (curr != null) {
				int cmp = curr.lt == LevelType.X ? 
						p.X.compareTo(curr.point.X) : p.Y.compareTo(curr.point.Y);
				if (cmp == 0)
					return curr;
				else if (cmp < 0) {
					curr = curr.left;
				} else {
					curr = curr.right;
				}
			}
		}
		return null;
	}

	Point<T> getMax(Point<T> p1, Point<T> p2, LevelType lt) {
		if (p1 == null)
			return p2;
		else if (p2 == null)
			return p1;
		else {
			int cmp = GetSwapperForLevel(lt).compare(p1, p2);
			return cmp > 0 ? p2 : p1;
		}
	}
	
	// FindMin in a cutting dimension - O(Sqrt(n))
	public Point<T> FindMin(KDTreeNode<T> node, LevelType lt) {
		if (node == null)
			return null;
		else if (node.lt == lt) {
			// if cutting dimension matches node's dimension then go left
			if (node.left == null)
				return node.point;
			else 
				return FindMin(node.left, lt);
		} else {
			// We have to go both left and right and compare with self
			Point<T> leftMin  = FindMin(node.left,  lt);
			Point<T> rightMin = FindMin(node.right, lt);
			return getMax(
						getMax(node.point, leftMin, lt),
						rightMin, lt);
		}
	}
	
	
	// Finds the nearest neighbor to ref
	public Point<T> FindNearest(KDTreeNode<T> node, Point<T> ref, Point<T> curr,
						BiFunction<Point<T>, Point<T>, Double> distancer,
						BiFunction<T, T, Double> differencer) {
		// Basis
		if (node == null)
			return curr;

		Double radiusOfSearch = distancer.apply(ref, curr);
		Double distFromThis   = distancer.apply(ref, node.point);

System.out.printf("Visiting Node: %s (%s) Reference: %s, Radius:%s\n", node.point, distFromThis, curr, radiusOfSearch);

		
		// Leaf Node case
		if (node.left == null && node.right == null) {
			return distFromThis < radiusOfSearch ? node.point : curr;	
		}

		// Each recursive call will reduce the search width
		T refVal  = node.lt == LevelType.X ? ref.X : ref.Y;
		T nodeVal = node.lt == LevelType.X ? node.point.X : node.point.Y;

		// Search order is important in pruning, find which one to search first
		boolean searchLeftFirst = true;
		if (refVal.compareTo(nodeVal) > 0) {
			searchLeftFirst = false;
		}
		
		// Calculate the signed distance between this node and ref point 
		Double diff_Ref_Node = differencer.apply(refVal, nodeVal);

		// We cannot prune by the dimension, but we can prune by distance
		if (searchLeftFirst) {
			
			// We will search left first and then right
			// The search space will shrink after the first search
			if (diff_Ref_Node < radiusOfSearch)
				curr = FindNearest(node.left, ref, curr, distancer, differencer);

			if (diff_Ref_Node > - radiusOfSearch)
				curr = FindNearest(node.right, ref, curr, distancer, differencer);
			
			Double distFromCurr   = distancer.apply(ref, curr);
			curr = distFromCurr < distFromThis ? curr : node.point;
		} else {
			
			// We will search right first and then left
			// The search space will shrink after the first search
			if (diff_Ref_Node > - radiusOfSearch)
				curr = FindNearest(node.right, ref, curr, distancer, differencer);
			
			if (diff_Ref_Node < radiusOfSearch)
				curr = FindNearest(node.left, ref, curr, distancer, differencer);
			Double distFromCurr   = distancer.apply(ref, curr);
			curr = distFromCurr < distFromThis ? curr : node.point;
		}
		return curr;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) {
		BiFunction<Point<Integer>, Point<Integer>, Double> euDistance = 
				(Point<Integer> p1, Point<Integer> p2) -> {
					return Math.sqrt(Math.pow(p1.X - p2.X, 2) + Math.pow(p1.Y - p2.Y, 2)); 
				};
		KDTree<Integer> kdTree1 = new KDTree<Integer>();
		KDTree<Integer> kdTree2 = new KDTree<Integer>();

		Point<Integer>[] points = new Point[] { 
				new Point<>(35, 90), new Point<>(70, 80), new Point<>(10, 75), new Point<>(80, 40), 
				new Point<>(50, 90), new Point<>(70, 30), new Point<>(90, 60), new Point<>(50, 25), 
				new Point<>(25, 10), new Point<>(20, 50), new Point<>(60, 10) };
//							new Point<>(30, 40), new Point<>(5, 25),  new Point<>(10, 12), 
//							new Point<>(70, 70), new Point<>(50, 30), new Point<>(35, 45) };
		for (Point<Integer> p : points)
			kdTree1.Insert(p);
		kdTree2.BulkImport(points);;

		System.out.printf("Tree 1: %s\n", kdTree1);
		System.out.printf("Tree 2: %s\n", kdTree2);

		// Find Min. They should be the same
		if (true) {
			Point<Integer> min1X = kdTree1.FindMin(kdTree1.root, LevelType.X);
			Point<Integer> min2X = kdTree2.FindMin(kdTree2.root, LevelType.X);
			System.out.printf("Tree 1: %s, Tree 2: %s\n", min1X, min2X);
		}
		if (true) {
			Point<Integer> min1Y = kdTree1.FindMin(kdTree1.root, LevelType.Y);
			Point<Integer> min2Y = kdTree2.FindMin(kdTree2.root, LevelType.Y);
			System.out.printf("Tree 1: %s, Tree 2: %s\n", min1Y, min2Y);
		}
		
		if (true) {
			Point<Integer> searched1 = kdTree1.Search(new Point<>(50, 90)).point;
			Point<Integer> searched2 = kdTree2.Search(new Point<>(50, 90)).point;
		}

		if (true) {
			Point<Integer> ref  = new Point<>(50, 50);
			Point<Integer> curr = new Point<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
			BiFunction<Integer, Integer, Double> diff = (a, b) -> (double)(a - b); 
			Point<Integer> nn = kdTree1.FindNearest(kdTree1.root, ref, curr, euDistance, diff);
			System.out.printf("Point nearest to %s is: %s\n", ref, nn);
		}
	}
}
