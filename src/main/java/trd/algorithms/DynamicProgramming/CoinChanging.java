package trd.algorithms.DynamicProgramming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import trd.algorithms.utilities.ArrayPrint;

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
		return ret;
	}
	
	public static long CountWays_BottomUpDP(int N, int denomAndLower) {
		Integer[] table = new Integer[N + 1];

		// Initialize all table values as 0
		Arrays.fill(table, 0);

		// Base case (If given value is 0)
		table[0] = 1;
		

		// Pick one coin at a time
		for (int i = Denoms.length - 1; i >= 0; i--) {
			// Fill table[] using that coin
			for (int j = Denoms[i]; j <= N; j++)
				table[j] += table[j - Denoms[i]];
		}
		return table[N];
	}

	// What is the minimal number of coins to get N
	// The recursive formula is:
	//		MC(N) = min(MC(N-Coin[i]) for all i)
	HashMap<Integer, int[]> memo = new HashMap<Integer, int[]>();
	public static void Copy(int[] From, int[] To) {
		for (int i = 0; i < From.length; i++)
			To[i] = From[i];
	}
	
	public int[] MinimalCoins(int N) {		
		int[] CoinsUsed = memo.get(N);
		if (CoinsUsed != null)		
			return CoinsUsed;
		
		CoinsUsed = new int[Denoms.length]; 
		if (N > 0) {
			int minCount = Integer.MAX_VALUE;
			for (int i = 0; i < Denoms.length; i++) {
				if (Denoms[i] <= N) {
					int[] countForSubProblem = MinimalCoins(N - Denoms[i]);
					int totalCount = 1 + Arrays.stream(countForSubProblem).sum();
					if (totalCount < minCount) {
						minCount = totalCount;						
						Copy(countForSubProblem, CoinsUsed);
						CoinsUsed[i] += 1;
					}
				}
			}
		}
		memo.put(N, CoinsUsed);
		return CoinsUsed;
	}
	
	public static void main(String[] args) {
		for (int i = 51; i < 100; i++) {
			System.out.printf("Number of ways to make: %3d is %3d/%3d\n", i, CountWays_Recursive(i, 0), CountWays_BottomUpDP(i, 0));
		}
		
		for (int i = 56; i < 57; i++) {
			System.out.printf("Minimal Number of coins to make: %3d is %s\n", i, 
						ArrayPrint.ArrayToString("", new CoinChanging().MinimalCoins(i)));
		}
	}
}
