import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.ArrayList;

public class DisplayCelestialBody extends Circle{

    public static ArrayList<DisplayCelestialBody> displayCelestialBodyArrayList = new ArrayList<>();
    public static final double SCALAR = Math.pow(10, 5) * 8 * 4;// * 2;

    private final int index;
    private final DisplayCelestialBody origin;


    DisplayCelestialBody(int index, DisplayCelestialBody origin, int radius, String colour){
        this.index = index;
        this.origin = origin;

        this.setFill(Paint.valueOf(colour));
        setRadius(radius);

        this.setLocationOfCelestialBody();
        displayCelestialBodyArrayList.add(this);
    }

    //sets position of objects on the screen
    //with probe: all distances are scaled equally
    //without probe: each distance is scaled based on its magnitude and the object's size
    public void setLocationOfCelestialBody(){
        if(this.index == 0){
            this.setCenterX(GUI.window.getWidth()/2);
            this.setCenterY(GUI.window.getHeight()/2);
        }else{
            this.setCenterX(this.origin.getCenterX() + (SolarSystem.getPositions()[this.index][0] - SolarSystem.getPositions()[this.origin.index][0]) / SCALAR);
            this.setCenterY(this.origin.getCenterY() - (SolarSystem.getPositions()[this.index][1] - SolarSystem.getPositions()[this.origin.index][1]) / SCALAR);
        }
    }

    public static void setLocationOfAllCelestialBodies(){
        for(DisplayCelestialBody displayCelestialBody : displayCelestialBodyArrayList) {
            displayCelestialBody.setLocationOfCelestialBody();
        }
    }

    public int getIndex() {
        return index;
    }
}

