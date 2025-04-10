package proje;

import java.util.Scanner;

public class Graph {
    private int nodeCount;
    private int[] degrees;
    private char[] nodeNames;
    private int[][] relationMatrix;
    private boolean valid;
    private int[][] alteredMatrix; // isomorphic matrix
    private char[] permutedNodeNames; // permuted node names

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

    public int[][] getRelationMatrix() {
        return this.relationMatrix;
    }

    public void setRelationMatrix(int[][] relationMatrix) {
        this.relationMatrix = relationMatrix;
    }

    public boolean isValid() {
        return this.valid;
    }

    public int[][] getAlteredMatrix() {
        return this.alteredMatrix;
    }

    public char[] getPermutedNodeNames() {
        return this.permutedNodeNames;
    }

    // if sum of the degrees is odd then it is impossible to generate a graph
    private boolean isValidDegreeSequence() {
        int sum = sumArray(degrees);
        return sum % 2 == 0;
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
    public boolean generateGraph() {
        if (!isValidDegreeSequence()) {
            System.out.println("Invalid degree sequence (sum is odd).");
            return false;
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

                if (nodes.length == 0 || nodes[0][1] == 0)
                    break;

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
                    int j = (int) (Math.random() * (i + 1));
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
                return true;
            }
        }

        System.out.println("A valid graph could not be generated");
        return false;
    }

    public static Graph generateGraphUsingInterval() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of nodes: ");
        int nodeCount = sc.nextInt();
    
        System.out.print("Enter the minimum degree: ");
        int minDegree = sc.nextInt();
    
        System.out.print("Enter the maximum degree: ");
        int maxDegree = sc.nextInt();
    
        int[] degrees = new int[nodeCount];
        int attempt = 0;
    
        // try to find a valid degree sequence to generate a graph
        while (attempt < 1000) {
            int sum = 0;
            for (int i = 0; i < nodeCount; i++) {
                degrees[i] = minDegree + (int) (Math.random() * (maxDegree - minDegree + 1));
                sum += degrees[i];
            }
    
            if (sum % 2 == 0) {
                Graph graph = new Graph(nodeCount, degrees);
                if (graph.generateGraph()) {
                    System.out.println("\nGenerated Graph from Interval:");
                    graph.printRelationMatrix();
                    return graph;
                }
            }
            attempt++;
        }
    
