package Algorithms;

import graph.Edge;
import graph.Graph;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Kruskall {
    private Graph g; // Kruskall ne prend en parametre que des graphes non orientes

    private double weight;                        // weight of MST
    private LinkedList<Edge> mst = new LinkedList<>();

    public Kruskall(Graph g) {
        this.g = g;
    }

    public void visit(EdgeVisit func) {
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        for (Edge e : g.getEdgesList()) {
            pq.add(e);
        }

        // run greedy algorithm
        UnionFind uf = new UnionFind(g.V());
        while (!pq.isEmpty() && mst.size() < g.V() - 1) {
            Edge e = pq.remove();// del min
            int v = e.getFrom().getId();
            int w = e.getOther(e.getFrom()).getId();
            if (!uf.connected(v, w)) { // v-w does not create a cycle
                uf.union(v, w);  // merge v and w components
                mst.add(e);  // add edge e to mst
                weight += e.getWeigth();
                func.applyFunction(e);
            }
        }
    }

    public double getWeight(){return weight;}
    public LinkedList<Edge> getMST(){return mst;}

}
