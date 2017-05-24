package Algorithms;

import Algorithms.Utils.EdgeVisit;
import Algorithms.Utils.Step;
import Algorithms.Utils.UnionFind;
import graph.Edge;
import graph.UDiGraph;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Kruskall implements Algorithm{
    private UDiGraph g; // Kruskall ne prend en parametre que des graphes non orientes

    public LinkedList<Step> getPath(){
        visit(new EdgeVisit() {
            @Override
            public void applyFunction(Edge e) {

            }
        });
        return mst;
    }

    private double weight;                        // weight of MST
    private LinkedList<Step> mst = new LinkedList<>();

    public Kruskall(UDiGraph g) {
        this.g = g;
    }

    public LinkedList<Step> visit(EdgeVisit func) {
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

                String message = "On selectionne le sommet " + w;
                String structures = "Priority queue : " + pq.toString();

                Step step = new Step(Step.TYPE.EDGE);
                step.setMessage(message);
                step.setStructures(structures);
                step.setEdge(e);

                mst.add(step);
                weight += e.getWeigth();
                func.applyFunction(e);
            }
        }

        return mst;
    }

    public double getWeight(){return weight;}
}
