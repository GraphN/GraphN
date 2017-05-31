package graph.Stockage;

import graph.Edge;
import graph.Vertex;

import java.util.LinkedList;

/**
 * This class allow the user to use adjacency list stockage with graph
 */
public class AdjacencyStockage implements StockageType {
    private LinkedList<Edge>[] adjacencyEdgeLists;

    /**
     * Initialise the stockage components with a number of vertex
     * @param V the number of vertex to store in the graph
     */
    public void init(int V){
        adjacencyEdgeLists = new LinkedList[V];
        for(int i = 0; i < V; i++) {
            adjacencyEdgeLists[i] = new LinkedList<>();
        }
    }

    /**
     * Store an edge in the stockage structure
     * @param e the edge to store
     */
    public void addEdge(Edge e){
        addEdge(e.getTo(), e.getFrom(), e.getWeigth());
    }

    /**
     * Create and Store an edge in the stockage structure with a default weight of 0
     * @param v the starting vertex of the edge
     * @param w the ending vertex of the edge
     */
    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    /**
     * Create and Store an weigthed edge in the stockage structure
     * @param v the starting vertex of the edge
     * @param w the ending vertex of the edge
     * @param weigth the weight of the edge
     */
    public void addEdge(Vertex v, Vertex w, double weigth) {
        Edge e = new Edge(v, w, weigth);
        adjacencyEdgeLists[v.getId()].push(e);
    }

    /**
     * Get the first edge that start at v and end at w
     * @param from the starting point of the edge
     * @param to the ending vertex of the edge
     * @return the edge find or null
     */
    public Edge getEdge(Vertex from, Vertex to){
        for(LinkedList<Edge> l : adjacencyEdgeLists)
            for(Edge e : l)
                if(e.getFrom().equals(from) && e.getTo().equals(to))
                    return e;
        return null;
    }

    /**
     * Get the adjacent edges of v
     * @param v the vertex to check
     * @return the adjacent edges of v
     */
    public LinkedList<Edge> adjacentEdges(Vertex v) {
        return adjacencyEdgeLists[v.getId()];
    }

    /**
     * Get the adjacent vertex of v
     * @param v the vertex to check
     * @return the adjacent vertex of v
     */
    public LinkedList<Vertex> adjacentVertex(Vertex v){
        LinkedList<Vertex> ret = new LinkedList<>();
        for(Edge e : adjacencyEdgeLists[v.getId()]){
            ret.add(e.getOther(v));
        }
        return ret;
    }

    /**
     * Get the list of all the edges store in the stockage structure
     * @return the list of all the edges store in the stockage structure
     */
    public LinkedList<Edge> getEdgesList(){
        LinkedList<Edge> ret = new LinkedList<>();
        for (LinkedList<Edge> l : adjacencyEdgeLists)
            for (Edge e : l)
                if(!ret.contains(e))
                    ret.add(e);
        return ret;
    }

    /**
     * @return the number of vertex of the graph
     */
    public int V() {
        return adjacencyEdgeLists.length;
    }
}
