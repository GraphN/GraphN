package graph.Serialisation;

import Algorithms.Utils.Step;
import graph.Graph;
import graph.Stockage.StockageType;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 17.05.17.
 */
public interface Serialiseur {
    int DIRECTED = 1;
    int UNDIRECTED = 0;
    void exportGraph(Graph g, LinkedList<Step> path, String outputFile);
    Graph importGraph(String inputFile, StockageType stockage) throws Exception;
}
