
import java.awt.*;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class MainApp extends Application {

    private Stage primaryStage;
    private Stage algoStage;
    private AnchorPane rootLayout;
    private MainPageController mainPageController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("GraphN");

        primaryStage.setFullScreen(true);
        initRootLayout();

        //showPersonOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("mainPage.fxml"));
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
    // dans le futur, passer un graphe en paramètre
    public void showAlgoPage() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("algorithmPage.fxml"));
            //loader.setLocation(MainApp.class.getResource("algoPage.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            algoStage = new Stage();
            algoStage.setTitle("View");
            algoStage.initModality(Modality.WINDOW_MODAL);
            algoStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            algoStage.setScene(scene);

            //AlgoPageController controller = loader.getController();
            AlgorithmPageController controller = loader.getController();
            controller.setMainApp(this);

            // Set the person into the controller.
            /*PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);*/

            //set the mainwindow grey
            mainPageController.setGreyMain(true);
            // Show the dialog and wait until the user closes it
            algoStage.showAndWait();

            //we change the grey color of parent stage
             mainPageController.setGreyMain(false);
            //return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            //return false;
        }
    }
    public void newTab(){
        mainPageController.handleNewFromAlgoPage();
        algoStage.close();
    }


    /**
     * Shows the person overview inside the root layout.
     */
   /* public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
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