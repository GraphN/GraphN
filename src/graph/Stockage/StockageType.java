package graph.Stockage;

import graph.Edge;
import graph.Vertex;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 27.04.17.
 */
public interface StockageType {
    /**
     * Initialise the stockage components with a number of vertex
     * @param V the number of vertex to store in the graph
     */
    void init(int V);

    /**
     * Store an edge in the stockage structure
     * @param e the edge to store
     */
    void addEdge(Edge e);

    /**
     * Create and Store an edge in the stockage structure with a default weight of 0
     * @param v the starting vertex of the edge
     * @param w the ending vertex of the edge
     */
    void addEdge(Vertex v, Vertex w);

    /**
     * Create and Store an weigthed edge in the stockage structure
     * @param v the starting vertex of the edge
     * @param w the ending vertex of the edge
     * @param weigth the weight of the edge
     */
    void addEdge(Vertex v, Vertex w, double weigth);

    /**
     * Get the first edge that start at v and end at w
     * @param v the starting point of the edge
     * @param w the ending vertex of the edge
     * @return the edge find or null
     */
    Edge getEdge(Vertex v, Vertex w);

    /**
     * Get the adjacent vertex of v
     * @param v the vertex to check
     * @return the adjacent vertex of v
     */
    LinkedList<Vertex> adjacentVertex(Vertex v);

    /**
     * Get the adjacent edges of v
     * @param v the vertex to check
     * @return the adjacent edges of v
     */
    LinkedList<Edge> adjacentEdges(Vertex v);

    /**
     * Get the list of all the edges store in the stockage structure
     * @return the list of all the edges store in the stockage structure
     */
    LinkedList<Edge> getEdgesList();

}
