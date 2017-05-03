package graph;


import graph.Stockage.StockageType;

/**
 * Created by francoisquellec on 22.03.17.
 */
public class DiGraph extends Graph {

    public DiGraph(int V, StockageType s) {
        super(V,s);
    }

    public DiGraph(String filename, StockageType stockage) {
     super(filename, stockage);
    }

    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    public void addEdge(Vertex v, Vertex w, int weigth) {
        stockage.addEdge(v, w, weigth);
    }
}
