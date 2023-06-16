import java.util.*;

public class OpenController {
    public static void main(String[] args) {
        executeThrustSchedule();
    }

    private static List<double[]> thrustSchedule; // forces that will be exerted from the probe in a set of time intervals

    public static double[][] calculateThrustSchedule(int duration, double[] start, double[] destination) {
        int numIntervals = duration + 1;
        int dimention = start.length;
        double[] incrementation = new double[dimention];
        for (int i = 0; i < dimention; i++)
            incrementation[i] = (destination[i] - start[i]) / duration;

        double[][] thrustSchedule = new double[numIntervals][dimention];

        for (int i = 0; i < numIntervals; i++) {
            for (int j = 0; j < dimention; j++)
                thrustSchedule[i][j] = start[j] + (incrementation[j] * i);
        }

        return thrustSchedule;
    }

    // execution of our thrust schedule until landing in 500 second intervals. the interval can change to our liking.
    public static void executeThrustSchedule() {
        double[][] getVelocity = SolarSystem.getVelocities();
        for(var item: getVelocity){
            for(var items: item){
                thrustSchedule.get((int)items);
            }
        }
        for (double[] thrust : thrustSchedule) {
            applyThrust(thrust);
            // Assuming that each thrust is applied for 500 seconds
            try {
                Thread.sleep(500 * 1000); // sleep for 500 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static double[] currentPosition = new double[3]; // The current position of the probe
    private static double[] currentVelocity = new double[3]; // The current velocity of the probe

    private static void applyThrust(double[] thrust) {
        // Calculate the acceleration based on the thrust and the probe's mass (assuming mass is known)
        double[] acceleration = new double[3];
        for (int i = 0; i < 3; i++)
            acceleration[i] = thrust[i] / 50000;

        // Update the velocity and position using the calculated acceleration
        for (int i = 0; i < 3; i++) {
            currentVelocity[i] += acceleration[i] * 500; // Assuming each thrust is applied for 500 seconds
            currentPosition[i] += currentVelocity[i] * 500; // Assuming each thrust is applied for 500 seconds
        }
    }
}