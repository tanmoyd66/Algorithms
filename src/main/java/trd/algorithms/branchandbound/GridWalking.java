package trd.algorithms.branchandbound;

public class GridWalking {
	
	// How many ways can you go from the top left of a nXm rectangle to the bottom right?
	int width, height;
	public GridWalking(int width, int height) {
		this.width = width; this.height = height;
	}

	// Recursive Formulation
	public int Recursive(int X, int Y) {
		int count = 0;
		if (X == width - 1 && Y == height - 1) {
			count = 1;
		} else if (X >= width || Y >= height) {
			count = 0;
		} else {
			count = Recursive(X + 1, Y) + Recursive(X, Y + 1);
		}
		System.out.printf("Recursive(%d,%d) = %d\n", X, Y, count);
		return count;
	}
			
	public static void main(String[] args) {
		GridWalking gw = new GridWalking(4, 4);
		System.out.printf("Recursive Grid Walking: (%d X %d): %d\n", gw.width, gw.height, gw.Recursive(0, 0));
	}
}
