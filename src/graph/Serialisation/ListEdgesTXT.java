package graph.Serialisation;

import Algorithms.Utils.Step;
import graph.*;
import graph.Stockage.StockageType;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by francoisquellec on 17.05.17.
 */
public class ListEdgesTXT implements Serialiseur {
    static final String EXTENSION_FILE = ".txt";

    public void exportGraph(Graph g, LinkedList<Step> path, String outputFile){
        try{
            PrintWriter writer = new PrintWriter(outputFile + EXTENSION_FILE, "UTF-8");
            String graphType = g.getType() == DIRECTED ? "DIRECTED" : "UNDIRECTED";
            writer.println(g.V() + " " + g.E() + " " + graphType);

            // On ecrit la liste de edges
            for(Step e : path)
                if (e.getEdge() != null)
                    writer.println(e.getEdge().getFrom() + " " + e.getEdge().getTo() + " " + e.getEdge().getWeigth());

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Graph importGraph(String inputFile, StockageType stockage) throws IOException, NumberFormatException{
        Graph g;
        int V, E;
        String TYPE;

        BufferedReader br;
        String line;
        String csvSplitBy = " ";
        int nbEdges;

        br = new BufferedReader(new FileReader(inputFile));
        line = br.readLine();

        if (line == null)
            throw new IOException("Input File Empty");

        String [] structure = line.split(csvSplitBy);

        if (structure.length < 3)
            throw new IOException("ligne 1: Unsupported Format, has to be : {number of vertex; number of edges; Type(DIRECTED or UNDIRECTED)}");

        V = Integer.valueOf(structure[0]);
        E = Integer.valueOf(structure[1]);
        TYPE = structure[2];
        TYPE.toUpperCase();

        switch (TYPE){
            case "DIRECTED":
                g = new DiGraph(V, stockage);
                break;
            case "UNDIRECTED":
                g = new UDiGraph(V, stockage);
                break;
            default:
                throw new IOException("Graph Type unknown, has to be 'DIRECTED' or 'UNDIRECTED'");
        }

        nbEdges = 0;
        while ((line = br.readLine()) != null) {
            System.out.println("line while : " + line);
            if (++nbEdges > E)
                throw new IOException("Ligne " + nbEdges + ": Unsupported Format, Too many lines in the file, make sure the number of edges E is correct");

            // use comma as separator
            String[] edge = line.split(csvSplitBy);
            if(edge.length < 3)
                throw new IOException("Ligne " + nbEdges + ": Not enough arguments, has to be {start vertex; end vertex; weight}");

            Vertex node1 = g.getVertex(Integer.valueOf(edge[0]));
            Vertex node2 = g.getVertex(Integer.valueOf(edge[1]));
            double weight = Double.valueOf(edge[2]);

            System.out.println("Import edge : " + edge[0] + edge[1] + edge[2]);

            if (node1 == null)
                throw new IOException("Ligne " + nbEdges + ": Vertex " + Integer.valueOf(edge[0]) + " doesn't exist");
            if (node2 == null)
                throw new IOException("Ligne " + nbEdges + ": Vertex " + Integer.valueOf(edge[1]) + " doesn't exist");

            g.addEdge(node1, node2 , weight);
        }

        br.close();

        return g;
    }
}
