package trd.algorithms.matrices;

import java.util.Random;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import trd.algorithms.utilities.ArrayPrint;

public class Matrix {

    // return a random m-by-n matrix with random
    public static double[][] random(int m, int n) {
    	Random rand = new Random();
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = rand.nextDouble();
        return a;
    }

    // return n-by-n identity matrix I
    public static double[][] identity(int n) {
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++)
            a[i][i] = 1;
        return a;
    }

    // return x^T y
    public static double dot(double[] x, double[] y) {
        if (x.length != y.length) 
        	throw new RuntimeException("Illegal vector dimensions.");
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return B = A^T
    public static double[][] transpose(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                b[j][i] = a[i][j];
        return b;
    }

    // return c = a + b
    public static double[][] add(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i][j] = a[i][j] + b[i][j];
        return c;
    }

    // return c = a - b
    public static double[][] subtract(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i][j] = a[i][j] - b[i][j];
        return c;
    }

    // matrix multiplication (y = A * x)
    public static double[][] multiply(double[][] a, double[][] b) {
    	RealMatrix A = MatrixUtils.createRealMatrix(a);
    	RealMatrix B = MatrixUtils.createRealMatrix(b);
    	RealMatrix C = A.multiply(B);
    	return C.getData();
    }

    // matrix inverse 
    public static double[][] inverse(double[][] a) {
    	RealMatrix A = MatrixUtils.createRealMatrix(a);
    	RealMatrix C = new LUDecomposition(A).getSolver().getInverse();
    	return C.getData();
    }

    // matrix inverse 
    public static double[][] eigenvector(double[][] a) {
    	RealMatrix A = MatrixUtils.createRealMatrix(a);
    	RealMatrix C = new LUDecomposition(A).getSolver().getInverse();
    	return C.getData();
    }

    // test client
    public static void main(String[] args) {
        double[][] d = { { 1, 2, 3 }, { 4, 5, 6 }, { 9, 1, 3} };
        System.out.printf("D:\n%s\n", ArrayPrint.MatrixToString(d));
        
        double[][] c = Matrix.identity(5);
        System.out.printf("I:\n%s\n", ArrayPrint.MatrixToString(c));

        double[][] a = Matrix.random(5, 5);
        System.out.printf("A:\n%s\n", ArrayPrint.MatrixToString(a));

        double[][] b = Matrix.transpose(a);
        System.out.printf("A^T:\n%s\n", ArrayPrint.MatrixToString(b));

        double[][] e = Matrix.add(a, b);
        System.out.printf("A + A^T:\n%s\n", ArrayPrint.MatrixToString(e));

        double[][] f = Matrix.multiply(a, b);
        System.out.printf("A * A^T:\n%s\n", ArrayPrint.MatrixToString(f));

        double[][] g = Matrix.inverse(a);
        System.out.printf("Inv(A):\n%s\n", ArrayPrint.MatrixToString(g));

        double[][] h = Matrix.multiply(a, Matrix.inverse(a));
        System.out.printf("A * Inv(A):\n%s\n", ArrayPrint.MatrixToString(h));
    }
}