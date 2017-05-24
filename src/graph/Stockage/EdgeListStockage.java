package graph.Stockage;

import graph.Edge;
import graph.Vertex;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 29.04.17.
 */
public class EdgeListStockage implements StockageType {
    protected LinkedList<Edge> edgesList = new LinkedList<>();

    public void init(int V){};

    public void addEdge(Edge e){
        addEdge(e.getTo(), e.getFrom());
    }
    public void addEdge(Vertex v, Vertex w){
        addEdge(v, w, 0);
    }
    public void addEdge(Vertex v, Vertex w, int weigth){
        edgesList.add(new Edge(v, w, 0));
    }
    public Edge getEdge(Vertex from, Vertex to){
        for(Edge e : edgesList)
            if(e.getFrom() == from && e.getTo() == to)
                return e;
        return null;
    }

    public LinkedList<Vertex> adjacentVertex(Vertex v){
        LinkedList<Vertex> ret = new LinkedList<>();
        for(Edge e : edgesList)
            if(e.getFrom() == v)
                ret.add(e.getTo());
        return ret;
    }

    public  LinkedList<Edge> adjacentEdges(Vertex v){
        LinkedList<Edge> ret = new LinkedList<>();
        for(Edge e : edgesList)
            if(e.getFrom() == v)
                ret.add(e);
        return ret;
    }

    public LinkedList<Edge> getEdgesList(){
        return edgesList;
    }
}
