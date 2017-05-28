package Algorithms;

import Algorithms.Utils.IndexMinPQ;
import Algorithms.Utils.Step;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
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

    public Dijkstra(Graph G, Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
        this.G = G;

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
    }

    public Dijkstra(Graph G, Vertex source) {
        this(G, source, null);
    }

    public void visit() throws Exception{
        path = new LinkedList<>();

        for (Edge e : G.getEdgesList()) {
            System.out.println(e + " p " + e.getWeigth());
            if (e.getWeigth() < 0) {
                throw new Exception("L'arete  " + e + " a un poids négatif, Dijkstra ne peut pas s'appliquer sur les graphes " +
                        "a poids négatif, essayer Bellman-Ford");
                /*Step step = new Step();
                step.setEdge(e);
                step.setMessage("L'arete  " + e + " a un poids négatif, Dijkstra ne peut pas s'appliquer sur les graphes " +
                        "a poids négatif, essayer Bellman-Ford");
                path.add(step);
                return;*/
            }
        }

        validateVertex(source.getId());

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


    public double distTo(int v) throws Exception{
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) throws Exception{
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public void pathTo(Vertex v) throws Exception{

        if (!hasPathTo(v.getId())){
            Step step = new Step();
            step.setMessage("Il n'y a pas de chemin de " + source.getId() + " a " + v.getId());
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

    public LinkedList<Step> getPath() throws Exception{
        System.out.println("Apply Dijkstra algorithme on :");
        G.print();
        visit();

        if (target != null)
            pathTo(target);
        else{
            for (Vertex v : G.getVertexsList())
                if (v != source)
                    pathTo(v);
        }

        System.out.println("result : " + path);
        return path;
    }

    private boolean validateVertex(int v) throws Exception {
        int V = distTo.length;
        if (v < 0 || v >= V) {
            String msg = "Invalid vertex number : " + v;
            throw new Exception(msg);
        }
        return true;
    }
}

/* Inspired by University of Princeton course http://algs4.cs.princeton.edu/ */
