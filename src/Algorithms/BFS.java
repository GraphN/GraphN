package Algorithms;

import graph.GraphCommon;
import graph.Vertex;

import java.util.LinkedList;
import java.util.Vector;
/**
 * Created by francoisquellec on 24.03.17.
 */
public class BFS {
    private GraphCommon g;
    private Vector<Integer> parent;

    public BFS(GraphCommon g){
        this.g = g;
    }


    public void visit(Vertex v, VisitFunction f) {
        parent = new Vector<>(g.V());
        for(int i  = 0; i < g.V(); i++)
            parent.add(-1);
        bfs(v, f);
    }

    public int parentOf(Vertex v) {
        return parent.get(v.getId());
    }

    private void bfs(Vertex v, VisitFunction f) {
        LinkedList<Integer> pile = new LinkedList<>();
        parent.set(v.getId(), v.getId());
        pile.add(v.getId());

        while (!pile.isEmpty()) {
            int current = pile.removeFirst();

            for (Vertex w : g.adjacentVertex(g.getVertex(current))) {
                if (parent.get(w.getId()) == -1) {
                    parent.set(w.getId(), current);
                    pile.add(w.getId());
                }
            }

            f.applyFunction(g.getVertex(current));
        }
    }

}
