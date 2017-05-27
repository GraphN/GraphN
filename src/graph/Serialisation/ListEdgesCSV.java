package graph.Serialisation;

import Algorithms.Utils.Step;
import com.sun.deploy.security.ValidationState;
import graph.DiGraph;
import graph.Graph;
import graph.Stockage.StockageType;
import graph.UDiGraph;
import graph.Vertex;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by francoisquellec on 27.05.17.
 */
public class ListEdgesCSV implements Serialiseur{
        static final String EXTENSION_FILE = ".csv";

        public void exportGraph(Graph g, LinkedList<Step> path, String outputFile){
            try{
                PrintWriter writer = new PrintWriter(outputFile + EXTENSION_FILE, "UTF-8");
                String graphType = g.getType() == DIRECTED ? "DIRECTED" : "UNDIRECTED";
                writer.println(g.V() + ";" + g.E() + ";" + graphType);

                // On ecrit la liste de edges
                for(Step e : path)
                    if (e.getEdge() != null)
                        writer.println(e.getEdge().getFrom() + ";" + e.getEdge().getTo() + ";" + e.getEdge().getWeigth());

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
                int V, E;
                String TYPE;

                // On lit la premiere ligne pour connaitre les dimensions et le type de notre graphe
                V = scanner.nextInt();
                E = scanner.nextInt();
                TYPE = scanner.next();

                System.out.println("TYPE : " + TYPE);

                switch (TYPE){
                    case "DIRECTED":
                        g = new DiGraph(V, stockage);
                        break;
                    case "UNDIRECTED":
                        g = new UDiGraph(V, stockage);
                        break;
                    default:
                        throw new IOException("Graph Type unknow");
                }

                System.out.println("Import graph : nbNode = " + V + "; nbEdges = " + E + "; weigthed : " + TYPE);

                //  On lit les lignes suivante pour stocker le graphe

                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    for (int i = 0; i < E; i++) {
                        Vertex node1 = g.getVertex(scanner.nextInt());
                        Vertex node2 = g.getVertex((scanner.nextInt()));
                        double weigth = scanner.nextDouble();

                        g.addEdge(node1, node2, weigth);
                    }
                }

            } catch (IOException e) {
                System.err.print("Wrong input Format : " + e);
            }

            return g;
        }
}
