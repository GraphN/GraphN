package Algorithms;

import Algorithms.Utils.Step;
import Algorithms.Utils.UnionFind;
import graph.*;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Kruskall implements AlgorithmVisitor{
    private LinkedList<Step> path;

    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        path = new LinkedList<>();
        for (Edge e : g.getEdgesList()) {
            pq.add(e);
        }
        double weight = 0;

        // run greedy algorithm
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
        step.setVertex(g.getVertex(0));
        path.add(step);

        return path;
    }

    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        throw new Exception("Kruskall ne peut pas être appliquer sur des graphes orientés.");
    }
}
