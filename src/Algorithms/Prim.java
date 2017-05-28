package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import graph.DiGraph;
import graph.Edge;
import graph.Graph;

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

    private Graph g;
    private boolean isDirected;

    LinkedList<Step> path = new LinkedList<>();

    public Prim(Graph G) {
        this.g = G;
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        isDirected = G instanceof DiGraph;
    }

    // run Prim's algorithm in graph g, starting from vertex s
    private void prim(int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(g, v);
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

    public void visit(){
        for (int v = 0; v < g.V(); v++) {    // run from each vertex to find
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
    }

    public LinkedList<Step> getPath() throws Exception{
        System.out.println("Apply Dijkstra algorithme on :");
        g.print();
        path = new LinkedList<>();
        if (isDirected){
            throw new Exception("Prim ne peut pas être appliquer sur des graphes orientés.");
           /* String message = "Prim ne peut pas être appliquer sur des graphes orientés.";
            Step step = new Step();
            step.setMessage(message);
            step.setVertex(g.getVertex(0));
            path.add(step);*/
        }
        else
            visit();
        System.out.println("result : " + path);
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


    public double weight() throws Exception{
        double weight = 0.0;
        for (Step e : getPath())
            weight += e.getEdge().getWeigth();
        return weight;
    }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */
