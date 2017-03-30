package trd.algorithms.Arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;

public class Permutations<T extends Comparable<T>> {
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> List<T> getListFromArray(T[] A) {
		List<T> list = new ArrayList();
		for (T a: A)
			list.add(a);
		return list;
	}
	
	public static <T extends Comparable<T>> void getPermutations(T[] Entries, 
								int start, List<List<T>> perms, ISwapper<T> swapper) {
		if (start == Entries.length - 1) {
			perms.add(getListFromArray(Entries));
		} else {
			for (int i = start; i < Entries.length; i++) {
				swapper.swap(Entries, start, i);
				getPermutations(Entries, start + 1, perms, swapper);
				swapper.swap(Entries, i, start);
			}
		}
	}
	
	public static void main(String[] args) {
		Permutations<Integer> perm = new Permutations<>();
		List<List<Integer>> perms = new ArrayList<List<Integer>>();
		getPermutations(new Integer[] {1, 2, 3, 4, 5, 6}, 0, perms, new Swapper.SwapperImpl<Integer>());
		System.out.printf("Got %d permutations:\n", perms.size());
		for (List<Integer> l : perms)
			System.out.printf("%s\n", l);
	}
}
