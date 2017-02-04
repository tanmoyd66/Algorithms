package trd.algorithms.DynamicProgramming;

public class LargestCommonSubstring {
	public static String getLCS(String _s1, String _s2) {
		char[] s1 = _s1.toCharArray();
		char[] s2 = _s2.toCharArray();
		
		Integer[][] matches = new Integer[s1.length][s2.length];
		int maxMatchLen = 0, maxMatchRow = 0;
		for (int i = 0; i < s1.length; i++) {
			for (int j = 0; j < s2.length; j++) {
				if (s1[i] == s2[j]) {
					matches[i][j] = (i > 0 && j > 0) ? matches[i-1][j-1] + 1 : 1;	
					if (matches[i][j] > maxMatchLen) {
						maxMatchLen = matches[i][j];
						maxMatchRow = i;
					} 
				} else {
					matches[i][j] = 0;
				}
			}
		}
		
		if (maxMatchLen > 0) {
			char[] lcs = new char[maxMatchLen];
			while (--maxMatchLen >= 0) {
				lcs[maxMatchLen] = s1[maxMatchRow];
				maxMatchRow--;
			}
			String s = new String(lcs);
			return s;
		}
		return "";
	}
	public static void main(String[] args) {
		String s1 = "tanmoy", s2 = "sanmiy", s3 = LargestCommonSubstring.getLCS(s1, s2);
		System.out.printf("LCS of [%s] and [%s] is: [%s]\n", s1, s2, s3);
	}
}
