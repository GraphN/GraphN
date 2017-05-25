package Algorithms;

import Algorithms.Utils.EdgeWeightedCycle;
import Algorithms.Utils.Step;
import graph.DiGraph;
import graph.Edge;
import graph.Graph;
import graph.Stockage.EdgeListStockage;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Bellman_Ford implements Algorithm{
    private LinkedList<Step> path = new LinkedList<>();
    public LinkedList<Step> getPath(){
        return path;
    }
    private double[] distTo;               // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;         // edgeTo[v] = last edge on shortest s->v path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private LinkedList<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private LinkedList<Edge> cycle;  // negative cycle (or null if no such cycle)
    private Graph G;

    public Bellman_Ford(Graph G, int s) {
        this.G = G;
        distTo  = new double[G.V()];
        edgeTo  = new Edge[G.V()];
        onQueue = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // Bellman-Ford algorithm
        queue = new LinkedList<>();
        queue.addLast(s);
        onQueue[s] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.removeLast();
            onQueue[v] = false;
            relax(G, v);

            if(edgeTo[v] != null) {
                String message = "On selectionne le sommet " + v + "\n\n\n";
                String structures = "distTo : " + distTo.toString()
                        + "\nedgeTo : " + edgeTo.toString()
                        + "\nonQueue : " + onQueue.toString()
                        + "\nqueue : " + queue.toString();

                Edge e = edgeTo[v];

                Step step = new Step(Step.TYPE.EDGE);
                step.setMessage(message);
                step.setStructures(structures);
                step.setEdge(e);

                path.add(step);
            }
        }

        check(G, s);
    }

    private void relax(Graph G, int v) {
        for (Edge e : G.adjacentEdges(G.getVertex(v))) {
            int w = e.getTo().getId();
            if (distTo[w] > distTo[v] + e.getWeigth()) {
                distTo[w] = distTo[v] + e.getWeigth();
                edgeTo[w] = e;
                if (!onQueue[w]) {
                    queue.addLast(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % G.V() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) return;  // found a negative cycle
            }
        }
    }


    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    public LinkedList<Edge> negativeCycle() {
        return cycle;
    }

    // by finding a cycle in predecessor graph
    private void findNegativeCycle() {
        int V = edgeTo.length;
        Graph spt = new DiGraph(V, new EdgeListStockage());
        for (int v = 0; v < V; v++)
            if (edgeTo[v] != null)
                spt.addEdge(edgeTo[v]);

        EdgeWeightedCycle finder = new EdgeWeightedCycle(spt);
        cycle = finder.cycle();
    }

    public double distTo(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public LinkedList<Edge> pathTo(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        if (!hasPathTo(v)) return null;
        LinkedList<Edge> path = new LinkedList<>();
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.getFrom().getId()]) {
            path.push(e);
        }
        return path;
    }

    private boolean check(Graph G, int s) {

        // has a negative cycle
        if (hasNegativeCycle()) {
            double weight = 0.0;
            for (Edge e : negativeCycle()) {
                weight += e.getWeigth();
            }
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = " + weight);
                return false;
            }
        }

        else {

            // check that distTo[v] and edgeTo[v] are consistent
            if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
                return false;
            }
            for (int v = 0; v < G.V(); v++) {
                if (v == s) continue;
                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] and edgeTo[] inconsistent");
                    return false;
                }
            }

            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (int v = 0; v < G.V(); v++) {
                for (Edge e : G.adjacentEdges(G.getVertex(v))) {
                    int w = e.getTo().getId();
                    if (distTo[v] + e.getWeigth() < distTo[w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }

            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
            for (int w = 0; w < G.V(); w++) {
                if (edgeTo[w] == null) continue;
                Edge e = edgeTo[w];
                int v = e.getFrom().getId();
                if (w != e.getTo().getId()) return false;
                if (distTo[v] + e.getWeigth() != distTo[w]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
        }

        System.out.println("Satisfies optimality conditions");
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */