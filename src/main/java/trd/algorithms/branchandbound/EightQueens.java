package trd.algorithms.branchandbound;

import trd.algorithms.Arrays.ArrayProblems;

public class EightQueens {
	
	Integer[] 	Positions;
	int			Dim;
	
	public EightQueens(int dim) {
		this.Positions = new Integer[dim];
		this.Dim   = dim;
		for (int i = 0; i < dim; i++)
			Positions[i] = 0;
	}
	
	public boolean Compatible(int Queen, int col) {
		
		// Check for column compatibility
		for (int q = Queen - 1; q >= 0; q--) {
			if (Positions[q] == col)
				return false;
		}
		
		// check for diagonal entries
		if (Queen - 1 >= 0) {
			int c = Positions[Queen - 1];
			if (c == 0 && col == 1 ||
				c == Dim && col == Dim - 1 ||
				col == c - 1 ||
				col == c + 1)
			return false;
		}
		return true;
	}
	
	public boolean PlaceQueens(int Queen) {
		if (Queen == Dim)
			return true;
		
		boolean fContinue = false;
		for (int j = 0; !fContinue && j < Dim; j++) {
			if (Compatible(Queen, j)) {
				Positions[Queen] = j;
				
				// Recursively place the next queen
				fContinue = PlaceQueens(Queen + 1);
			}
		}
		return fContinue;
	}
	
	public static void main(String[] args) {
		EightQueens eq = new EightQueens(8);
		eq.PlaceQueens(0);
		System.out.printf("%s\n", ArrayProblems.ArrayToString("", eq.Positions));
	}
}
