package graph;

import Algorithms.AlgorithmVisitor;
import Algorithms.Utils.Step;
import graph.Stockage.StockageType;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 22.03.17.
 */
public abstract class Graph {
    protected LinkedList <Vertex> vertexList = new LinkedList<>();
    protected StockageType stockage;
    protected int E;
    protected int V;
    protected int TYPE;
    public static int DIRECTED = 1;

    // Constructeur d'initialisation
    public Graph(int V, StockageType stockage){
        this.E = 0;
        this.stockage = stockage;
        initList(V);
        stockage.init(V);
    }

    protected void initList(int V) {
        for (int i = 0; i < V; i++) {
            System.out.print(i);
            vertexList.add(new Vertex(i));
            this.V = V;
        }
    }

    // Getter/Setter Vertex
    public void addVertex(Vertex v){
        vertexList.add(v.getId(), v);
        V++;
    }
    public Vertex getVertex(int id){
        if (id < vertexList.size())
            return vertexList.get(id);
        else
            return null;
    }
    public LinkedList<Vertex> getVertexsList(){return vertexList;}

    // Getter/Setter Edge
    public void addEdge(Edge e){
        stockage.addEdge(e);
        E++;
    }
    public abstract void addEdge(Vertex v, Vertex w);
    public abstract void addEdge(Vertex v, Vertex w, double weigth);
    public Edge getEdge(Vertex v, Vertex w){
        return stockage.getEdge(v, w);
    }
    public LinkedList<Edge> getEdgesList(){
        return stockage.getEdgesList();
    }


    // Parcourt graphe
    public  LinkedList<Vertex> adjacentVertex(Vertex v){return stockage.adjacentVertex(v);}
    public  LinkedList<Edge> adjacentEdges(Vertex v){return stockage.adjacentEdges(v);}

    // Base infos
    public int V(){
        return V;
    }
    public int E(){
        return E;
    }
    public int getType(){
        return TYPE;
    }

    // Pattern Visitor
    public abstract LinkedList<Step> accept(AlgorithmVisitor v, Vertex source, Vertex target) throws Exception;

    // Fonctions Utilitaires
    public String toString(){
        String ret = this.getClass() + "nbVertex = " + V + "\n";
        for(Vertex v : vertexList){
            ret += "Sommet " + v.getId() + " : ";
            for(Edge e : adjacentEdges(v))
                ret += "Edge(v1: " + e.getFrom().getId() + ", v2: " + e.getTo().getId() + ", Weigth : " + e.getWeigth()+"); ";
            ret += "\n";
        }
        return ret;
    }
}
