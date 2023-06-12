import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Probe {

    private static final double mass = MissionData.PROBE_MASS;
    public static ArrayList<double[]> probePositionsLog = new ArrayList<>();
    public static ArrayList<double[]> probeVelocitiesLog = new ArrayList<>();
    private static double[] position = MissionData.PROBE_INIT_POSITION;
    private static double[] velocity = MissionData.PROBE_INIT_VELOCITY;

    private static final int maxVelocityMag = MissionData.PROBE_MAX_VELOCITY_MAG;

    private static double distToTitan;

    public static AtomicBoolean reachedTitan = new AtomicBoolean(false);
    public static AtomicBoolean goingBack = new AtomicBoolean(false);
    public static AtomicBoolean landed = new AtomicBoolean(false);




    private static double[] calculateTotalForceOnProbe(){
        double[] f = new double[3];

        //final formula:
        //f = -sum( g * m_i * m_j * dif(pos_i, pos_j) / (euclidean_distance(pos_i, pos_j))^3 ) ..where i!=j, pos_i/j vectors
        double m_i = mass;
        double[] pos_i = getPosition();

        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            double m_j = MissionData.MASSES[j];
            double[] pos_j = SolarSystem.getPositions()[j];
            double dist = Math.sqrt(Math.pow((pos_i[0] - pos_j[0]), 2) + Math.pow(pos_i[1] - pos_j[1], 2) + Math.pow(pos_i[2] - pos_j[2], 2));

            for(int entry = 0; entry < 3; entry++) {
                double dif = pos_i[entry] - pos_j[entry];
                f[entry] -= (MissionData.GRAVITY_CONSTANT * m_i * m_j * dif / Math.pow(dist, 3));
            }
        }

        return f;
    }

    public static double[] calculateAccelerationOfProbe(){
        double[] f = calculateTotalForceOnProbe();
        double[] a = new double[3];
        double m = mass;

        for(int entry = 0; entry < 3; entry++) {
            a[entry] = f[entry]/m;
        }

        return a;
    }

    public static double calculateDistanceToTarget(String target){
        double d1 = SolarSystem.getPositions()[MissionData.idMap.get(target)][0] - getPosition()[0];
        double d2 = SolarSystem.getPositions()[MissionData.idMap.get(target)][1] - getPosition()[1];
        double d3 = SolarSystem.getPositions()[MissionData.idMap.get(target)][2] - getPosition()[2];
        double distance = Math.sqrt(Math.pow(d1, 2) + Math.pow(d2, 2) + Math.pow(d3, 2));

        return distance;
    }

    //  ASS: fuel = ||impulse||    (fuel ~ I, fuel/s = ||F||, I = ||F||*h)
    public static double calculateFuelConsumed(double[] currentVelocity, double[] desiredVelocity){
        double[] impulse = {(desiredVelocity[0] - currentVelocity[0]) * mass, (desiredVelocity[1] - currentVelocity[1]) * mass, (desiredVelocity[2] - currentVelocity[2]) * mass};
        double fuelUnits = Math.sqrt(Math.pow(impulse[0], 2) + Math.pow(impulse[1], 2) + Math.pow(impulse[2], 2));
        return fuelUnits;
    }


    public static double[] getPosition() {
        return position;
    }

    public static void setPosition(double[] position) {
        Probe.position = position;
    }

    public static double[] getVelocity() {
        return velocity;
    }

    public static void setVelocity(double[] velocity) {
        Probe.velocity = velocity;
    }

    public static void resetState() {
        setPosition(MissionData.PROBE_INIT_POSITION);
        setVelocity(MissionData.PROBE_INIT_VELOCITY);
    }
}
