//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Arrays;

public class OpenController {
    private static double[][] thrustSchedule;
    private static double[] currentState = new double[4]; //position in 3 dimention + angle
    private static double[] currentVelocity = new double[4]; //velocity in 3 dimention + angle

    private static final double GRAVITY_FROM_TITAN = 0.001352; //km s^-2, acceleration due to the gravity of titan

    private static final double X_POSITION_TOLERANCE = 0.0001; //km
    private static final double ANGLE_TOLERANCE = 0.02; //no unit
    private static final double X_VELOCITY_TOLERANCE = 0.0001; //km s^-1
    private static final double Y_VELOCITY_TOLERANCE = 0.0001; //km s^-1
    private static final double ANGLE_VELOCITY_TOLERANCE = 0.0001; //rad s^-1

    private static final double FINAL_STATE = new double[] {0, 0, 0}; // x_position, y_position, angle
    private static final double FINAL_VELOCITY = new double[] {0, 0, 0}; // x_velocity, y_velocity, angle_velocity

    public OpenController(double[] initialState) { //initial state = position in 3 dimention + angle
        executeThrustSchedule(initialState);
    }

    public static void main(String[] args) { //Just for testing
        double[] start = new double[]{20.0, 45.0, 50.0, }; //position on the orbit
        double[] destination = new double[]{40.0, 20.0, 50.0}; //target position
        double[] finalPosition = executeThrustSchedule(10, start, destination);
        System.out.println("Final Position: " + Arrays.toString(finalPosition));
    }

    public static boolean checkFinalState() {
        Probe.
        for (int i = 1; i < 3; i++) {
            if ()
        }
    }

    public static boolean checkFinalVelocity() {

    }

    public static double[][] calculateThrustSchedule(int duration, double[] start, double[] destination) {
        int numIntervals = duration + 1;
        int dimension = start.length;
        double[] incrementation = new double[dimension];
        double[][] thrustSchedule = new double[numIntervals][dimension];

        int i;
        for(i = 0; i < dimension; ++i) {
            incrementation[i] = (destination[i] - start[i]) / (double)duration;
        }

        for(i = 0; i < numIntervals; ++i) {
            for(int j = 0; j < dimension; ++j) {
                thrustSchedule[i][j] = start[j] + incrementation[j] * (double)i;
            }
        }

        return thrustSchedule;
    }

    public static double[] executeThrustSchedule(int duration, double[] start, double[] destination) {
        double[][] getVelocity = SolarSystem.getVelocities();
        thrustSchedule = calculateThrustSchedule(duration, start, destination);
        int velocityIndex = 0;
        double[][] var5 = getVelocity;
        int var6 = getVelocity.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            double[] var10000 = var5[var7];
            if (velocityIndex < thrustSchedule.length) {
                double[] thrust = thrustSchedule[velocityIndex];
                applyThrust(thrust);
                ++velocityIndex;
            }
        }

        return currentPosition;
    }

    private static void applyThrust(double[] thrust) {
        int dimensions = 3;
        double thrustDivisor = 50000.0;
        double timeInterval = 500.0;

        for(int i = 0; i < dimensions; ++i) {
            double acceleration = thrust[i] / thrustDivisor;
            double[] var10000 = currentVelocity;
            var10000[i] += acceleration * timeInterval;
            var10000 = currentPosition;
            var10000[i] += currentVelocity[i] * timeInterval;
        }

    }
}
