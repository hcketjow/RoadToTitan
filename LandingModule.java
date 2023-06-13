import java.util.ArrayList;

public class LandingModule {
    public static ArrayList<double[]> positionsLog = new ArrayList<>();
    public static ArrayList<double[]> velocitiesLog = new ArrayList<>();
    private static double[] position;
    private static double[] velocity;

    //moving the rocket while landing, depending on u, v, θ, where:
    //x is the horizontal position, y the vertical position,
    //θ the angle of rotation,
    //u the acceleration provided by the main thruster,
    //v the total torque provided by the side thrusters;
    //aX is x'', aY is y'', aTheta is θ''
    public static double[] calculateAccelerationOfModule(double u, double v, double theta){
        double aX = u * Math.sin(theta);
        double aY = u * Math.cos(theta) - MissionData.GRAVITY_TITAN;
        double aTheta = v;

        //???
        return new double[1];
    }

    public static boolean checkLandingConditions(double u, double v, double theta){
        if(position[1] == 0){
            //???
            return true;
        }
        return false;
    }
}
