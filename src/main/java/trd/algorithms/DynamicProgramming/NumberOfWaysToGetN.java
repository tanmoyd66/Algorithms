package trd.algorithms.DynamicProgramming;

import java.util.ArrayList;
import java.util.List;

public class NumberOfWaysToGetN {
	public static List<Integer> clone(List<Integer> l) {
		List<Integer> ll = new ArrayList<Integer>();
		for (Integer i : l) 
			ll.add(i);
		return ll;
	}
	
	public static List<List<Integer>> GetCount(int N, int startFrom) {
		List<List<Integer>> ret = new ArrayList<List<Integer>>();		
		if (N == 0) {
		} else if (N == 1) {
			List<Integer> elem = new ArrayList<Integer>();
			elem.add(1);
			ret.add(elem);
		} else { 
			int _startFrom = Math.min(N, startFrom);
			for (int i = _startFrom; i > 0; i--) {
				List<List<Integer>> childRet = GetCount(N - i, i);
				if (childRet.size() > 0) {
					for (List<Integer> elem: childRet) {
						List<Integer> elemClone = clone(elem);
						elemClone.add(i);
						ret.add(elemClone);
					}
				} else {
					List<Integer> l = new ArrayList<Integer>();
					l.add(i);
					ret.add(l);
				}
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		List<List<Integer>> ret = GetCount(6, 6);
		System.out.printf("%s\n", ret);
	}
}
