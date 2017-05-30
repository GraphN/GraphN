package Algorithms;

import Algorithms.Utils.Step;
import graph.DiGraph;
import graph.UDiGraph;
import graph.Vertex;

import java.util.LinkedList;

public interface AlgorithmVisitor {
    /**
     * visit function, apply an algorithm on a Undirected Graph
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @throws Exception if something went wrong in the algorithm
     */
    LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception;

    /**
     * visit function, apply an algorithm on a Directed Graph
     * @param g, the graph to visit
     * @param source, the starting point of the visit
     * @param target, the target point of the visit
     * @throws Exception if something went wrong in the algorithm
     */
    LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception;
}
