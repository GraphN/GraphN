package Algorithms;

import Algorithms.Utils.EdgeWeightedCycle;
import Algorithms.Utils.Step;
import graph.*;
import graph.Stockage.EdgeListStockage;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Bellman_Ford implements AlgorithmVisitor{
    private LinkedList<Step> path;
    private double[] distTo;               // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;         // edgeTo[v] = last edge on shortest s->v path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private LinkedList<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private LinkedList<Edge> cycle;  // negative cycle (or null if no such cycle)

    private DiGraph g;
    Vertex source;


    private void pathTo(Vertex v){
        for (Edge e = edgeTo[v.getId()]; e != null; e = edgeTo[e.getFrom().getId()]) {
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

    private boolean hasNegativeCycle() {
        return cycle != null;
    }


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
        }
    }

    private void validateVertex(int v) throws Exception{
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new Exception("Le sommet " + v + " n'est pas valide, max : " + (V-1));
    }


    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        throw new Exception("Bellman-Ford ne peut pas être appliquer sur des graphes non orientés.");
    }

    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        this.g = g;
        this.source = source;

        // initialisation
        path = new LinkedList<>();
        distTo = new double[g.V()];
        edgeTo = new Edge[g.V()];
        onQueue = new boolean[g.V()];
        for (int v = 0; v < g.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[source.getId()] = 0.0;

        // Bellman-Ford algorithm
        queue = new LinkedList<>();
        queue.addLast(source.getId());
        onQueue[source.getId()] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.removeLast();
            onQueue[v] = false;
            relax(g, v);
        }

        if (hasNegativeCycle()) return path;

        // On retourne soit tous les plus court chemin depuis source soit le plus court chemin de source a target
        if (target != null) {
            pathTo(target);
        } else {
            for (Vertex v : g.getVertexsList())
                if (v != source)
                    pathTo(v);
        }

        return path;
    }
}