package graph;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by francoisquellec on 22.03.17.
 */
public abstract class GraphCommon {
    protected LinkedList<Edge>[] adjacencyEdgeLists;
    protected Vertex[] vertexList;
    protected LinkedList<Edge> edgesList = new LinkedList<>();

    //Constructeur par defaut
    public GraphCommon(String filename){
        try {
            // On cree un scanner pour lire notre fichier
            Scanner scanner = new Scanner(new File(filename));
            String line;
            int nbNode = 0;
            int nbEdges = 0;
            int weightedGraph = 0;

            // On lit la premiere ligne pour connaitre les dimensions de notre graphe
            if (scanner.hasNextLine()) {
                nbNode = scanner.nextInt();
                nbEdges = scanner.nextInt();
                weightedGraph = scanner.nextInt();
            }

            System.out.println("nbNode = " + nbNode + "\nnbEdges = " + nbEdges + ", weigthed : " + weightedGraph);

            initList(nbNode);

            //  On lit les lignes suivante pour stocker le graphe

            for(int i = 0; i < nbEdges; i++) {
                Vertex node1 = vertexList[scanner.nextInt()];
                Vertex node2 = vertexList[scanner.nextInt()];

                int weigth = 0;
                if(weightedGraph != 0) {
                    weigth = scanner.nextInt();
                }

               // System.out.println("addEdge = v1: " + node1 + ", v2: " + node2 + ", w: " + weigth);
                addEdge(node1, node2, weigth);
            }

        }catch(IOException e){
            System.err.print(e);
        }
    }

    // Constructeur d'initialisation
    public GraphCommon(int V){
        initList(V);
    }

    protected void initList(int V){
        adjacencyEdgeLists = new LinkedList[V];
        vertexList = new Vertex[V];
        for (int i = 0; i < V; i++) {
            adjacencyEdgeLists[i] = new LinkedList<Edge>();
            vertexList[i] = new Vertex(i);
        }
    }

    // Debug Function
    public void print(){
        for(int i = 0; i < adjacencyEdgeLists.length; i++){
            System.out.print("Sommet " + i + " : ");
            for(Edge e : adjacencyEdgeLists[i])
                System.out.print("Edge(v1: " + e.getFrom().getId() + ", v2: " + e.getTo().getId() + ", Weigth : " + e.getWeigth()+"); ");
            System.out.println();
        }
    }

    public abstract void addEdge(Vertex v, Vertex w);
    public abstract void addEdge(Vertex v, Vertex w, int weigth);
    public LinkedList<Edge> adjacentEdges(Vertex v){
        return adjacencyEdgeLists[v.getId()];
    }
    public abstract LinkedList<Vertex> adjacentVertex(Vertex v);
    public Vertex getVertex(int id){
        return vertexList[id];
    }
    public abstract int V();

    public LinkedList<Edge> getEdgesList(){return edgesList;}
}
