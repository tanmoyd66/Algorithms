package trd.algorithms.DynamicProgramming;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BreakIntoWords {
	//-----------------------------------------------------------------------------------------------------
    // Given a dictionary of words and a string of characters find a sentence
    // Dictionary: { "i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "icecream", "man", "go", "mango", "and"}
    private static LinkedList<String> BreakIntoWords(Set<String> dictionary, String sentence) {
    	//System.out.println(sentence);
    	LinkedList<String> ret = new LinkedList<String>();
    	int size = sentence.length();
    	boolean fContinue = true;
    	for (int i = 0; fContinue && i < size; i++) {
    		String prefix = sentence.substring(0, i + 1);
    		String suffix = sentence.substring(i + 1, size);
    		if (dictionary.contains(prefix)) {
    			if (suffix.length() > 0) {
	    			List<String> retRec = BreakIntoWords(dictionary, suffix);
	    			if (!retRec.isEmpty()) {
	    				for (String word : retRec)
	    					ret.add(word);
	    				ret.add(prefix);
	    				fContinue = false;
	    			}
    			} else {
    				ret.add(prefix);
    			}
    		}
    	}
    	return ret;
    }
	public static void main(String[] args) {
		
		if (true) {
			String[] dict = new String[] { "i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "icecream", "man", "go", "mango", "and"};
	        String[] s1   = new String[] {"ilikesamsung", "iiiiiiii", "ilikelikeimangoiii", "samsungandmango", "samsungandmangok"};
	        for (String s : s1) {
	        	List<String>  ret = BreakIntoWords(Stream.of(dict).collect(Collectors.toSet()), s);
	        	Collections.reverse(ret);
	        	System.out.printf("Words in %s are: %s\n", s, ret);
	        }
		}
	}
}
