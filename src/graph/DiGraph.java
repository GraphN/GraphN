package graph;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 22.03.17.
 */
public class DiGraph extends GraphCommon{

    public DiGraph(int V) {
        super(V);
    }

    public DiGraph(String filename) {
     super(filename);

    }

    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    public void addEdge(Vertex v, Vertex w, int weigth) {
        Edge e = new Edge(v, w, weigth);
        adjacencyLists[v.getId()].push(e);
    }

    public LinkedList<Edge> adjacent(Vertex v) {
        return adjacencyLists[v.getId()];
    }

    public int V() {
        return adjacencyLists.length;
    }
    

}
