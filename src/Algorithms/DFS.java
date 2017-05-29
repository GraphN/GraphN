package Algorithms;

import Algorithms.Utils.Step;
import Algorithms.Utils.VertexVisit;
import graph.*;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class DFS implements AlgorithmVisitor{
    private Vector<Boolean> marked;
    private LinkedList<Step> path;
    private LinkedList<Vertex> listLastVisitedVertex;

    private void dfs(Graph g, Vertex v) {
        if(!listLastVisitedVertex.isEmpty()) {
            String message = "On selectionne le sommet " + v.getId();
            String structures = "last visited vertex : " + listLastVisitedVertex.getLast().getId()
                                + "\nmarked : " + marked.toString();
            //System.out.println("Add step : " +lastVisitedVertex.getId() + v.getId());
            Edge e = null;
            while (!listLastVisitedVertex.isEmpty() && (e = g.getEdge(listLastVisitedVertex.removeLast(), v)) == null);
            Step step = new Step();
            step.setMessage(message);
            step.setStructures(structures);
            if (e != null) {
                step.setEdge(e);
                listLastVisitedVertex.addLast(e.getFrom());
            }
            step.setVertex(v);
            path.add(step);
        }

        listLastVisitedVertex.addLast(v);

        marked.set(v.getId(), true);

        for (Vertex w : g.adjacentVertex(v))
            if (!marked.get(w.getId()))
                dfs(g, w);
    }

    private LinkedList<Step> visit(Graph g, Vertex source){
        listLastVisitedVertex = new LinkedList<>();
        marked = new Vector<>(g.V());
        path = new LinkedList<>();

        for(int i  = 0; i < g.V(); i++)
            marked.add(false);

        dfs(g, source);

        return path;
    }

    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        return visit(g, source);
    }

    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        return visit(g, source);
    }

}
