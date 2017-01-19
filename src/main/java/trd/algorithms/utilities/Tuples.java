package trd.algorithms.utilities;

import java.util.ArrayList;
import java.util.List;

public class Tuples {
	public static class Pair<S,T> {
		public S elem1;
		public T elem2;
		public S elem1() {
			return elem1;
		}
		public T elem2() {
			return elem2;
		}
		public Pair(S s, T t) {
			elem1 = s; elem2 = t;
		}
	}

	public static class Triple<S,T,U> {
		public S e1;
		public T e2;
		public U e3;
		public Triple(S e1, T e2, U e3) {
			this.e1 = e1; this.e2 = e2; this.e3 = e3;
		}
	}
	
	public static char[] CharacterArrayToCharArray(Character[] input) {
		char[] 	ret = new char[input.length];
		int 	i = 0;
		for (Character c : input)
			ret[i++] = c;
		return ret;
	}
	public static Character[] CharArrayToCharacterArray(char[] input) {
		Character[] 	ret = new Character[input.length];
		int 	i = 0;
		for (Character c : input)
			ret[i++] = c;
		return ret;
	}
	public static char[] CharacterListToCharArray(List<Character> input) {
		char[] 	ret = new char[input.size()];
		int 	i = 0;
		for (Character c : input)
			ret[i++] = c;
		return ret;
	}

	public static <T> List<T> GetClone(List<T> input) {
		List<T> clone = new ArrayList<T>();
		for (T t : input)
			clone.add(t);
		return clone;
	}
	
}
