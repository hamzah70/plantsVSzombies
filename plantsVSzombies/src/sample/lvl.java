package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;



public class lvl {
    @FXML
    ImageView lvl0;
    @FXML
    ImageView lvl1;
    @FXML
    ImageView lvl2;

    @FXML
    public void levelSelect(MouseEvent event) throws IOException {
        System.out.println("level selected");

        ImageView levell = (ImageView) event.getSource();
        lawn.Game = null;
        String lvlStr = levell.getId();
        if (lvlStr.equals("lvl0")) {
            lawn.currLevel = 0;
        }
        else if (lvlStr.equals("lvl1")) {
            lawn.currLevel = 1;
        }
        else if (lvlStr.equals("lvl2")) {
            lawn.currLevel = 2;
        }
        else if (lvlStr.equals("lvl3")) {
            lawn.currLevel = 3;
        }
        else if (lvlStr.equals("lvl4")) {
            lawn.currLevel = 4;
        }

        Parent root = FXMLLoader.load(getClass().getResource("lawn.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
