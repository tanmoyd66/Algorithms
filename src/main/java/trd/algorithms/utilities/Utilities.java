package trd.algorithms.utilities;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Utilities {
	
	public static Long LongFromTwoIntegers(int i1, int i2) {
		Long ret = (long) i1;
		return ret << 32 | i2;
	}

	public static Tuples.Pair<Integer, Integer> TwoIntegersFromLong(Long l) {
		Tuples.Pair<Integer, Integer> ret = new Tuples.Pair<Integer, Integer>(0,0);
		ret.elem2 = (int)(long)(l & 0xFFFFFFFF);
		ret.elem1 = (int)(long)(l >> 32);
		return ret;
	}
	
	public static char[] CharacterCollectionToCharArray(Set<Character> input) {
		char[] 	ret = new char[input.size()];
		int 	i = 0;
		for (Character c : input)
			ret[i++] = c;
		return ret;
	}

}
