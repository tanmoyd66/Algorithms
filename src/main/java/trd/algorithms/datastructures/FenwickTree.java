package trd.algorithms.datastructures;

import trd.algorithms.utilities.ArrayPrint;

//The key point is this: In a Fenwick tree, each position "responsible" for storing cumulative data of N previous positions (N could be 1)
	// For example:
	// array[40] stores: array[40] + array[39] ... + array[32] (8 positions)
	// array[32] stores: array[32] + array[31] ... + array[1]  (32 positions)
	// <strong>But, how do you know how much positions a given index is "responsible" for?</strong>
	// To know the number of items that a given array position 'ind' is responsible for
	// We should extract from 'ind' the portion up to the first significant one of the binary representation of 'ind'
	// for example, given ind == 40 (101000 in binary), according to Fenwick algorithm
	// what We want is to extract 1000(8 in decimal).
	// This means that array[40] has cumulative information of 8 array items.

public class FenwickTree {
	
	Integer 	FenwickTree[];
	Integer[]	A;
	
	public int getParent(int i) {
		// 1. Get 2s complement (~x + 1)
		// 2. And with the original ((1) & x)
		// 3. Subtract from the original number ((2) + x)
		return i - ((~i + 1) & i);
	}
	
	public int getNext(int i) {
		// 1. Get 2s complement (~x + 1)
		// 2. And with the original ((1) & x)
		// 3. Add to the original number ((2) + x)
		return ((~i + 1) & i) + i;
	}
	
	public void update(int ind, int value) {
        while (ind < FenwickTree.length) {
        	FenwickTree[ind] += value;
            
            // Extracting the portion up to the first significant one of the binary representation 
            // of 'ind' and incrementing ind by that number
            ind = getNext(ind);
        }
    }
	
	private int rangeSumQuery(int ind) {
		assert ind > 0;
		int sum = 0;
		while (ind > 0) {
			sum += FenwickTree[ind];

			// Extracting the portion up to the first significant one of the
			// binary representation of 'ind' and decrementing ind by that
			// number
			ind = getParent(ind);
		}
		return sum;
	}

    public int rangeSumQuery(int a, int b) {
        return rangeSumQuery(b) - rangeSumQuery(a - 1);
    }

	public FenwickTree(Integer[] A) {
		
		this.A = A;
		FenwickTree = new Integer[A.length + 1];
		for (int i = 0; i < A.length + 1; i++)
			FenwickTree[i] = 0;
		
		for (int i = 1; i < FenwickTree.length; i++)
			update(i, A[i - 1]);
		
		System.out.printf("%s\n", ArrayPrint.ArrayToString("Fenwick Tree", FenwickTree));
	}

	public static void main(String[] args) {
		FenwickTree ftree = new FenwickTree(new Integer[] { 3, 2, -1, 6, 5, 4, -3, 3, 7, 2, 3 });
		System.out.printf("RSQ (%d, %d): %d\n", 0, 11, ftree.rangeSumQuery(0, 11));
		System.out.printf("RSQ (%d, %d): %d\n", 2, 4,  ftree.rangeSumQuery(2, 4));
	}
}
