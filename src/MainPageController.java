/**
 * Created by LBX on 31/03/2017.
 */

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;


import java.security.PublicKey;

public class MainPageController {
    private MainApp mainApp;
    @FXML
    private TabPane tabPane;

    private int indiceTab;

    public MainPageController(){
        indiceTab = 0;
    }
    @FXML
    private void initialize() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        DraggingTabPaneSupport support = new DraggingTabPaneSupport();
        support.addSupport(tabPane);
    }
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void handleLaunch(){
        mainApp.showAlgoPage();
    }
    @FXML
    private void handleNew(){
        Tab tab = new Tab();
        tab.setText("new tab"+ indiceTab++);
        // Ajouter tout ce qu'on veut dans la  tab
        AnchorPane pane = new AnchorPane();
        tab.setContent(pane);
        pane.getChildren().add(newNode(pane));
        tabPane.getTabs().add(tab);


    }
    // petit test
    private Circle newNode(AnchorPane pane){
        class Cercle extends Circle {
            public Cercle(AnchorPane pane){
                super(pane.getLayoutX(), pane.getLayoutY(), 40);
                this.setFill(Paint.valueOf("BLUE"));
            }
        }
        return new Cercle(pane);
    }

    public void handleNewFromAlgoPage(){
        handleNew();
    }
    @FXML
    private void handleSave(){
    }
    @FXML
    private void handleOpen(){
    }
    @FXML
    private void handleImport(){
    }
    @FXML
    private void handleVectrice(){
    }
    @FXML
    private void handleVectrice2(){
    }
    @FXML
    private void handleEdge(){
    }
    @FXML
    private void handleDiEdge(){
    }
    @FXML
    private void handleDiEdgeWeighted(){
    }
    @FXML
    private void handleText(){
    }
    @FXML
    private void handleNote(){
    }



}
