package graph;

import graph.Stockage.StockageType;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by francoisquellec on 22.03.17.
 */
public abstract class Graph {
    protected LinkedList <Vertex> vertexList = new LinkedList<>();
    protected StockageType stockage;
    protected int E;
    protected int V;


    // Constructeur par defaut
    public Graph(String filename, StockageType stockage) {
        try {
            // On cree un scanner pour lire notre fichier
            Scanner scanner = new Scanner(new File(filename));
            String line;
            int weightedGraph = 0;

            // On lit la premiere ligne pour connaitre les dimensions de notre graphe
            if (scanner.hasNextLine()) {
                V = scanner.nextInt();
                E = scanner.nextInt();
                weightedGraph = scanner.nextInt();
            }

            System.out.println("nbNode = " + V + "\nnbEdges = " + E + ", weigthed : " + weightedGraph);

            initList(V);

            this.stockage = stockage;
            stockage.init(V);

            //  On lit les lignes suivante pour stocker le graphe

            for (int i = 0; i < E; i++) {
                Vertex node1 = vertexList.get(scanner.nextInt());
                Vertex node2 = vertexList.get(scanner.nextInt());

                int weigth = 0;
                if (weightedGraph != 0) {
                    weigth = scanner.nextInt();
                }

                //System.out.println("addEdge = v1: " + node1 + ", v2: " + node2 + ", w: " + weigth);
                addEdge(node1, node2, weigth);
            }

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    // Constructeur d'initialisation
    public Graph(int V){
        initList(V);
    }

    protected void initList(int V) {
        for (int i = 0; i < V; i++) {
            System.out.print(i);
            vertexList.add(new Vertex(i));
        }
    }

    public void addVertex(Vertex v){
        vertexList.add(v.getId(), v);
        V++;
    }

    public Vertex getVertex(int id){
        return vertexList.get(id);
    }

    public Edge getEdge(Vertex v, Vertex w){
        return stockage.getEdge(v, w);
    }

    public int V(){
     return V;
    }


    public  LinkedList<Vertex> adjacentVertex(Vertex v){return stockage.adjacentVertex(v);}
    public  LinkedList<Edge> adjacentEdges(Vertex v){return stockage.adjacentEdges(v);}

    public abstract void addEdge(Vertex v, Vertex w);
    public abstract void addEdge(Vertex v, Vertex w, int weigth);

    public void print(){
        System.out.println(this.getClass());
        for(Vertex v : vertexList){
            System.out.print("Sommet " + v.getId() + " : ");
            for(Edge e : adjacentEdges(v))
                System.out.print("Edge(v1: " + e.getFrom().getId() + ", v2: " + e.getTo().getId() + ", Weigth : " + e.getWeigth()+"); ");
            System.out.println();
        }
    }

    public LinkedList<Edge> getEdgesList(){
        return stockage.getEdgesList();
    }
}
