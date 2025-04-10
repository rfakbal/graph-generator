package proje;
import java.util.Random;
import enigma.console.TextWindow;
import enigma.console.TextWindow;
public class board {
    int[][] tablo;
    public board() {
        tablo = new int[25][37];
        int a;
        for (int i = 0; i < 25; i++) {
            a = 0;
            for (int j = 0; j < 37; j++) {
                if ((j == a) && (i % 4 == 0)) {
                    a += 4;
                    tablo[i][j] = 0;
                } else {
                    tablo[i][j] = 0;
                }
            }
        }
    }
    public void printBoardWithCursor(int startCol, int startRow, TextWindow tw) {
        for (int i = 0; i < 25; i++) {
            tw.setCursorPosition(startCol, startRow + i);
            for (int j = 0; j < 37; j++) {
                if (tablo[i][j] == 0) {
                    if ((i % 4 == 0 && j % 4 == 0) && tablo[i][j] < 65) {
                        System.out.print(".");
                    } else {
                        System.out.print(" ");
                    }
                } else {
                    if (tablo[i][j] > 64) {
                        System.out.print((char) tablo[i][j]);
                    } else {
                        System.out.print(tablo[i][j]);
                    }
                }
            }
        }
    }

    public void tabloyuYazdir(int drawing_mode) {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 37; j++) {
                if (tablo[i][j] == 0) {
                	if((i % 4== 0 && j % 4 == 0)&&tablo[i][j]<65) {
                		System.out.print(".");
                	}
                	else {
                    System.out.print(" ");}
                } else {
                    if (tablo[i][j] > 0) {
                    	if(drawing_mode==1) {
                    		if(tablo[i][j]>64) {
                    			System.out.print((char) tablo[i][j]);
                    		}
                    		else {
                    			System.out.print(tablo[i][j]);
                    		}
                    	}
                    	else if(drawing_mode==2) {
                    		if(tablo[i][j]==1) {
                    			System.out.print((char) 43);
                    		}
                    		else if(tablo[i][j]==2) {
                    			System.out.print((char) 111);
                    		}
                    		else if(tablo[i][j]==3) {
                    			System.out.print((char) 35);
                    		}
                    		else if(tablo[i][j]==4) {
                    			System.out.print((char) 64);
                    		}
                    		else if(tablo[i][j]>64&&tablo[i][j]<100){
                    			System.out.print((char)tablo[i][j]);
                    		}
                    	}
                    	else {
                    		if(tablo[i][j]>64) {
                    			System.out.print((char) tablo[i][j]);
                    		}
                    		else {
                    			System.out.print((char) 43);
                    		}
                    	}
                    } else {
                        System.out.print(tablo[i][j]);
                    }
                }
            }
            System.out.println();
        }
    }
    public int[][] getTablo() {
        return tablo;
    }


    public void random_node_place(int count_of_node, int[][] board, int count) {
    	
    	Random rand = new Random();
    	int i = rand.nextInt(25);
        int j = rand.nextInt(37);
        
        while(count_of_node>0){
        while ((i % 4 != 0 || j % 4 != 0)||board[i][j]>64) {
            i = rand.nextInt(25);
            j = rand.nextInt(37);
        }
        board[i][j]=65+count;
        count++;
        count_of_node--;
    }
    }
}
