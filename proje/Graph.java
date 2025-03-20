import java.util.Scanner;

public class Graph {
    private int nodeCount;
    private int[] degrees;
    private char[] nodeNames;
    private int[][] relationMatrix;
    private boolean valid;

    public Graph(int nodeCount, int[] degrees) {
        this.nodeCount = nodeCount;
        this.degrees = copyArray(degrees);
        this.nodeNames = new char[nodeCount];
        this.relationMatrix = new int[nodeCount][nodeCount];
        this.valid = false;

        for (int i = 0; i < nodeCount; i++) {
            nodeNames[i] = (char) ('A' + i);
        }
    }

    public int[][] getRelationMatrix(){
        return this.relationMatrix;
    }

    // if sum of the degrees is odd then it is impossible to generate a graph
    private boolean isValidDegreeSequence() {
        int sum = sumArray(degrees);
        return sum % 2 == 0;
    }

    // check if degree array is valid (checking if the graph possible)
    private boolean isGraphPossible(int[] degrees) {
        sortAscending(degrees);
        int n = degrees.length;

        for (int i = n - 1; i >= 0; i--) {
            if (degrees[i] < 0) return false;
            int first = degrees[i];
            if (first == 0) continue;

            if (first > i) return false;

            for (int j = i - 1; j >= i - first; j--) {
                degrees[j]--;
                if (degrees[j] < 0) return false;
            }

            degrees[i] = 0;
            sortAscending(degrees);
        }

        return true;
    }

    // using bubble sort, sorts the array is ascending order
    private void sortAscending(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // selection sort algorithm using 2 dimention arrays as parameters
    private void sortDescending(int[][] nodes) {
        int n = nodes.length;
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (nodes[j][1] > nodes[maxIdx][1]) {
                    maxIdx = j;
                }
            }
            // swapping
            int[] temp = nodes[i];
            nodes[i] = nodes[maxIdx];
            nodes[maxIdx] = temp;
        }
    }

    // removes the nodes with zero degree
    private int[][] removeZeroDegreeNodes(int[][] nodes) {
        int count = 0;
        for (int[] node : nodes) {
            if (node[1] > 0) {
                count++;
            }
        }

        int[][] filteredNodes = new int[count][2];
        int index = 0;
        for (int[] node : nodes) {
            if (node[1] > 0) {
                filteredNodes[index++] = node;
            }
        }

        return filteredNodes;
    }

    // funcion that calculates the sum of the array
    private int sumArray(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return sum;
    }

    // function that copies array
    private int[] copyArray(int[] arr) {
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }

    // graph generating algorithm
    public void generateGraph() {
        if (!isValidDegreeSequence()) {
            System.out.println("Invalid degree sequence (sum is odd).");
            return;
        }

        int[] degreesCopy = copyArray(degrees);
        if (!isGraphPossible(degreesCopy)) {
            System.out.println("A valid graph cannot be formed with this degree sequence.");
            return;
        }

        int[][] nodes = new int[nodeCount][2];
        for (int i = 0; i < nodeCount; i++) {
            nodes[i][0] = i;
            nodes[i][1] = degrees[i];
        }

        while (nodes.length > 0) {
            sortDescending(nodes);

            int nodeIndex = nodes[0][0];
            int degree = nodes[0][1];

            if (degree > nodes.length - 1) {
                System.out.println("A valid graph could not be generated.");
                return;
            }

            for (int i = 1; i <= degree; i++) {
                int targetIndex = nodes[i][0];

                relationMatrix[nodeIndex][targetIndex] = 1;
                relationMatrix[targetIndex][nodeIndex] = 1;
                nodes[i][1]--;
            }

            nodes[0][1] = 0; // set the degree of the node that processed to 0
            nodes = removeZeroDegreeNodes(nodes); // remove nodes with 0 degree
        }

        this.valid = true;
    }

    // printing relation matrix
    public void printRelationMatrix() {
        if (this.valid) {
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
        } else {
            System.out.println("A valid graph could not be generated.");
        }
    }

    // main function to test the methods etc.
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of nodes: ");
        int nodeCount = sc.nextInt();
        int[] degrees = new int[nodeCount];

        System.out.println("Enter the degree for each node:");
        for (int i = 0; i < nodeCount; i++) {
            degrees[i] = sc.nextInt();
        }

        Graph graph = new Graph(nodeCount, degrees);
        graph.generateGraph();
        System.out.println("\nGenerated Adjacency Matrix:");
        graph.printRelationMatrix();
    }
}
