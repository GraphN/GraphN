package Algorithms;

import Algorithms.Utils.Step;
import graph.*;
import java.util.LinkedList;
import java.util.Vector;

/**
 * BFS algorithmVisitor
 * Compute the standard Breath First Search algorithm :
 * https://fr.wikipedia.org/wiki/Algorithme_de_parcours_en_largeur
 */
public class BFS implements AlgorithmVisitor{
    private Vector<Integer> parent;
    private LinkedList<Step> path;

    /**
     * bfs algorithm
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @return the list of step compute by the algorithm
     */
    private LinkedList<Step> bfs(Graph g, Vertex source) {
        LinkedList<Edge> pile = new LinkedList<>();

        parent.set(source.getId(), source.getId());

        do{
            Vertex current;

            if(pile.isEmpty())
                current = source;
            else
                current = pile.removeFirst().getTo();

            for (Edge e : g.adjacentEdges(current)) {
                if (parent.get(e.getTo().getId()) == -1) {
                    parent.set(e.getTo().getId(), current.getId());
                    pile.add(e);
                }
            }

            if(g.getEdge(g.getVertex(parent.get(current.getId())), current) != null) {
                String message = "On selectionne le sommet " + current.getId();
                String structures = "parent : " + parent.toString();
                Edge e = g.getEdge(g.getVertex(parent.get(current.getId())), current);

                Step step = new Step();
                step.setMessage(message);
                step.setStructures(structures);
                step.setEdge(e);
                step.setVertex(current);

                path.add(step);
            }

        }while (!pile.isEmpty());

        return path;
    }

    /**
     * visit function for apply BFS algorithm initialise the structures, avoid redundancy of code
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @return the list of step compute by the algorithm
     */
    private LinkedList<Step> visit(Graph g, Vertex source){
        parent = new Vector<>(g.V());
        path = new LinkedList<>();
        for(int i  = 0; i < g.V(); i++)
            parent.add(-1);
        return bfs(g, source);
    }

    /**
     * visit function, apply an algorithm on a Undirected Graph
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @return the list of step compute by the algorithm
     * @throws Exception if something went wrong in the algorithm
     */
    public LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception{
        return visit(g, source);
    }

    /**
     * visit function, apply an algorithm on a Directed Graph
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @return the list of step compute by the algorithm
     * @throws Exception if something went wrong in the algorithm
     */
    public LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception {
        return visit(g, source);
    }
}
