package graph;

import Algorithms.BFS;
import Algorithms.VisitFunction;

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


        System.out.println("Application BFS");
        System.out.println("Avant application");
        gNO.print();
        System.out.println("Apres affichage");
        BFS bfs = new BFS(gNO);
        bfs.visit(gNO.getVertex(0), new VisitFunction() {
            @Override
            public void applyFunction(Vertex v) {
                System.out.println(v.getId());
            }
        });

    }
}
