package graph.Serialisation;

import Algorithms.Utils.Step;
import graph.DiGraph;
import graph.Graph;
import graph.Stockage.StockageType;
import graph.UDiGraph;

import java.util.LinkedList;

/**
 * Interface for import/export class
 */
public interface Serialiseur {
    /**
     * Export the graph g with the specified path in the specified file
     * @param g the graph to export
     * @param path the path to export
     * @param outputFile the file to use
     */
    void exportGraph(Graph g, LinkedList<Step> path, String outputFile);

    /**
     * Import a graph from a specified input file, use a specified stockageType
     * for create the graph.
     * @param inputFile the filepath of the file to import
     * @param stockage the type of stockage to use
     * @return the graph imported
     * @throws Exception if the input file doesn't respect the typo
     */
    Graph importGraph(String inputFile, StockageType stockage) throws Exception;
}
