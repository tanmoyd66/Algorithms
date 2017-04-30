package trd.test.questions;

public class BitOperations {
	public static int SmallestPowerOfTwoGreaterThan(int n) {
		if (n == 1)
			return 2;
		else if (n != 0 && ((n & (n - 1)) > 0)) {
			while ((n & (n - 1)) > 0) {
				n = n & (n - 1);
			}
			return n << 1;
		} else {
			return n;
		}
	}
	public static int BinaryLogarithm(int n) {
		if (n == 0)
			return 0;
		int ret = SmallestPowerOfTwoGreaterThan(n);
		int count = 0;
		while ((ret & 0x1) == 0) {
			count++;
			ret = ret >> 1;
		}
		return count;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++)
			System.out.printf("%4d: %4d %4d\n", i, SmallestPowerOfTwoGreaterThan(i), BinaryLogarithm(i));
	}
}
