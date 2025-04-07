package proje;

import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Enigma_Terminal {
    public enigma.console.Console cn = Enigma.getConsole("Mouse and Keyboard");
    public TextMouseListener tmlis; 
    public KeyListener klis; 
    public int[][] matrix;
    public board main_graph = null;
    public board seconday_graph = null;
    public board[] depotGraphs = new board[9];
    
    // Standard variables
    public int mousepr, mousex, mousey, keypr, rkey;

    public Enigma_Terminal() throws Exception {
        // Initialize input listeners
        tmlis = new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {}
            public void mousePressed(TextMouseEvent arg0) {
                if (mousepr == 0) {
                    mousepr = 1;
                    mousex = arg0.getX();
                    mousey = arg0.getY();
                }
            }
            public void mouseReleased(TextMouseEvent arg0) {}
        };
        cn.getTextWindow().addTextMouseListener(tmlis);

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);
        
        clearConsole();
        main_menu();
        boolean is_main_graph_created = false;
        
        while (true) {
            if (keypr == 1) {
                if (rkey == KeyEvent.VK_Z) {
                    clearConsole();
                    if (!is_main_graph_created) {
                        graph_generate_menu();
                        is_main_graph_created = true;
                    }
                    clearConsole();
                    System.out.println("Main Graph");
                    main_graph.tabloyuYazdir();
                    printMatrix(matrix);
                    System.out.println("Press Enter to see secondary graph");
                    cn.readLine();
                    clearConsole();
                    System.out.println("Secondary Graph");
                    seconday_graph.tabloyuYazdir();
                    press_backspace_to_back();
                }
                else if(rkey == KeyEvent.VK_X) {
                    clearConsole();
                    test_menu();
                    press_backspace_to_back();
                }
                else if(rkey == KeyEvent.VK_C) {
                    clearConsole();
                    transfer_menu();
                    press_backspace_to_back();
                }
                keypr = 0;
            }
            Thread.sleep(20);
        }
    }

    public void graph_generate_menu() {
        System.out.println("Graph Generate Menu");
        boolean flag = true;
        int verticle = 0, node;
        Graph g = null;

        while (flag) {
            System.out.println("Number of verticles:");
            verticle = Integer.parseInt(cn.readLine());  
            int[] nodes = new int[verticle];
            for (int i = 0; i < verticle; i++) {
                System.out.println("Enter degree for vertex " + (char)('A' + i) + ":");
                node = Integer.parseInt(cn.readLine());  
                nodes[i] = node;
            }
            
            g = new Graph(verticle, nodes);

            if (g.generateGraph()) {
                clearConsole();
                flag = false;
            } else {
                clearConsole();
                System.out.println("Invalid degree sequence. Please try again.");
            }
        }

        matrix = g.getRelationMatrix();
        main_graph = new board();
        seconday_graph = new board();
        drawing_graph dg = new drawing_graph();
        main_graph = dg.draw_graph(matrix, main_graph, verticle);
        seconday_graph = dg.draw_graph(matrix, seconday_graph, verticle);
    }

    public void test_menu() throws Exception {
        main_graph.tabloyuYazdir();
        System.out.println("Graph Test Menu");
        System.out.println("1. Connected?");
        System.out.println("2. Contains C3?");
        System.out.println("3. Isolated vertices?");
        System.out.println("4. Complete graph (Kn)?");
        System.out.println("5. Bipartite?");
        System.out.println("6. Complete bipartite (Km,n)?");
        System.out.println("7. Cycle graph (Cn)?");
        System.out.println("8. Wheel graph (Wn)?");
        System.out.println("9. Star graph (Sn)?");
        System.out.println("10. Isomorphic? (main and secondary)");
        
        System.out.print("Enter test number (1-10): ");
        int testNum = Integer.parseInt(cn.readLine());
        
        switch(testNum) {
            case 1: 
                System.out.println(isGraphConnected(matrix) ? "Yes" : "No");
                break;
            case 2: 
                System.out.println(containsC3(matrix) ? "Yes" : "No");
                break;
            case 3: 
                String isolated = findIsolatedVertices(matrix);
                System.out.println(isolated.isEmpty() ? "No isolated vertices" : isolated);
                break;
            case 4: 
                System.out.println(isCompleteGraph(matrix) ? "Yes" : "No");
                break;
            case 5: 
                System.out.println(checkBipartite(matrix));
                break;
            case 6: 
                System.out.println(checkCompleteBipartite(matrix));
                break;
            case 7: 
                System.out.println(isCycleGraph(matrix) ? "Yes" : "No");
                break;
            case 8: 
                String wheelCenter = checkWheelGraph(matrix);
                System.out.println(wheelCenter.isEmpty() ? "No" : "Yes. Center: " + wheelCenter);
                break;
            case 9: 
                String starCenter = checkStarGraph(matrix);
                System.out.println(starCenter.isEmpty() ? "No" : "Yes. Center: " + starCenter);
                break;
            case 10: 
                if(seconday_graph == null) {
                    System.out.println("Secondary graph not created!");
                } else {
                    System.out.println(checkIsomorphic(matrix, seconday_graph.getTablo()) ? "Yes" : "No");
                }
                break;
        }
        press_backspace_to_back();
    }

    public void transfer_menu() throws Exception {
        System.out.println("Choose one operation:");
        System.out.println("1. Copy main graph to secondary graph (key: G)");
        System.out.println("2. Copy secondary graph to main graph (key: H)");
        System.out.println("3. Load a graph file (\"graph1.txt\") to main graph (key: L)");
        System.out.println("4. Save main graph to a file (\"graph1.txt\") (key: S)");
        System.out.println("5. Copy main graph to a depot graph (1-9) (Keys: QWE RTY UIO)");
        System.out.println("6. Copy a depot graph to main graph (Keys: 123 456 789)");
        
        while (true) {
            if (keypr == 1) {
                if (rkey == KeyEvent.VK_G) {
                    seconday_graph = new board();
                    copyBoard(main_graph, seconday_graph);
                    System.out.println("Copied main graph to secondary graph");
                    break;
                }
                else if(rkey == KeyEvent.VK_H) {
                    main_graph = new board();
                    copyBoard(seconday_graph, main_graph);
                    System.out.println("Copied secondary graph to main graph");
                    break;
                }
                else if(rkey == KeyEvent.VK_L) {
                    loadGraphFromFile("graph1.txt", true);
                    System.out.println("Graph loaded from file");
                    break;
                }
                else if(rkey == KeyEvent.VK_S) {
                    saveGraphToFile("graph1.txt", main_graph);
                    System.out.println("Graph saved to file");
                    break;
                }
                else if(rkey == KeyEvent.VK_Q || rkey == KeyEvent.VK_W || rkey == KeyEvent.VK_E || 
                        rkey == KeyEvent.VK_R || rkey == KeyEvent.VK_T || rkey == KeyEvent.VK_Y ||
                        rkey == KeyEvent.VK_U || rkey == KeyEvent.VK_I || rkey == KeyEvent.VK_O) {
                    int depotNum = getDepotNumber(rkey);
                    depotGraphs[depotNum-1] = new board();
                    copyBoard(main_graph, depotGraphs[depotNum-1]);
                    System.out.println("Copied to depot " + depotNum);
                    break;
                }
                else if(rkey >= KeyEvent.VK_1 && rkey <= KeyEvent.VK_9) {
                    int depotNum = Character.getNumericValue((char)rkey);
                    if(depotGraphs[depotNum-1] != null) {
                        main_graph = new board();
                        copyBoard(depotGraphs[depotNum-1], main_graph);
                        System.out.println("Loaded from depot " + depotNum);
                    } else {
                        System.out.println("Depot " + depotNum + " is empty!");
                    }
                    break;
                }
                keypr = 0;
            }
            Thread.sleep(20);
        }
        press_backspace_to_back();
    }

    private void copyBoard(board source, board target) {
        int[][] sourceTablo = source.getTablo();
        int[][] targetTablo = target.getTablo();
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 37; j++) {
                targetTablo[i][j] = sourceTablo[i][j];
            }
        }
    }

    private int getDepotNumber(int keyCode) {
        switch(keyCode) {
            case KeyEvent.VK_Q: return 1;
            case KeyEvent.VK_W: return 2;
            case KeyEvent.VK_E: return 3;
            case KeyEvent.VK_R: return 4;
            case KeyEvent.VK_T: return 5;
            case KeyEvent.VK_Y: return 6;
            case KeyEvent.VK_U: return 7;
            case KeyEvent.VK_I: return 8;
            case KeyEvent.VK_O: return 9;
            default: return 0;
        }
    }

    private boolean isGraphConnected(int[][] matrix) {
        boolean[] visited = new boolean[matrix.length];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0] = true;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[current][i] == 1 && !visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }
        
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    private boolean containsC3(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == 1) {
                    for (int k = 0; k < matrix.length; k++) {
                        if (matrix[j][k] == 1 && matrix[k][i] == 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String findIsolatedVertices(int[][] matrix) {
        StringBuilder isolated = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            boolean hasEdge = false;
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == 1) {
                    hasEdge = true;
                    break;
                }
            }
            if (!hasEdge) {
                isolated.append((char)('A' + i)).append(",");
            }
        }
        return isolated.length() > 0 ? isolated.substring(0, isolated.length()-1) : "";
    }

    private boolean isCompleteGraph(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i != j && matrix[i][j] != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private String checkBipartite(int[][] matrix) {
        int[] color = new int[matrix.length];
        Arrays.fill(color, -1);
        Queue<Integer> queue = new LinkedList<>();
        
        for (int i = 0; i < matrix.length; i++) {
            if (color[i] == -1) {
                color[i] = 1;
                queue.add(i);
                
                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    for (int v = 0; v < matrix.length; v++) {
                        if (matrix[u][v] == 1) {
                            if (color[v] == -1) {
                                color[v] = 1 - color[u];
                                queue.add(v);
                            } else if (color[v] == color[u]) {
                                return "No";
                            }
                        }
                    }
                }
            }
        }
        
        StringBuilder v1 = new StringBuilder(), v2 = new StringBuilder();
        for (int i = 0; i < color.length; i++) {
            if (color[i] == 1) v1.append((char)('A' + i)).append(",");
            else v2.append((char)('A' + i)).append(",");
        }
        return "Yes. V1=" + v1.substring(0, v1.length()-1) + " V2=" + v2.substring(0, v2.length()-1);
    }

    private String checkCompleteBipartite(int[][] matrix) {
        return "Not implemented yet";
    }

    private boolean isCycleGraph(int[][] matrix) {
        return false;
    }

    private String checkWheelGraph(int[][] matrix) {
        return "";
    }

    private String checkStarGraph(int[][] matrix) {
        return "";
    }

    private boolean checkIsomorphic(int[][] matrix1, int[][] matrix2) {
        return false;
    }

    private void loadGraphFromFile(String filename, boolean isMain) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private void saveGraphToFile(String filename, board graph) {
        try (FileWriter writer = new FileWriter(filename)) {
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void main_menu() {
        cn.getTextWindow().setCursorPosition(0, 0);
        System.out.println("Menu");
        System.out.println("There are 3 menus.");
        System.out.println("1.Graph Generation Menu(key: z)");
        System.out.println("2.Graph Test Menu(key: x)");
        System.out.println("3.Graph Transfer Menu(key: c)");
    }

    public void press_backspace_to_back() throws Exception {
        while (true) {
            if (keypr == 1) {
                if (rkey == KeyEvent.VK_BACK_SPACE) {
                    cn.getTextWindow().setCursorPosition(0, 0);
                    clearConsole(); 
                    main_menu(); 
                    break;
                }
                keypr = 0;
            }
            Thread.sleep(20);
        }
    }

    public void clearConsole() {
        int rows = cn.getTextWindow().getRows();
        int columns = cn.getTextWindow().getColumns();
        cn.getTextWindow().setCursorPosition(0, 0);
        for (int i = 0; i < rows; i++) {
            cn.getTextWindow().setCursorPosition(0, i); 
            for (int j = 0; j < columns; j++) {
                cn.getTextWindow().output(" ");
            }
        }
        cn.getTextWindow().setCursorPosition(0, 0);
    }

    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        new Enigma_Terminal();
    }
}