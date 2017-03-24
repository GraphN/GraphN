package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

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

    public void addEdge(int v, int w) {
        addEdge(v, w, 0);
    }

    public void addEdge(int v, int w, int weigth) {
        Edge e = new Edge(v, w, weigth);
        adjacencyLists[v].push(e);
    }

    public LinkedList<Edge> adjacent(int v) {
        return adjacencyLists[v];
    }

    public int V() {
        return adjacencyLists.length;
    }

    public DiGraph reverse()  {
        DiGraph dg = new DiGraph(V());
        for(int v=0;v<V();++v)
            for(Edge e : adjacent(v))
                dg.addEdge(e.getV2(),e.getV1());
        return dg;
    }

}
