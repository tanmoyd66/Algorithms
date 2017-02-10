package trd.algorithms.MachineLearning;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import trd.algorithms.utilities.ArrayPrint;

public class Recommender {
	RealMatrix  propensities;
	double[][]  U;
	double[][]  S;
	double[][]  V;
	
	@SuppressWarnings("deprecation")
	public Recommender(Double[][] propensityMatrix) {
		propensities = MatrixUtils.createRealMatrix(propensityMatrix.length, 
													propensityMatrix[0].length);
		for (int i = 0; i < propensityMatrix.length; i++) 
			for (int j = 0; j < propensityMatrix[0].length; j++) {
				if (propensityMatrix[i][j] != null)
					propensities.setEntry(i, j, propensityMatrix[i][j]);
			}
		propensities.transpose();
	}
	
	public void PerformSVDFactorization() {
		SingularValueDecomposition svd = new SingularValueDecomposition(propensities);
		this.U = svd.getU().getData();
		this.S = svd.getS().getData();
		this.V = svd.getV().getData();
		
		
		// Find Rank
		int rank = -1;
		for (int i = 0; i < S.length; i++) {
			if (S[i][i] != 0)
				rank++;
		}
		rank = 1;
		// Reduce Rank
		if (rank < S.length) {
			U = svd.getU().getSubMatrix(0, rank, 0, U[0].length - 1).getData();
			S = svd.getS().getData();
			for (int i = rank + 1; i < S.length; i++)
				S[i][i] = 0;
			V = svd.getV().getSubMatrix(0, rank, 0, V[0].length - 1).getData();
		}
		System.out.printf("U\n%s\n", ArrayPrint.MatrixToString(this.U));
		System.out.printf("S\n%s\n", ArrayPrint.MatrixToString(this.S));
		System.out.printf("V\n%s\n", ArrayPrint.MatrixToString(this.V));
		
		RealMatrix UserProduct = MatrixUtils.createRealMatrix(U)
								 .multiply(MatrixUtils.createRealMatrix(S))
								 .multiply(MatrixUtils.createRealMatrix(V).transpose());
		System.out.printf("User Product\n%s\n", ArrayPrint.MatrixToString(UserProduct.transpose().getData()));
		
	}
	
	public double GetScore(int i, int j) {
		return 0.0;
	}
	public static void main(String[] args) {
		Double[][] UserProduct = new Double[][] { 
							//         M1   M2   M3   M4
							/*U1*/	{ 1.0, 4.0, 1.0,  4.0  },
							/*U2*/	{ 5.0, 1.0, 5.0,  1.0  },
							/*U3*/	{ 1.0, 1.0, null, 1.0  },
							/*U4*/	{ 1.0, 2.0, 1.0,  3.0  },
							/*U5*/	{ 2.0, 1.0, 2.0,  1.0  },
							/*U6*/	{ 1.0, 2.0, 1.0,  4.0 },
							/*U7*/	{ 2.0, 1.0, 2.0,  1.0  },
							/*U8*/	{ 1.0, 2.0, 2.0,  null },
							/*U9*/	{ 2.0, 1.0, 2.0,  1.0  },
							/*U10*/	{ 1.0, 2.0, 1.0,  null },
							/*U11*/	{ 2.0, 1.0, 2.0,  1.0  },
							};
									
		Recommender recommender = new Recommender(UserProduct);
		recommender.PerformSVDFactorization();
		
	}
}
