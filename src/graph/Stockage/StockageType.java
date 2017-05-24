package graph.Stockage;

import graph.Edge;
import graph.Vertex;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 27.04.17.
 */
public interface StockageType {
    void init(int V);
    void addEdge(Edge e);
    void addEdge(Vertex v, Vertex w);
    void addEdge(Vertex v, Vertex w, int weigth);
    Edge getEdge(Vertex v, Vertex w);
    LinkedList<Vertex> adjacentVertex(Vertex v);
    LinkedList<Edge> adjacentEdges(Vertex v);
    LinkedList<Edge> getEdgesList();

}
