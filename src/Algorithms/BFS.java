package Algorithms;

import graph.Edge;
import graph.GraphCommon;
import graph.Vertex;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;
/**
 * Created by francoisquellec on 24.03.17.
 */
public class BFS {
    private GraphCommon g;
    Vector<Vertex> parent;

    public BFS(GraphCommon g){
        this.g = g;
    }


    public void visit(Vertex v, VisitFunction f) {
        parent.ensureCapacity(g.V());
        for(Vertex i : parent)
            i.setId(-1);
        bfs(v, f);
    }

    public Vertex parentOf(Vertex v) {
        return parent.get(v);
    }

    public void bfs(Vertex v, VisitFunction f) {
        Stack<Integer> pile = new Stack<>();
        parent.insertElementAt(v, v);
        pile.push(v);

        while (!pile.empty()) {
            v = pile.firstElement();
            pile.pop();

            for (Edge w : g.adjacentEdges(v))
                if (parent.get(w.) == -1) {
                    parent[w] = v;
                    pile.push(w);
                }

            f.applyFunction(v);
        }
    }

}
