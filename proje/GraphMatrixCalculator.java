import java.util.*;
public class GraphMatrixCalculator{
	
	
	private int [][]  relationMatrix;
	private int [][]  Rstar;
	private int [][]  Rmin;
	private int n ; // Number of vertices;
	
	
	public GraphMatrixCalculator(int[][]relationMatrix) {
		this.relationMatrix = relationMatrix;
		this.n  = relationMatrix.length;
		this.Rstar = new int[n][n];
		this.Rmin = new int [n][n];
	}
	  // Calculate R^2, R^3, R^4, ...., R^n
	public void calculatePowerMatrices () {
		int [][] currentMatrix = relationMatrix ;
		
		for (int power = 2 ; power < n ; power++ ) {
			int [][]nextMatrix = new int [n][n];
			
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					for (int k = 0 ; k < n ; k++) {
						nextMatrix [i][j] += currentMatrix[i][k]*relationMatrix[j][k];
					}
				}
			}
			printMatrix(nextMatrix, "R^" +power );
			currentMatrix = nextMatrix;
		}
	}
	// Calculate R*
	public void calculateRstar() {
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < n ; j++) {
				Rstar[i][j] = relationMatrix[i][j];			
			}
		}
		int[][] currentMatrix = relationMatrix;
		
		for(int power = 2 ; power <= n ; power++) {
			int[][] nextMatrix = new int [n][n];
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					for(int k = 0 ; k < n ; k++) {
						nextMatrix[i][j] +=  currentMatrix[i][k] * relationMatrix[k][j];
					}
				}
			}
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					Rstar[i][j]  |= nextMatrix[i][j];
				}
			}
			currentMatrix = nextMatrix;
		}
		printMatrix(Rstar, "R*");
	}
	
	// Calculate Rmin
	public void calculateRmin () {
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < n ; j++) {
				Rmin[i][j] = Integer.MAX_VALUE;
			}
		}
		for (int i = 0 ; i < n ; i++) {
			Rmin[i][i] = 0 ;
		}
		int[][] currentMatrix = relationMatrix;
		for (int power = 1 ; power <= n ; power++) {
			int[][] nextMatrix = new int[n][n];
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					for (int k = 0 ; k < n ; k++) {
						nextMatrix[i][j] += currentMatrix[i][k] * relationMatrix[k][j];	 
					}
				}
			}
			currentMatrix = nextMatrix;
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					if (currentMatrix[i][j] > 0 && Rmin[i][j] == Integer.MAX_VALUE) {
						Rmin[i][j] = power;
					}
				}
			}
		}
		printMatrix(Rmin, "Rmin");
	}
	
	// Method to print Matrix
	private void printMatrix(int[][] matrix, String matrixName) {
		System.out.println(matrixName + ":");
		for (int []row : matrix) {
			for (int value : row) {
				System.out.print(value + " ");
			}
			System.out.println();	 
		}
		System.out.println();
	}
	
	public static void main (String[] args) {
		// Example of relation matrix
		int [][] relationMatrix = {
			{0 , 1 , 0 , 0 },
			{1 , 0 , 1 , 0 },
			{0 , 1 , 0 , 1 },
			{0 , 1 , 0 , 0 }
		};
		GraphMatrixCalculator calculator = new GraphMatrixCalculator(relationMatrix);
		calculator.calculatePowerMatrices();
		calculator.calculateRstar();
		calculator.calculateRmin();
	}
}