import java.util.Arrays;
public class OpenController {
    public static void main(String[] args) {
        double[] start = {20, 45, 50}; // x, y, z position of the start planet
        double[] destination = {40, 20, 50}; // x, y, z position of the destination planet
        double[] finalPosition = executeThrustSchedule(10, start, destination);
        System.out.println("Final Position: " + Arrays.toString(finalPosition));
    }

    private static double[][] thrustSchedule; // forces that will be exerted from the probe in a set of time intervals
    private static double[] currentPosition = new double[3]; // The current position of the probe
    private static double[] currentVelocity = new double[3]; // The current velocity of the probe

    public static double[][] calculateThrustSchedule(int duration, double[] start, double[] destination) {
        int numIntervals = duration + 1;
        int dimension = start.length;
        double[] incrementation = new double[dimension];
        double[][] thrustSchedule = new double[numIntervals][dimension];
        for (int i = 0; i < dimension; i++)
            incrementation[i] = (destination[i] - start[i]) / duration;
        for (int i = 0; i < numIntervals; i++)
            for (int j = 0; j < dimension; j++)
                thrustSchedule[i][j] = start[j] + (incrementation[j] * i);
        return thrustSchedule;
    }

    public static double[] executeThrustSchedule(int duration, double[] start, double[] destination) {
        double[][] getVelocity = SolarSystem.getVelocities();
        thrustSchedule = calculateThrustSchedule(duration, start, destination);
        int velocityIndex = 0; // index for iteration from the velocity
        for (var item : getVelocity) {
            if (velocityIndex < thrustSchedule.length) {
                double[] thrust = thrustSchedule[velocityIndex];
                applyThrust(thrust);
                velocityIndex++;
            }
        }
        return currentPosition;
    }

    private static void applyThrust(double[] thrust) {
        int dimensions = 3;
        double thrustDivisor = 50000.0;
        double timeInterval = 500.0;

        for (int i = 0; i < dimensions; i++) {
            double acceleration = thrust[i] / thrustDivisor;
            currentVelocity[i] += acceleration * timeInterval;
            currentPosition[i] += currentVelocity[i] * timeInterval;
        }
    }
}
