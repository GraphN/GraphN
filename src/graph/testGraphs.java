package graph;

import Algorithms.*;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class testGraphs {
    public static void main(String[] args){
        final String graphPath = "DataTest/col_exemple.txt";
        final String resultGraphPath = "DataTest/col_exemple_Result.txt";
        System.out.println("Construction du graph non orienté");
        GraphCommon gNO = new Graph(graphPath);
        System.out.println("---- Affichage ----");
        gNO.print();
        System.out.println("-------------------");

        System.out.println("Construction du graph orienté");
        GraphCommon gO = new DiGraph(graphPath);
        System.out.println("---- Affichage ----");
        gO.print();
        System.out.println("-------------------");


        System.out.println("Application BFS");
        System.out.println("Avant application");
        gNO.print();
        System.out.println("Apres application");
        BFS bfs = new BFS(gNO);
        bfs.visit(gNO.getVertex(0), new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {
                System.out.println(v.getId());
            }
        });


        System.out.println("Application DFS");
        System.out.println("Avant application");
        gNO.print();
        System.out.println("Apres application");
        DFS dfs = new DFS(gNO);
        dfs.visit(gNO.getVertex(0), new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {
                System.out.println(v.getId());
            }
        });


        final String graphPathW = "DataTest/col_exempleW.txt";
        final String resultGraphPathW = "DataTest/col_exemple_ResultW.txt";
        System.out.println("Construction du graph non orienté Pondére");
        Graph gWNO = new Graph(graphPathW);
        System.out.println("---- Affichage ----");
        gWNO.print();
        System.out.println("-------------------");


        System.out.println("Application Kruskall");
        System.out.println("Avant application");
        gWNO.print();
        System.out.println("Apres application");
        Kruskall kru = new Kruskall(gWNO);
        kru.visit(new EdgeVisit() {
            @Override
            public void applyFunction(Edge e) {
                System.out.println(" From : " + e.getFrom().getId() + "; To : " + e.getTo().getId());
            }
        });
        System.out.println("Poids Total : " + kru.getWeight());

    }
}
