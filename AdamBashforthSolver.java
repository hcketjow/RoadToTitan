import java.util.ArrayList;

public class AdamBashforthSolver {
    public static ArrayList<double[][]> solarSystemPositionsLog = new ArrayList<>();
    public static ArrayList<double[][]> solarSystemVelocitiesLog = new ArrayList<>();
    public static ArrayList<double[]> probePositionsLog = new ArrayList<>();
    public static ArrayList<double[]> probeVelocitiesLog = new ArrayList<>();

    public static double[][] ab3ForBody(int i) {
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[][] initPositions = SolarSystem.getPositions();
        double[][] initVelocities = SolarSystem.getVelocities();

        int n = SolarSystem.solarSystemPositionsLog.size();
        double[][] prevPositions = SolarSystem.solarSystemPositionsLog.get(n-1);
        double[][] prevVelocities = SolarSystem.solarSystemVelocitiesLog.get(n-1);

        double[] prevPos = prevPositions[i];
        double[] prevVel = prevVelocities[i];
        double[] prevAcc = SolarSystem.calculateAccelerationOfBody(i);

        double[][] prevPositions1 = SolarSystem.solarSystemPositionsLog.get(n-2);
        double[][] prevVelocities1 = SolarSystem.solarSystemVelocitiesLog.get(n-2);
        double[] prevVel1 = prevVelocities1[i];
        SolarSystem.setPositions(prevPositions1);
        SolarSystem.setVelocities(prevVelocities1);
        double[] prevAcc1 = SolarSystem.calculateAccelerationOfBody(i);

        double[][] prevPositions2 = SolarSystem.solarSystemPositionsLog.get(n-3);
        double[][] prevVelocities2 = SolarSystem.solarSystemVelocitiesLog.get(n-3);
        double[] prevVel2 = prevVelocities2[i];
        SolarSystem.setPositions(prevPositions2);
        SolarSystem.setVelocities(prevVelocities2);
        double[] prevAcc2 = SolarSystem.calculateAccelerationOfBody(i);

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);

        for (int entry = 0; entry < 3; entry++) {
            newPos[entry] = prevPos[entry] + (MissionData.TIME_STEP_SIZE / 12.0) * (23.0 * prevVel[entry] - 16.0 * prevVel1[entry] + 5.0 * prevVel2[entry]);
            newVel[entry] = prevVel[entry] + (MissionData.TIME_STEP_SIZE / 12.0) * (23.0 * prevAcc[entry] - 16.0 * prevAcc1[entry] + 5.0 * prevAcc2[entry]);
        }
        newState[0] = newPos;
        newState[1] = newVel;

        return newState;
    }

    public static double[][] adamsBashforthForProbe() {
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[][] initPositions = SolarSystem.getPositions();
        double[][] initVelocities = SolarSystem.getVelocities();

        int n = SolarSystem.solarSystemPositionsLog.size();
        double[][] prevPositions = SolarSystem.solarSystemPositionsLog.get(n-1);
        double[][] prevVelocities = SolarSystem.solarSystemVelocitiesLog.get(n-1);
        double[] prevPosition = Probe.probePositionsLog.get(n-1);
        double[] prevVelocity = Probe.probeVelocitiesLog.get(n-1);
        double[] prevAcc = Probe.calculateAccelerationOfProbe();

        double[][] prevPositions1 = SolarSystem.solarSystemPositionsLog.get(n-2);
        double[][] prevVelocities1 = SolarSystem.solarSystemVelocitiesLog.get(n-2);
        double[] prevPosition1 = Probe.probePositionsLog.get(n-2);
        double[] prevVelocity1 = Probe.probeVelocitiesLog.get(n-2);
        SolarSystem.setPositions(prevPositions1);
        SolarSystem.setVelocities(prevVelocities1);
        Probe.setPosition(prevPosition1);
        Probe.setVelocity(prevVelocity1);
        double[] prevAcc1 = Probe.calculateAccelerationOfProbe();

        double[][] prevPositions2 = SolarSystem.solarSystemPositionsLog.get(n-3);
        double[][] prevVelocities2 = SolarSystem.solarSystemVelocitiesLog.get(n-3);
        double[] prevPosition2 = Probe.probePositionsLog.get(n-2);
        double[] prevVelocity2 = Probe.probeVelocitiesLog.get(n-2);
        SolarSystem.setPositions(prevPositions2);
        SolarSystem.setVelocities(prevVelocities2);
        Probe.setPosition(prevPosition2);
        Probe.setVelocity(prevVelocity2);
        double[] prevAcc2 = Probe.calculateAccelerationOfProbe();

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);
        Probe.setPosition(prevPosition);
        Probe.setVelocity(prevVelocity);

        for (int entry = 0; entry < 3; entry++) {
            newPos[entry] = prevPosition[entry] + (MissionData.TIME_STEP_SIZE / 12.0) * (23.0 * prevVelocity[entry] - 16.0 * prevVelocity1[entry] + 5.0 * prevVelocity2[entry]);
            newVel[entry] = prevVelocity[entry] + (MissionData.TIME_STEP_SIZE / 12.0) * (23.0 * prevAcc[entry] - 16.0 * prevAcc1[entry] + 5.0 * prevAcc2[entry]);
        }
        newState[0] = newPos;
        newState[1] = newVel;

        return newState;
    }
}