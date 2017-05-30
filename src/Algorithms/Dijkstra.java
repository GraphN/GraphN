package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import graph.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Dijkstra algorithmVisitor
 * Compute the standard BDijkstra algorithm :
 * https://fr.wikipedia.org/wiki/Algorithme_de_Dijkstra
 */
public class Dijkstra implements AlgorithmVisitor{
    private double[] distTo;
    private Edge[] edgeTo;
    private IndexMinPQ<Double> pq;
    private LinkedList<Step> path;

    /**
     * Check if there is a path from vertex source to vertex v in g
     * @param v the vertex to reach
     * @return true if there is a path
     */
    public boolean hasPathTo(Vertex v) throws Exception{
        return distTo[v.getId()] < Double.POSITIVE_INFINITY;
    }

    /**
     * Compute the step to have the path from source to vertex v
     * @param source the vertex to start
     * @param target the vertex to reach
     */
    public void pathTo(Vertex source, Vertex target) throws Exception{

        if (!hasPathTo(target)){
            Step step = new Step();
            step.setMessage("Il n'y a pas de chemin de " + source.getId() + " a " + target.getId());
            path.add(step);
        }

        for (Edge e = edgeTo[target.getId()]; e != null; e = edgeTo[e.getFrom().getId()]) {
            Step step = new Step();
            step.setMessage("On selectionne l'arrete " + e);
            step.setStructures("distTo : " + Arrays.toString(distTo)
                    + "\nedgeTo : " + Arrays.toString(edgeTo)
                    + "\nPriorityQueue : " + pq.toString());
            step.setEdge(e);
            step.setVertex(e.getTo());

            path.add(step);
        }
    }

    /**
     * Dijkstra algorithm
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the vertex to reach
     * @return the list of step compute by the algorithm
     */
    public LinkedList<Step> dijkstra(Graph g, Vertex source, Vertex target) throws Exception{
        distTo = new double[g.V()];
        edgeTo = new Edge[g.V()];

        for (int v = 0; v < g.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        path = new LinkedList<>();

        for (Edge e : g.getEdgesList()) {
            System.out.println(e + " p " + e.getWeigth());
            if (e.getWeigth() < 0) {
                throw new Exception("L'arete  " + e + " a un poids négatif, Dijkstra ne peut pas s'appliquer sur les graphes " +
                        "a poids négatif, essayer Bellman-Ford");
            }
        }

        distTo[source.getId()] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(g.V());
        pq.insert(source.getId(), distTo[source.getId()]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : g.adjacentEdges(g.getVertex(v))) {
                relax(g, source, e);
            }
        }

        if (target != null)
            pathTo(source, target);
        else{
            for (Vertex v : g.getVertexsList())
                if (v != source)
                    pathTo(source, target);
        }

        System.out.println("result : " + path);
        return path;
    }

    /**
     * Relax a Vertex and update the pq, part of the Dijkstra algorithm
     * @param g the graph concerned
     * @param source the vertex source of the Dijkstra algorithm
     * @param e the edge to relax
     */
    private void relax(Graph g, Vertex source, Edge e) {
        int v = e.getFrom().getId(), w = e.getTo().getId();
        if (distTo[w] > distTo[v] + e.getWeigth()) {
            distTo[w] = distTo[v] + e.getWeigth();
            edgeTo[w] = e;

            // prepare the step
            String message = "On met à jour la distance qui separe " + source.getId() + " à " + w + ": " + distTo[w] + "\n\n";
            String structures = "distTo : " + Arrays.toString(distTo)
                    + "\nedgeTo : " + Arrays.toString(edgeTo)
                    + "\nPriorityQueue : " + pq.toString();


            Step step = new Step();
            step.setMessage(message);
            step.setStructures(structures);
            g.getVertex(w).setDescription(distTo[w] + "");
            step.setVertex(g.getVertex(w));

            path.add(step);


            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }


    /**
     * visit function, apply an algorithm on a UNDirected Graph
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @return the list of step compute by the algorithm
     * @throws Exception if something went wrong in the algorithm
     */
    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        return dijkstra(g, source, target);
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
        return dijkstra(g, source, target);
    }
}