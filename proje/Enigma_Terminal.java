package proje;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.TextAttributes;
import java.awt.Color;

public class Enigma_Terminal {
   public enigma.console.Console cn = Enigma.getConsole("Mouse and Keyboard");
   public TextMouseListener tmlis; 
   public KeyListener klis; 
   public int[][] matrix;
   public board main_graph=null;
   public board seconday_graph=null;
   // ------ Standard variables for mouse and keyboard ------
   public int mousepr;          // mouse pressed?
   public int mousex, mousey;   // mouse text coords.
   public int keypr;            // key pressed?
   public int rkey;             // key   (for press/release)
   // ----------------------------------------------------
   
   Enigma_Terminal() throws Exception {   // --- Constructor
      // ------ Standard code for mouse and keyboard ------
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
      // ----------------------------------------------------
      
      int px = 5, py = 5, node = 0;
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
    	        if(rkey==KeyEvent.VK_X) {
    	        	clearConsole();
    	        	test_menu();
    	        	press_backspace_to_back();
    	        }
    	        if (rkey == KeyEvent.VK_C) {
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
	    int i;
	    Graph g = null;

	    while (flag) {
	        System.out.println("Number of verticles:");
	        int[] nodes;
	        verticle = 0;
	        verticle = Integer.parseInt(cn.readLine());  
	        nodes = new int[verticle];
	        for (i = 0; i < verticle; i++) {
	            node = Integer.parseInt(cn.readLine());  
	            nodes[i] = node;
	        }
	        i = 0;
	        g = new Graph(verticle, nodes);

	        if (g.generateGraph()) {
	            clearConsole();
	            flag = false;
	        } else {
	            clearConsole();
	            System.out.println("Write Again");
	        }
	    }

	    matrix = g.getRelationMatrix();
	    main_graph = new board();
	    seconday_graph = new board();
	    drawing_graph dg = new drawing_graph();
	    main_graph = dg.draw_graph(matrix, main_graph, verticle);
	    seconday_graph = dg.draw_graph(matrix, seconday_graph, verticle);
	}
   public void test_menu() throws Exception{
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
   }

   public void transfer_menu() throws Exception{
	    System.out.println("Choose one operation:");
	    System.out.println("1. Copy main graph to secondary graph(key: g).");
	    System.out.println("2. Copy secondary graph to main graph  (Key: H)");
	    System.out.println("3. Load a graph file (\"graph1.txt\") to main graph (Key: L)");
	    System.out.println("4. Save main graph to a file (\"graph1.txt\")          (Key: S)");
	    System.out.println("5. Copy main graph to a depot graph (1-9) (Keys: QWE RTY UIO)");
	    System.out.println("6. Copy a depot graph to main graph         (Keys: 123  456  789)");
	     while (true) {
	         if (keypr == 1) {
	            if (rkey == KeyEvent.VK_G) {
	               clearConsole();
	               seconday_graph=main_graph;
	               System.out.println("Copied...");
	               break;
	            }
	            if(rkey== KeyEvent.VK_H) {
	            	clearConsole();
	            	main_graph=seconday_graph;
	            	System.out.println("Copied...");
		            break;
	            }
	            keypr = 0;
	         }
	         Thread.sleep(20);
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


}
