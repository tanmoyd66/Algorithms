package trd.algorithms.datastructures;

import java.util.function.BiFunction;

import trd.algorithms.utilities.ArrayPrint;

@SuppressWarnings("rawtypes")
public class SegmentTree<T extends Comparable<T>> {
	T[]	 						A;
	Comparable[]				ST;
	BiFunction<T,T,Integer>		func;
	
	public int SmallestPowerGreaterThan(int n) {
		int twoRaisedToPow = 2, pow = 1;
		while (twoRaisedToPow < n) {
			pow++; twoRaisedToPow *= 2;
		}
		return pow;
	}
	
	@SuppressWarnings("unchecked")
	private T getElementVal(int pos) {
		// Simulate that A is the right side of ST
		if (pos >= ST.length) {
			int posInA = pos - ST.length;
			if (posInA >= A.length)
				return null;
			else
				return A[posInA];
		} else
			return (T)ST[pos];
	}

	public void BuildSegments(int pos, BiFunction<T,T,Integer> biFunc) {
		int idxL = (2 * pos) + 1, idxR  = (2 * pos) + 2;
		T   valL = getElementVal(idxL);
		T   valR = getElementVal(idxR);
		
		ST[pos] = valL == null && valR == null ? null :
				  valL == null && valR != null ? valR :
				  valL != null && valR == null ? valL :
				  biFunc.apply(valL, valR) <= 0 ? valL : valR;		
	}

	public SegmentTree(T[] A, BiFunction<T,T,Integer> func) {
		this.A = A;
		this.func = func;
		
		int sp = SmallestPowerGreaterThan(A.length);
		int nonLeafEnd = (int)Math.pow(2, sp) - 2;
		
		this.ST = new Comparable[nonLeafEnd + 1];
		
		// Initialize to a large value
		for (int i = 0; i < ST.length; i++)
			ST[i] = Integer.MAX_VALUE;
		
		// Now heapify the internal nodes
		for (int i = nonLeafEnd; i >= 0; i--) {
			BuildSegments(i, func);
		}		
	}

	
	private T Query(int i, int j, int lo, int hi, int pos) {
		if (i <= lo && j >= hi) {
			// Full overlap
			return getElementVal(pos);
		} else if (i > hi || j < lo) {
			// No overlap
			return null;
		} else {
			// Partial Overlap. 
			int mid = (lo + hi)/2;
			T val1 = Query(i, j, lo, mid, 2 * pos + 1);
			T val2 = Query(i, j, mid + 1, hi, 2 * pos + 2);
			
			if (val1 == null && val2 == null)
				return null;
			else if (val1 == null || val2 == null) {
				return val1 == null ? val2 : val1;
			} else {
				return func.apply(val1, val2) <= 0 ? val1 : val2;
			}
		}
	} 
	public T Query(int i, int j) {
		return Query(i, j, 0, ST.length - 1, 0);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ArrayPrint.ArrayToString("Data", A));
		sb.append(ArrayPrint.ArrayToString("Tree", ST));
		return sb.toString();
	}
	public static void main(String[] args) {
		if (true) {
			Integer[] A = new Integer[] {0, 3, 4, 2, 1, 6, -1};
			SegmentTree<Integer> stMin = new SegmentTree<>(A, (a,b) -> a.compareTo(b));
			SegmentTree<Integer> stMax = new SegmentTree<>(A, (a,b) -> b.compareTo(a));
			System.out.printf("Min:%s\nMax:%s\n", stMin, stMax);
			System.out.printf("Between %2d, %2d Min:%3d, Max:%3d\n", 1, 4, stMin.Query(1, 4), stMax.Query(1, 4));
			System.out.printf("Between %2d, %2d Min:%3d, Max:%3d\n", 0, 7, stMin.Query(0, 7), stMax.Query(0, 7));
			System.out.printf("Between %2d, %2d Min:%3d, Max:%3d\n", 1, 2, stMin.Query(1, 2), stMax.Query(1, 2));
		}
	}
}
