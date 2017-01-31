package trd.algorithms.utilities;

public class ArrayPrint {
	public static <S> String ArrayToString(String tag, S[] A) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s:[", tag));
		for (int i = 0; i < A.length; i++)
			sb.append(String.format("%s ", A[i]));
		sb.append(String.format("]", tag));
		return sb.toString();
	}

	public static <S> String MatrixToString(S[][] A, int M, int N) {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------\n");
		for (int row = 0; row < M; row++) {
			for (int col = 0; col < N; col++) {
				String sVal;
				if (A[0][0].getClass() == Double.class)
					sVal = String.format("%5.2f ", A[row][col]);
				else 
					sVal = String.format("%4s ", A[row][col]);
					
				sb.append(String.format("%-5s ", sVal));
			}
			sb.append("\n");
		}
		sb.append("----------------------------\n");
		return sb.toString();
	}
}
