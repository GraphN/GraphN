package mainProgram;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import view.*;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

public class MainApp extends Application {

    private Stage algoStage;
    private Stage helpStage;
    private Stage primaryStage;
    private AnchorPane rootLayout;
    private MainPageController mainPageController;

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("GraphN");
        // Set app icon
        this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/assets/img/logo.png")));

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout()
    {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/assets/fxml/mainPage.fxml"));
            rootLayout = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);

            primaryStage.setScene(scene);
            primaryStage.show();

            mainPageController = loader.getController();
            mainPageController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlgoPage(GraphDom graph)
    {
        try {

            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/assets/fxml/algorithmPage.fxml"));

            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            algoStage = new Stage();
            algoStage.setTitle("View");
            algoStage.initModality(Modality.WINDOW_MODAL);
            algoStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            algoStage.setScene(scene);
            algoStage.setMaximized(true);
            algoStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("assets/img/logo.png")));


            AlgorithmPageController controller = loader.getController();
            controller.setMainApp(this);

            //setting the graph pane to the algo page
            controller.setGraph(graph);

            //set the mainwindow grey
            mainPageController.setGreyMain(true);

            // Show the dialog and wait until the user closes it
            algoStage.showAndWait();

            //we change the grey color of parent stage
             mainPageController.setGreyMain(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int showWeightPage()
    {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/assets/fxml/weight.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage weightStage = new Stage();
            weightStage.setTitle("Weight");
            weightStage.setResizable(false);
            weightStage.initModality(Modality.WINDOW_MODAL);
            weightStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            weightStage.setScene(scene);

            WeightController controller = loader.getController();
            controller.setStage(weightStage);

            weightStage.showAndWait();

            return controller.getWeight();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int showVertex(int nbVertex, String text) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/assets/fxml/Vertex.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage startVertexStage = new Stage();
            startVertexStage.setTitle("VertexNumber");
            startVertexStage.setResizable(false);
            startVertexStage.initModality(Modality.WINDOW_MODAL);
            startVertexStage.initOwner(algoStage);
            Scene scene = new Scene(page);
            startVertexStage.setScene(scene);

            VertexController controller = loader.getController();
            controller.setStage(startVertexStage);
            controller.setNbVertex(nbVertex);
            controller.setText(text);
            startVertexStage.showAndWait();

            return controller.getVertex();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void showSavePage(GraphDom graph)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Graph");

        //Set extension filter
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("GraphN files (*.graphn)", "*.graphn");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(xmlFilter);
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.getExtensionFilters().add(jpgFilter);

        //set initial directory
        File directory = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        fileChooser.setInitialDirectory(directory);

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            switch (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.') + 1)) {
                case "jpg":
                case "png":
                    WritableImage imagePNG = mainPageController.getGraphPane().snapshot(new SnapshotParameters(), null);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(imagePNG, null), "png", file);
                    } catch (IOException e) {
                        // TODO: handle exception here
                    }
                    break;
                case "graphn":
                    try {
                        graph.saveGraphXML(file);
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.err.println("Wrong input type file !");
            }
        }
    }

    public void showImportPage()
    {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/assets/fxml/Import.fxml"));
            //loader.setLocation(mainProgram.MainApp.class.getResource("algoPage.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            algoStage = new Stage();
            algoStage.setTitle("Import");
            algoStage.initModality(Modality.WINDOW_MODAL);
            algoStage.initOwner(primaryStage);
            algoStage.setResizable(false);
            Scene scene = new Scene(page);
            algoStage.setScene(scene);


            ImportController controller = loader.getController();
            controller.setMainApp(this);

            // Show the dialog and wait until the user closes it
            algoStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            //return false;
        }

    }

    public GraphDom showOpenPage() throws ParserConfigurationException {
        GraphDom graphDom;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph");

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GraphN files (*.graphn)", "*.graphn");
        fileChooser.getExtensionFilters().add(extFilter);

        //set initial directory
        File directory = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        fileChooser.setInitialDirectory(directory);

        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null)
        {
            //set the name of the graphdom
            String fileName = file.getName();
            String [] fileN = fileName.split("\\.");
            graphDom = new GraphDom(fileN[0]);

            //read the xml in this file, and create view.GraphDom
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try
            {
                final DocumentBuilder builder = factory.newDocumentBuilder();
                final Document document = builder.parse(file);
                final Element racine = document.getDocumentElement();
                final NodeList nodes = racine.getChildNodes();

                int vertexAdded = 0;
                //for each node in racine add it to the graphdom if it is vertex or edge

                graphDom.setGraphType(racine.getAttribute("graphType"));
                for(int i = 0; i < nodes.getLength(); i++)
                {
                    //this if is very important. when a node is equal to #text its just that it is a jump the line,
                    // so we have to test if the current node is not a jump the line
                    if(!nodes.item(i).getNodeName().equals("#text"))
                    {
                        Element currentNode = (Element) nodes.item(i);
                        if (currentNode.getNodeName().equals("vertex"))
                        {
                            graphDom.addVertex(Integer.valueOf(currentNode.getAttribute("posX")),
                                    Integer.valueOf(currentNode.getAttribute("posY")));

                            // change the name of the vertex to have good one (corresponding of the xml file)
                            graphDom.setNameOfVertex(currentNode.getAttribute("name"), vertexAdded);

                            vertexAdded++;
                        }
                        else if (currentNode.getNodeName().equals("edges_group"))
                        {
                            NodeList gNodes = currentNode.getChildNodes();
                            for(int j = 0; j < gNodes.getLength(); j++) {
                                if(gNodes.item(j).getNodeName().equals("#text"))
                                    continue;
                                if(graphDom.getGraphType().equals("weightedDiGraph") || graphDom.getGraphType().equals("weightedNonDiGraph")) {
                                    graphDom.addDiWeightedEdge(((Element) gNodes.item(j)).getAttribute("start"),
                                            ((Element) gNodes.item(j)).getAttribute("end"),
                                            ((Element) gNodes.item(j)).getAttribute("weight"));
                                }
                                else {
                                    graphDom.addDiEdge(((Element) gNodes.item(j)).getAttribute("start"),
                                            ((Element) gNodes.item(j)).getAttribute("end"));
                                }
                            }
                        }
                    }
                }
            }
            catch (final ParserConfigurationException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (org.xml.sax.SAXException e) {
                e.printStackTrace();
            }
        return graphDom;
        }
        return null;
    }



    public void newFromAlgoPage(GraphDom graphAlgoPage){
        mainPageController.handleNewFromAlgoPage(graphAlgoPage);
        algoStage.close();
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}