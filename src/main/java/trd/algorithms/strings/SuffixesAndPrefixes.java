package trd.algorithms.strings;

public class SuffixesAndPrefixes {

	// Longest Common Prefix of two substrings
	public static String LongestCommonPrefix(String s1, String s2) {
		int N = Math.min(s1.length(), s2.length());
		for (int i = 0; i < N; i++)
			if (s1.charAt(i) != s2.charAt(i))
				return s1.substring(0, i);
		return s1.substring(0, N);
	}
	
	// Longest Repeated Substring
	//		(1) Find all possible suffixes (i.e.: abc -> {abc, bc, c}) of the string into an array
	//		(2) Sort them lexicographically
	//		(3) Scan thru the list in O(n) time comparing and remembering the max
	public static String LongestRepeatedSubstring(String s1) {
		
		// Form all possible suffixes (O(length(s1)) space)
		String[] suffixes = new String[s1.length()];
		for (int i = 0; i < s1.length(); i++) {
			suffixes[i] = s1.substring(i, s1.length());
		}
		
		// Sort them lexicographically (O(N) radix sort)
		StringSortingAlgorithms.MostSignificantDigitSort(suffixes);
		
		// Make a pass remembering the location of the substrings
		String largest = "";
		for (int i = 1; i < suffixes.length; i++) {
			String lcp = LongestCommonPrefix(suffixes[i], suffixes[i - 1]);
			if (lcp != null && lcp.length() > largest.length())
				largest = lcp;;
		}
		return largest;
	}
	
	public static class SuffixArray {
		private final String[] 	suffixes; 	// suffix array
		private final int 		N; 			// length of string (and array)
		public SuffixArray(String s) {
			N = s.length(); suffixes = new String[N];
			for (int i = 0; i < N; i++)
				suffixes[i] = s.substring(i);
			StringSortingAlgorithms.MostSignificantDigitSort(suffixes);
		}
		
		public int length() { 
			return N; 
		}
		public String select(int i) { 
			return suffixes[i]; 
		}
		public int index(int i) { 
			return N - suffixes[i].length(); 
		}
		public int lcp(int i) { 
			return LongestCommonPrefix(suffixes[i], suffixes[i-1]).length(); 
		}
		public int rank(String key) { 
			// binary search
			int lo = 0, hi = N - 1;
			while (lo <= hi) {
				int mid = lo + (hi - lo) / 2;
				int cmp = key.compareTo(suffixes[mid]);
				if (cmp < 0) 
					hi = mid - 1;
				else if (cmp > 0) 
					lo = mid + 1;
				else return mid;
			}
			return lo;
		}
	}
	
	public static void main(String[] args) {
		
		if (true) {
			String s1 = "acctgttaac", s2 = "accgttaa";
			System.out.printf("Longest Common Prefix of [%s] and [%s] is [%s]\n", s1, s2, LongestCommonPrefix(s1, s2));
		}

		if (true) {
			String s1 = "aacaagtttacaagc";
			System.out.printf("Largest Repeated Substring of [%s] is [%s]\n", s1, LongestRepeatedSubstring(s1));
		}
		
		if (true) {
			String text = 	"it was the best of times it was the worst of times " +
							"it was the age of wisdom it was the age of foolishness " +
							"it was the epoch of belief it was the epoch of incredulity " +
							"it was the season of light it was the season of darkness " +
							"it was the spring of hope it was the winter of despair ";
			int context = 10;
			int N = text.length();
			SuffixArray sa = new SuffixArray(text);
			
			String q = "it was the";
			for (int i = sa.rank(q); i < N && sa.select(i).startsWith(q); i++) {
				int from = Math.max(0, sa.index(i) - context);
				int to = Math.min(N-1, from + q.length() + 2 * context);
				System.out.printf("%s\n", text.substring(from, to));
			}
		}
	}
}
