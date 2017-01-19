package trd.Algorithms.bitoperations;

public class BitOperations {
	public static void PowerSet(int n) {
		int count = 0;
		long num  = (long)Math.pow(2.0, (double)n);
		for (int i = 0; i < num; i++) {
			++count;
			
			if (i == 0)
				System.out.printf("[%4d]:null\n", count);
			else {
				int thisI = i;
				boolean fFirst = true;
				while (thisI > 0) {
					int NextBit = LeastBitSet(thisI);
					if (fFirst) {
						System.out.printf("[%4d]:", count);
						fFirst = false;
					}
					System.out.printf("%d ", NextBit);
					thisI = (int)Unset(thisI, NextBit);
				}
				System.out.println();
			}
		}
	}
	
	public static int CountBitsSet(int n) {
		int count = 0;
		while (n > 0) {
			n = n & (n-1);
			count++;
		}
		return count;
	}

	public static int binlog( int bits ) // returns 0 for bits=0
	{
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
	
	public static int LeastBitSet(Integer n) {
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
}
