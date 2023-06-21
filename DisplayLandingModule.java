import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class DisplayLandingModule extends Rectangle {

    DisplayLandingModule(DisplayTitan titan){
        this.setFill(Paint.valueOf("00ff00"));
        this.setWidth(6);
        this.setHeight(12);
//        this.setWidth(5000);
//        this.setHeight(10000);
        this.setLocationOfModule(titan);
    }

    public void setLocationOfModule(DisplayTitan titan) {
//        this.setX(100);
//        this.setY(100);
        this.setX(titan.getCenterX() + (LandingModule.getPosition()[0] - SolarSystem.getPositions()[MissionData.idMap.get("titan")][0]) / LandingScene.scalar);
        this.setY(titan.getCenterY() - (LandingModule.getPosition()[1] - SolarSystem.getPositions()[MissionData.idMap.get("titan")][1]) / LandingScene.scalar);
        this.setRotate(0);
        this.setRotate(LandingModule.getPosition()[2]);
    }

    public void alignModuleCentre(){
        this.setX(this.getX() - this.getWidth()/2);
        this.setY(this.getY() - this.getHeight()/2);
    }
}
