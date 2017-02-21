package trd.algorithms.utilities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import trd.algorithms.utilities.Swapper.SwapperImpl;

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
	
	public static Character[] StringToCharacterArray(String s) {
		Character[] ret = new Character[s.length()];
		for (int i = 0; i < s.length(); i++)
			ret[i] = s.charAt(i);
		return ret;
	}

	public static void Verbose(boolean fPrint, String format, Object...args) {
		if (fPrint)
			System.out.printf(format, args);
	}
	
	public static <T> void RemoveListElementsFromSet(Set<T> set, Collection<T> list) {
		for (T elem : list )
			set.remove(elem);
	}

	// Reverse an array 
	public static <S extends Comparable<S>> void rev(S[] A, int start, int end) {
		SwapperImpl<S> swapper = new SwapperImpl<S>();
		for (int i = start, j = end; i < j; i++, j--) {
			swapper.swap(A, i, j);
		}
	}
	
	public static <T> LinkedList<T> CloneLinkedList(List<T> listT) {
		LinkedList<T> ret = new LinkedList<T>();
		for (T t : listT)
			ret.add(t);
		return ret;
	}
}
