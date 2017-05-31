package graph;

import Algorithms.AlgorithmVisitor;
import Algorithms.Utils.Step;
import graph.Stockage.StockageType;

import java.util.LinkedList;

/**
 *  Representation of a graph
 */
public abstract class Graph {
    protected LinkedList <Vertex> vertexList = new LinkedList<>();
    protected StockageType stockage;
    protected int E; // number of edges
    protected int V; // number of vertex

    /**
     * Constructor Graph
     * @param V number of vertex
     * @param stockage type of stockage to use
     */
    public Graph(int V, StockageType stockage){
        this.E = 0;
        this.stockage = stockage;
        initList(V);
        stockage.init(V);
    }

    /**
     * initialise the vertex list
     * @param V the number of vertex of the graph
     */
    protected void initList(int V) {
        for (int i = 0; i < V; i++) {
            System.out.print(i);
            vertexList.add(new Vertex(i));
            this.V = V;
        }
    }

    /**
     * Add a vertex to the graph
     * @param v the vertex to add
     */
    public void addVertex(Vertex v){
        vertexList.add(v.getId(), v);
        V++;
    }

    /**
     * get a Vertex of the graph
     * @param id the id of the vertex
     * @return the vertex with correct id
     */
    public Vertex getVertex(int id){
        if (id < vertexList.size())
            return vertexList.get(id);
        else
            return null;
    }

    /**
     * Get the list of vertex of the graph
     * @return
     */
    public LinkedList<Vertex> getVertexsList(){return vertexList;}

    /**
     * Add an edge to the graph
     * @param e the edge to store
     */
    public void addEdge(Edge e){
        stockage.addEdge(e);
        E++;
    }

    /**
     * Add an edge with 0 weight to the graph
     * @param v the starting vertex
     * @param w the ending vertex
     */
    public abstract void addEdge(Vertex v, Vertex w);

    /**
     * Add an edge to the graph
     * @param v the starting vertex
     * @param w the ending vertex
     * @param weight the weight of the edge
     */
    public abstract void addEdge(Vertex v, Vertex w, double weight);

    /**
     * Get the first edge that start at v and end at w
     * @param v the starting point of the edge
     * @param w the ending vertex of the edge
     * @return the edge find or null
     */
    public Edge getEdge(Vertex v, Vertex w){
        return stockage.getEdge(v, w);
    }

    /**
     * Get the list of edge that compose the graph
     * @return the list of edge that compose the graph
     */
    public LinkedList<Edge> getEdgesList(){
        return stockage.getEdgesList();
    }


    /**
     * Get the adjacent vertex of v
     * @param v the vertex to check
     * @return the adjacent vertex of v
     */
    public  LinkedList<Vertex> adjacentVertex(Vertex v){return stockage.adjacentVertex(v);}

    /**
     * Get the adjacent edges of v
     * @param v the vertex to check
     * @return the adjacent edges of v
     */
    public  LinkedList<Edge> adjacentEdges(Vertex v){return stockage.adjacentEdges(v);}

    /**
     * Getter number of vertex of the graph
     * @return V
     */
    public int V(){
        return V;
    }

    /**
     * Getter number of edges of the graph
     * @return E
     */
    public int E(){
        return E;
    }

    /**
     * Apply an algorithme(AlgorithmVisitor) to the current graph
     * @param v the visitor to apply
     * @param source the source vertex
     * @param target the vertex to reach
     * @return A list of step produce by the algorithm visitor
     * @throws Exception if the algorithm cant finish his execution
     */
    public abstract LinkedList<Step> accept(AlgorithmVisitor v, Vertex source, Vertex target) throws Exception;

    /**
     * Display the graph  in a string
     * @return this graph in string
     */
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
