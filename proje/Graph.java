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

    public boolean isValid(){
        return this.valid;
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
    
        for (int attempt = 0; attempt < 100; attempt++) {
            int[][] tempMatrix = new int[nodeCount][nodeCount];
            int[][] nodes = new int[nodeCount][2];
    
            for (int i = 0; i < nodeCount; i++) {
                nodes[i][0] = i;
                nodes[i][1] = degrees[i];
            }
    
            boolean success = true;
    
            while (true) {
                sortDescending(nodes);
    
                if (nodes.length == 0 || nodes[0][1] == 0) break;
    
                int currentNode = nodes[0][0];
                int degree = nodes[0][1];
    
                if (degree > nodes.length - 1) {
                    success = false;
                    break;
                }
    
                int[] targetIndices = new int[nodes.length - 1];
                for (int i = 0; i < nodes.length - 1; i++) {
                    targetIndices[i] = i + 1;
                }
    
                // shuffles array randomly
                for (int i = targetIndices.length - 1; i > 0; i--) {
                    int j = (int)(Math.random() * (i + 1));
                    int temp = targetIndices[i];
                    targetIndices[i] = targetIndices[j];
                    targetIndices[j] = temp;
                }
    
                boolean failed = false;
                for (int k = 0; k < degree; k++) {
                    int targetIndex = targetIndices[k];
                    int targetNode = nodes[targetIndex][0];
    
                    if (nodes[targetIndex][1] == 0) {
                        failed = true;
                        break;
                    }
    
                    tempMatrix[currentNode][targetNode] = 1;
                    tempMatrix[targetNode][currentNode] = 1;
                    nodes[targetIndex][1]--;
                }
    
                if (failed) {
                    success = false;
                    break;
                }
    
                nodes[0][1] = 0;
                nodes = removeZeroDegreeNodes(nodes);
            }
    
            if (success) {
                this.relationMatrix = tempMatrix;
                this.valid = true;
                return;
            }
        }
    
        System.out.println("A valid graph could not be generated");
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
