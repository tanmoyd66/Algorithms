package trd.test.questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LargestCommonSubsequence {
	public static List<Integer> Clone(List<Integer> l, Integer hi) {
		List<Integer> ret = new ArrayList<Integer>();
		if (hi != null)
			ret.add(hi);
		for (Integer i : l)
			ret.add(i);
		return ret;
	}
	
	public static List<Integer> Find(List<Integer> l1, int i1, List<Integer> l2, int i2) {
		if (i1 >= l1.size() || i2 >= l2.size())
			return new ArrayList<Integer>();
		
		if (l1.get(i1) == l2.get(i2)) {
			List<Integer> ret = Find(l1, i1 + 1, l2, i2 + 1);
			return Clone(ret, l1.get(i1));
		} else {
			List<Integer> ret1 = Find(l1, i1, l2, i2 + 1);
			List<Integer> ret2 = Find(l1, i1 + 1, l2, i2);
			return ret1.size() > ret2.size() ? ret1 : ret2;
		}
	}

	public static List<Integer> FindContiguous(List<Integer> l1, int i1, List<Integer> l2, int i2, List<Integer> largestSoFar) {
		if (i1 >= l1.size() || i2 >= l2.size())
			return new ArrayList<Integer>();
		
		List<Integer> retP = new ArrayList<Integer>();
		while (i1 < l1.size() && i2 < l2.size() && l1.get(i1++) == l2.get(i2++)) {
			retP.add(l1.get(i1 - 1));
		}
		List<Integer> lsf  = largestSoFar.size() > retP.size() ? largestSoFar : retP;
		List<Integer> retC = FindContiguous(l1, i1, l2, i2, lsf);
		return lsf.size() > retC.size() ? lsf : retC;
	}
	
	public static void main(String[] args) {
		List<Integer> l1  = Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 7});
		List<Integer> l2  = Arrays.asList(new Integer[] {1, 3, 2, 4, 5, 6});
		List<Integer> ret1 = Find(l1, 0, l2, 0);
		List<Integer> ret2 = FindContiguous(l1, 0, l2, 0, new ArrayList<>());
		System.out.printf("%s %s\n", ret1, ret2);
	}
}
