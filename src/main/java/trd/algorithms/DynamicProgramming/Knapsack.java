package trd.algorithms.DynamicProgramming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;

import trd.algorithms.DynamicProgramming.DynamicProgramming.LongestPalindromicSubstring;
import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.CompositeKey;
import trd.algorithms.utilities.Swapper;

public class Knapsack {
	//-----------------------------------------------------------------------------------------------------
	// Knapsack: Memoized Formulation
	public static class KnapsackConfig {
		Integer[] 	Values;
		Integer[] 	Weights;
		HashMap<CompositeKey<Integer,Integer>, KnapsackResults> MemoTable = new HashMap<>();
		public KnapsackConfig(Integer[] Values, Integer[] Weights) {
			this.Values = Values; this.Weights = Weights;
		}
	}
	public static class KnapsackResults {
		List<Integer> 	Elements;
		Integer			CurrentValue;
		Integer			CurrentCapacity;
		public KnapsackResults() {
			Elements = new ArrayList<Integer>();
			CurrentValue = CurrentCapacity = 0;
		}
	}
	private static KnapsackResults KnapsackMemoized(KnapsackConfig kc, Integer capacity, int idx) {
		
		// Short circuit by looking up the memo-table
		KnapsackResults ret = kc.MemoTable.get(new CompositeKey<>(idx,capacity));
		if (ret != null)
			return ret;
		
		ret = new KnapsackResults();
		kc.MemoTable.put(new CompositeKey<>(idx,capacity), ret);
		
		// Base of recursion
		if (idx == 0) {
			if (capacity >= kc.Weights[idx]) {
				ret.Elements.add(idx); ret.CurrentCapacity = kc.Weights[idx]; ret.CurrentValue = kc.Values[idx];
				return ret;
			} else 
				return ret;
		} else if (idx < 0) {
			return ret;
		} else {
			// Inductive Step
			KnapsackResults ret1 = KnapsackMemoized(kc, capacity, idx - 1);
			KnapsackResults ret2 = KnapsackMemoized(kc, capacity - kc.Weights[idx], idx - 1);
			if ((ret1.CurrentValue > (ret2.CurrentValue + kc.Values[idx])) || ((ret2.CurrentCapacity + kc.Weights[idx]) > capacity))
				return ret1;
			ret2.CurrentValue    += kc.Values[idx]; 
			ret2.CurrentCapacity += kc.Weights[idx]; 
			ret2.Elements.add(idx);
			return ret2;
		}
	}
	public static void KnapsackMemoized(Integer[] values, Integer[] weights, int capacity) {
		KnapsackConfig  kc = new KnapsackConfig(values, weights);
		KnapsackResults kr = KnapsackMemoized(kc, capacity, values.length - 1);
        System.out.printf("Knapsack of %s,%s with capacity %d is: (%d)%s\n", 
        		ArrayPrint.ArrayToString("Values", values), 
        		ArrayPrint.ArrayToString("Weights", weights), capacity, kr.CurrentValue, kr.Elements); 
	}
	//-----------------------------------------------------------------------------------------------------
	// Knapsack: Bottom-up Formulation
	private static List<Integer> TraceBack(Integer[][] C, Integer[] values) {
		int rows = C.length, cols = C[0].length;
		int maxVal = C[rows - 1][cols - 1];
		int col = cols - 1, row = 1;
		List<Integer> ret = new ArrayList<Integer>(); 
		
		// Start with the top-right
		for ( ; col >=0 && row < rows; ) {
			// Find first row that gave us maxVal
			if (C[row][col] == maxVal) {
				
				// Add the item to our collection
				ret.add(row - 1);
				
				// What is the maxVal by removing this item (row)?
				maxVal = maxVal - values[row - 1];
				if (maxVal <= 0) 
					break;
				
				// Move left from bottom right till you get a column that
				// has this value of maxVal
				while (C[rows - 1][col] > maxVal) 
					col--;
				
				// Reset the start to the top of this sub-matrix
				row = 1;
			} else {
				row++;
			}
		}
		return ret;
	}	
	public static void KnapsackBottomUp(Integer[] values, Integer[] weights, int capacity) {

		// O(nW) space where n = number of items and W = capacity
		Integer[][] C = new Integer[values.length + 1][capacity + 1];
		
		// Initialization.
		for (int j = 0; j < C[0].length; j++)
			C[0][j] = 0;
		
		// Fill the array
		for (int i = 1; i < values.length + 1; i++) {
			for (int j = 0; j < capacity + 1; j++) {
				// We will compute the max(A[i-1,j], A[i-1][j-W[i-1]]
				// j-W[i-1] might be less than 0. To avoid this, we will set that to 0 if less than 0 
				int colWithReducedCapacity = j - weights[i - 1] < 0 ? 0 : j - weights[i - 1];
				int res1 = C[i - 1][j];
				int res2 = weights[i - 1] <= j ?	// Can only do this, if the item fits 
								(C[i - 1][colWithReducedCapacity] + values[i - 1]) : 0;
				C[i][j] = Math.max(res1, res2);
			}
		}
		
		System.out.printf("%s\n", ArrayPrint.MatrixToString(C, values.length + 1, capacity + 1));		
		// Trace back from the right.
		List<Integer> traceBack = TraceBack(C, values);
		
		System.out.printf("Knapsack of %s,%s with capacity %d is: (%d)%s\n", 
				ArrayPrint.ArrayToString("Values", values), 
				ArrayPrint.ArrayToString("Weights", weights), capacity, 
        		traceBack.stream().mapToInt(i -> values[i]).sum(), 
        		traceBack); 
	}
    
	public static void main(String[] args) {
		
		if (true) {
			KnapsackMemoized(new Integer[] {3, 2, 4, 4}, new Integer[] {4, 3, 2, 3}, 6);
			KnapsackBottomUp(new Integer[] {3, 2, 4, 4}, new Integer[] {4, 3, 2, 3}, 6);
		}
	}
}
