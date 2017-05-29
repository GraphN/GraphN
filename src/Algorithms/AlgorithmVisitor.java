package Algorithms;

import Algorithms.Utils.Step;
import graph.DiGraph;
import graph.UDiGraph;
import graph.Vertex;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 29.05.17.
 */
public interface AlgorithmVisitor {
    LinkedList<Step> visit(UDiGraph g, Vertex source, Vertex target) throws Exception;
    LinkedList<Step> visit(DiGraph g, Vertex source, Vertex target) throws Exception;
}
