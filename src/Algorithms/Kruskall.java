package Algorithms;

import Algorithms.Utils.EdgeVisit;
import Algorithms.Utils.Step;
import Algorithms.Utils.UnionFind;
import graph.DiGraph;
import graph.Edge;
import graph.Graph;
import graph.UDiGraph;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Kruskall implements Algorithm{
    private Graph g; // Kruskall ne prend en parametre que des graphes non orientes

    public LinkedList<Step> getPath(){
        mst = new LinkedList<>();
        if (isDirected){
            String message = "Kruskall ne peut pas être appliquer sur des graphes orientés.";
            Step step = new Step();
            step.setMessage(message);
            step.setVertex(g.getVertex(0));
            mst.add(step);
        }
        else
            visit();

        return mst;
    }

    private double weight;                        // weight of MST
    private LinkedList<Step> mst;
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
        while (!pq.isEmpty() && mst.size() < g.V() - 1) {
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

                mst.add(step);

                weight += e.getWeigth();
            }
        }

        return mst;
    }

    public double getWeight(){return weight;}
}
