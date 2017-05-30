package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import graph.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Prim algorithmVisitor
 * Compute the standard Prim algorithm :
 * https://fr.wikipedia.org/wiki/Algorithme_de_Prim
 */
public class Prim implements AlgorithmVisitor{
    private Edge[] edgeTo;
    private double[] distTo;
    private boolean[] marked;
    private IndexMinPQ<Double> pq;
    LinkedList<Step> path = new LinkedList<>();

    /**
     * Prim algorithm, starting from vertex s
     * @param g, the graph to visit
     * @param s, the starting point of the visit
     */
    private void prim(Graph g, Vertex s) {
        distTo[s.getId()] = 0.0;
        pq.insert(s.getId(), distTo[s.getId()]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(g, g.getVertex(v));
        }
    }

    /**
     * Scan the vertex v in the graph g
     * @param g the graph to scan
     * @param v the vertex to reach
     */
    private void scan(Graph g, Vertex v) {
        marked[v.getId()] = true;
        for (Edge e : g.adjacentEdges(v)) {
            int w = e.getOther(v).getId();
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
                g.getVertex(w).setDescription(distTo[w] + "");
                //step.setVertex(g.getVertex(w));

                path.add(step);

                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
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
        // initialise les structures
        edgeTo = new Edge[g.V()];
        distTo = new double[g.V()];
        marked = new boolean[g.V()];
        pq = new IndexMinPQ<>(g.V());
        for (int v = 0; v < g.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        path = new LinkedList<>();

        // On demarre l'algorithme de Prim
        for (int v = 0; v < g.V(); v++) {
            if (!marked[v]) prim(g, g.getVertex(v));
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

            // On calcul le poids total
            double weight = 0.0;
            for (Step e : path)
                if (e.getEdge() != null)
                    weight += e.getEdge().getWeigth();

            Step step = new Step();
            step.setMessage("Poids Total : " + weight);
            //step.setVertex(g.getVertex(0));

            path.add(step);
        }
        return path;
    }

    /**
     * visit function, apply an algorithm on a Directed Graph, throw directly an exception,
     * Kruskall don't handle Directed graph
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @return the list of step compute by the algorithm
     * @throws Exception if something went wrong in the algorithm
     */
    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        throw new Exception("Prim ne peut pas être appliquer sur des graphes orientés.");
    }
}
