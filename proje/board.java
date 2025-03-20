import java.util.Random;

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

    public void tabloyuYazdir() {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 37; j++) {
                if (tablo[i][j] > 64) {
                    System.out.print((char) tablo[i][j] + " ");
                } else {
                    System.out.print(tablo[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    
      
    

    public int[][] getTablo() {
        return tablo;
    }

    public void random_node_place(int count_of_node, int[][] board, int count) {
        int i = 0, j = 0;
        Random rand = new Random();
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
