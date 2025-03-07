public class board {
    int[][] tablo;

    public board() {
        tablo = new int[27][37];
        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 37; j++) {
                tablo[i][j] = 0;
            }
        }
    }
    public void tabloyuYazdir() {
        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 37; j++) {
                System.out.print(tablo[i][j] + " ");
            }
            System.out.println();
        }
    }
}
