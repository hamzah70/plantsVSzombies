package sample;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.Node;


import java.io.*;
import java.net.URL;

import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


import static java.lang.Math.abs;
import static java.lang.Math.random;
import static javafx.util.Duration.seconds;

class Progress implements Serializable{
    double val;
    static Progress progress = new Progress();


    public static Progress getProgress() {
        return progress;
    }

    private Progress(){
        val=400;
    }
}



class Level implements Serializable{
    private int level=0;
    ArrayList<Zombie>[] zombieLane = new ArrayList[5];
    ArrayList<Plant>[] plantLane = new ArrayList[5];
    int zombiesSpawned = 0;

    private int zombiestoSpawn=10;
    int zombieCount = level * 10;

    public Level() {
        for(int i=0;i<5;i++) {
            zombieLane[i]=new ArrayList<Zombie>();
            plantLane[i]=new ArrayList<Plant>();
        }
    }


    public int getZombiestoSpawn() {
        return zombiestoSpawn;
    }

    public void setZombiestoSpawn(int zombiestoSpawn) {
        this.zombiestoSpawn = zombiestoSpawn;
    }



    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}




class Lawnmower implements Serializable{

    private boolean use=false;
    int lane;
    double x;
    double y;
    transient ImageView mower;

    public Lawnmower(int lane, ImageView mower) {
        this.lane = lane;
        this.mower = mower;
        x = mower.getLayoutX();
        y = mower.getLayoutY();
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }


}

class Suntoken implements Serializable{
    private double count=300;

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}



public class lawn implements Initializable, Serializable {

//    private static final long serialVersionUID = -1805330704134480115L;


    lawn lawnGame = null;

    @FXML
    transient ImageView lawn1;
    @FXML
    transient ImageView lawn2;
    @FXML
    transient ImageView lawn3;
    @FXML
    transient ImageView lawn4;
    @FXML
    transient ImageView lawn5;
    @FXML
    transient ImageView peaShooterToken;
    @FXML
    transient ImageView sunflowerToken;
    @FXML
    transient ImageView wallnutToken;
    @FXML
    transient ImageView potatoToken;
    @FXML
    transient ImageView cherryToken;
    @FXML
    transient ImageView repeaterToken;
    @FXML
    transient AnchorPane anchor;
    @FXML
    transient AnchorPane pause;
    @FXML
    transient ArrayList<ImageView> sunToken_list = new ArrayList<>();
    @FXML
    transient Label sunTokenCount;
    @FXML
    transient ProgressBar progressBar;
    @FXML
    transient AnchorPane gameWonPage;
    @FXML
    transient AnchorPane gameRestartPage;

    @FXML
    transient TilePane t00, t01, t02, t03, t04, t05, t06, t07;

    @FXML
    transient TilePane t10, t11, t12, t13, t14, t15, t16, t17;

    @FXML
    transient TilePane t20, t21, t22, t23, t24, t25, t26, t27;

    @FXML
    transient TilePane t30, t31, t32, t33, t34, t35, t36, t37;

    @FXML
    transient TilePane t40, t41, t42, t43, t44, t45, t46, t47;

    transient TilePane[][] grid = new TilePane[5][];

    int laneProgress = 0;

    boolean gameWin;

    static lawn Game=null;
    static boolean frenzy = false;


    transient Progress progress = Progress.getProgress();


    Level level = new Level();
    Suntoken suntoken = new Suntoken();

    ArrayList<Plant> sunflower = new ArrayList<>();

    ArrayList<Zombie> zombie = new ArrayList<Zombie>();

    ArrayList<Lawnmower> lawnmowers = new ArrayList<>();

    String currentDirectory = System.getProperty("user.dir");
//    currentDirectory = currentDirectory +"/src/sample/menuPage.fxml";

    ArrayList<ImageView> pea_list = new ArrayList<>();
    ArrayList<Double> pea_list_coordinateX = new ArrayList<>();
    ArrayList<Double> pea_list_coordinateY = new ArrayList<>();
    transient ArrayList<Timeline> allTimeline = new ArrayList<>();

//    public void setMenupage(Menupage menupage) {
//        this.menupage = menupage;
//    }

    boolean gameLost = false;

    transient ImageView selectedPlant;

