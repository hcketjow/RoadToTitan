import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LandingScene extends Scene {
    public static double scalar = 7;//0.2;//

    public LandingScene(Stage window) {
        super(initialize(window));
    }

    private static Parent initialize(Stage window){

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color:BLACK");

        DisplayTitan titan = new DisplayTitan();
        pane.getChildren().add(titan);

        //TODO: change z-component to rotation

        //TODO: set centre of module
        //LandingModule.setPosition();
        //TODO: rotate module
        //LandingModule.setVelocity();

        DisplayLandingModule landingModule = new DisplayLandingModule(titan);
        pane.getChildren().add(landingModule);

        double angleForAlignment = getAngle(titan, landingModule);

        Group group = new Group();
        group.getChildren().addAll(titan, landingModule);
        pane.getChildren().add(group);

        //group.setRotate(angleForAlignment);
        landingModule.setLocationOfModule(titan);
        landingModule.setRotate(-angleForAlignment);
        landingModule.alignModuleCentre();

        Line line = new Line(window.getWidth()/2, 0, window.getWidth()/2, window.getHeight());
        line.setStroke(Color.WHITE);
        pane.getChildren().add(line);

        AtomicInteger count = new AtomicInteger(0);
        System.out.println();

        System.out.println("t "+titan.getCenterX()+" "+ titan.getCenterY());
        System.out.println("m "+landingModule.getX()+" "+ landingModule.getY());
        System.out.println(Arrays.toString(LandingModule.getPosition()));
        System.out.println(Arrays.toString(LandingModule.getVelocity()));

        Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(100), x -> {
            if(!LandingModule.checkLandingConditions()){
                if(count.get() == 1){
                    //LandingModule.setU(0);
                    //LandingModule.setV(0);
                }
                System.out.println();

                System.out.println("landing");
                System.out.println(Arrays.toString(SolarSystem.getPositions()[8]));

                //group.setRotate(0);
                EulerSimulation.simulationWithLandingModuleOnce(count.get());
                titan.setLocationOfTitan();
                landingModule.setLocationOfModule(titan);
                landingModule.alignModuleCentre();
                //group.setRotate(angleForAlignment);

                System.out.println("t "+titan.getCenterX()+" "+ titan.getCenterY());
                System.out.println("m "+landingModule.getX()+" "+ landingModule.getY());

                count.set(count.get() + 1);
            }


        }));
        animationTimeline.setCycleCount(30);
        animationTimeline.setDelay(Duration.seconds(3));
        animationTimeline.setOnFinished(e -> {
            System.out.println("とりあえずpositionは行けてる");
            System.out.println(LandingModule.checkLandingConditions());
            System.out.println("landing done");
        });

        animationTimeline.play();

        return pane;
    }

    private static double getAngle(DisplayTitan titan, DisplayLandingModule landingModule){
        double deltaX = landingModule.getX() - titan.getCenterX();
        double deltaY = titan.getCenterY() - landingModule.getY();
        double angle = 90 - Math.toDegrees(Math.atan2(deltaY, deltaX));

        return -angle;
    }

}
