package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Dijkstra implements Algorithm{
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private Vertex target;

    public void setTarget(Vertex target){
        this.target = target;
    }

    public Dijkstra(Graph G, Vertex s, Vertex target) {
        this.target = target;
        for (Edge e : G.getEdgesList()) {
            if (e.getWeigth() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];

        validateVertex(s.getId());

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s.getId()] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s.getId(), distTo[s.getId()]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adjacentEdges(G.getVertex(v)))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s.getId());
    }

    // relax edge e and update pq if changed
    private void relax(Edge e) {
        int v = e.getFrom().getId(), w = e.getTo().getId();
        if (distTo[w] > distTo[v] + e.getWeigth()) {
            distTo[w] = distTo[v] + e.getWeigth();
            edgeTo[w] = e;
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

    public LinkedList<Edge> getPath() {
        validateVertex(target.getId());
        if (!hasPathTo(target.getId())) return null;
        LinkedList<Edge> path = new LinkedList<>();
        for (Edge e = edgeTo[target.getId()]; e != null; e = edgeTo[e.getFrom().getId()]) {
            path.push(e);
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
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */
