package Algorithms;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.LinkedList;
import java.util.Vector;
/**
 * Created by francoisquellec on 24.03.17.
 */
public class BFS implements Algorithm{
    private Graph g;
    private Vector<Integer> parent;
    private LinkedList<Edge> edgesPath;

    public LinkedList<Edge> getPath(){
        return edgesPath;
    }

    public BFS(Graph g){
        this.g = g;
    }


    public LinkedList<Edge> visit(Vertex v, VertexVisit f) {
        parent = new Vector<>(g.V());
        for(int i  = 0; i < g.V(); i++)
            parent.add(-1);
        return bfs(v, f);
    }

    public int parentOf(Vertex v) {
        return parent.get(v.getId());
    }

    private LinkedList<Edge> bfs(Vertex v, VertexVisit f) {
        edgesPath = new LinkedList<>();
        LinkedList<Edge> pile = new LinkedList<>();

        parent.set(v.getId(), v.getId());

        do{
            Vertex current;

            if(pile.isEmpty())
                current = v;
            else
                current = pile.removeFirst().getTo();

            for (Edge e : g.adjacentEdges(current)) {
                if (parent.get(e.getTo().getId()) == -1) {
                    parent.set(e.getTo().getId(), current.getId());
                    pile.add(e);
                }
            }

            if(g.getEdge(g.getVertex(parent.get(current.getId())), current) != null)
                edgesPath.add(g.getEdge(g.getVertex(parent.get(current.getId())), current));

            f.applyFunction(current);
        }while (!pile.isEmpty());

        return edgesPath;
    }

}
