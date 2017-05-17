package graph.Serialisation;

import graph.*;
import graph.Stockage.StockageType;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by francoisquellec on 17.05.17.
 */
public class ListEdgesTXT implements Serialiseur {
    static final String EXTENSION_FILE = ".txt";

    public void exportGraph(Graph g, LinkedList<Edge> path, String outputFile){
        try{
            PrintWriter writer = new PrintWriter(outputFile + EXTENSION_FILE, "UTF-8");
            writer.println(g.V() + " " + g.E() + " " + g.getType());

            // On ecrit la liste de edges
            for(Edge e : path)
                writer.println(e.getFrom() + " " + e.getTo() + " " + e.getWeigth());

            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    public Graph importGraph(String inputFile, StockageType stockage){
        Graph g = null;
        try {
            // On cree un scanner pour lire notre fichier
            Scanner scanner = new Scanner(new File(inputFile));
            String line;
            int V, E, TYPE;

            // On lit la premiere ligne pour connaitre les dimensions et le type de notre graphe
            V = scanner.nextInt();
            E = scanner.nextInt();
            TYPE = scanner.nextInt();

            switch (TYPE){
                case DIRECTED:
                    g = new DiGraph(V, stockage);
                    break;
                case UNDIRECTED:
                    g = new UDiGraph(V, stockage);
                    break;
                default:
                    throw new IOException("Graph Type unknow");
            }

            System.out.println("Import graph : nbNode = " + V + "; nbEdges = " + E + "; weigthed : " + TYPE);

            //  On lit les lignes suivante pour stocker le graphe

            for (int i = 0; i < E; i++) {
                Vertex node1 = g.getVertex(scanner.nextInt());
                Vertex node2 = g.getVertex((scanner.nextInt()));
                int weigth = scanner.nextInt();

                g.addEdge(node1, node2, weigth);
            }

        } catch (IOException e) {
            System.err.print(e);
        }

        return g;
    }
}
