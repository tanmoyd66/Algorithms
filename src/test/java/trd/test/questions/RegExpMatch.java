package trd.test.questions;

public class RegExpMatch {
	public static boolean Match(String text, int textPos, String pat, int patPos) {
		if (patPos >= pat.length() && textPos >= text.length())
			return true;
		else if (patPos < pat.length() && textPos == text.length())
			return false;
		else { 
			if (pat.charAt(patPos) == '.' || text.charAt(textPos) == pat.charAt(patPos))
				return Match(text, textPos + 1, pat, patPos + 1);
			else if (pat.charAt(patPos) == '*') {
				char patToRepeatMatch = text.charAt(textPos - 1);
				while (text.charAt(textPos++) == patToRepeatMatch);
				return Match(text, textPos, pat, patPos + 1);
			} else {
				return false;
			}
		}
	}
	public static void main(String[] args) {
		String pat = "xa*b.c", text = "xaabyc"; 
		boolean match = Match(text, 0, pat, 0);
		System.out.printf("%s:%s:%s\n", pat, text, match);
	}
}
