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

    public void addEdge(int v, int w) {
        addEdge(v, w, 0);
    }

    public void addEdge(int v, int w, int weigth) {
        Edge e = new Edge(v, w, weigth);
        adjacencyLists[v].push(e);
        adjacencyLists[w].push(e);
    }

    public LinkedList<Edge> adjacent(int v) {
        return adjacencyLists[v];
    }

    public int V() {
        return adjacencyLists.length;
    }
}


