package proje;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class drawing_graph {
	public static int penalty=0;
	private int verticle=0;
    public static int[] relatecheck(int size,int[][] arr){
        int row=65,column=65;
        int[] reduced_relation;
        reduced_relation= new int[size*size];
        int c=0;
        for(int i=0;i<size*size;i++) reduced_relation[i]=0;
        for(int a=0;a<size;a++){
            column=65;
            for(int b=0;b<size;b++){
                if(arr[a][b]==1){
                    if(is_included(reduced_relation,size,row,column)==true){}
                    else{
                        reduced_relation[c]=row;
                        reduced_relation[c+1]=column;
                        c+=2;
                    }
                }
                column++;
            }
            row++;
        }
        return reduced_relation;
    }
    public static boolean is_included(int arr[],int size,int row,int column){
        int number=0;
        while((number<size*size)&&arr[number]!=0){
            if(arr[number]==column&&arr[number+1]==row) return true;
            else{number++;}
        }
        return false;
    }
    public static int[] location_finder(int size,int[][] board){
        int[] locations;
        locations= new int[size*2];
        int ascii=65;
        int x=0;
        for(int i=0;i<size;i++){
            for (int a = 0; a < 25; a++) {
                for (int j = 0; j < 37; j++) {
                    if(board[a][j]==ascii){
                        locations[x]=a;
                        locations[x+1]=j;
                        x+=2;
                        ascii++;
                    }
                }
            }
        }
        return locations;
    }
    public static void fill_line(int[][] board, int starting_x, int starting_y, int target_x, int target_y,int type) {
        while (starting_y != target_y) {
            if(type==1){//left
                starting_y--;
                if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
                else if(board[starting_x][starting_y]>64&&starting_y!=target_y) {
                	penalty+=1000;
                }
            }
            else{//right
                starting_y++;
                if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
                else if(board[starting_x][starting_y]>64&&starting_y!=target_y) {
                	penalty+=1000;
                }
            }
        }
    }
    public static void fill_up(int[][] board, int starting_x, int starting_y, int target_x, int target_y,int type){
        while(starting_x!=target_x){
            if(type==1){//down
                starting_x++;
                if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
                else if(board[starting_x][starting_y]>64&&starting_x!=target_x) {
                	penalty+=1000;
                }
            }
            else{//up
                starting_x--;
                if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
                else if(board[starting_x][starting_y]>64&&starting_x!=target_x) {
                	penalty+=1000;
                }
            }
        }
    }
    
    public static void fill_diagonal(int[][] board, int starting_x, int starting_y, int target_x, int target_y, int type) {
        //type 1 sağ üst 2 sol üst 3 sol alt 4 sağ alt
        int type2;
       while(starting_x!=target_x&&starting_y!=target_y){
        if(type==1){
            starting_x--;
            starting_y++;
            if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
            else if(board[starting_x][starting_y]>64&&!(starting_y==target_y&&starting_x!=target_x)) {
            	penalty+=1000;
            }
        }
        else if(type==2){
            starting_x--;
            starting_y--;
            if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
            else if(board[starting_x][starting_y]>64&&!(starting_y!=target_y&&starting_x!=target_x)) {
            	penalty+=1000;
            }
        }
        else if(type==3){
            starting_x++;
            starting_y--;
            if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
            else if(board[starting_x][starting_y]>64&&!(starting_y!=target_y&&starting_x!=target_x)) {
            	penalty+=1000;
            }
        }
        else{
            starting_x++;
            starting_y++;
            if(board[starting_x][starting_y]<65) {board[starting_x][starting_y]++;if(board[starting_x][starting_y]>1)penalty++;}
            else if(board[starting_x][starting_y]>64&&!(starting_y!=target_y&&starting_x!=target_x)) {
            	penalty+=1000;
            }
        }
       }
       if(starting_x==target_x){
        if(starting_y<target_y){
            type2=2;//sağ
            fill_line(board, starting_x, starting_y, target_x, target_y, type2);
            //sağa doldurma fonksiyonunu yaz
        }
        else{
            type2=1;//sol
            fill_line(board, starting_x, starting_y, target_x, target_y, type2);
        }
       }
       else if(starting_y==target_y){
        if(starting_x<target_x){//down
            type2=1;
            fill_up(board, starting_x, starting_y, target_x, target_y, type2);
        }
        else{//up
            type2=2;
            fill_up(board, starting_x, starting_y, target_x, target_y, type2);
        }
       }

    }
    
    public static void shortest_way_finder(int[][] board, int[] relation, int[] locations) {
        int i = 0;
        penalty=0;
        while (i < relation.length && relation[i] != 0) {
            int ascii = 65;
            int first_number = relation[i] - ascii;
            int second_number = relation[i + 1] - ascii;
    
            if (first_number < 0 || second_number < 0 || first_number * 2 + 1 >= locations.length || second_number * 2 + 1 >= locations.length) {
                return;
            }
    
            int first_number_x_coordinate = locations[first_number * 2];
            int first_number_y_coordinate = locations[first_number * 2 + 1];
            int second_number_x_coordinate = locations[second_number * 2];
            int second_number_y_coordinate = locations[second_number * 2 + 1];
    
            int begining_x, begining_y, target_x, target_y,type;
            begining_x=first_number_x_coordinate;
            begining_y=first_number_y_coordinate;
            target_x=second_number_x_coordinate;
            target_y=second_number_y_coordinate;
            if(begining_x<target_x){//down
                if(begining_y==target_y){//hemen altta
                    //deneysel
                    type=0;
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
                else if(begining_y<target_y){//sağ altta diagonal
                    type=4;
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
                else{//sol altta diagonal
                    type=3;
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
            }
            else if(begining_x>target_x){//up
                if(begining_y==target_y){//hemen üstte
                    //deneysel
                    type=0;
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
                else if(begining_y<target_y){//sağ üstte diagonal
                    type=1;
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
                else{//sol üstte
                    type=2;
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
            }
            else{// aynı satırdalarsa
                if(begining_y<target_y){//sağda
                    //deneysel
                    type=0;
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
                else{//solda
                    type=0;
                    //deneysel
                    fill_diagonal(board, begining_x, begining_y, target_x, target_y, type);
                }
            }
            
            i += 2;
        }
    }
    public board draw_graph(int[][] relation,board t,int size) {
    	int[] reduced_relation,locations;
    	verticle=size;
    	reduced_relation=relatecheck(size,relation);
    	t.random_node_place(size, t.getTablo(), 0);
    	locations=location_finder(size, t.getTablo());
    	shortest_way_finder(t.getTablo(), reduced_relation, locations);
    	return t;
    }
    public int get_penalty() {
    	return penalty;
    }
    

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
        int size = nodeCount;
        int[][] relation_matrix = graph.getRelationMatrix();
        int[] reduced_relation;
        reduced_relation=relatecheck(size,relation_matrix);
        board t = new board();
        t.random_node_place(size, t.getTablo(), 0);
        int[] locations;
        locations=location_finder(size, t.getTablo());
        shortest_way_finder(t.getTablo(),reduced_relation,locations);
        t.tabloyuYazdir(0);
        graph.printRelationMatrix();
    }
}
