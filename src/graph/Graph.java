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
    protected int TYPE;

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

    public void addVertex(Vertex v){
        vertexList.add(v.getId(), v);
        V++;
    }

    public Vertex getVertex(int id){
        return vertexList.get(id);
    }
    public LinkedList<Vertex> getVertexsList(){return vertexList;}

    public Edge getEdge(Vertex v, Vertex w){
        return stockage.getEdge(v, w);
    }

    public int V(){
     return V;
    }
    public int E(){
        return E;
    }
    public int getType(){
        return TYPE;
    }


    public  LinkedList<Vertex> adjacentVertex(Vertex v){return stockage.adjacentVertex(v);}
    public  LinkedList<Edge> adjacentEdges(Vertex v){return stockage.adjacentEdges(v);}

    public void addEdge(Edge e){
        stockage.addEdge(e);
        E++;
    }
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
