package trd.algorithms.datastructures;

import java.util.function.Function;


public class KDTreeNode<T extends Comparable<T>> implements Comparable<KDTreeNode<T>> {
	
	public static enum LevelType { X, Y };
	public static class Point<T> implements Comparable<Point<T>> {
		T X; T Y; 
		public Point(T X, T Y) {
			this.X = X; this.Y = Y;
		}
		public T distanceFrom(Point<T> p, Function<Point<T>,T> func) {
			return func.apply(p);
		}
		public int compareTo(Point<T> o) {
			return 0;
		}
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(X.toString());
			sb.append(",");
			sb.append(Y.toString());
			sb.append(")");
			return sb.toString();
		}
	}
	
	Point<T>  point;		
	LevelType lt; 
	KDTreeNode<T> left,right;
		
	public KDTreeNode(Point<T> p, LevelType lt) {
		this.point = p; this.lt = lt;
	}
	
	@Override
	public int compareTo(KDTreeNode<T> o) {
		return lt == LevelType.X ? point.X.compareTo(o.point.X) : 
						 point.Y.compareTo(o.point.Y);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (left != null)
			sb.append(left.toString());
		sb.append("{"); sb.append(point.toString()); sb.append(":");
		sb.append(lt == LevelType.X ? "X" : "Y"); sb.append("}");
		if (right != null)
			sb.append(right.toString());
		sb.append("]");
		return sb.toString();
	}
}
