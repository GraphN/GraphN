package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Controller {
    @FXML
    private Pane workspace;
    private double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    @FXML
    public void initialize() {
        Circle circle_Red = new Circle(50.0f, Color.RED);
        circle_Red.setCenterX(workspace.getLayoutX());
        circle_Red.setCenterY(workspace.getLayoutY());
        circle_Red.setOnMousePressed(circleOnMousePressedEventHandler);
        circle_Red.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        workspace.getChildren().add(circle_Red);

        Group root = new Group();
        root.getChildren().add(circle_Red);
        workspace.getChildren().add(root);
    }

    EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
                    orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    System.out.println(workspace.getWidth()-((Circle)(t.getSource())).getRadius());
                    if(newTranslateX < 0)
                        newTranslateX = 0;

                    if(newTranslateX > workspace.getWidth())
                        newTranslateX = workspace.getWidth();

                    ((Circle)(t.getSource())).setTranslateX(newTranslateX);
                    ((Circle)(t.getSource())).setTranslateY(newTranslateY);
                }
            };
}
