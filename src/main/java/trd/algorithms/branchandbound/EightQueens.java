package trd.algorithms.branchandbound;

import trd.algorithms.utilities.ArrayPrint;

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
	
    public static void printQueens(Integer[] q) {
        int n = q.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (q[i] == j) System.out.printf("Q ");
                else           System.out.printf("* ");
            }
            System.out.println();
        }  
        System.out.println();
    }

    public static boolean isConsistent(Integer[] q, int n) {
        for (int i = 0; i < n; i++) {
            if (q[i] == q[n])             return false;   // same column
            if ((q[i] - q[n]) == (n - i)) return false;   // same major diagonal
            if ((q[n] - q[i]) == (n - i)) return false;   // same minor diagonal
        }
        return true;
    }


	public static void main(String[] args) {
		EightQueens eq = new EightQueens(8);
		eq.PlaceQueens(0);
		System.out.printf("%s\n", ArrayPrint.ArrayToString("", eq.Positions));
		printQueens(eq.Positions);
	}
}
