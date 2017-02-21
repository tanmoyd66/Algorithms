package trd.algorithms.DynamicProgramming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LongestChunkedPalindrome {

	private static List<String> Find(String input) {
		List<String> ret = new ArrayList<String>();
		if (input.length() < 2)
			ret.add(input);
		else {
			boolean fFound = false;
			for (int i = 1; !fFound && i <= input.length() / 2; i++) {
				String begin = input.substring(0, i);
				String end 	 = input.substring(input.length() - i, input.length());
				if (begin.equals(end)) {
					String sub = input.substring(i, input.length() - i);
					List<String> subRet = Find(sub);
					if (subRet.size() > 0) {
						ret.add(begin);
						ret.addAll(subRet);
						fFound = true;
					}
				}
			}
			if (!fFound)
				ret.add(input);
		}
		return ret;
	}

	public static String Print(List<String> chunks) {
		StringBuilder sb = new StringBuilder();
		for (String c : chunks) {
			if (c.length() > 0) {
				sb.append("("); sb.append(c);sb.append(")");
			}
		}
		Collections.reverse(chunks);
		for (String c : chunks) {
			if (c.length() > 0) {
				sb.append("("); sb.append(c);sb.append(")");
			}
		}
		return sb.toString();
	}
	
	public static void main(String args[]) {
		String[] inputs = new String[] { "volvo", "ghiabcdefhelloadamhelloabcdefghi", "foobarfoo"};
		for (String input : inputs) {
			List<String> ret = Find(input);
			System.out.printf("%s\n", Print(ret));
		}
	}
}