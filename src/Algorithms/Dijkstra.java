package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import graph.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Dijkstra implements AlgorithmVisitor{
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private LinkedList<Step> path;


    public boolean hasPathTo(int v) throws Exception{
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public void pathTo(Vertex source, Vertex target) throws Exception{

        if (!hasPathTo(target.getId())){
            Step step = new Step();
            step.setMessage("Il n'y a pas de chemin de " + source.getId() + " a " + target.getId());
            path.add(step);
        }

        for (Edge e = edgeTo[target.getId()]; e != null; e = edgeTo[e.getFrom().getId()]) {
            Step step = new Step();
            step.setMessage("On selectionne l'arrete " + e);
            step.setStructures("distTo : " + Arrays.toString(distTo)
                    + "\nedgeTo : " + Arrays.toString(edgeTo)
                    + "\nPriorityQueue : " + pq.toString());
            step.setEdge(e);

            path.add(step);
        }
    }

    private void validateVertex(int v) throws Exception {
        int V = distTo.length;
        if (v < 0 || v >= V) {
            throw new Exception("Invalid vertex number : " + v);
        }
    }

    public LinkedList<Step> dijkstra(Graph g, Vertex source, Vertex target) throws Exception{
        distTo = new double[g.V()];
        edgeTo = new Edge[g.V()];

        for (int v = 0; v < g.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        path = new LinkedList<>();

        for (Edge e : g.getEdgesList()) {
            System.out.println(e + " p " + e.getWeigth());
            if (e.getWeigth() < 0) {
                throw new Exception("L'arete  " + e + " a un poids négatif, Dijkstra ne peut pas s'appliquer sur les graphes " +
                        "a poids négatif, essayer Bellman-Ford");
            }
        }

        validateVertex(source.getId());

        distTo[source.getId()] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(g.V());
        pq.insert(source.getId(), distTo[source.getId()]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : g.adjacentEdges(g.getVertex(v))) {
                relax(g, source, e);
            }
        }

        if (target != null)
            pathTo(source, target);
        else{
            for (Vertex v : g.getVertexsList())
                if (v != source)
                    pathTo(source, target);
        }

        System.out.println("result : " + path);
        return path;
    }

    // relax edge e and update pq if changed
    private void relax(Graph g, Vertex source, Edge e) {
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
            g.getVertex(w).setDescription(distTo[w] + "");
            step.setVertex(g.getVertex(w));

            path.add(step);


            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }


    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        return dijkstra(g, source, target);
    }

    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        return dijkstra(g, source, target);
    }
}