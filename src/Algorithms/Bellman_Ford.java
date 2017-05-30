package Algorithms;

import Algorithms.Utils.EdgeWeightedCycle;
import Algorithms.Utils.Step;
import graph.*;
import graph.Stockage.EdgeListStockage;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Bellman Ford algorithmVisitor
 * Compute the standard Bellman-Ford algorithm :
 * https://fr.wikipedia.org/wiki/Algorithme_de_Bellman-Ford
 */
public class Bellman_Ford implements AlgorithmVisitor{
    private LinkedList<Step> path;
    private double[] distTo;
    private Edge[] edgeTo;
    private boolean[] onQueue;
    private LinkedList<Integer> queue;
    private int cost;
    private LinkedList<Edge> cycle;

    private DiGraph g;
    Vertex source;


    /**
     * Check if there is a path from vertex source to vertex v in g
     * @param v the vertex to reach
     * @return true if there is a path
     */
    public boolean hasPathTo(Vertex v) {
        return distTo[v.getId()] < Double.POSITIVE_INFINITY;
    }

    /**
     * Compute the step to have the path from source to vertex v
     * @param v the vertex to reach
     */
    private void pathTo(Vertex v){
        for (Edge e = edgeTo[v.getId()]; e != null; e = edgeTo[e.getFrom().getId()]) {
            // On notifie que la distance min a changer
            String message = "On selectionne l'arete " + source.getId() + e +  "\n\n\n";
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

    /**
     * Relax a Vertex, part of the bellman ford algorithm
     * @param G the graph to relax
     * @param v the vertex to relax
     */
    private void relax(Graph G, int v) throws Exception{
        for (Edge e : G.adjacentEdges(G.getVertex(v))) {
            int w = e.getTo().getId();
            if (distTo[w] > distTo[v] + e.getWeigth()) {
                distTo[w] = distTo[v] + e.getWeigth();
                edgeTo[w] = e;

                // On notifie que la distance min a changer
                String message = "On met à jour la distance qui separe " + source.getId() + " à " + w + ": " + distTo[w] + "\n\n\n";
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

    /**
     * Return true if a negative cycle is find
     * @return true if a negative cycle is find
     */
    private boolean hasNegativeCycle() {
        return cycle != null;
    }


    /**
     * Find negative cycles on a partiel graph of current graph
     * that contain the current smaller edges
     */
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


    /**
     * visit function, apply an algorithm on a Undirected Graph, Bellman Ford does not support Undirected graph
     * so throw directly a exception.
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @return the list of step compute by the algorithm
     * @throws Exception if something went wrong in the algorithm
     */
    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        throw new Exception("Bellman-Ford ne peut pas être appliquer sur des graphes non orientés.");
    }

    /**
     * visit function, apply an algorithm on a Directed Graph
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @return the list of step compute by the algorithm
     * @throws Exception if something went wrong in the algorithm
     */
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
            if (hasPathTo(target))
                pathTo(target);
            else
                throw new Exception("Il n'y a pas de chemin de " + source.getId() + " a " + target.getId());
        } else {
            for (Vertex v : g.getVertexsList())
                if (v != source)
                    if (hasPathTo(v))
                        pathTo(v);
        }

        return path;
    }
}
