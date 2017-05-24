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
public class BFS implements Algorithm{
    private Graph g;
    private Vector<Integer> parent;
    private LinkedList<Step> path;

    public LinkedList<Step> getPath(){
        visit(g.getVertex(0), new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {

            }
        });

        return path;
    }

    public BFS(Graph g){
        this.g = g;
    }


    public LinkedList<Step> visit(Vertex v, VertexVisit f) {
        parent = new Vector<>(g.V());
        for(int i  = 0; i < g.V(); i++)
            parent.add(-1);
        return bfs(v, f);
    }

    public int parentOf(Vertex v) {
        return parent.get(v.getId());
    }

    private LinkedList<Step> bfs(Vertex v, VertexVisit f) {
        path = new LinkedList<>();
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

                Step step = new Step(Step.TYPE.EDGE);
                step.setMessage(message);
                step.setStructures(structures);
                step.setEdge(e);

                path.add(step);
            }

            f.applyFunction(current);
        }while (!pile.isEmpty());

        return path;
    }

}
