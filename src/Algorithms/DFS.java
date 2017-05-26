package Algorithms;

import Algorithms.Utils.Step;
import Algorithms.Utils.VertexVisit;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class DFS implements Algorithm{
    private Graph g;
    private Vector<Boolean> marked;
    private LinkedList<Step> path = new LinkedList<>();
    private Vertex lastVisitedVertex = null;
    private Vertex source;

    public LinkedList<Step> getPath(){
        visit(source);
        return path;
    }

    public DFS(Graph g, Vertex source){
        this.g = g;
        this.source = source;
    }

    // DFS en mode recursif
    LinkedList<Step> visit(Vertex v) {
        marked = new Vector<>(g.V());
        path = new LinkedList<>();

        for(int i  = 0; i < g.V(); i++)
            marked.add(false);

        recursion(v);

        return path;
    }

    private void recursion(Vertex v) {
        if(lastVisitedVertex != null) {
            String message = "On selectionne le sommet " + v.getId();
            String structures = "last visited vertex : " + lastVisitedVertex.getId()
                                + "\nmarked : " + marked.toString();
            //System.out.println("Add step : " +lastVisitedVertex.getId() + v.getId());
            //Edge e = g.getEdge(lastVisitedVertex, v);
            Step step = new Step();
            step.setMessage(message);
            step.setStructures(structures);
            //step.setEdge(e);
            step.setVertex(v);
            path.add(step);
        }

        lastVisitedVertex = v;

        marked.set(v.getId(), true);

        for (Vertex w : g.adjacentVertex(v))
            if (!marked.get(w.getId()))
                recursion(w);
    }

}
