package graph;
        import Algorithms.AlgorithmVisitor;
        import Algorithms.Utils.Step;
        import graph.Stockage.StockageType;

        import java.util.LinkedList;

/**
 * Created by Adrian on 22.03.2017.
 */
public class UDiGraph extends Graph {

    public UDiGraph(int V, StockageType s) {
        super(V,s);
        TYPE = 0;
        System.out.println(this);
    }

    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    public void addEdge(Vertex v, Vertex w, double weigth) {
        stockage.addEdge(v, w, weigth);
        stockage.addEdge(w, v, weigth);
        E ++;
    }

    public LinkedList<Step> accept(AlgorithmVisitor v, Vertex source, Vertex target) throws Exception{
        return v.visit(this, source, target);
    }
}


