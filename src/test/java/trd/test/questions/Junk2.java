package trd.test.questions;

public class Junk2 {
	public static int powerOf2LogN(int n) {
		System.out.printf("Pow(%d)\n", n);
		if (n == 0)
			return 1;
		else if (n == 1)
			return 2;
		else {
			int m = powerOf2LogN(n/2);
			int k = powerOf2LogN(n%2);
			return m * m * k;
		}
	}
	
	public static void main(String[] args) {
		int k = 20;
		System.out.printf("%d:%d\n", k, powerOf2LogN(k));
	}
 }
