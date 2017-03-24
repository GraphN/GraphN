package graph;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class testGraphs {
    public static void main(String[] args){
        final String graphPath = "DataTest/col_exemple.txt";
        final String resultGraphPath = "DataTest/col_exemple_Result.txt";
        System.out.println("Construction du graph non orienté");
        GraphCommon gNO = new Graph(graphPath);
        System.out.println("Avant affichage");
        gNO.print();
        System.out.println("Apres affichage");

        System.out.println("Construction du graph orienté");
        GraphCommon gO = new DiGraph(graphPath);
        System.out.println("Avant affichage");
        gO.print();
        System.out.println("Apres affichage");
    }
}
