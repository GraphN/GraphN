package Algorithms;

import Algorithms.Utils.Step;
import Algorithms.Utils.UnionFind;
import graph.DiGraph;
import graph.Edge;
import graph.Graph;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Kruskall implements Algorithm{
    private Graph g; // Kruskall ne prend en parametre que des graphes non orientes

    private double weight;                        // weight of MST
    private LinkedList<Step> path;
    boolean isDirected;

    public Kruskall(Graph g) {
        isDirected = g instanceof DiGraph;
        this.g = g;
    }

    public LinkedList<Step> visit() {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for (Edge e : g.getEdgesList()) {
            pq.add(e);
        }

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

        return path;
    }

    public LinkedList<Step> getPath() throws Exception{
        System.out.println("Apply Kruskal algorithme on :");
        g.print();
        path = new LinkedList<>();
        if (isDirected){
            throw new Exception("Kruskall ne peut pas être appliquer sur des graphes orientés.");
            /*String message = "Kruskall ne peut pas être appliquer sur des graphes orientés.";
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

    public double getWeight(){return weight;}
}
