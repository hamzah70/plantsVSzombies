package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class createUserController {
    public void goBack(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuPage.fxml"));

        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void createUser(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("lawn.fxml"));

        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
