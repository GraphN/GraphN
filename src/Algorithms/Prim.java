package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import Algorithms.Utils.UnionFind;
import graph.DiGraph;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Prim implements Algorithm{
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;

    private Graph G;
    private boolean isDirected;

    LinkedList<Step> path = new LinkedList<>();

    public void visit(){
        for (int v = 0; v < G.V(); v++) {    // run from each vertex to find
            if (!marked[v]) prim(v);      // minimum spanning forest
            if (edgeTo[v] != null) {
                String message = "On selectionne l arete " + edgeTo[v] + "\n\n";
                String structures = "distTo : " + Arrays.toString(distTo)
                        + "\nedgeTo : " + Arrays.toString(edgeTo)
                        + "\nmarked : " + Arrays.toString(marked);

                Edge e = edgeTo[v];

                Step step = new Step();
                step.setMessage(message);
                step.setStructures(structures);
                step.setEdge(e);

                path.add(step);
            }
        }

        // check optimality conditions
//        check(G);
    }
    public Prim(Graph G) {
        this.G = G;
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        isDirected = G instanceof DiGraph;
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(Graph G, int v) {
        marked[v] = true;
        for (Edge e : G.adjacentEdges(G.getVertex(v))) {
            int w = e.getOther(G.getVertex(v)).getId();
            if (marked[w]) continue;         // v-w is obsolete edge
            if (e.getWeigth() < distTo[w]) {
                distTo[w] = e.getWeigth();
                edgeTo[w] = e;

                // prepare the step
                String message = "On met à jour les poids \n\n";
                String structures = "distTo : " + Arrays.toString(distTo)
                        + "\nedgeTo : " + Arrays.toString(edgeTo)
                        + "\nPriorityQueue : " + pq.toString();


                Step step = new Step();
                step.setMessage(message);
                step.setStructures(structures);
                G.getVertex(w).setDescription(distTo[w] + "");
                step.setVertex(G.getVertex(w));

                path.add(step);

                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    public LinkedList<Step> getPath() {
        path = new LinkedList<>();
        if (isDirected){
            String message = "Prim ne peut pas être appliquer sur des graphes orientés.";
            Step step = new Step();
            step.setMessage(message);
            step.setVertex(G.getVertex(0));
            path.add(step);
        }
        else
            visit();
        return path;

    }
    /*LinkedList<Edge> mst = new LinkedList<>();
    for (int v = 0; v < edgeTo.length; v++) {
        Edge e = edgeTo[v];
        if (e != null) {
            mst.add(e);
        }
    }
    return mst;*/


    public double weight() {
        double weight = 0.0;
        for (Step e : getPath())
            weight += e.getEdge().getWeigth();
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(Graph G) {

        // check weight
        double totalWeight = 0.0;
        for (Step e : getPath()) {
            totalWeight += e.getEdge().getWeigth();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UnionFind uf = new UnionFind(G.V());
        for (Step e : getPath()) {
            int v = e.getEdge().getFrom().getId(), w = e.getEdge().getTo().getId();
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : G.getEdgesList()) {
            int v = e.getFrom().getId(), w = e.getTo().getId();
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Step e : getPath()) {

            // all edges in MST except e
            uf = new UnionFind(G.V());
            for (Step  f : getPath()) {
                int x = f.getEdge().getFrom().getId(), y = f.getEdge().getTo().getId();
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.getEdgesList()) {
                int x = f.getFrom().getId(), y = f.getTo().getId();
                if (!uf.connected(x, y)) {
                    if (f.getWeigth() < e.getEdge().getWeigth()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */
