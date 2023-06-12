import java.util.ArrayList;
import java.util.Arrays;

public class AdamsMoultonSimulations {
    public static boolean toBeInitialized = true;

    public static ArrayList<double[][]> prevPrevState = new ArrayList<double[][]>(); 
    public static ArrayList<double[][]> prevState = new ArrayList<double[][]>();
    public static ArrayList<double[]> prevPrevStateProbe = new ArrayList<double[]>(); 
    public static ArrayList<double[]> prevStateProbe = new ArrayList<double[]>();
    private static double[][] prevPrevPos = new double[SolarSystem.N_OF_OBJECTS][3];
    private static double[][] prevPrevVel = new double[SolarSystem.N_OF_OBJECTS][3];
    private static double[][] prevPrevAcc = new double[SolarSystem.N_OF_OBJECTS][3];
    private static double[][] prevPos = new double[SolarSystem.N_OF_OBJECTS][3];
    private static double[][] prevVel = new double[SolarSystem.N_OF_OBJECTS][3];
    private static double[][] prevAcc = new double[SolarSystem.N_OF_OBJECTS][3];
    private static double[] prevPrevPosProbe = new double[3];
    private static double[] prevPrevVelProbe = new double[3];
    private static double[] prevPrevAccProbe = new double[3];
    private static double[] prevPosProbe = new double[3];
    private static double[] prevVelProbe = new double[3];
    private static double[] prevAccProbe = new double[3];

    public static void initialization() {
        // updating prevprev and prev states of celestial bodies
        prevPrevPos = SolarSystem.getPositions();
        prevPrevState.add(prevPrevPos);

        prevPrevVel = SolarSystem.getVelocities();
        prevPrevState.add(prevPrevVel);

        prevPrevAcc = new double[SolarSystem.N_OF_OBJECTS][3];
        for (int i = 0; i < SolarSystem.N_OF_OBJECTS; i++) {
            prevPrevAcc[i] = SolarSystem.calculateAccelerationOfBody(i);
        }
        prevPrevState.add(prevPrevAcc);
    
        for (int i = 0; i < SolarSystem.N_OF_OBJECTS; i++) {
            prevPos[i] = RK3Solver.rungeKutta3ForBody(i)[0];
            prevVel[i] = RK3Solver.rungeKutta3ForBody(i)[1];
    }

        prevState.add(prevPos);
        prevState.add(prevVel);

        // calculate prevAcc with prevPos
        // prevAcc = new double[SolarSystem.N_OF_OBJECTS][3];

        for (int i = 0; i < SolarSystem.N_OF_OBJECTS; i++) {
            prevAcc[i] = RK3Solver.rk4AccelerationForBody(i, prevPos[i]);
        }
        prevState.add(prevAcc);

        SolarSystem.setPositions(prevPos);
        SolarSystem.setVelocities(prevVel);

        // updating prevprev and prev state of probe
        prevPrevPosProbe = Probe.getPosition();
        prevPrevStateProbe.add(prevPrevPosProbe);
        prevPrevVelProbe = Probe.getVelocity();
        prevPrevStateProbe.add(prevPrevVelProbe);
        prevPrevAccProbe = Probe.calculateAccelerationOfProbe();
        prevPrevStateProbe.add(prevPrevAccProbe);

        prevPosProbe = RK3Solver.rungeKutta3ForProbe()[0];
        prevStateProbe.add(prevPosProbe);
        prevVelProbe = RK3Solver.rungeKutta3ForProbe()[1];
        prevStateProbe.add(prevVelProbe);

        prevAccProbe = AdamsMoulton.calculateAccelerationForProbe(prevPosProbe);
        prevStateProbe.add(prevAccProbe);

        Probe.setPosition(prevPosProbe);
        Probe.setVelocity(prevVelProbe);
    }

    public static void simulationOnce(){
        ArrayList<double[][]> newState = AdamsMoulton.adamsMoulton_solverForBody();

        SolarSystem.setPositions(newState.get(0));
        SolarSystem.setVelocities(newState.get(1));
    }

    public static void simulation(){
        for(int k = 0; k < MissionData.N_OF_STEPS; k++){
            simulationOnce();
        }
    }

    public static void simulationWithProbe(){
        for(int k = 0; k < MissionData.N_OF_STEPS; k++){
            simulationWithProbeOnce();
        }
    }

    public static void simulationWithProbeOnce(){

        if (toBeInitialized) {
            initialization();//prevprev and prev state updated
            toBeInitialized = false; 
        }

        simulationOnce(); //positions and velocities of celestial bodies already updated

        double[][] newProbeState = AdamsMoulton.adamsMoulton_solverForProbe(); // ima koko yade
        
        Probe.setPosition(newProbeState[0]);
        Probe.setVelocity(newProbeState[1]);

        //update each state
        AdamsMoultonSimulations.prevPrevState = AdamsMoultonSimulations.prevState; //zero
        AdamsMoultonSimulations.prevState.set(0, SolarSystem.getPositions()); //error
        AdamsMoultonSimulations.prevState.set(1, SolarSystem.getVelocities());
        AdamsMoultonSimulations.prevState.set(2, SolarSystem.calculateAccelerations());
        AdamsMoultonSimulations.prevPrevStateProbe = AdamsMoultonSimulations.prevStateProbe;
        AdamsMoultonSimulations.prevStateProbe.set(0, Probe.getPosition());
        AdamsMoultonSimulations.prevStateProbe.set(1, Probe.getVelocity());
        AdamsMoultonSimulations.prevStateProbe.set(2, Probe.calculateAccelerationOfProbe());
    }

}