package trd.algorithms.DynamicProgramming;

import java.math.BigInteger;
import java.util.HashMap;

public class Fibonacci {
	public HashMap<Integer, BigInteger> memo = new HashMap<Integer, BigInteger>();
	public BigInteger Recursive(int n) {
		BigInteger ret = memo.get(n);
		if (ret != null)
			return ret;
		if (n == 0)
			ret = BigInteger.valueOf(0);
		else if (n == 1)
			ret = BigInteger.valueOf(1);
		else 
			ret = Recursive(n - 1).add(Recursive(n - 2));
		memo.put(n, ret);
		return ret;
	}

	BigInteger[] table;
	public Fibonacci(int max) {
		table = new BigInteger[max];
		table[0] = BigInteger.valueOf(0); table[1] = BigInteger.valueOf(1);
		for (int i = 2; i < max; i++) {
			table[i] = table[i - 1].add(table[i - 2]);
		}
	}
	
	public BigInteger DynammicProgramming(int n) {
		return table[n];
	}
	
	public BigInteger MatrixMultiply(int n) {
		BigInteger[][] F = new BigInteger[][] { 
								{ BigInteger.valueOf(1), BigInteger.valueOf(1) }, 
								{ BigInteger.valueOf(1), BigInteger.valueOf(0) } };
		if (n == 0)
			return BigInteger.valueOf(0);
		power(F, n - 1);
		return F[0][0];
	}

	static void multiply(BigInteger F[][], BigInteger M[][]) {
		BigInteger x = F[0][0].multiply(M[0][0]).add(F[0][1].multiply(M[1][0]));
		BigInteger y = F[0][0].multiply(M[0][1]).add(F[0][1].multiply(M[1][1]));
		BigInteger z = F[1][0].multiply(M[0][0]).add(F[1][1].multiply(M[1][0]));
		BigInteger w = F[1][0].multiply(M[0][1]).add(F[1][1].multiply(M[1][1]));
		F[0][0] = x;
		F[0][1] = y;
		F[1][0] = z;
		F[1][1] = w;
	}

	/* Optimized version of power() in method 4 */
	static void power(BigInteger F[][], int n) {
		if (n == 0 || n == 1)
			return;
		BigInteger[][] M = new BigInteger[][] { 
								{ BigInteger.valueOf(1), BigInteger.valueOf(1) }, 
								{ BigInteger.valueOf(1), BigInteger.valueOf(0) } };
		power(F, n / 2);
		multiply(F, F);
		if (n % 2 != 0)
			multiply(F, M);
	}
	     
	public static void main(String[] args) {
		Fibonacci fib = new Fibonacci(101);
		for (int i = 0; i < 100; i++) {
			System.out.printf("[%3d]: Recursive:[%s], DP:[%s], MM:[%s]\n", i, fib.Recursive(i), fib.DynammicProgramming(i), fib.MatrixMultiply(i));
		}
	}
}
