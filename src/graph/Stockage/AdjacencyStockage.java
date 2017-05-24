package graph.Stockage;

import graph.Edge;
import graph.Vertex;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 27.04.17.
 */
public class AdjacencyStockage implements StockageType {
    private LinkedList<Edge>[] adjacencyEdgeLists;

    public void init(int V){
        adjacencyEdgeLists = new LinkedList[V];
        for(int i = 0; i < V; i++) {
            adjacencyEdgeLists[i] = new LinkedList<>();
        }
    }

    public void addEdge(Edge e){
        addEdge(e.getTo(), e.getFrom());
    }
    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    public void addEdge(Vertex v, Vertex w, int weigth) {
        Edge e = new Edge(v, w, weigth);
        adjacencyEdgeLists[v.getId()].push(e);
    }

    public Edge getEdge(Vertex from, Vertex to){
        for(LinkedList<Edge> l : adjacencyEdgeLists)
            for(Edge e : l)
                if(e.getFrom() == from && e.getTo() == to)
                    return e;
        return null;
    }

    public LinkedList<Edge> adjacentEdges(Vertex v) {
        return adjacencyEdgeLists[v.getId()];
    }

    public int V() {
        return adjacencyEdgeLists.length;
    }

    public LinkedList<Vertex> adjacentVertex(Vertex v){
        LinkedList<Vertex> ret = new LinkedList<>();
        for(Edge e : adjacencyEdgeLists[v.getId()]){
            ret.add(e.getOther(v));
        }
        return ret;
    }

    public LinkedList<Edge> getEdgesList(){
        LinkedList<Edge> ret = new LinkedList<>();
        for (LinkedList<Edge> l : adjacencyEdgeLists)
            for (Edge e : l)
                if(!ret.contains(e))
                    ret.add(e);
        return ret;
    }
}
