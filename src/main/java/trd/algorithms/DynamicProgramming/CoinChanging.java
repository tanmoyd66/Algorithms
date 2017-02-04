package trd.algorithms.DynamicProgramming;

public class CoinChanging {
	
	public static Integer[] Denoms = new Integer[] { 25, 10, 5, 1 };
	public static int CountWays_Recursive(int N, int denomAndLower) {
		int ret = 0;
		if (N == 0) {
			return 0;
		} else if (denomAndLower == Denoms.length - 1) {
			ret = N % Denoms[denomAndLower] == 0 ? 1 : 0;
		} else {
			int n = N; int count = 0;
			count += N % Denoms[denomAndLower] == 0 ? 1 : 0;
			while (n > 0) {
				count += CountWays_Recursive(n, denomAndLower + 1);
				n -= Denoms[denomAndLower];
			}
			ret = count;
		}
//		System.out.printf("N = %2d, Denom = %2d ", N, Denoms[denomAndLower]);
//		System.out.printf("Count = %2d\n", ret);
		return ret;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 51; i++) {
			System.out.printf("Number of ways to make: %3d is %3d\n", i, CountWays_Recursive(i, 0));
		}
	}
}
