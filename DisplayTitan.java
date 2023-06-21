import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DisplayTitan extends Circle {

    DisplayTitan(){
        this.setFill(Paint.valueOf("f5c77e"));
        this.setRadius(MissionData.TITAN_RADIUS / LandingScene.scalar);
        setLocationOfTitan();
    }

    public void setLocationOfTitan() {
        this.setCenterX(GUI.window.getWidth()/2);
        this.setCenterY(GUI.window.getHeight()/2);// + 0.99 * this.getRadius());//
    }
}