    public void lawnMower_move(Lawnmower mower, ImageView lawnMower) {

        Timeline lawnmowerMove = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lawnMower.setLayoutX(lawnMower.getLayoutX()+3);
                mower.x = lawnMower.getLayoutX();
                mower.y = lawnMower.getLayoutY();
                mower.setUse(true);
                for(int i=0;i<level.zombieLane[mower.lane].size();i++){
                    if(intersects(lawnMower, level.zombieLane[mower.lane].get(i).zombie)){
                        Zombie zombieTemp = level.zombieLane[mower.lane].remove(i);
                        level.zombieCount-=1;
                        zombieTemp.setHealth(-10);
                        zombieTemp.zombie.setVisible(false);
                        zombieTemp.zombie.setDisable(true);
                        anchor.getChildren().remove(zombieTemp.zombie);
                        zombieTemp.timeline.pause();
                    }
                }

            }
        }));
        lawnmowerMove.setCycleCount(Timeline.INDEFINITE);
        lawnmowerMove.play();

        allTimeline.add(lawnmowerMove);
    }



    public Timeline zombieMove(ImageView zombieImage, Zombie zombie) {
        Timeline gameWon = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("ZOMBIE TO KILL ----  " + level.zombieCount);
                if(level.zombieCount<=0) {
                    gameWin = true;
                    lawn.Game = null;
                    lawn.frenzy = false;
                    for(int i=0; i<allTimeline.size(); i++){
                        allTimeline.get(i).pause();
                    }
                    gameWonPage.setDisable(false);
                    gameWonPage.setVisible(true);
                }
            }
        }));
        gameWon.setCycleCount(Timeline.INDEFINITE);
        gameWon.play();
        allTimeline.add(gameWon);




        Timeline zombiee = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                zombieImage.setLayoutX(zombieImage.getLayoutX()-(2*zombie.move));

                if(zombieImage.getLayoutX()<=-40){
                    gameLost = true;
                }

                zombie.setPosX(zombieImage.getLayoutX());
                zombie.setPosY(zombieImage.getLayoutY());

                for (int i = 0; i < level.plantLane[zombie.lane].size(); i ++) {
                    if(!(level.plantLane[zombie.lane].get(i) instanceof PotatoMine) && abs(level.plantLane[zombie.lane].get(i).getPosX()-zombieImage.getLayoutX()) <= 5){
                        zombie.move=0;
                        level.plantLane[zombie.lane].get(i).setHealth(level.plantLane[zombie.lane].get(i).getHealth()-5);
                        System.out.println("Plant Health: "+level.plantLane[zombie.lane].get(i).getHealth());


                        if(level.plantLane[zombie.lane].get(i).getHealth()<=0.0) {
                            Plant removePlant = level.plantLane[zombie.lane].remove(i);
                            System.out.println("HELLO");
//                            try {
                                System.out.println("HAMZAH");
                                removePlant.getPlantImage().setVisible(false);
                                removePlant.getPlantImage().setDisable(true);
                                removePlant.setPlantImage(null);
                                zombie.move = 1;
                                if(removePlant instanceof PeaShooter||removePlant instanceof Sunflower) {
                                    removePlant.timeline.stop();
                                }
//                              removePlant.timeline.stop();
                                System.out.println("PLANT READY TO BE RREMOVEDDD");
//                                anchor.getChildren().remove(removePlant.getPlantImage());
                                removePlant.tile.getChildren().remove(removePlant.getPlantImage());
//                            }
//                            catch (Exception e) { }
                            break;
                        }
                    }
                    else {
                        zombie.move = 1;
                    }
                }

                if(intersects(zombieImage, lawnmowers.get(zombie.lane).mower)){
                    lawnMower_move(lawnmowers.get(zombie.lane), lawnmowers.get(zombie.lane).mower);
                }

            }
        }));
        zombiee.setCycleCount(Timeline.INDEFINITE);
        zombiee.play();
        allTimeline.add(zombiee);


        return zombiee;



    }

    public void selectPlant(MouseEvent event) {
        this.selectedPlant = (ImageView) event.getSource();

        selectedPlant.setDisable(true);
        ImageView f = selectedPlant;

        Timeline tileDisable = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                f.setDisable(false);
            }
        }));
        tileDisable.setCycleCount(1);
        tileDisable.play();

        if(level.getLevel()==0) {
            if (selectedPlant.equals(potatoToken) || selectedPlant.equals(cherryToken) || selectedPlant.equals(wallnutToken) || selectedPlant.equals(sunflowerToken)){
                selectedPlant = null;
            }
        }
        else if(level.getLevel()==1){
            if (selectedPlant.equals(potatoToken) || selectedPlant.equals(cherryToken) || selectedPlant.equals(wallnutToken)){
                selectedPlant = null;
            }
        }
        else if(level.getLevel()==2){
            if (selectedPlant.equals(potatoToken) || selectedPlant.equals(cherryToken) ){
                selectedPlant = null;
            }
        }
        else if(level.getLevel()==3){
            if (selectedPlant.equals(cherryToken)){
                selectedPlant = null;
            }
        }

    }





    public Timeline peaMove(ImageView pea, int laneNumber){

        Timeline peaM = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                pea.setLayoutX(pea.getLayoutX()+5);



//                for(int i=0;i<5;i++) {
                    for ( int j = 0 ; j < level.zombieLane[laneNumber].size() ; j++ ) {
                        ImageView zombietemp = level.zombieLane[laneNumber].get(j).zombie;
                        if (intersects(pea,zombietemp)) {
//                        if (zombietemp.getLayoutBounds().intersects(pea.getLayoutBounds())) {

                            System.out.println("");
                            System.out.println("xxxx ----- " + pea.getLayoutBounds().getMaxX());


                            anchor.getChildren().remove(pea);
                            pea.setVisible(false);
                            pea.setDisable(true);
                            pea.setImage(null);
                            pea.setLayoutX(10000);
                            pea.setLayoutY(10000);

                            level.zombieLane[laneNumber].get(j).setHealth(level.zombieLane[laneNumber].get(j).getHealth() - 20.0);
                            System.out.println("Health -------  " + level.zombieLane[laneNumber].get(j).getHealth());
                            if (level.zombieLane[laneNumber].get(j).getHealth() <= 0) {
                                anchor.getChildren().remove(zombietemp);
                                level.zombieLane[laneNumber].get(j).timeline.pause();
                                level.zombieLane[laneNumber].get(j);
                                level.zombieLane[laneNumber].remove(j);
                                level.zombieCount-=1;
                                break;
                            }
//
                        }


                    }
//                }
            }
        }));
        peaM.setCycleCount(Timeline.INDEFINITE);
        peaM.play();
        allTimeline.add(peaM);

        return peaM;

    }

    public boolean intersects(ImageView a,ImageView b) {

        if(b==null){
            System.out.println("THE VALUE OF b is null");
        }
        else if(a==null){
            System.out.println("THE VALUE OF a is null");
        }
        if(b.getLayoutX()-a.getLayoutX()<=5 && b.getLayoutX()-a.getLayoutX()>=-5) {
            return true;
        }
        return false;
    }

    public void sunMove(ImageView sun){

        double minY=0;
        double maxY=360;
        double coordy = (int)(360.0 * Math.random()) + 11;


        Timeline sunmove = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(sun.getLayoutY()<=coordy){
                    sun.setLayoutY(sun.getLayoutY()+0.5);
                }
            }
        }));
        sunmove.setCycleCount(Timeline.INDEFINITE);
        sunmove.play();

        allTimeline.add(sunmove);

    }

    public Timeline potatoBlast(ImageView me, Plant potato, int laneNum){
        Timeline potatoBOOMBOOM = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                boolean fl = false;
                for (int i = 0; i < level.zombieLane[laneNum].size(); i ++) {
                    System.out.println("POTATOOOOOOO");
//                    if (intersects(me, level.zombieLane[laneNum].get(i).zombie) ) {
                    if (abs(potato.getPosX() - level.zombieLane[laneNum].get(i).zombie.getLayoutX())<=5 ) {
                        System.out.println("POTATOOOOO  COLLISIONN");
                        fl = true;
                        Zombie removeZombie = level.zombieLane[laneNum].remove(i);
                        removeZombie.move=0;
                        level.zombieCount-=1;
                        removeZombie.setHealth(0);
                        removeZombie.zombie.setVisible(false);
                        removeZombie.zombie.setDisable(true);
                        anchor.getChildren().remove(removeZombie.zombie);
                        removeZombie.timeline.pause();
                        }
                }
                if (fl) {
                    potato.setPlantImage(null);
                    me.setVisible(false);
                    me.setDisable(true);
                    potato.place = false;
                    anchor.getChildren().remove(me);
                    potato.tile.getChildren().removeAll();
                    potato.timeline.stop();
                }
            }
        }));
        potatoBOOMBOOM.setCycleCount(Timeline.INDEFINITE);
        potatoBOOMBOOM.play();
        allTimeline.add(potatoBOOMBOOM);

        return potatoBOOMBOOM;
    }

    public Timeline cherryBlast(ImageView me, Plant cherry){
        Timeline cherryBOOMBOOM = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for ( int i = 0 ; i < 5 ; i++ ) {
                    for ( int j = 0 ; j < level.zombieLane[i].size() ; j++ ) {
                        if (abs(cherry.getPosX() - level.zombieLane[i].get(j).zombie.getLayoutX()) <= 100 && abs(cherry.getposY() - level.zombieLane[i].get(j).zombie.getLayoutY()) <= 100) {
                            Zombie removeZombie = level.zombieLane[i].remove(j);
                            j--;
                            removeZombie.move = 0;
                            level.zombieCount -= 1;
                            removeZombie.setHealth(0);
                            removeZombie.zombie.setVisible(false);
                            removeZombie.zombie.setDisable(true);
                            anchor.getChildren().remove(removeZombie.zombie);
                            removeZombie.timeline.pause();
                        }
                    }
                }

                cherry.setPlantImage(null);
                me.setVisible(false);
                me.setDisable(true);
                anchor.getChildren().remove(me);
                cherry.setHealth(0.0);
                cherry.place = false;
                cherry.tile.getChildren().removeAll();
                cherry.timeline.stop();
            }

       }));
        cherryBOOMBOOM.setCycleCount(Timeline.INDEFINITE);
        cherryBOOMBOOM.play();
        allTimeline.add(cherryBOOMBOOM);

        return cherryBOOMBOOM;
    }

    int getLane(TilePane currTile) {
        int lane = 0;
        if(currTile.getId().equals("t00") || currTile.getId().equals("t01")|| currTile.getId().equals("t02")||
                currTile.getId().equals("t03")|| currTile.getId().equals("t04")|| currTile.getId().equals("t05")||
                currTile.getId().equals("t06")|| currTile.getId().equals("t07")){
            lane = 0;

        }
        else if(currTile.getId().equals("t10") || currTile.getId().equals("t11")|| currTile.getId().equals("t12")||
                currTile.getId().equals("t13")|| currTile.getId().equals("t14")|| currTile.getId().equals("t15")||
                currTile.getId().equals("t16")|| currTile.getId().equals("t17")){
            lane = 1;
        }
        else if(currTile.getId().equals("t20") || currTile.getId().equals("t21")|| currTile.getId().equals("t22")||
                currTile.getId().equals("t23")|| currTile.getId().equals("t24")|| currTile.getId().equals("t25")||
                currTile.getId().equals("t26")|| currTile.getId().equals("t27")){
            lane = 2;
        }
        else if(currTile.getId().equals("t30") || currTile.getId().equals("t31")|| currTile.getId().equals("t32")||
                currTile.getId().equals("t33")|| currTile.getId().equals("t34")|| currTile.getId().equals("t35")||
                currTile.getId().equals("t36")|| currTile.getId().equals("t37")){
            lane = 3;
        }
        else if(currTile.getId().equals("t40") || currTile.getId().equals("t41")|| currTile.getId().equals("t42")||
                currTile.getId().equals("t43")|| currTile.getId().equals("t44")|| currTile.getId().equals("t45")||
                currTile.getId().equals("t46")|| currTile.getId().equals("t47")){
            lane = 4;
        }
        return lane;
    }

    public Timeline sunSpawn(Sunflower sunflower1){
        double time = 25;
        if(this.frenzy){
            time=0.5;
        }
        Timeline sunSpawn = new Timeline(new KeyFrame(Duration.seconds(time), new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {
                try {
                    ImageView sunToken = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Lawn/sun.png")));
                    sunToken.setLayoutY(sunflower1.getposY() + 28);
                    sunToken.setLayoutX(sunflower1.getPosX());
//                        sunToken.setFitHeight(10);
                    sunToken.setFitWidth(32);

                    sunToken.setFitHeight(45);
                    System.out.println("sunssssswidth  " + sunToken.getFitWidth());
                    System.out.println("sunssssheight  " + sunToken.getFitHeight());
                    anchor.getChildren().add(sunToken);

                    sunToken.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            sunToken.setVisible(false);
                            int tokencount = Integer.valueOf(sunTokenCount.getText());
                            tokencount = tokencount + 25;
                            sunTokenCount.setText(String.valueOf(tokencount));

                            suntoken.setCount(Integer.valueOf(sunTokenCount.getText()));
                        }
                    });

                    if (sunflower1.getHealth() <= 0) {
                        sunflower1.timeline.stop();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }));
        sunSpawn.setCycleCount(Timeline.INDEFINITE);
        sunSpawn.play();
        return sunSpawn;
    }

    public Timeline peaSHOOT(int finalLane, TilePane currTile, Plant peashooter){
        Timeline peaShoot = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (level.zombieLane[finalLane].size() > 0) {
                    ImageView pea = null;
                    try {
                        pea = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Plants/peashooter/pea.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println("inside");
                    pea.setLayoutX(currTile.getLayoutX() + 30);
                    pea.setLayoutY(currTile.getLayoutY() + 20);
                    pea.setFitWidth(20);
                    pea.setFitHeight(20);
                    anchor.getChildren().add(pea);
                    Timeline peaM = peaMove(pea, finalLane);

                    //
                    ImageView finalPea = pea;

                    if (peashooter.getHealth() <= 0) {
                        peaM.setCycleCount(0);
                        peaM.stop();
//                        peaShoot.stop();
                    }
                }
                System.out.println("Pea is shot every 5 seconds using pea_thread");


            }
        }));
        peaShoot.setCycleCount(Timeline.INDEFINITE);
        peaShoot.play();
        return peaShoot;
    }

    public void potPlant(MouseEvent event) throws FileNotFoundException {
//        this.scene = (event.getSource()).getScene();

        if (selectedPlant == null) {
            System.out.println(("Error in selecting tokens"));
        }
        else if (selectedPlant.equals(peaShooterToken) && suntoken.getCount()>=100) {
            System.out.println("shooter");
            TilePane currTile = (TilePane) event.getSource();
            System.out.println("TILEPANE   ID ------  "+currTile.getId());

            if(currTile.getChildren().size()==0) {

//            ImageView plantToPot = new ImageView(new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/PvZ_PNGs/Plants/peashooter/peashooter_moving.gif")));
                ImageView plantToPot = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Plants/peashooter/peashooter_moving.gif")));

                plantToPot.setFitWidth(49);
                plantToPot.setFitHeight(72);


                currTile.getChildren().add(plantToPot);
//            pea_list.add(plantToPot);
                int lane = getLane(currTile);
                Plant peashooter = new PeaShooter(plantToPot, currTile.getLayoutX(), currTile.getLayoutY());

                peashooter.TileId = currTile.getId();
                peashooter.setPlantImage(plantToPot);

                level.plantLane[lane].add(peashooter);

                suntoken.setCount(suntoken.getCount() - 100);

                sunTokenCount.setText(String.valueOf((int) suntoken.getCount()));

//            pea_list_coordinateX.add(currTile.getLayoutX());
//            pea_list_coordinateY.add(currTile.getLayoutY());

                peashooter.tile = currTile;





                int finalLane = lane;
                Timeline shoot = peaSHOOT(finalLane, currTile, peashooter);
//                Timeline peaShoot = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        if (level.zombieLane[finalLane].size() > 0) {
//                            ImageView pea = null;
//                            try {
//                                pea = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Plants/peashooter/pea.png")));
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            System.out.println("inside");
//                            pea.setLayoutX(currTile.getLayoutX() + 30);
//                            pea.setLayoutY(currTile.getLayoutY() + 20);
//                            pea.setFitWidth(20);
//                            pea.setFitHeight(20);
//                            anchor.getChildren().add(pea);
//                            Timeline peaM = peaMove(pea, finalLane);
//
//                            //
//                            ImageView finalPea = pea;
//
//                            //                    Timeline peaCollision = new Timeline(new KeyFrame(Duration.seconds(0.001), new EventHandler<ActionEvent>() {
//                            //
//                            //                        @Override
//                            //                        public void handle(ActionEvent event) {
//                            //
//                            //                            for(int i=0;i<5;i++){
//                            //                                for(int j=0;j<level.lane[i].size();j++){
//                            //                                    ImageView zombietemp = level.lane[i].get(j).zombie;
//                            //                                    if(finalPea.getLayoutBounds().intersects(zombietemp.getLayoutBounds())){
//                            ////                                    if(abs(finalPea.getLayoutX()-zombietemp.getLayoutX())<=10){
//                            //                                        System.out.println("xxxx ----- "+ finalPea.getLayoutBounds().getMaxX());
//                            ////                                        System.out.println("yyyy ----- "+ finalPea.getLayoutBounds().getMaxY());
//                            ////
//                            //                                        System.out.println("i, j  "+i+j);
//                            ////                                        System.out.println("ZOMxxxx ----- "+ zombietemp.getLayoutBounds().getMinX());
//                            ////                                        System.out.println("ZOMyyyy ----- "+ zombietemp.getLayoutBounds().getMinY());
//                            ////                                        peaM.stop();
//                            ////                                        peaM.
//                            ////                                        finalPea.setOpacity(0);
//                            ////                                        peaM.setCycleCount(0);
//                            //                                        anchor.getChildren().remove(finalPea);
//                            //                                        finalPea.setVisible(false);
//                            //                                        finalPea.setDisable(true);
//                            //                                        finalPea.setLayoutX(10000);
//                            //                                        finalPea.setLayoutY(10000);
//                            //
//                            //                                        level.lane[i].get(j).setHealth(level.lane[i].get(j).getHealth()-20.0);
//                            //                                        System.out.println("Health -------  "+ level.lane[i].get(j).getHealth());
//                            //                                        if(level.lane[i].get(j).getHealth()<=0){
//                            //                                            anchor.getChildren().remove(zombietemp);
//                            //                                            level.lane[i].remove(j);
//                            //                                            break;
//                            //                                        }
//                            ////                                    zombie1.setVisible(false);
//                            ////                                        zombie1.setLayoutX(-1000);
//                            //                                    }
//                            //
//                            //
//                            //                                }
//                            //                            }
//                            //
//                            ////                            if(abs(finalPea.getLayoutX()-zombie1.getLayoutX())<=1 && finalPea.getLayoutY()==zombie1.getLayoutY()+60){
//                            ////                                System.out.println("Pea Contact");
//                            ////                                Zombie zom = zombie.get(0);
//                            ////                                zom.setHealth(0);
//                            ////                                System.out.println("HEALTH  "+zom.getHealth());
//                            ////                                if(zom.getHealth()==0){
//                            ////                                    anchor.getChildren().remove(zombie1);
//                            //////                                    zombie1.setVisible(false);
//                            ////                                    zombie1.setLayoutX(-1000);
//                            ////                                }
//                            ////                                finalPea.setVisible(false);
//                            ////                            }
//                            ////                            else if(abs(finalPea.getLayoutX()-zombie2.getLayoutX())<=1 && finalPea.getLayoutY()==zombie2.getLayoutY()+52){
//                            ////                                Zombie zom = zombie.get(1);
//                            ////                                zom.setHealth(0);
//                            ////                                if(zom.getHealth()==0){
//                            ////                                    anchor.getChildren().remove(zombie2);
//                            //////                                    zombie2.setVisible(false);
//                            //////                                    zombie2.setLayoutX(-1000);
//                            ////
//                            ////                                }
//                            ////                                finalPea.setVisible(false);
//                            ////                            }
//                            ////                            else if(abs(finalPea.getLayoutX()-zombie3.getLayoutX())<=1 && finalPea.getLayoutY()==zombie3.getLayoutY()+44){
//                            ////                                Zombie zom = zombie.get(2);
//                            ////                                zom.setHealth(0);
//                            ////                                if(zom.getHealth()==0){
//                            ////                                    anchor.getChildren().remove(zombie3);
//                            //////                                    zombie3.setVisible(false);
//                            //////                                    zombie3.setLayoutX(-1000);
//                            ////                                }
//                            ////                                finalPea.setVisible(false);
//                            ////                            }
//                            ////                            else if(abs(finalPea.getLayoutX()-zombie4.getLayoutX())<=1 && finalPea.getLayoutY()==zombie4.getLayoutY()+36){
//                            ////                                Zombie zom = zombie.get(3);
//                            ////                                zom.setHealth(0);
//                            ////                                if(zom.getHealth()==0){
//                            ////                                    anchor.getChildren().remove(zombie4);
//                            //////                                    zombie4.setVisible(false);
//                            //////                                    zombie4.setLayoutX(-1000);
//                            ////                                }
//                            ////                                finalPea.setVisible(false);
//                            ////                            }
//                            ////                            else if(abs(finalPea.getLayoutX()-zombie5.getLayoutX())<=1 && finalPea.getLayoutY()==zombie5.getLayoutY()+28){
//                            ////                                Zombie zom = zombie.get(4);
//                            ////                                zom.setHealth(0);
//                            ////                                if(zom.getHealth()==0){
//                            ////                                    anchor.getChildren().remove(zombie5);
//                            //////                                    zombie5.setVisible(false);
//                            //////                                    zombie5.setLayoutX(-1000);
//                            ////                                }
//                            ////                                finalPea.setVisible(false);
//                            ////                            }
//                            //                        }
//                            //                    }));
//                            //                    peaCollision.setCycleCount(Timeline.INDEFINITE);
//                            //                    peaCollision.play();
//                            if (peashooter.getHealth() <= 0) {
//                                peaM.setCycleCount(0);
//                                peaM.stop();
////                        peaShoot.stop();
//                            }
//                        }
//                        System.out.println("Pea is shot every 5 seconds using pea_thread");
//
//
//                    }
//                }));
//                peaShoot.setCycleCount(Timeline.INDEFINITE);
//                peaShoot.play();
                peashooter.timeline = shoot;
                allTimeline.add(shoot);


            }
        }


        else if (selectedPlant.equals(sunflowerToken) && suntoken.getCount()>=50) {
            System.out.println("sunflower");
            TilePane currTile = (TilePane) event.getSource();
            System.out.println("TILEPANE   ID ------  "+currTile.getId());
                if(currTile.getChildren().size()==0) {

//            ImageView plantToPot = new ImageView(new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/PvZ_PNGs/Plants/sunflower/sunflower_moving.gif")));
                ImageView plantToPot = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Plants/sunflower/sunflower_moving.gif")));

                plantToPot.setFitWidth(49);
                plantToPot.setFitHeight(72);

                suntoken.setCount(suntoken.getCount() - 50);

                Sunflower sunflower1 = new Sunflower(plantToPot, currTile.getLayoutX(), currTile.getLayoutY());
                sunflower1.setPosX(currTile.getLayoutX());
                sunflower1.setposY(currTile.getLayoutY());
                sunflower.add(sunflower1);


                sunflower1.setPlantImage(plantToPot);

                sunflower1.tile = currTile;

                sunflower1.TileId = currTile.getId();


                sunTokenCount.setText(String.valueOf((int) suntoken.getCount()));

//                Timeline sunSpawn = new Timeline(new KeyFrame(Duration.seconds(25), new EventHandler<ActionEvent>() {
//
//
//                    @Override
//                    public void handle(ActionEvent event) {
//                        try {
//                            ImageView sunToken = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Lawn/sun.png")));
//                            sunToken.setLayoutY(sunflower1.getposY() + 28);
//                            sunToken.setLayoutX(sunflower1.getPosX());
////                        sunToken.setFitHeight(10);
//                            sunToken.setFitWidth(32);
//
//                            sunToken.setFitHeight(45);
//                            System.out.println("sunssssswidth  " + sunToken.getFitWidth());
//                            System.out.println("sunssssheight  " + sunToken.getFitHeight());
//                            anchor.getChildren().add(sunToken);
//
//                            sunToken.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//                                @Override
//                                public void handle(MouseEvent event) {
//                                    sunToken.setVisible(false);
//                                    int tokencount = Integer.valueOf(sunTokenCount.getText());
//                                    tokencount = tokencount + 25;
//                                    sunTokenCount.setText(String.valueOf(tokencount));
//
//                                    suntoken.setCount(Integer.valueOf(sunTokenCount.getText()));
//                                }
//                            });
//
//                            if (sunflower1.getHealth() <= 0) {
//                                sunflower1.timeline.stop();
//                            }
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }));
//                sunSpawn.setCycleCount(Timeline.INDEFINITE);
//                sunSpawn.play();

                sunflower1.timeline = sunSpawn(sunflower1);
                allTimeline.add(sunflower1.timeline);

