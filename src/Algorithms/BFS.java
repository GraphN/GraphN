package Algorithms;

import Algorithms.Utils.Step;
import Algorithms.Utils.VertexVisit;
import graph.*;

import java.util.LinkedList;
import java.util.Vector;
/**
 * Created by francoisquellec on 24.03.17.
 */
public class BFS implements AlgorithmVisitor{
    private Vector<Integer> parent;
    private LinkedList<Step> path;

    private LinkedList<Step> bfs(Graph g, Vertex v) {
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

            if(g.getEdge(g.getVertex(parent.get(current.getId())), current) != null) {
                String message = "On selectionne le sommet " + current.getId();
                String structures = "parent : " + parent.toString();
                Edge e = g.getEdge(g.getVertex(parent.get(current.getId())), current);

                Step step = new Step();
                step.setMessage(message);
                step.setStructures(structures);
                step.setEdge(e);
                step.setVertex(current);

                path.add(step);
            }

        }while (!pile.isEmpty());

        return path;
    }

    private LinkedList<Step> visit(Graph g, Vertex source){
        parent = new Vector<>(g.V());
        path = new LinkedList<>();
        for(int i  = 0; i < g.V(); i++)
            parent.add(-1);
        return bfs(g, source);
    }

    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        return visit(g, source);
    }

    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        return visit(g, source);
    }
}
