package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import graph.*;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Prim implements AlgorithmVisitor{
    private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;

    LinkedList<Step> path = new LinkedList<>();


    // run Prim's algorithm in graph g, starting from vertex s
    private void prim(Graph g, Vertex s) {
        distTo[s.getId()] = 0.0;
        pq.insert(s.getId(), distTo[s.getId()]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(g, v);
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

                // prepare the step
                String message = "On met à jour les poids \n\n";
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
    }


    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        edgeTo = new Edge[g.V()];
        distTo = new double[g.V()];
        marked = new boolean[g.V()];
        pq = new IndexMinPQ<>(g.V());
        for (int v = 0; v < g.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        path = new LinkedList<>();

        for (int v = 0; v < g.V(); v++) {    // run from each vertex to find
            if (!marked[v]) prim(g, g.getVertex(v));      // minimum spanning forest
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

            double weight = 0.0;
            for (Step e : path)
                if (e.getEdge() != null)
                    weight += e.getEdge().getWeigth();

            Step step = new Step();
            step.setMessage("Poids Total : " + weight);
            step.setVertex(g.getVertex(0));

            path.add(step);
        }
        return path;
    }

    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        throw new Exception("Prim ne peut pas être appliquer sur des graphes orientés.");
    }
}
