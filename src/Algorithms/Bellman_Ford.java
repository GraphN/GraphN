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
    private double[] distTo;               // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;         // edgeTo[v] = last edge on shortest s->v path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private LinkedList<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private LinkedList<Edge> cycle;  // negative cycle (or null if no such cycle)

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
            String message = "On selectionne l'arete "  + e +  "\n\n\n";
            String structures = "distTo : " + Arrays.toString(distTo)
                    + "\nedgeTo : " + Arrays.toString(edgeTo)
                    + "\nonQueue : " + Arrays.toString(onQueue)
                    + "\nqueue : " + queue.toString();

            Step step = new Step();
            step.setMessage(message);
            step.setStructures(structures);
            step.setEdge(e);
            step.setVertex(e.getTo());

            path.add(step);
        }
    }

    /**
     * Relax a Vertex, part of the bellman ford algorithm
     * @param v the vertex to relax
     */
    private void relax(Vertex v) throws Exception{
        for (Edge e : g.adjacentEdges(g.getVertex(v.getId()))) {
            int w = e.getTo().getId();
            if (distTo[w] > distTo[v.getId()] + e.getWeigth()) {
                distTo[w] = distTo[v.getId()] + e.getWeigth();
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
                g.getVertex(w).setDescription(distTo[w] + "");
                //step.setVertex(g.getVertex(w));

                path.add(step);


                if (!onQueue[w]) {
                    queue.addLast(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % g.V() == 0) {
                findNegativeCycle();
                if (cycle != null) return;  // found a negative cycle
            }
        }
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
        while (!queue.isEmpty() && cycle != null) {
            int v = queue.removeLast();
            onQueue[v] = false;
            relax(g.getVertex(v));
        }

        // On retourne soit tous les plus court chemin depuis source soit le plus court chemin de source a target
        if (target != null) {
            if (!hasPathTo(target))
                throw new Exception("Il n'y a pas de chemin de " + source.getId() + " a " + target.getId());
            else
                pathTo(target);
        } else {
            for (Vertex v : g.getVertexsList())
                if (v != source)
                    if(hasPathTo(v))
                        pathTo(v);
        }

        return path;
    }
}