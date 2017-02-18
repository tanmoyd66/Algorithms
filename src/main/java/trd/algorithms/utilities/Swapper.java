package trd.algorithms.utilities;

import java.util.function.BiFunction;

public class Swapper<T extends Comparable<T>> {

	public static interface ISwapper<T extends Comparable<T>> {
		void swap(T[] target, int i, int j);
		boolean less(T v, T w);
		int compare(T v, T w);
		int getSwapCount();
		int getCompareCount();
	}
	
	public static class SwapperImpl<T extends Comparable<T>> implements ISwapper<T> {
		public int SwapCount = 0;
		public int CompareCount = 0;
		public BiFunction<T, T, Integer> comparer = null;
	
		public SwapperImpl(BiFunction<T, T, Integer> comparer) {
			this.comparer = comparer;
		}

		public SwapperImpl() {
			this.comparer = null;
		}
		
		public void swap(T[] A, int i, int j) {
			T temp; temp = A[i]; A[i] = A[j]; A[j] = temp;
			++SwapCount;
		}
		
	    // is v < w ?
		public boolean less(T v, T w) {
			CompareCount++;
			return compare(v, w) < 0;
	    }

		// is v < w ?
		public int compare(T v, T w) {
			CompareCount++;
			if (comparer != null)
				return comparer.apply(v, w);
			return v.compareTo(w);
	    }

		public int getSwapCount() {
			return SwapCount;
		}

		@Override
		public int getCompareCount() {
			return CompareCount;
		}
	}
}
