package Algorithms;

import graph.GraphCommon;
import graph.Vertex;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class DFS {
    private GraphCommon g;
    private Vector<Boolean> marked;

    public DFS(GraphCommon g){
        this.g = g;
    }

    public void visit(Vertex v, VisitFunction f) {
        visit(v,f, new VisitFunction() {
            @Override
            public void applyFunction(Vertex v) {}
        });
    }

    // DFS en mode recursif
    void visit(Vertex v, VisitFunction fpre, VisitFunction fpost) {
        marked = new Vector<>(g.V());
        for(int i  = 0; i < g.V(); i++)
            marked.add(false);
        recursion(v, fpre, fpost);
    }

    private void recursion(Vertex v, VisitFunction fpre, VisitFunction fpost) {
        fpre.applyFunction(v);
        marked.set(v.getId(), true);

        for (Vertex w : g.adjacentVertex(v))
            if (!marked.get(w.getId()))
                recursion(w, fpre, fpost);
        fpost.applyFunction(v);
    }

}