//                sunflower1.timeline = sunSpawn;


                currTile.getChildren().add(plantToPot);

                int lane = getLane(currTile);
                level.plantLane[lane].add(sunflower1);
            }
        }
        else if (selectedPlant.equals(wallnutToken) && suntoken.getCount()>=50) {
            System.out.println("sunflower");
            TilePane currTile = (TilePane) event.getSource();

            if(currTile.getChildren().size()==0 && suntoken.getCount()>=50) {

//            ImageView plantToPot = new ImageView(new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/PvZ_PNGs/Plants/wallnut/walnut_full_life.gif")));
                ImageView plantToPot = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Plants/wallnut/walnut_full_life.gif")));

                plantToPot.setFitWidth(42);
                plantToPot.setFitHeight(55);

                suntoken.setCount(suntoken.getCount() - 50);

                sunTokenCount.setText(String.valueOf((int) suntoken.getCount()));

                currTile.getChildren().add(plantToPot);

                Plant walnut = new Walnut(plantToPot, currTile.getLayoutX(), currTile.getLayoutY());

                walnut.TileId = currTile.getId();

                walnut.setPlantImage(plantToPot);

                walnut.tile = currTile;

                int lane = getLane(currTile);
                level.plantLane[lane].add(walnut);
//            plantToPot.setLayoutY(plantToPot.getLayoutY()+20);
            }
        }
        else if (selectedPlant.equals(potatoToken) && suntoken.getCount()>=25) {
            System.out.println("potato");
            TilePane currTile = (TilePane) event.getSource();
            if(currTile.getChildren().size()==0) {

//            ImageView plantToPot = new ImageView(new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/PvZ_PNGs/Plants/potatomine/potatomine_1_armed.png")));
                ImageView plantToPot = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Plants/potatomine/potatomine_1_armed.png")));

                plantToPot.setFitWidth(49);
                plantToPot.setFitHeight(72);

                currTile.getChildren().add(plantToPot);
                suntoken.setCount(suntoken.getCount() - 25);

                Plant potato = new PotatoMine(plantToPot, currTile.getLayoutX(), currTile.getLayoutY());

                potato.TileId = currTile.getId();

                potato.setPlantImage(plantToPot);

                potato.tile = currTile;

                int lane = getLane(currTile);
                level.plantLane[lane].add(potato);


                Timeline potatoTimeline = potatoBlast(plantToPot, potato, lane);
                potato.timeline = potatoTimeline;
            }

        }
        else if (selectedPlant.equals(cherryToken)) {
            System.out.println("cherry");
            TilePane currTile = (TilePane) event.getSource();

            if(currTile.getChildren().size()==0 && suntoken.getCount()>=150) {

//            ImageView plantToPot = new ImageView(new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/PvZ_PNGs/Plants/cherrybomb/cherrybomb_1.png")));
                ImageView plantToPot = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Plants/cherrybomb/cherrybomb_1.png")));

                plantToPot.setFitWidth(49);
                plantToPot.setFitHeight(72);
                System.out.println("TILEPANE   ID ------  "+currTile.getId());

                currTile.getChildren().add(plantToPot);
                suntoken.setCount(suntoken.getCount() - 150);

                Plant cherry = new CherryBomb(plantToPot, currTile.getLayoutX(), currTile.getLayoutY());

                cherry.TileId = currTile.getId();

                cherry.setPlantImage(plantToPot);

                cherry.tile = currTile;

                int lane = getLane(currTile);
                level.plantLane[lane].add(cherry);

                Timeline cherryTimeline = cherryBlast(plantToPot, cherry);
                cherry.timeline = cherryTimeline;
            }
        }
        selectedPlant = null;
    }


    static int currLevel = 0;

    public void startGame(){



        level.setLevel(currLevel);

        System.out.println("Current level -------   "+level.getLevel());

//        lawnMower_move(lawn1);
//        lawnMower_move(lawn2);
//        lawnMower_move(lawn3);
//        lawnMower_move(lawn4);
//        lawnMower_move(lawn5);

        if(true) {

            lawnmowers.add(new Lawnmower(0, lawn1));
            lawnmowers.add(new Lawnmower(1, lawn2));
            lawnmowers.add(new Lawnmower(2, lawn3));
            lawnmowers.add(new Lawnmower(3, lawn4));
            lawnmowers.add(new Lawnmower(4, lawn5));


            Suntoken sun = suntoken;
            int time = 12;
            final int[] zombiesSpawned = {0};


            if (level.getLevel() == 0) {
                time = 10;
                level.zombieCount = 10;
                level.setZombiestoSpawn(10);
            }
            else if (level.getLevel() == 1) {
                time = 10;
                level.zombieCount = 15;
                level.setZombiestoSpawn(15);
            } else if (level.getLevel() == 2) {
                time = 9;
                level.zombieCount = 20;
                level.setZombiestoSpawn(20);
            } else if (level.getLevel() == 3) {
                time = 7;
                level.zombieCount = 30;
                level.setZombiestoSpawn(30);
            } else if (level.getLevel() == 4) {
                time = 4;
                level.zombieCount = 40;
                level.setZombiestoSpawn(40);
            }

            if(this.frenzy){
                time = 3;
                level.zombieCount = 100;
                level.setZombiestoSpawn(100);
                suntoken.setCount(2000);
                sunTokenCount.setText(String.valueOf((int)suntoken.getCount()));
                level.setLevel(4);
            }

            Timeline zombieSpawn = new Timeline(new KeyFrame(Duration.seconds(time), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Random random = new Random();

                    int val1 = random.nextInt(5);


                    int val2 = random.nextInt(level.getLevel()+1);

                    System.out.println("val2 -------------  "+val2);

                    Zombie zombie = new NormalZombie();

                    if (val2 == 0) {
                        zombie = new NormalZombie();

                    } else if(val2 == 1) {
                        zombie = new ConeHead();

                    }
                    else if(val2 ==2){
                        zombie = new FootballZombie();
                    }
                    else if(val2 == 3){
                        zombie = new BalloonZombie();
                    }
                    else if(val2 == 4){
                        zombie = new FlagZombie();
                    }
//                    System.out.println("val1   :   ----- " + val1);
                    zombie.lane = val1;
                    if (zombiesSpawned[0] <= level.getZombiestoSpawn()) {
                        level.zombieLane[val1].add(zombie);



                        try {
                            ImageView zombieLawn = new ImageView(new Image(new FileInputStream(currentDirectory + zombie.imgPath)));

                            zombieLawn.setLayoutY(zombie.yrange[val1]);
                            zombieLawn.setLayoutX(537);

                            zombieLawn.setFitHeight(zombie.height);
                            zombieLawn.setFitWidth(zombie.width);

                            anchor.getChildren().add(zombieLawn);
                            Timeline zombieTimeline = zombieMove(zombieLawn, zombie);
                            zombie.zombie = zombieLawn;
                            zombie.timeline = zombieTimeline;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        zombiesSpawned[0] += 1;
                    }
                }
            }));
            zombieSpawn.setCycleCount(Timeline.INDEFINITE);
            zombieSpawn.play();
            allTimeline.add(zombieSpawn);

            Timeline gameLose = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(gameLost){
                        System.out.println("THE GAME IS LOST ");
                        for(int i=0;i<allTimeline.size();i++){
                            allTimeline.get(i).pause();
                        }
                        gameRestartPage.setVisible(true);
                        gameRestartPage.setDisable(false);
                    }

                }
            }));
            gameLose.setCycleCount(Timeline.INDEFINITE);
            gameLose.play();
            allTimeline.add(gameLose);

            double duration = 10;

            if(this.frenzy){
                duration = 2;
            }


            Timeline sunTokenGenerator = new Timeline(new KeyFrame(Duration.seconds(duration), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try {
                        //                    ImageView sunToken = new ImageView(new Image(new FileInputStream("/Users/hamzah/IdeaProjects/PvZ_home_page/src/sample/PvZ_PNGs/Lawn/sun.png")));
                        ImageView sunToken = new ImageView(new Image(new FileInputStream(currentDirectory + "/src/sample/PvZ_PNGs/Lawn/sun.png")));


                        sunToken.setOnMouseClicked(new EventHandler<MouseEvent>() {

                            @Override
                            public void handle(MouseEvent event) {
                                sunToken.setVisible(false);
                                int tokencount = Integer.valueOf(sunTokenCount.getText());
                                tokencount = tokencount + 25;
                                sunTokenCount.setText(String.valueOf(tokencount));

                                sun.setCount(Integer.valueOf(sunTokenCount.getText()));
                            }
                        });


                        double minX = 10;
                        double maxX = 440;

                        double coordx = (int) (430.0 * Math.random()) + 11;

                        sunToken.setLayoutY(0);
                        sunToken.setLayoutX(coordx);
                        sunToken.setFitHeight(10);
                        sunToken.setFitHeight(60);
                        anchor.getChildren().add(sunToken);
                        sunMove(sunToken);
                        sunToken_list.add(sunToken);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }));
            sunTokenGenerator.setCycleCount(Timeline.INDEFINITE);
            sunTokenGenerator.play();
            allTimeline.add(sunTokenGenerator);

            Timeline progressbar = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
