package proje;
import java.util.Arrays;

class GraphMatrixCalculator {

	private int[][] relationMatrix; // Grafın komşuluk matrisi
	private int n; // Grafın düğüm (vertex) sayısı

	public GraphMatrixCalculator(int[][] relationMatrix) {
		this.relationMatrix = relationMatrix;
		this.n = relationMatrix.length;
	}

	// R^k matrisini hesaplama (k uzunluğundaki yolları gösteren matris)
	public int[][] calculateRk(int k) {
		int[][] result = relationMatrix; // Orijinal matrisle başla

		for (int power = 2; power <= k; power++) {
			result = multiplyMatrices(result, relationMatrix);
		}

		return result;
	}

	//R* matrisini hesaplama
	public int[][] calculateRstar() {
		int[][] Rstar = new int[n][n];

		// Orijinal matrisi kopyala
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Rstar[i][j] = relationMatrix[i][j];
			}
		}

		// R* = R + R^2 + R^3 + ... + R^n hesapla
		for (int power = 2; power <= n; power++) {
			int[][] Rk = calculateRk(power);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (Rk[i][j] > 0) {
						Rstar[i][j] = 1;
					}
				}
			}
		}

		return Rstar;
	}

	// Rmin matrisini hesaplama (minimum mesafeler matrisi)
	public int[][] calculateRmin() {
		int[][] Rmin = new int[n][n];

		// Rmin matrisini başlatilmasi
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					Rmin[i][j] = 0; // Bir düğümün kendine olan mesafesi 0
				} else if (relationMatrix[i][j] == 1) {
					Rmin[i][j] = 1; // Doğrudan kenar varsa mesafe 1
				} else {
					Rmin[i][j] = Integer.MAX_VALUE / 2; // Aksi takdirde mesafe "sonsuz"
				}
			}
		}

		//minimum mesafeleri buluyoruz
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (Rmin[i][k] + Rmin[k][j] < Rmin[i][j]) {
						Rmin[i][j] = Rmin[i][k] + Rmin[k][j];
					}
				}
			}
		}

		return Rmin;
	}

	// İki matrisi çarpma
	private int[][] multiplyMatrices(int[][] a, int[][] b) {
		int[][] result = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = 0;
				for (int k = 0; k < n; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
				// Sonucu binary matrise dönüştür
				if (result[i][j] > 0) {
					result[i][j] = 1;
				} else {
					result[i][j] = 0;
				}
			}
		}

		return result;
	}
	// Matrisi ekrana yazdırma
	public void printMatrix(int[][] matrix, String name) {
		System.out.println(name + ":");
		for (int[] row : matrix) {
			for (int value : row) {
				System.out.print(value + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void main(String[] args) {
		// Örnek matris
		int[][] relationMatrix = {
				{0, 1, 0, 0},
				{1, 0, 1, 0},
				{0, 1, 0, 1},
				{0, 0, 1, 0}
		};

		// GraphMatrixCalculator nesnesi oluştur
		GraphMatrixCalculator calculator = new GraphMatrixCalculator(relationMatrix);

		int[][] R2 = calculator.calculateRk(2);
		calculator.printMatrix(R2, "R^2");

		int[][] R3 = calculator.calculateRk(3);
		calculator.printMatrix(R3, "R^3");

		int[][] Rstar = calculator.calculateRstar();
		calculator.printMatrix(Rstar, "R*");

		int[][] Rmin = calculator.calculateRmin();
		calculator.printMatrix(Rmin, "Rmin");
	}
}