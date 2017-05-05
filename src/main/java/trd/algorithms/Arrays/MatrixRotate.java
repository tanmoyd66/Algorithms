package trd.algorithms.Arrays;

import trd.algorithms.utilities.ArrayPrint;

public class MatrixRotate {
	public static <T> void FourWaySwap(T[][] matrix, int i1, int j1, int i2, int j2, int i3, int j3, int i4, int j4) {
		T temp1, temp2;
		
		temp1 = matrix[i2][j2];
		matrix[i2][j2] = matrix[i1][j1];
		temp2 = matrix[i3][j3];
		matrix[i3][j3] = temp1;
		temp1 = matrix[i4][j4];
		matrix[i4][j4] = temp2;
		matrix[i1][j1] = temp1;
	}
	
	// Strategy:
	//		We will do a layer at a time.
	//		To rotate a N X N matrix we need N - 1 rotations of elements
	//		(0,i)->(i,N-1)->(N-1,N-i-1)->(N-i-1,0)->(0,i)
	//		We will have to offset the above dimensions by adding (N-i,N-i) to them
	//			as the left point goes from (0,0)->(1,1)->(2,2) etc.
	public static <T> void RotateRight(T[][] matrix) {
		int DimX  = matrix.length, DimY = matrix[0].length;
		int leftX = -1, leftY = -1;
		
		// Loop until the matrix is of size at least 2 (otherwise all swaps will be to same element)
		while (DimX > 1 && DimY > 1) {
			leftX += 1; leftY += 1;
			for (int i = 1; i < DimX ; i++) {
				FourWaySwap(matrix, 
						leftX + 0,            leftY + i,            //(0,i)
						leftX + i,            leftY + DimX - 1, 	//(i, N - 1)
						leftX + DimX - 1,     leftY + DimX - i - 1, //(N - 1, N - i - 1)
						leftX + DimX - i - 1, leftY + 0);			//(N - i - 1, 0)
			}
			DimX -= 2; DimY -= 2;
		}
	}
	
	public static void main(String[] args) {
		Integer[][] matrix = new Integer[][] {
				{  1,  2,  3,  4,  5 },
				{  6,  7,  8,  9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }};
		
		System.out.printf("Original:\n%s\n", ArrayPrint.MatrixToString(matrix, matrix.length, matrix[0].length));	
		MatrixRotate.RotateRight(matrix);
		System.out.printf("Rotated :\n%s\n", ArrayPrint.MatrixToString(matrix, matrix.length, matrix[0].length));	
	}
}