//                    double pos =  465 - z.getLayoutX();
//                    double progressbar_value = (pos/465) * 1;
                    progress.val = progressBar.getProgress()+0.002;
                    progressBar.setProgress(progressBar.getProgress()+0.002);

                }
            }));
            progressbar.setCycleCount(Timeline.INDEFINITE);
            progressbar.play();
            allTimeline.add(progressbar);
        }
        else{
            for(int i=0;i<allTimeline.size();i++){
                allTimeline.get(i).play();
            }
        }


    }

    public void lawnmowerLoad(lawn game){
        for(int i=0; i<game.lawnmowers.size(); i++){
            if(game.lawnmowers.get(i).isUse()) {
                if (i == 0) {
                    lawn1.setLayoutX(2000000);
                    lawn1.setVisible(false);
                    lawn1.setDisable(true);

                } else if (i == 1) {
                    lawn2.setLayoutX(2000000);
                    lawn2.setVisible(false);
                    lawn2.setDisable(true);

                } else if (i == 2) {
                    lawn3.setLayoutX(2000000);
                    lawn3.setVisible(false);
                    lawn3.setDisable(true);

                } else if (i == 3) {
                    lawn4.setLayoutX(2000000);
                    lawn4.setVisible(false);
                    lawn4.setDisable(true);

                } else if (i == 4) {
                    lawn5.setLayoutX(2000000);
                    lawn5.setVisible(false);
                    lawn5.setDisable(true);
                }

            }

            if(i==0){
                game.lawnmowers.get(i).mower = lawn1;
            }
            else if(i==1){
                game.lawnmowers.get(i).mower = lawn2;
            }
            else if(i==2){
                game.lawnmowers.get(i).mower = lawn3;
            }
            else if(i==3){
                game.lawnmowers.get(i).mower = lawn4;
            }
            else if(i==4){
                game.lawnmowers.get(i).mower = lawn5;
            }

        }

    }

    public void loadGame(lawn game){
//        anchor.setMaxWidth(590);
//        anchor.setMaxHeight(445);

        grid[0] = new TilePane[]{t00, t01, t02, t03, t04, t05, t06, t07};
        grid[1] = new TilePane[]{t10, t11, t12, t13, t14, t15, t16, t17};;
        grid[2] = new TilePane[]{t20, t21, t22, t23, t24, t25, t26, t27};
        grid[3] = new TilePane[]{t30, t31, t32, t33, t34, t35, t36, t37};
        grid[4] = new TilePane[]{t40, t41, t42, t43, t44, t45, t46, t47};

        progress.val = game.laneProgress;
        progressBar.setProgress(progress.val);

        this.gameWin = game.gameWin;

        this.level = game.level;
        this.suntoken = game.suntoken;

        this.sunflower = new ArrayList<>(game.sunflower);

        this.zombie = new ArrayList<>(game.zombie);

        this.lawnmowers = new ArrayList<>(game.lawnmowers);

        this.currentDirectory = game.currentDirectory;

        sunTokenCount.setText(String.valueOf((int)game.suntoken.getCount()));

        this.gameLost = game.gameLost;

        lawnmowerLoad(game);



        for(int i=0; i<5; i++){
            for(int j=0; j<game.level.zombieLane[i].size();j++){
                Zombie zombie = game.level.zombieLane[i].get(j);
                try {
                    ImageView zombieLawn = new ImageView(new Image(new FileInputStream(currentDirectory + zombie.imgPath)));

                    zombieLawn.setLayoutY(zombie.getPosY());
                    zombieLawn.setLayoutX(zombie.getPosX());

                    zombieLawn.setFitHeight(zombie.height);
                    zombieLawn.setFitWidth(zombie.width);

                    anchor.getChildren().add(zombieLawn);
                    Timeline zombieTimeline = zombieMove(zombieLawn, zombie);
                    zombie.zombie = zombieLawn;
                    zombie.timeline = zombieTimeline;
                    allTimeline.add(zombieTimeline);


                } catch (FileNotFoundException e) {
                    e.getMessage();
                }
            }

        }



        for(int i=0; i<5; i++){
            for(int j=0; j<game.level.plantLane[i].size();j++){

                Plant plantt = game.level.plantLane[i].get(j);
                if(game.level.plantLane[i].get(j).place) {
                    try {
                        ImageView plantLawn = new ImageView(new Image(new FileInputStream(currentDirectory + plantt.imgPath)));

                        plantLawn.setFitWidth(49);
                        plantLawn.setFitHeight(72);


                        int x = Integer.valueOf(plantt.TileId.substring(1, 2));
                        int y = Integer.valueOf(plantt.TileId.substring(2));

                        plantt.tile = grid[x][y];

                        plantt.setPlantImage(plantLawn);

                        grid[x][y].getChildren().add(plantLawn);


                        if(plantt instanceof PeaShooter){
                            int lanenum = getLane(grid[x][y]);
                            plantt.timeline = peaSHOOT(lanenum, grid[x][y], plantt);
                            allTimeline.add(plantt.timeline);

                        }
                        else if(plantt instanceof Sunflower){
                            plantt.timeline = sunSpawn((Sunflower) plantt);
                            allTimeline.add(plantt.timeline);
                        }

                    } catch (FileNotFoundException e) {
                        e.getMessage();
                    }

                }
            }
        }
        this.startGame();






    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(this.Game==null) {
            this.startGame();
        }
        else{
            loadGame(this.Game);
        }
    }

    public void pausePress(ActionEvent event) throws IOException {
        System.out.println("Pause Pressed");
        for(int i=0;i<allTimeline.size();i++){
            allTimeline.get(i).pause();
        }
        pause.setDisable(false);
        pause.setVisible(true);

//        Parent root = FXMLLoader.load(getClass().getResource("pauseMenu.fxml"));
//        Scene scene = new Scene(root);
//        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
//        window.setScene(scene);
//        window.show();
    }

    public void resumePress(ActionEvent actionEvent) {
        pause.setDisable(true);
        pause.setVisible(false);

        System.out.println("Resume Pressed");
        for(int i=0;i<allTimeline.size();i++){
            allTimeline.get(i).play();
        }

    }

    public void exitPress(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuPage.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void nextLevel(ActionEvent actionEvent) throws IOException {
        lawn.currLevel++;
        if(lawn.currLevel>4){
            lawn.currLevel = 4;
        }
        gameWonPage.setDisable(true);
        gameWonPage.setVisible(false);
        FXMLLoader loader=new FXMLLoader(getClass().getResource("lawn.fxml"));
        Stage app=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Parent root=null;
        root=(Parent)loader.load();
        lawn controller=loader.<lawn>getController();
        Scene scene=new Scene(root);
        app.setScene(scene);
        app.show();


    }


    public void restartLevel(ActionEvent actionEvent) throws IOException {
        this.Game = null;
        gameRestartPage.setDisable(true);
        gameRestartPage.setVisible(false);
        FXMLLoader loader=new FXMLLoader(getClass().getResource("lawn.fxml"));
        Stage app=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Parent root=null;
        root=(Parent)loader.load();
        lawn controller=loader.<lawn>getController();
        Scene scene=new Scene(root);
        app.setScene(scene);
        app.show();

    }

    public void savePress(ActionEvent actionEvent) throws IOException {

        FileOutputStream fop=new FileOutputStream("object.ser");
        ObjectOutputStream oos=new ObjectOutputStream(fop);
        oos.writeObject(this);
        oos.close();



        System.out.println("GAME SAVED");
    }
}
