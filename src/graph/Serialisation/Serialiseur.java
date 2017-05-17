package graph.Serialisation;

import graph.DiGraph;
import graph.Edge;
import graph.Graph;
import graph.Stockage.StockageType;
import graph.UDiGraph;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 17.05.17.
 */
interface Serialiseur {
    int DIRECTED = 1;
    int UNDIRECTED = 0;
    void exportGraph(Graph g, LinkedList<Edge> path, String outputFile);
    Graph importGraph(String inputFile, StockageType stockage);
}
