package Algorithms;

import Algorithms.Utils.Step;
import Algorithms.Utils.UnionFind;
import graph.*;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Kruskall algorithmVisitor
 * Compute the standard Kruskal algorithm :
 * https://fr.wikipedia.org/wiki/Algorithme_de_Kruskal
 */
public class Kruskall implements AlgorithmVisitor{
    private LinkedList<Step> path;

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
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        path = new LinkedList<>();
        for (Edge e : g.getEdgesList()) {
            pq.add(e);
        }
        double weight = 0;

        // Demmare l'algorithme de kruskall
        UnionFind uf = new UnionFind(g.V());
        while (!pq.isEmpty() && path.size() < g.V() - 1) {
            Edge e = pq.remove();// del min
            int v = e.getFrom().getId();
            int w = e.getOther(e.getFrom()).getId();
            if (!uf.connected(v, w)) { // v-w does not create a cycle
                uf.union(v, w);  // merge v and w components

                String message = "On selectionne l'arrete " + e;
                String structures = "Priority queue : " + pq.toString();

                Step step = new Step();
                step.setMessage(message);
                step.setStructures(structures);
                step.setEdge(e);

                path.add(step);

                weight += e.getWeigth();
            }
        }

        String message = "Algorithme terminé, poids Total : " + weight;
        Step step = new Step();
        step.setMessage(message);
        //step.setVertex(g.getVertex(0));
        path.add(step);
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
        throw new Exception("Kruskall ne peut pas être appliquer sur des graphes orientés.");
    }
}
