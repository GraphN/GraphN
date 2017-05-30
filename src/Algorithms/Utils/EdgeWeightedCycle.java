package Algorithms.Utils;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.LinkedList;

/**
 * Cycle detector on a graph
 */
public class EdgeWeightedCycle {
    private boolean[] marked;
    private Edge[] edgeTo;
    private boolean[] onStack;
    private LinkedList<Edge> cycle;

    /**
     * Determines whether the edge-weighted digraph {@code G} has a directed cycle and,
     * if so, finds such a cycle.
     * @param G the graph to check
     */
    public EdgeWeightedCycle(Graph G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new Edge[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v])
                dfs(G, G.getVertex(v));
        }
    }

    /**
     * Run a dfs on a graph g to find if the graph is cyclic
     * @param G the graph to compute
     * @param v the starting vertex
     */
    private void dfs(Graph G, Vertex v) {
        onStack[v.getId()] = true;
        marked[v.getId()] = true;
        for (Edge e : G.adjacentEdges(v)) {
            int w = e.getTo().getId();

            // short circuit if directed cycle found
            if (cycle != null) return;

                // found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = e;
                dfs(G, G.getVertex(w));
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

        onStack[v.getId()] = false;
    }

    /**
     * Does the edge-weighted digraph have a directed cycle?
     * @return true if the graph has a directed cycle,
     * false otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * Returns a directed cycle if the graph has a directed cycle,
     * and null otherwise.
     * @return a directed cycle if the graph
     *    has a directed cycle, and null otherwise
     */
    public LinkedList<Edge> cycle() {
        return cycle;
    }
}