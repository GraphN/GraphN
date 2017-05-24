package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.UnionFind;
import graph.Edge;
import graph.Graph;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Prim implements Algorithm{
        private static final double FLOATING_POINT_EPSILON = 1E-12;

        private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
        private double[] distTo;      // distTo[v] = weight of shortest such edge
        private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
        private IndexMinPQ<Double> pq;

        public Prim(Graph G) {
            edgeTo = new Edge[G.V()];
            distTo = new double[G.V()];
            marked = new boolean[G.V()];
            pq = new IndexMinPQ<Double>(G.V());
            for (int v = 0; v < G.V(); v++)
                distTo[v] = Double.POSITIVE_INFINITY;

            for (int v = 0; v < G.V(); v++)      // run from each vertex to find
                if (!marked[v]) prim(G, v);      // minimum spanning forest

            // check optimality conditions
            assert check(G);
        }

        // run Prim's algorithm in graph G, starting from vertex s
        private void prim(Graph G, int s) {
            distTo[s] = 0.0;
            pq.insert(s, distTo[s]);
            while (!pq.isEmpty()) {
                int v = pq.delMin();
                scan(G, v);
            }
        }

        // scan vertex v
        private void scan(Graph G, int v) {
            marked[v] = true;
            for (Edge e : G.adjacentEdges(G.getVertex(v))) {
                int w = e.getOther(G.getVertex(v)).getId();
                if (marked[w]) continue;         // v-w is obsolete edge
                if (e.getWeigth() < distTo[w]) {
                    distTo[w] = e.getWeigth();
                    edgeTo[w] = e;
                    if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                    else                pq.insert(w, distTo[w]);
                }
            }
        }

        public LinkedList<Edge> getPath() {
            LinkedList<Edge> mst = new LinkedList<>();
            for (int v = 0; v < edgeTo.length; v++) {
                Edge e = edgeTo[v];
                if (e != null) {
                    mst.add(e);
                }
            }
            return mst;
        }

        public double weight() {
            double weight = 0.0;
            for (Edge e : getPath())
                weight += e.getWeigth();
            return weight;
        }


        // check optimality conditions (takes time proportional to E V lg* V)
        private boolean check(Graph G) {

            // check weight
            double totalWeight = 0.0;
            for (Edge e : getPath()) {
                totalWeight += e.getWeigth();
            }
            if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
                System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
                return false;
            }

            // check that it is acyclic
            UnionFind uf = new UnionFind(G.V());
            for (Edge e : getPath()) {
                int v = e.getFrom().getId(), w = e.getTo().getId();
                if (uf.connected(v, w)) {
                    System.err.println("Not a forest");
                    return false;
                }
                uf.union(v, w);
            }

            // check that it is a spanning forest
            for (Edge e : G.getEdgesList()) {
                int v = e.getFrom().getId(), w = e.getTo().getId();
                if (!uf.connected(v, w)) {
                    System.err.println("Not a spanning forest");
                    return false;
                }
            }

            // check that it is a minimal spanning forest (cut optimality conditions)
            for (Edge e : getPath()) {

                // all edges in MST except e
                uf = new UnionFind(G.V());
                for (Edge f : getPath()) {
                    int x = f.getFrom().getId(), y = f.getTo().getId();
                    if (f != e) uf.union(x, y);
                }

                // check that e is min weight edge in crossing cut
                for (Edge f : G.getEdgesList()) {
                    int x = f.getFrom().getId(), y = f.getTo().getId();
                    if (!uf.connected(x, y)) {
                        if (f.getWeigth() < e.getWeigth()) {
                            System.err.println("Edge " + f + " violates cut optimality conditions");
                            return false;
                        }
                    }
                }

            }

            return true;
        }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */
