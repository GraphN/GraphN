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

    public LinkedList<Step> getPath(){
        visit(g.getVertex(0), new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {

            }
        });
        return path;
    }

    public DFS(Graph g){
        this.g = g;
    }

    public LinkedList<Step> visit(Vertex v, VertexVisit f) {
        visit(v,f, new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {}
        });
        return getPath();
    }

    // DFS en mode recursif
    LinkedList<Step> visit(Vertex v, VertexVisit fpre, VertexVisit fpost) {
        marked = new Vector<>(g.V());
        for(int i  = 0; i < g.V(); i++)
            marked.add(false);
        recursion(v, fpre, fpost);

        return getPath();
    }

    private void recursion(Vertex v, VertexVisit fpre, VertexVisit fpost) {
        fpre.applyFunction(v);

        if(lastVisitedVertex != null) {
            String message = "On selectionne le sommet " + v.getId();
            String structures = "last visited vertex : " + lastVisitedVertex.getId()
                                + "\nmarked : " + marked.toString();
            Edge e = g.getEdge(lastVisitedVertex, v);

            Step step = new Step(Step.TYPE.EDGE);
            step.setMessage(message);
            step.setStructures(structures);
            step.setEdge(e);

            path.add(step);
        }

        lastVisitedVertex = v;

        marked.set(v.getId(), true);

        for (Vertex w : g.adjacentVertex(v))
            if (!marked.get(w.getId()))
                recursion(w, fpre, fpost);
        fpost.applyFunction(v);
    }

}
