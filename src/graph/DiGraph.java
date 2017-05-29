package graph;


import Algorithms.AlgorithmVisitor;
import Algorithms.Utils.Step;
import graph.Stockage.StockageType;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 22.03.17.
 */
public class DiGraph extends Graph {

    public DiGraph(int V, StockageType s) {
        super(V,s);
        TYPE = 1;
        System.out.println(this);
    }

    public void addEdge(Vertex v, Vertex w) {
        addEdge(v, w, 0);
    }

    public void addEdge(Vertex v, Vertex w, double weigth) {
        System.out.println(" add edge " + v + " " + w + " " + weigth);
        stockage.addEdge(v, w, weigth);
        E++;
    }

    public LinkedList<Step> accept(AlgorithmVisitor v, Vertex source, Vertex target) throws Exception{
        return v.visit(this, source, target);
    }
}
