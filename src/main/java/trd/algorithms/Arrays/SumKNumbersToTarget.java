package trd.algorithms.Arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import trd.algorithms.utilities.ArrayPrint;

public class SumKNumbersToTarget {
	public static List<List<Integer>> ExactK(Integer[] A, Integer k, Integer target) {
		List<Integer> Base = Arrays.asList(A);
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
		List<List<Integer>> combinations = CombinationGenerator.GenerateAllCombinations(Base, (int)k);
		for (List<Integer> combi : combinations) {
			int sum = 0;
			for (int i = 0; i < combi.size(); i++)
				sum += combi.get(i);
			if (sum == target)
				ret.add(combi);
		}
		return ret;
	}

	public static List<List<Integer>> UpToK(Integer[] A, Integer k, Integer target) {
		List<Integer> Base = Arrays.asList(A);
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
		List<List<Integer>> combinations = CombinationGenerator.GenerateAllCombinations(Base, -1);
		for (List<Integer> combi : combinations) {
			if (combi.size() <= k) {
				int sum = 0;
				for (int i = 0; i < combi.size(); i++)
					sum += combi.get(i);
				if (sum == target)
					ret.add(combi);
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] {1, 2, 3, 4, 5, 6, 7};
		System.out.printf("Subset Sums of %s is Exact:%s, UpTo:%s\n", ArrayPrint.ArrayToString("", A),
				SumKNumbersToTarget.ExactK(A, 3, 9), SumKNumbersToTarget.UpToK(A, 3, 9));
	}
}
