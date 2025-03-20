import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {
    private int nodeCount;
    private int[] degrees;
    private char[] nodeNames;
    private int[][] relationMatrix;
    private Random rand;

    public Graph(int nodeCount, int[] degrees) {
        this.nodeCount = nodeCount;
        this.degrees = Arrays.copyOf(degrees, degrees.length);
        this.nodeNames = new char[nodeCount];
        this.relationMatrix = new int[nodeCount][nodeCount];
        this.rand = new Random();
        
        for (int i = 0; i < nodeCount; i++) {
            nodeNames[i] = (char) ('A' + i);
        }
    }

    private static void shuffleArray(int[] array, int size) {
        Random rand = new Random();
        for (int i = size - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    
    private static void bubbleSort(int arr[], int n){
        int i, j, temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++) {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    private boolean isValidDegreeSequence() {
        int sum = Arrays.stream(degrees).sum();
        return sum % 2 == 0;
    }
    
    private boolean isGraphPossible(int[] degrees) {
        bubbleSort(degrees, degrees.length);
        reverseArray(degrees);
        
        while (true) {
            if (degrees[0] == 0) return true;
            
            int first = degrees[0];
            if (first >= degrees.length) return false;
            
            for (int i = 1; i <= first; i++) {
                degrees[i]--;
                if (degrees[i] < 0) return false;
            }
            
            degrees[0] = 0;
            bubbleSort(degrees, degrees.length);
            reverseArray(degrees);
        }
    }
    
    private void reverseArray(int[] array) {
        int i = 0, j = array.length - 1;
        while (i < j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
        }
    }
    
    public boolean generateGraph() {
        if (!isValidDegreeSequence()) {
            System.out.println("Invalid degree sequence: Sum must be even");
            return false;
        }
        
        int[] degreesCopy = Arrays.copyOf(degrees, degrees.length);
        if (!isGraphPossible(degreesCopy)) {
            System.out.println("Invalid degree sequence: No graph can be formed with these degrees.");
            return false;
        }
        
        int totalEdges = Arrays.stream(degrees).sum();
        int[] nodeList = new int[totalEdges];
        int index = 0;
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < degrees[i]; j++) {
                nodeList[index++] = i;
            }
        }
        
        shuffleArray(nodeList, totalEdges);
        
        index = 0;
        while (index < totalEdges) {
            int a = nodeList[index++];
            int b = nodeList[index++];
            
            if (a != b && relationMatrix[a][b] == 0) {
                relationMatrix[a][b] = 1;
                relationMatrix[b][a] = 1;
                degrees[a]--;
                degrees[b]--;
            } else {
                index -= 2;
                shuffleArray(nodeList, totalEdges);
            }
        }
        
        for (int degree : degrees) {
            if (degree != 0) {
                System.out.println("Graph creation failed: Some degrees remain unmatched.");
                return false;
            }
        }
        
        return true;
    }
    
    public void printRelationMatrix() {
        System.out.print("  ");
        for (char name : nodeNames) {
            System.out.print(name + " ");
        }
        System.out.println();
        
        for (int i = 0; i < nodeCount; i++) {
            System.out.print(nodeNames[i] + " ");
            for (int j = 0; j < nodeCount; j++) {
                System.out.print(relationMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int[][] getRelationMatrix() {
        return relationMatrix;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of nodes: ");
        int nodeCount = sc.nextInt();
        int[] degrees = new int[nodeCount];
        System.out.println("Enter the degrees of each node:");
        for (int i = 0; i < nodeCount; i++) {
            degrees[i] = sc.nextInt();
        }
        
        Graph graph = new Graph(nodeCount, degrees);
        if (graph.generateGraph()) {
            System.out.println("Graph successfully generated.");
            graph.printRelationMatrix();
            
            try (FileWriter writer = new FileWriter("graph_matrix.txt")) {
                for (int i = 0; i < graph.nodeCount; i++) {
                    for (int j = 0; j < graph.nodeCount; j++) {
                        writer.write(graph.relationMatrix[i][j] + " ");
                    }
                    writer.write("\n");
                }
                System.out.println("Matrix has been saved to graph_matrix.txt");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to generate graph.");
        }
    }
}
