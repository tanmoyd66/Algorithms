package trd.algorithms.branchandbound;

import java.util.Set;
import java.util.TreeSet;

public class Parenthesize {
	public static Set<String> DifferentWays(int left, int right, String str) {
		Set<String> ret = new TreeSet<String>();
		if (left == 0 && right == 0) {
			ret.add(str);
		} else {
			if (left > 0) {
				Set<String> lRet = DifferentWays(left - 1, right + 1, str + "(");
				ret.addAll(lRet);
			}
			if (right > 0) {
				Set<String> rRet = DifferentWays(left, right - 1, str + ")");
				ret.addAll(rRet);
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		for (int i = 1; i < 10; i++) {
			Set<String> ret = DifferentWays(i, 0, "");
			System.out.printf("Parenthesization using %2d (%4d) : %s\n", i, ret.size(), ret);
		}
	}
}
