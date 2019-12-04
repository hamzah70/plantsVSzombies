package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    public void Homebutton(ActionEvent event) throws IOException {
        System.out.println("Hello World");
        String currentDirectory = System.getProperty("user.dir");
        currentDirectory = currentDirectory +"/src/sample/menuPage.fxml";
        Parent root = FXMLLoader.load(getClass().getResource("menuPage.fxml"));

        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
//        stage.setScene(scene);

    }


}
