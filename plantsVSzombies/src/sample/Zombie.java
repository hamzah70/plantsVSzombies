package sample;


import javafx.animation.Timeline;
import javafx.scene.image.ImageView;

import java.io.Serializable;



public class Zombie implements Serializable{
    int move = 1;
    private double health ;
    private double posX;
    private double posY;
    private String type;
    int lane = 0;

    transient Timeline timeline = null;
    transient ImageView zombie=null;
    int[] yrange = new int[5];
    String imgPath;
    int height = 110;
    int width = 93;


    void setHealth(double val){
        health=val;
    }



    double getHealth(){
        return health;
    }


    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
}

class ConeHead extends Zombie implements Serializable{
    ConeHead(){
        super();
        super.setHealth(200);
        super.height = 60;
        super.width = 40;
        super.imgPath = "/src/sample/PvZ_PNGs/Zombies/conehead/conehead_zombie_moving.gif";
        yrange[0] = 66;
        yrange[1] = 145;
        yrange[2] = 215;
        yrange[3] = 289;
        yrange[4] = 357;
//        yrange = {64, 142, 215, 280, 357 };
    }
}

class NormalZombie extends Zombie implements Serializable{
    String eatingGIFpath = "";
    NormalZombie() {
        super();
        super.setHealth(100);
        super.height = 60;
        super.width = 40;
        super.imgPath = "/src/sample/PvZ_PNGs/Zombies/normal/zombie_normal.gif";


        yrange[0] = 66;
        yrange[1] = 145;
        yrange[2] = 215;
        yrange[3] = 289;
        yrange[4] = 357;

//        yrange = {66, 145, 215, 289, 357}
    }
}

class FootballZombie extends Zombie implements Serializable{
    FootballZombie() {
        super();
        super.setHealth(300);
        super.height = 70;
        super.width = 52;
        super.imgPath = "/src/sample/PvZ_PNGs/Zombies/Footballer/zombie_football.gif";


        yrange[0] = 66;
        yrange[1] = 145;
        yrange[2] = 215;
        yrange[3] = 289;
        yrange[4] = 357;

//        yrange = {66, 145, 215, 289, 357}
    }
}

class BalloonZombie extends Zombie implements Serializable {
    BalloonZombie() {
        super();
        super.setHealth(400);
        super.height = 70;
        super.width = 52;
        super.imgPath = "/src/sample/PvZ_PNGs/Zombies/Balloon/BalloonZombie.gif";


        yrange[0] = 66;
        yrange[1] = 145;
        yrange[2] = 215;
        yrange[3] = 289;
        yrange[4] = 357;

//        yrange = {66, 145, 215, 289, 357}
    }
}

class FlagZombie extends Zombie implements Serializable{
    FlagZombie() {
        super();
        super.setHealth(400);
        super.height = 70;
        super.width = 52;
        super.imgPath = "/src/sample/PvZ_PNGs/Zombies/FlagZombie/FlagZombie.gif";


        yrange[0] = 66;
        yrange[1] = 145;
        yrange[2] = 215;
        yrange[3] = 289;
        yrange[4] = 357;

//        yrange = {66, 145, 215, 289, 357}
    }
}


