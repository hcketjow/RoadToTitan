import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class LandingModule {
    public static ArrayList<double[]> stateLog = new ArrayList<>();
    private static double[] state;
    private static double u = 0;
    private static double v = 0;

    public static AtomicBoolean landed = new AtomicBoolean(false);

    //moving the rocket while landing, depending on u, v, θ, where:
    //x is the horizontal position, y the vertical position,
    //θ the angle of rotation,
    //u the acceleration provided by the main thruster,
    //v the total torque provided by the side thrusters;
    //aX is x'', aY is y'', aTheta is θ''
    public static double[] calculateAccelerationOfModule(double t){
        double aX = getU(t) * Math.sin(state[0]);
        double aY = getU(t) * Math.cos(position[2]) - MissionData.GRAVITY_TITAN;
        double aTheta = getV(t);

        double[] a = new double[]{aX, aY, aTheta};
        return a;
    }

    public static boolean checkLandingConditions(){
//        return (position[1] == MissionData.Y_LANDING)
//                && (Math.abs(position[0]) <= MissionData.DELTA_X_LANDING)
//                && (Math.abs(theta % (2*Math.PI)) <= MissionData.DELTA_THETA_LANDING)
//                && (Math.abs(velocity[0]) <= MissionData.EPSILON_X_LANDING)
//                && (Math.abs(velocity[1]) <= MissionData.EPSILON_Y_LANDING)
//                && (Math.abs(velocity[2]) <= MissionData.EPSILON_THETA_LANDING);
        double dist = calculateDistanceToTarget("titan");
        if(dist <= MissionData.Y_LANDING
                && (Math.abs(position[0]) <= MissionData.DELTA_X_LANDING)
                && (Math.abs(position[2] % (2*Math.PI)) <= MissionData.DELTA_THETA_LANDING)
                && (Math.abs(velocity[0]) <= MissionData.EPSILON_X_LANDING)
                && (Math.abs(velocity[1]) <= MissionData.EPSILON_Y_LANDING)
                && (Math.abs(velocity[2]) <= MissionData.EPSILON_THETA_LANDING)){
            System.out.println("\tLANDED SAFELY !!");
            return true;
        }else if(dist <= MissionData.Y_LANDING){
            System.out.println("\tLANDED (CRASHED)");
            return true;
        }
        return false;
    }

    public static double calculateDistanceToTarget(String target){
        double d1 = SolarSystem.getPositions()[MissionData.idMap.get(target)][0] - getPosition()[0];
        double d2 = SolarSystem.getPositions()[MissionData.idMap.get(target)][1] - getPosition()[1];
        double distance = Math.sqrt(Math.pow(d1, 2) + Math.pow(d2, 2));

        return distance;
    }

    public static double[] getPosition() {
        return position;
    }

    public static void setPosition(double[] position) {
        LandingModule.position = position;
    }

    public static double[] getVelocity() {
        return velocity;
    }

    public static void setVelocity(double[] velocity) {
        LandingModule.velocity = velocity;
    }

    public static double getU() {
        return u;
    }

    public static void setU(double u) {
        LandingModule.u = u;
    }

    public static double getV() {
        return v;
    }

    public static void setV(double v) {
        LandingModule.v = v;
    }
}