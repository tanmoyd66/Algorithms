package trd.algorithms.DynamicProgramming;

import java.util.HashMap;

// Given a boolean expression containing:
//		alphabet : 0, 1
//		operators: XOR(^), OR(|), AND(&)
// How many ways can we parenthesize it to be true
public class BooleanExpressionTrue {
	HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	
	public int CountWays(String str, boolean target) {
		if (str.isEmpty())
			return 0;
		
		String  countMapKey = str + ":" + (target ? "1" : "0");
		Integer countMapVal = countMap.get(countMapKey);
		if (countMapVal != null)
			return countMapVal;
		
		int count = 0;
		if (str.length() == 1) {
			if (target == true)
				count = str.equals("1") ? 1 : 0;
			else 
				count = str.equals("0") ? 1 : 0;
		} else {
			for (int i = 0; i < str.length(); i++) {
				char thisChar = str.charAt(i);
				if (thisChar == '0' || thisChar == '1')
					continue;
				else if (thisChar == '|') {
					int lT  = CountWays(str.substring(0, i), true);
					int lF  = CountWays(str.substring(0, i), false);
					int rT  = CountWays(str.substring(i + 1), true);
					int rF  = CountWays(str.substring(i + 1), false);
					count += ((lT * rF) + (lF * rT) + (lT * rT));
				}
				else if (thisChar == '&') {
					int lT  = CountWays(str.substring(0, i), true);
					int rT  = CountWays(str.substring(i + 1), true);
					count += (lT * rT);
				} else {
					int lT  = CountWays(str.substring(0, i), true);
					int lF  = CountWays(str.substring(0, i), false);
					int rT  = CountWays(str.substring(i + 1), true);
					int rF  = CountWays(str.substring(i + 1), false);
					count += ((lT * rF) + (lF * rT));
				}
			}
		}
		countMap.put(countMapKey, count);
		return count;
	}
	public static void main (String[] args) {
		if (true) {
			BooleanExpressionTrue bet = new BooleanExpressionTrue();
			String[] bes = new String[] {"0", "1", "0|1&1" };
			
			for (String be : bes)
				System.out.printf("Number of ways to make [%16s] true: %d\n", be, bet.CountWays(be, true));
		}
	}
}
