package graph;
        import graph.Stockage.StockageType;

/**
 * Created by Adrian on 22.03.2017.
 */
public class UDiGraph extends Graph {

    public UDiGraph(int V) {
        super(V);
    }

    public UDiGraph(String filename, StockageType stockage) {
        super(filename, stockage);
    }

    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    public void addEdge(Vertex v, Vertex w, int weigth) {
        stockage.addEdge(v, w, weigth);
        stockage.addEdge(w, v, weigth);
    }
}


