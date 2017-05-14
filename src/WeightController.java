import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * Created by LBX on 14/05/2017.
 */
public class WeightController {
    Stage dialogStage;
    int weight;
    @FXML
    private Button sendButton;
    @FXML
    private TextField weightField;
    @FXML
    public void handleSendButton(){
        weight = Integer.parseInt(weightField.getText().replaceAll("[\\D]", ""));
        dialogStage.close();
    }
    @FXML
    private void keyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            handleSendButton();
    }
    int getWeight(){
        return weight;
    }
    public void setStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

}
