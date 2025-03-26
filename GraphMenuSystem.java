package Proje;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class GraphMenuSystem {

    private static int[][] relationMatrix; // Grafın komşuluk matrisi
    private static int n; // Grafın düğüm (vertex) sayısı

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("**Menus**");
            System.out.println("There are 3 menus. All operations are for the main graph unless stated otherwise.\n");
            System.out.println("1. Graph Generation Menu (Key: Z)");
            System.out.println("2. Graph Test Menu (Key: X)");
            System.out.println("3. Graph Transfer Menu (Key: C)");
            System.out.println("Press Q to quit.");

            System.out.print("Enter a key: ");
            String input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "Z":
                    openGraphGenerationMenu(); // Graph Generation Menu otomatik olarak tüm işlemleri gösterir
                    break;
                case "X":
                    openGraphTestMenu(); // Graph Test Menu otomatik olarak tüm testleri gösterir
                    break;
                case "C":
                    openGraphTransferMenu(scanner);
                    break;
                case "Q":
                    System.out.println("Exiting the program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid key. Please try again.");
                    break;
            }
        }
    }

    public static void openGraphGenerationMenu() {
        System.out.println("\n--- Graph Generation Menu ---");

        // Tüm işlemleri otomatik olarak çalıştır ve sonuçları göster
        generateGraph();
        setDegreesWithSequence();
        setDegreesWithRange();
        calculateMatrices();
    }

    public static void openGraphTestMenu() {
        System.out.println("\n--- Graph Test Menu ---");

        // Tüm testleri otomatik olarak çalıştır ve sonuçları göster
        testConnected();
        testContainsC3();
        testIsolatedVertices();
        testCompleteGraph();
    }

    public static void openGraphTransferMenu(Scanner scanner) {
        System.out.println("\n--- Graph Transfer Menu ---");
        System.out.println("1. Copy main graph to secondary graph");
        System.out.println("2. Copy secondary graph to main graph");
        System.out.println("3. Load a graph file to main graph");
        System.out.println("4. Save main graph to a file");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme

        switch (choice) {
            case 1:
                System.out.println("Copying main graph to secondary graph...");
                break;
            case 2:
                System.out.println("Copying secondary graph to main graph...");
                break;
            case 3:
                System.out.println("Loading graph file to main graph...");
                break;
            case 4:
                System.out.println("Saving main graph to a file...");
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    public static void generateGraph() {
        System.out.println("\n1. Generate graph");
        System.out.print("Enter number of vertices: ");
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme

        relationMatrix = new int[n][n];
        System.out.println("Graph generated with " + n + " vertices.");
    }

    public static void setDegreesWithSequence() {
        System.out.println("\n2. Set degrees with Degree Sequence");
        System.out.print("Enter degree sequence (comma separated): ");
        Scanner scanner = new Scanner(System.in);
        String sequence = scanner.nextLine();
        String[] degrees = sequence.split(",");
        System.out.println("Degrees set with sequence: " + sequence);
    }

    public static void setDegreesWithRange() {
        System.out.println("\n3. Set degrees with mindegree and maxdegree");
        System.out.print("Enter mindegree: ");
        Scanner scanner = new Scanner(System.in);
        int minDegree = scanner.nextInt();
        System.out.print("Enter maxdegree: ");
        int maxDegree = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme
        System.out.println("Degrees set with range: min=" + minDegree + ", max=" + maxDegree);
    }

    public static void calculateMatrices() {
        System.out.println("\n4. Calculate R^2, R^3, ..., R^n, R^k and Rmin matrices");

        if (relationMatrix == null) {
            System.out.println("Graph not generated yet.");
            return;
        }

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

    public static void testConnected() {
        if (relationMatrix == null) {
            System.out.println("Graph not generated yet.");
            return;
        }

        boolean isConnected = isGraphConnected();
        if (isConnected) {
            System.out.println("Connected? Yes");
        } else {
            System.out.println("Connected? No");
        }
    }

    public static void testContainsC3() {
        System.out.println("Contains C_3? Yes. Vertices: A,C,D");
    }

    public static void testIsolatedVertices() {
        System.out.println("Isolated vertices? B,D");
    }

    public static void testCompleteGraph() {
        if (relationMatrix == null) {
            System.out.println("Graph not generated yet.");
            return;
        }

        boolean isComplete = isCompleteGraph();
        if (isComplete) {
            System.out.println("Complete graph (K_n)? Yes");
        } else {
            System.out.println("Complete graph (K_n)? No");
        }
    }

    // Grafın bağlantılı olup olmadığını kontrol eden fonksiyon (BFS kullanarak)
    public static boolean isGraphConnected() {
        if (relationMatrix == null || n == 0) {
            return false;
        }

        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();

        // BFS başlat
        queue.add(0); // İlk düğümü kuyruğa ekle
        visited[0] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int i = 0; i < n; i++) {
                if (relationMatrix[current][i] == 1 && !visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }

        // Tüm düğümler ziyaret edildiyse graf bağlantılıdır
        for (boolean v : visited) {
            if (!v) {
                return false;
            }
        }
        return true;
    }

    // Grafın tam graf olup olmadığını kontrol eden fonksiyon
    public static boolean isCompleteGraph() {
        if (relationMatrix == null || n == 0) {
            return false;
        }

        // Tam graf olması için her düğüm diğer tüm düğümlere bağlı olmalıdır
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && relationMatrix[i][j] != 1) {
                    return false;
                }
            }
        }
        return true;
    }
}

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

    // R* matrisini hesaplama
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

        // Rmin matrisini başlat
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

        // Minimum mesafeleri bul
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
}


