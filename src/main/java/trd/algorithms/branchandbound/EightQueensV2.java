package trd.algorithms.branchandbound;

public class EightQueensV2 {
	int board[][];
	int dim;
	
	public EightQueensV2(int dim) {
		this.dim = dim;
		board = new int[dim][dim];
	}
	
	// check if the queen will be safe in row, col
	public boolean isSafe(int row, int col) {
		// There should be no other queen in the same column
		for (int i = 0; i < dim; i++) {
			if (board[i][col] == 1)
				return false;
		}

		// There should be no other queen in the same row
		for (int i = 0; i < dim; i++) {
			if (board[row][i] == 1)
				return false;
		}
		
		// Upper diagonal
	    for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
	        if (board[i][j] == 1)
	            return false;
	 
	    // Lower Diagonal
	    for (int i = row, j = col; j >= 0 && i < dim; i++, j--)
	        if (board[i][j] == 1)
	            return false;

	    return true;
	}
	
	int count = 1;
	public void PrintBoard() {
		System.out.printf("%d\n", count);
		System.out.printf("|-");
		for (int i = 0; i < dim; i++)
			System.out.printf("--");
		System.out.printf("|\n");
		
		for (int i = 0; i < dim; i++) {
			System.out.printf("| ");
			for (int j = 0; j < dim; j++) {
				System.out.printf("%s ", board[i][j] == 1 ? "X" : " ");
			}
			System.out.printf("|\n");
		}

		System.out.printf("|-");
		for (int i = 0; i < dim; i++)
			System.out.printf("--");
		System.out.printf("|\n");
	}

	public boolean PlaceQueen(int col, boolean firstOnly) {
		
		if (col == dim) {
			PrintBoard();
			count++;
			return true;
		}
		
		for (int i = 0; i < dim; i++) {
			if (isSafe(i, col)) {
				board[i][col] = 1;
				boolean ret = PlaceQueen(col + 1, firstOnly);
				if (ret && firstOnly)
					return true;
				board[i][col] = 0;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		new EightQueensV2(8).PlaceQueen(0, false);
	}
}
