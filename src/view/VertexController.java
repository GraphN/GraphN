package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by LBX on 14/05/2017.
 */
public class VertexController {
    Stage dialogStage;
    int vertex;
    int nbVertex;
    @FXML
    private Button sendButton;
    @FXML
    private TextField textField;
    @FXML
    private Text text;
    @FXML
    public void handleSendButton(){
        try {
            vertex = Integer.parseInt(textField.getText().replaceAll("[\\D]", ""));
            if(vertex > nbVertex || vertex < 0) return;
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        dialogStage.close();
    }
    @FXML
    private void keyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            handleSendButton();
    }

    public void setNbVertex(int nbVertex) {
        this.nbVertex = nbVertex;
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public int getVertex(){
        return vertex;
    }
    public void setStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
}
