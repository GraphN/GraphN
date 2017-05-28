package Algorithms;

import Algorithms.Utils.EdgeWeightedCycle;
import Algorithms.Utils.Step;
import graph.DiGraph;
import graph.Edge;
import graph.Graph;
import graph.Stockage.EdgeListStockage;
import graph.Vertex;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Bellman_Ford implements Algorithm{
    private LinkedList<Step> path = new LinkedList<>();
    private double[] distTo;               // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;         // edgeTo[v] = last edge on shortest s->v path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private LinkedList<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private LinkedList<Edge> cycle;  // negative cycle (or null if no such cycle)
    private Graph G;
    private Vertex source;
    private Vertex target;
    private boolean isDirected;

    public void pathTo(Vertex v){
        for (Edge e = edgeTo[target.getId()]; e != null; e = edgeTo[e.getFrom().getId()]) {
            // On notifie que la distance min a changer
            String message = "On selectionne l'arete " + source.getId() + e +  "\n\n";
            String structures = "distTo : " + Arrays.toString(distTo)
                    + "\nedgeTo : " + Arrays.toString(edgeTo)
                    + "\nonQueue : " + Arrays.toString(onQueue)
                    + "\nqueue : " + queue.toString();

            Step step = new Step();
            step.setMessage(message);
            step.setStructures(structures);
            step.setEdge(e);

            path.add(step);
        }
    }
    public LinkedList<Step> getPath() throws Exception{
        path = new LinkedList<>();

        if (!isDirected){
            throw new Exception("Bellman-Ford ne peut pas être appliquer sur des graphes non orientés.");
            /*String message = "Bellman-Ford ne peut pas être appliquer sur des graphes non orientés.";
            Step step = new Step();
            step.setMessage(message);
            step.setVertex(G.getVertex(0));
            path.add(step);*/
        }else {

            visit();

            if (hasNegativeCycle()) return path;

            if (target != null){
                pathTo(target);
            }else {
                for (Vertex v : G.getVertexsList())
                    if (v != source)
                        pathTo(v);
            }

        }
        return path;
    }

    private void visit() throws Exception{

            distTo = new double[G.V()];
            edgeTo = new Edge[G.V()];
            onQueue = new boolean[G.V()];
            for (int v = 0; v < G.V(); v++)
                distTo[v] = Double.POSITIVE_INFINITY;
            distTo[source.getId()] = 0.0;

            // Bellman-Ford algorithm
            queue = new LinkedList<>();
            queue.addLast(source.getId());
            onQueue[source.getId()] = true;
            while (!queue.isEmpty() && !hasNegativeCycle()) {
                int v = queue.removeLast();
                onQueue[v] = false;
                relax(G, v);
            }


    }

    public Bellman_Ford(Graph G, Vertex source, Vertex target) {
        this.G = G;
        this.isDirected = G instanceof DiGraph;
        this.source = source;
        this.target = target;
    }

    public Bellman_Ford(Graph G, Vertex source) {
        this(G, source, null);
    }

    private void relax(Graph G, int v) throws Exception{
        for (Edge e : G.adjacentEdges(G.getVertex(v))) {
            int w = e.getTo().getId();
            if (distTo[w] > distTo[v] + e.getWeigth()) {
                distTo[w] = distTo[v] + e.getWeigth();
                edgeTo[w] = e;

                // On notifie que la distance min a changer
                String message = "On met à jour la distance qui separe " + source.getId() + " à " + w + ": " + distTo[w] + "\n\n";
                String structures = "distTo : " + Arrays.toString(distTo)
                        + "\nedgeTo : " + Arrays.toString(edgeTo)
                        + "\nonQueue : " + Arrays.toString(onQueue)
                        + "\nqueue : " + queue.toString();

                Step step = new Step();
                step.setMessage(message);
                step.setStructures(structures);
                G.getVertex(w).setDescription(distTo[w] + "");
                step.setVertex(G.getVertex(w));

                path.add(step);


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
    private void findNegativeCycle() throws Exception{
        int V = edgeTo.length;
        Graph spt = new DiGraph(V, new EdgeListStockage());
        for (int v = 0; v < V; v++)
            if (edgeTo[v] != null)
                spt.addEdge(edgeTo[v]);

        EdgeWeightedCycle finder = new EdgeWeightedCycle(spt);
        cycle = finder.cycle();

        if (cycle != null) {
            throw new Exception("Un circuit absorbant est détécté sur ce graphe, impossible de trouver un plus court chemin !\n" +
            "Circuit : " + cycle);
            /*// On notifie que la distance min a changer
            String message = "Un circuit absorbant est détécté sur ce graphe, impossible de trouver un plus court chemin !\n" +
                    "Circuit : " + cycle;
            String structures = "distTo : " + Arrays.toString(distTo)
                    + "\nedgeTo : " + Arrays.toString(edgeTo)
                    + "\nonQueue : " + Arrays.toString(onQueue)
                    + "\nqueue : " + queue.toString();

            Step step = new Step();
            step.setMessage(message);
            step.setStructures(structures);
            step.setVertex(source);

            path.add(step);*/
        }

    }

    public double distTo(int v) throws Exception{
        validateVertex(v);
        if (hasNegativeCycle())
            throw new Exception("Negative cost cycle exists");
        return distTo[v];
    }

    public boolean hasPathTo(int v) throws Exception{
        validateVertex(v);
        boolean ret = distTo[v] < Double.POSITIVE_INFINITY;
        if (!ret){
            throw new Exception("Il n'y a pas de chemin entre " + source + " et " + G.getVertex(v));
            /*path.clear();
            String message = "Il n'y a pas de chemin entre " + source + " et " + G.getVertex(v);

            Step step = new Step();
            step.setMessage(message);
            step.setVertex(source);
            path.add(step);*/
        }
        return ret;
    }


    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) throws Exception{
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new Exception("vertex " + v + " is not between 0 and " + (V-1));
    }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */