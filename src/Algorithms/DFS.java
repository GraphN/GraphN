package Algorithms;

import Algorithms.Utils.Step;
import graph.*;
import java.util.LinkedList;
import java.util.Vector;

/**
 * DFS algorithmVisitor
 * Compute the standard Depth First Search algorithm :
 * https://fr.wikipedia.org/wiki/Algorithme_de_parcours_en_profondeur
 */
public class DFS implements AlgorithmVisitor{
    private Vector<Boolean> marked;
    private LinkedList<Step> path;
    private LinkedList<Vertex> listLastVisitedVertex;

    /**
     * bfs algorithm
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @return the list of step compute by the algorithm
     */
    private void dfs(Graph g, Vertex source) {
        if(!listLastVisitedVertex.isEmpty()) {
            String message = "On selectionne le sommet " + source.getId() + "\n";
            String structures = "last visited vertex : " + listLastVisitedVertex.getLast().getId()
                    + "\nmarked : " + marked.toString();
            //System.out.println("Add step : " +lastVisitedVertex.getId() + v.getId());
            Edge e = null;
            while (!listLastVisitedVertex.isEmpty() && (e = g.getEdge(listLastVisitedVertex.removeLast(), source)) == null);
            Step step = new Step();
            step.setMessage(message);
            step.setStructures(structures);
            if (e != null) {
                step.setEdge(e);
                listLastVisitedVertex.addLast(e.getFrom());
            }
            step.setVertex(source);
            path.add(step);
        }

        listLastVisitedVertex.addLast(source);

        marked.set(source.getId(), true);

        for (Vertex w : g.adjacentVertex(source))
            if (!marked.get(w.getId()))
                dfs(g, w);
    }

    /**
     * visit function for apply DFS algorithm initialise the structures, avoid redundancy of code
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @return the list of step compute by the algorithm
     */
    private LinkedList<Step> visit(Graph g, Vertex source){
        listLastVisitedVertex = new LinkedList<>();
        marked = new Vector<>(g.V());
        path = new LinkedList<>();

        for(int i  = 0; i < g.V(); i++)
            marked.add(false);

        dfs(g, source);

        return path;
    }

    /**
     * visit function, apply an algorithm on a UNDirected Graph
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
