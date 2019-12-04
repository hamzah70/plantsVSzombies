package sample;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Random;



abstract class Plant implements Serializable{
    private double posX;
    private double posY;
    private transient ImageView plantImage;
    transient TilePane tile = null;
    boolean place = true;
    String TileId = "";
    String imgPath;


    public void setPlantImage(ImageView plantImage) {
        this.plantImage = plantImage;
    }

    private Double Health = 100.0;
    transient Timeline timeline=null;
    String currentDirectory = System.getProperty("user.dir");
    Plant(ImageView plantImage, double x, double y) {
        this.plantImage = plantImage;
        this.posX = x;
        this.posY = y;
    }
    @FXML
    transient Label sunTokenCount;

    @FXML
    transient AnchorPane anchor;

    public ImageView getPlantImage() {
        return plantImage;
    }
    public Double getHealth() {
        return Health;
    }

    public void setHealth(Double health) {
        Health = health;
    }

    public Double getposY() {
        return posY;
    }

    public void setposY(Double posY) {
        posY = posY;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    abstract void usePower(Object T);


}

class PeaShooter extends Plant implements Serializable {


    PeaShooter(ImageView plantImage, double x, double y) {
        super(plantImage, x, y );
        super.imgPath = "/src/sample/PvZ_PNGs/Plants/peashooter/peashooter_moving.gif";
    }
    @Override
    void usePower(Object o){

    }
}

class CherryBomb extends Plant implements Serializable {
    CherryBomb(ImageView plantImage, double x, double y) {
        super(plantImage, x, y);
        super.imgPath = "/src/sample/PvZ_PNGs/Plants/cherrybomb/cherrybomb_1.png";
    }

    @Override
    void usePower(Object o){

    }
}

class PotatoMine extends Plant implements Serializable {

    PotatoMine(ImageView plantImage, double x, double y) {
        super(plantImage, x, y);
        super.imgPath = "/src/sample/PvZ_PNGs/Plants/potatomine/potatomine_1_armed.png";
    }
    @Override
    void usePower(Object o){

    }
}

class Sunflower extends Plant implements Serializable {
    Sunflower(ImageView plantImage, double x, double y) {
        super(plantImage, x, y);
        super.imgPath = "/src/sample/PvZ_PNGs/Plants/sunflower/sunflower_moving.gif";
    }
    @Override
    void usePower(Object o){

    }
}

class Walnut extends Plant implements Serializable {
    Walnut(ImageView plantImage, double x, double y) {
        super(plantImage, x , y);
        super.imgPath = "/src/sample/PvZ_PNGs/Plants/wallnut/wallnut_1.png";
    }
    @Override
    void usePower(Object o){

    }
}

