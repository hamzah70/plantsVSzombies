package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class PauseMenu {

    public void resumePress(ActionEvent event) throws IOException {
        System.out.println("Resume Pressed");

        Parent root = FXMLLoader.load(getClass().getResource("lawn.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void exitPress(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menuPage.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
