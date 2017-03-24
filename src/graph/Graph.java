package graph;
        import java.util.LinkedList;
        import java.util.Scanner;

/**
 * Created by Adrian on 22.03.2017.
 */
public class Graph extends GraphCommon{

    public Graph(int V) {
        super(V);
    }

    public Graph(String filename) {
        super(filename);
    }

    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    public void addEdge(Vertex v, Vertex w, int weigth) {
        Edge e = new Edge(v, w, weigth);
        adjacencyLists[v.getId()].push(e);
        adjacencyLists[w.getId()].push(e);
    }

    public LinkedList<Edge> adjacent(Vertex v) {
        return adjacencyLists[v.getId()];
    }

    public int V() {
        return adjacencyLists.length;
    }
}


