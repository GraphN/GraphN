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
        adjacencyEdgeLists[v.getId()].push(e);
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
}
