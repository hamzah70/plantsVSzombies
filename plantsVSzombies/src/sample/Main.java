package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


class Menupage{

    private final BorderPane rootPane = new BorderPane() ; // or any other kind of pane, or  Group...

    public Menupage(Stage primaryStage) {

//        HBox hbox = new HBox();
//        StackPane hbox = new StackPane();
//        Button newGame = new Button("New Game");
//        Button loadGame = new Button("Load Game");
//        Button quitGame = new Button("Quit");
//
//
//        hbox.getChildren().add(newGame);
//        Scene scene = new Scene(hbox, 200,200);
////        scene.setRoot(hbox);
//
//
//        primaryStage.setScene(scene);
//        primaryStage.show();


        // build UI, register event handlers, etc etc

    }

    public Scene Menu() throws FileNotFoundException {

        VBox vbox = new VBox();
        StackPane root = new StackPane();

        Button newGame = new Button("New Game");
        Button loadGame = new Button("Load Game");
        Button quitGame = new Button("Quit");

        final ImageView selectedImage = new ImageView();
        Image image1 = new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/menu.jpg"));
        selectedImage.setImage(image1);

        vbox.getChildren().addAll(newGame, loadGame, quitGame);
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(selectedImage);
        root.getChildren().add(vbox);

        return new Scene(root);


    }

    public Pane getRootPane() {
        return rootPane ;
    }
}

class Homepage {
     void display(Stage primaryStage) throws Exception{
         Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
         primaryStage.setTitle("plantsVSzombies");

//         VBox root1 = new VBox();
//
//         Button startButton = new Button("Start Button");
//
//         final ImageView selectedImage = new ImageView();
//         Image image1 = new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/homePage.jpg"));
//
//         selectedImage. setImage(image1);
//
//         root1.getChildren().addAll(selectedImage);
////         root.getChildren().add(root1);
//
//         Scene scene = new Scene(root1);
//
//         scene.setOnMouseClicked(e -> {
//             Menupage menupage = new Menupage(primaryStage);
//             try {
//                 primaryStage.setScene(menupage.Menu());
//             } catch (FileNotFoundException e1) {
//                 e1.printStackTrace();
//             }
//         });


         Scene scenee = new Scene(root);
         primaryStage.setScene(scenee);

//         primaryStage.setScene(scene);
         primaryStage.sizeToScene();
         primaryStage.show();

     }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Homepage homepage = new Homepage();
        homepage.display(primaryStage);


    }


    public static void main(String[] args) {
        String currentDirectory = System.getProperty("user.dir");
        launch(args);
    }
}
