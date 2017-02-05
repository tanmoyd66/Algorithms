package trd.algorithms.bitoperations;

import java.util.ArrayList;
import java.util.List;

public class BitOperations {
	// Strategy:
	//		Power set of a number can be encoded in a number [0,...2^n]
	public static List<List<Integer>> PowerSet(int n) {
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
		long num  = (long)Math.pow(2.0, (double)n);
		
		// Each number is an encoding of a set in the power-set
		for (int i = 0; i < num; i++) {
			
			if (i == 0) 
				ret.add(new ArrayList<Integer>());
			else {
				List<Integer> thisRet = new ArrayList<Integer>();
				
				// Each set bit is the element in the output 
				int thisI = i;
				while (thisI > 0) {
					int NextBit = LowestBitSet(thisI);
					thisRet.add(NextBit);
					thisI = (int)Unset(thisI, NextBit);
				}
				ret.add(thisRet);
			}
		}
		return ret;
	}
	
	public static int BitsSet(int n) {
		// This is the Kernighan Algorithm
		// Each iteration of the loop removes the last bit that is set
		int count = 0;
		while (n > 0) {
			n = n & (n-1);
			count++;
		}
		return count;
	}

	// Computes the binary logarithm of a number
	public static int binlog(int bits) {
	    int log = 0;
	    if( ( bits & 0xffff0000 ) != 0 ) { 
	    	bits >>>= 16; log = 16; 
	    }
	    if( bits >= 256 ) { 
	    	bits >>>= 8; log += 8; 
	    }
	    if( bits >= 16  ) { 
	    	bits >>>= 4; log += 4; 
	    }
	    if( bits >= 4   ) { 
	    	bits >>>= 2; log += 2; 
	    }
	    return log + ( bits >>> 1 );
	}
	
	public static boolean IsPowerOfTwo(int n) {
		// If a number is not a power of 2, then there should be at least 2, 1s
		// in the binary representation of the number.
		// Hence (n - 1) needs to have at least one 1 and hence 
		// n & (n - 1) cannot be 0
		return n != 0 && 	
			   (n & (n - 1)) == 0;
	}

	public static int GetNextPowerOfTwo(int n) {
		// First basic check
		if (IsPowerOfTwo(n))
			return n;
		
		// n & (n - 1) removes the least significant bit
		// Loop will end when we have the most significant bit as the only bit set
		while ((n & (n - 1)) > 0) {
			n = n & (n - 1);
		}
		
		// Shift that bit 1 to the left
		return n << 1;
	}
	
	public static int HighestBitSet(int n) {
		while ((n & (n - 1)) > 0) {
			n = n & (n - 1);
		}
		return binlog(n);
	}

	public static int LowestBitSet(Integer n) {
		int prod = n & (-n);
		int bitNo =  binlog(prod);
		return bitNo;
	}
	
	public static long Set(long num, int bit) {
		return num | (1 << bit);
	}

	public static long Unset(long num, int bit) {
		return num & ~(1 << bit);
	}
	
	public static long TwosComplement(long num) {
		return ~num + 1;
	}
	
	public static boolean HasAZeroByte(int num) {
		return ((num & 0xff) != 0 && 
				(num & 0xff00) != 0 && 
				(num & 0xff0000) != 0 && 
				(num & 0xff000000) != 0);
	}
	
	public static void main(String[] args) {
		System.out.printf("Twos Complement of %d(%s) is: %d(%s)\n", 3, Integer.toBinaryString(3), TwosComplement(3), Integer.toBinaryString(3));

		for (int n = 0; n < 64; n++) {
			System.out.printf("[%3d]:", n);
			System.out.printf("PowerOfTwo(%3d) = %s ", n, IsPowerOfTwo(n) ? "true " : "false");
			System.out.printf("BitsSet(%3d) = %3d ", n, BitsSet(n));
			System.out.printf("NextPowerOfTwo(%3d) = %3d ", n, GetNextPowerOfTwo(n));
			System.out.printf("HighestBitSet(%3d) = %3d ", n, HighestBitSet(n));
			System.out.printf("LowestBitSet(%3d) = %3d ", n, LowestBitSet(n));
			System.out.printf("HasAZeroByte(%3d) = %s ", n, HasAZeroByte(n));
			//System.out.printf("PowerSet(%3d) = %s ", n, PowerSet(n));
			System.out.printf("\n");
		}
	}
}
