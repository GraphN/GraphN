package Algorithms.Utils;

import graph.DiGraph;
import graph.Edge;
import graph.Graph;
import graph.Stockage.EdgeListStockage;

import java.util.LinkedList;

public class EdgeWeightedCycle {
    private boolean[] marked;             // marked[v] = has vertex v been marked?
    private Edge[] edgeTo;        // edgeTo[v] = previous edge on path to v
    private boolean[] onStack;            // onStack[v] = is vertex on the stack?
    private LinkedList<Edge> cycle;    // directed cycle (or null if no such cycle)

    /**
     * Determines whether the edge-weighted digraph {@code G} has a directed cycle and,
     * if so, finds such a cycle.
     * @param G the edge-weighted digraph
     */
    public EdgeWeightedCycle(Graph G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new Edge[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v])
                dfs(G, v);
        }

        // check that digraph has a cycle
        //assert check();
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(Graph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (Edge e : G.adjacentEdges(G.getVertex(v))) {
            int w = e.getTo().getId();

            // short circuit if directed cycle found
            if (cycle != null) return;

                // found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = e;
                dfs(G, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new LinkedList<>();

                Edge f = e;
                while (f.getFrom().getId() != w) {
                    cycle.push(f);
                    f = edgeTo[f.getFrom().getId()];
                }
                cycle.push(f);

                return;
            }
        }

        onStack[v] = false;
    }

    /**
     * Does the edge-weighted digraph have a directed cycle?
     * @return {@code true} if the edge-weighted digraph has a directed cycle,
     * {@code false} otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * Returns a directed cycle if the edge-weighted digraph has a directed cycle,
     * and {@code null} otherwise.
     * @return a directed cycle (as an iterable) if the edge-weighted digraph
     *    has a directed cycle, and {@code null} otherwise
     */
    public LinkedList<Edge> cycle() {
        return cycle;
    }


    // certify that digraph is either acyclic or has a directed cycle
    private boolean check() {

        // edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
            Edge first = null, last = null;
            for (Edge e : cycle()) {
                if (first == null) first = e;
                if (last != null) {
                    if (last.getTo() != e.getFrom()) {
                        System.err.printf("cycle edges %s and %s not incident\n", last, e);
                        return false;
                    }
                }
                last = e;
            }

            if (last.getTo() != first.getFrom()) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first);
                return false;
            }
        }


        return true;
    }
}