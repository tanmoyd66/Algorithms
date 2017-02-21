package trd.algorithms.sorting;

public class MedianOfTwoSortedArrays {

	// Each Array is sorted. Find the median of each of them
	public static Double FindMedian(Integer[] A, Integer[] B, int s_a, int e_a, int s_b, int e_b) {
		
		//		As m1 Ae
		//		Bs m2 Be
		if (e_a - s_a > 1 && e_b - s_b > 1) {
			// Recursive Step
			Integer m_a = (s_a + e_a) / 2;
			Integer m_b = (s_b + e_b) / 2;
			
	 		if (A[m_a] < B[m_b]) {
				// In this scenario: 
				// Since m1 < m2, in the virtually sorted array we will have:
				//		{As, some elements of Bs} m1 {some of Bs, some of Ae} m2 {some of Ae, Be}
				//      Thus we can remove As and Be from the search
				return FindMedian(A, B, m_a, e_a, s_b, m_b);
			} else if (A[m_a] > B[m_b]) {
				// In this scenario: 
				// Since m2 < m1, in the virtually sorted array we will have:
				//		{Bs, some elements of As} m2 {some of As, some of Be} m1 {Ae, some of Be}
				//      Thus we can remove Bs and Ae from the search
				return FindMedian(A, B, s_a, m_a, m_b, e_b);
			} else {
				// In this scenario:
				//		{As, Bs} m1 m2 {Be, Ae}
				return ((double)A[m_a] + (double)B[m_b])/2;
			}
		} else {
			// Base Case
			return (Math.max((double)A[s_a], (double)A[s_b]) + 
					Math.min((double)A[e_a], (double)A[e_b]))/2;
		}
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] { 1, 3, 5, 7,  9};
		Integer[] B = new Integer[] { 2, 4, 6, 8, 10};
		double median = FindMedian(A, B, 0, A.length - 1, 0, B.length - 1);
		System.out.printf("Median: %f\n", median);
	}
}
