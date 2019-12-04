package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.event.ActionEvent;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MenuPage {


    @FXML
    public void startPress(ActionEvent event) throws IOException {
        System.out.println("Start Pressed");
        lawn.Game=null;
        lawn.frenzy = false;




        Parent root = FXMLLoader.load(getClass().getResource("lawn.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void LoadPress(ActionEvent event) throws IOException, ClassNotFoundException {
        System.out.println("Load Pressed");

        FileInputStream fis = new FileInputStream("object.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        lawn.Game = (lawn) ois.readObject();


        Parent root = FXMLLoader.load(getClass().getResource("lawn.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void quitPress(ActionEvent event){
        System.exit(0);
    }

    public void levelSelectOpen(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("levelPage.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void frenzy(ActionEvent event) throws IOException {
        lawn.frenzy = true;
        lawn.Game = null;

        Parent root = FXMLLoader.load(getClass().getResource("lawn.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}

