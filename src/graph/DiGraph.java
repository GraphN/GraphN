package graph;

import Algorithms.AlgorithmVisitor;
import Algorithms.Utils.Step;
import graph.Stockage.StockageType;

import java.util.LinkedList;

/**
 *  Representation of a Directed graph
 */
public class DiGraph extends Graph {

    /**
     * Constructor
     * @param V the number of vertex of the graph
     * @param s the type of stockage to use
     */
    public DiGraph(int V, StockageType s) {
        super(V,s);
    }

    /**
     * Add an edge with 0 weight to the graph
     * @param v the starting vertex
     * @param w the ending vertex
     */
    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    /**
     * Add an edge to the graph
     * @param v the starting vertex
     * @param w the ending vertex
     * @param weight the weight of the edge
     */
    public void addEdge(Vertex v, Vertex w, double weight) {
        stockage.addEdge(v, w, weight);
        E++;
    }

    /**
     * Apply an algorithme(AlgorithmVisitor) to the current graph
     * @param v the visitor to apply
     * @param source the source vertex
     * @param target the vertex to reach
     * @return A list of step produce by the algorithm visitor
     * @throws Exception if the algorithm cant finish his execution
     */
    public LinkedList<Step> accept(AlgorithmVisitor v, Vertex source, Vertex target) throws Exception{
        return v.visit(this, source, target);
    }
}