        System.out.println("Couldn't generate a valid graph with the given interval after many attempts.");
        return null;
    }
    
    public void printAlteredMatrix() {
        System.out.print("  ");
        for (char c : permutedNodeNames) {
            System.out.print(c + " ");
        }
        System.out.println();
    
        for (int i = 0; i < nodeCount; i++) {
            System.out.print(permutedNodeNames[i] + " ");
            for (int j = 0; j < nodeCount; j++) {
                System.out.print(alteredMatrix[i][j] + " ");
            }
            System.out.println();
        }
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

    // method to check if two graphs are isomorphic
    public boolean isIsomorphicWith(Graph other) {
        if (this.nodeCount != other.nodeCount) {
            return false;
        }
    
        int[][] otherMatrix = other.getRelationMatrix();
        int totalPermutations = factorialCalculate(nodeCount);
    
        for (int count = 0; count < totalPermutations; count++) {
            int[] perm = generateNthPermutation(count, nodeCount);

            // check if the relation matrix of this graph is equal to the permuted version of the other graph's relation matrix
            if (areMatricesEqualWithPermutation(this.relationMatrix, otherMatrix, perm)) {
                System.out.println("Isomorphic at permutation index: " + count);
    
                // set the altered matrix to the permuted version of the relation matrix
                this.alteredMatrix = new int[nodeCount][nodeCount];
                for (int i = 0; i < nodeCount; i++) {
                    for (int j = 0; j < nodeCount; j++) {
                        this.alteredMatrix[i][j] = this.relationMatrix[perm[i]][perm[j]];
                    }
                }
    
                // update the permuted node names
                this.permutedNodeNames = new char[nodeCount];
                for (int i = 0; i < nodeCount; i++) {
                    this.permutedNodeNames[i] = this.nodeNames[perm[i]];
                }
    
                return true;
            }
        }
    
        return false;
    }
    
    
    

    private int[][] getPermutedMatrix(int[][] matrix, int[] perm) {
    int[][] result = new int[nodeCount][nodeCount];
    for (int i = 0; i < nodeCount; i++) {
        for (int j = 0; j < nodeCount; j++) {
            result[i][j] = matrix[perm[i]][perm[j]];
        }
    }
    return result;
}


    // check if two matrices are equal after applying a permutation to one
    private boolean areMatricesEqualWithPermutation(int[][] m1, int[][] m2, int[] perm) {
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (m1[perm[i]][perm[j]] != m2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    

    // generate nth permutation of a given size
    private static int[] generateNthPermutation(int n, int size) {
        int[] result = new int[size];
        boolean[] used = new boolean[size];
        int[] factorials = new int[size];
        factorials[0] = 1;
        for (int i = 1; i < size; i++) {
            factorials[i] = factorials[i - 1] * i;
        }

        for (int i = 0; i < size; i++) {
            int fact = factorials[size - 1 - i];
            int index = n / fact;
            n = n % fact;

            int count = -1;
            for (int j = 0; j < size; j++) {
                if (!used[j]) {
                    count++;
                    if (count == index) {
                        result[i] = j;
                        used[j] = true;
                        break;
                    }
                }
            }
        }

        return result;
    }

    // factorial calculator
    private int factorialCalculate(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++)
            result *= i;
        return result;
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
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
        
       graph = Graph.generateGraphUsingInterval();

        int[] testDegrees = { 4, 3, 3, 3, 3, 2, 2, 2, 2 };
        Graph graph1 = new Graph(9, testDegrees);
        graph1.generateGraph();
        Graph graph2 = new Graph(9, testDegrees);
        graph2.generateGraph();
        int[][] m1 = {
                { 0, 1, 1, 0, 1, 1, 0, 0, 0 },
                { 1, 0, 0, 1, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 1, 1, 0, 0, 0, 0 },
                { 0, 1, 1, 0, 0, 0, 0, 0, 1 },
                { 1, 0, 1, 0, 0, 0, 0, 1, 0 },
                { 1, 0, 0, 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0, 0 },
                { 0, 1, 0, 1, 0, 0, 0, 0, 0 }
        };
        int[][] m2 = {
                { 0, 0, 1, 1, 1, 0, 1, 0, 0 },
                { 0, 0, 1, 1, 0, 1, 0, 0, 0 },
                { 1, 1, 0, 0, 1, 0, 0, 0, 0 },
                { 1, 1, 0, 0, 0, 1, 0, 0, 0 },
                { 1, 0, 1, 0, 0, 0, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 1, 1, 0 }
        };
        

        graph1.setRelationMatrix(m1);
        graph1.printRelationMatrix();
        graph2.setRelationMatrix(m2);
        graph2.printRelationMatrix();
        System.out.println("Isomorphic: " + graph1.isIsomorphicWith(graph2));

        /*for (int i = 0; i < 120; i++) {
            int[] testPermutation = generateNthPermutation(i, 5);
            System.out.print(i + ". permutation: ");
            for (int j = 0; j < testPermutation.length; j++) {
                System.out.print(testPermutation[j] + 1);
                if (j < testPermutation.length - 1)
                    System.out.print(", ");
            }
            System.out.println();
        }
        */
        int[][] alteredMatrix = graph1.getAlteredMatrix();
        for(int i = 0;i<alteredMatrix.length;i++){
            for(int j = 0;j<alteredMatrix[i].length;j++){
                System.out.print(alteredMatrix[i][j]);
            }
            System.out.println();
        }
        graph1.printAlteredMatrix();
    }
}
