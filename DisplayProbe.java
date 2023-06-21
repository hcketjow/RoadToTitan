import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DisplayProbe extends Circle{

    DisplayProbe(){
        this.setFill(Paint.valueOf("ff0000"));
        this.setRadius(1);
        this.setLocationOfProbe();
    }

    public void setLocationOfProbe(DisplayProbe this){
        this.setCenterX(GUI.window.getWidth()/2 + Probe.getPosition()[0] / DisplayCelestialBody.SCALAR);
        this.setCenterY(GUI.window.getHeight()/2 - Probe.getPosition()[1] / DisplayCelestialBody.SCALAR);
    }

}
