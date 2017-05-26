package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Dijkstra implements Algorithm{
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private Vertex source;
    private Vertex target;
    private Graph G;

    private LinkedList<Step> path;



    public void visit(){
        path = new LinkedList<>();

        for (Edge e : G.getEdgesList()) {
            System.out.println(e + " p " + e.getWeigth());
            if (e.getWeigth() < 0) {
                Step step = new Step();
                step.setEdge(e);
                step.setMessage("L'arete  " + e + " a un poids négatif, Dijkstra ne peut pas s'appliquer sur les graphes " +
                        "a poids négatif, essayer Bellman-Ford");
                path.add(step);
                return;
            }
        }

        if(!validateVertex(source.getId())) return;

        distTo[source.getId()] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(source.getId(), distTo[source.getId()]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adjacentEdges(G.getVertex(v))) {
                relax(e);
            }
        }

        // check optimality conditions
        assert check(G, source.getId());
    }

    public Dijkstra(Graph G, Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
        this.G = G;

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
    }

    // relax edge e and update pq if changed
    private void relax(Edge e) {
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
            G.getVertex(w).setDescription(distTo[w] + "");
            step.setVertex(G.getVertex(w));

            path.add(step);


            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }


    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public LinkedList<Step> getPath() {
        visit();

        validateVertex(target.getId());

        if (!hasPathTo(target.getId())){
            Step step = new Step();
            step.setMessage("Il n'y a pas de chemin de " + source.getId() + " a " + target.getId());
            path.add(step);
            return path;
        }

        for (Edge e = edgeTo[target.getId()]; e != null; e = edgeTo[e.getFrom().getId()]) {
            Step step = new Step();
            step.setMessage("On selectionne l'arrete " + e);
            step.setStructures("distTo : " + distTo.toString()
                    + "\nedgeTo : " + edgeTo.toString()
                    + "\nPriorityQueue : " + pq.toString());
            step.setEdge(e);

            path.add(step);
        }
        return path;
    }


    private boolean check(Graph G, int s) {

        // check that edge weights are nonnegative
        for (Edge e : G.getEdgesList()) {
            if (e.getWeigth() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adjacentEdges(G.getVertex(v))) {
                int w = e.getTo().getId();
                if (distTo[v] + e.getWeigth() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            Edge e = edgeTo[w];
            int v = e.getFrom().getId();
            if (w != e.getTo().getId()) return false;
            if (distTo[v] + e.getWeigth() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private boolean validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V) {
            Step step = new Step();
            step.setMessage("vertex " + v + " is not between 0 and " + (V - 1));
            return false;
        }
        return true;
    }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */
