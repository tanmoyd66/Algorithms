package trd.algorithms.Arrays;


public class RecurringSequence {
	public static String findRecurringSuffix(String s) {
		for (int i = 0; i < s.length(); i++) {
			int fe = s.length(), fs = fe - 1 - i;
			int se = fs, ss = se - 1 - i;
			if (ss >= 0) {
				String end = s.substring(fs, fe);
				String pen = s.substring(ss, se);
				if (!end.equals("0") && !pen.equals("0") && end.equals(pen))
					return pen;
			}
		}
		return "";
			
	}
	
	public static String getRecurringSequence(int num, int denom) {
		String after = "";
		int before = num > denom ? num/denom : 0;
		int rem    = num % denom;

		while (rem != 0 && after.length() < 10) {
			num = rem * 10;
			int quot = num / denom;
			rem = num % denom;
			after += quot;
			String suff = findRecurringSuffix(after);
			if (!suff.isEmpty()) {
				int prefixStart = 0, prefixEnd = after.length() - 2 * suff.length();
				String prefix = prefixEnd > prefixStart ? after.substring(0, prefixEnd) : "";
				after = prefix + "(" + suff + ")";
				break;
			}
		}
		return before + "." + after;
	}
	
	public static void main(String[] args) {
		String ret = getRecurringSequence(50, 22);
		System.out.println(ret);
	}
}
