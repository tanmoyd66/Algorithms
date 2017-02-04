package trd.algorithms.DynamicProgramming;

public class RangeMinimalQuery<T extends Comparable<T>> {
	T[] 		A;
	Integer[][] M;	// M[i,j] is the maximal between i and j 
	
	public RangeMinimalQuery(T[] A) {
		this.A = A;
		M = new Integer[A.length][A.length];
		
		// base of recursion. M[i,i] = i
		for (int i = 0; i < A.length; i++) 
			M[i][i] = i;
		
		// Recursion: M[i,j] = min(A[M[i,j-1]], A[j])
		for (int i = 0; i < A.length; i++) {
			for (int j = i + 1; j < A.length; j++) {
				if (A[M[i][j-1]].compareTo(A[j]) > 0) {
					M[i][j] = M[i][j-1];
				} else {
					M[i][j] = j;
				}
			}
		}
	}

	public T getRangeMinimal(int i, int j) {
		if (i > j)
			return null;
		return A[M[i][j]];
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] { 2, 4, 3, 1, 6, 7, 8, 9, 1, 7 }; 
		RangeMinimalQuery<Integer> rmq = new RangeMinimalQuery<Integer>(A);
		System.out.printf("RMQ[%d,%d]: %s\n", 0, 9, rmq.getRangeMinimal(0, 9));
		System.out.printf("RMQ[%d,%d]: %s\n", 2, 8, rmq.getRangeMinimal(2, 8));
		System.out.printf("RMQ[%d,%d]: %s\n", 2, 4, rmq.getRangeMinimal(2, 4));
		System.out.printf("RMQ[%d,%d]: %s\n", 0, 3, rmq.getRangeMinimal(0, 3));
	}
}
