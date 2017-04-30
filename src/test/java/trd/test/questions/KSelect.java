package trd.test.questions;

public class KSelect {
	public static void Swap(Integer[] A, int i, int j) {
		Integer temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}

	public static int Partition(Integer[] A, int start, int end) {
		if (start == end)
			return start;
		if (end - start == 1) {
			if (A[start] > A[end])
				Swap(A, start, end);
			return start;
		} else {
			int i = start + 1, j = end;
			while (i <= j) {
				for (;A[i] < A[start] && i <= j; i++);
				for (;A[j] > A[start] && i <= j; j--);
				if (i < j)
					Swap(A, i, j);
			}
			Swap(A, start, j);
			return j;
		}
	}
	
	public static Integer DoSelect(Integer[] A, int start, int end, int k) {
		if (start == end)
			return A[start];
		int p = Partition(A, start, end);
		if (k == p)
			return A[p];
		else if (k < p) {
			return DoSelect(A, start, p - 1, k);
		} else {
			return DoSelect(A, p + 1, end, k - p);
		}
	}
	public static void main(String[] args) {
		Integer[] A = new Integer[] {10, 3, 4, 2, 1, 11, 15, 17, 6, 5};
		Integer kth = DoSelect(A, 0, A.length - 1, 4);
		System.out.printf("%d\n", kth);
	}
}
