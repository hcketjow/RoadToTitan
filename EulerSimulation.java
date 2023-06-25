import java.util.Arrays;

public class EulerSimulation {
    public static void simulationOnce(){
        double[][] newPositions = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocities = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int i = 0; i < SolarSystem.N_OF_OBJECTS; i++){
            double[][] newState = EulerSolver.eulersMethodForBody(i);
            newPositions[i] = newState[0];
            newVelocities[i] = newState[1];
        }
        SolarSystem.setPositions(newPositions);
        SolarSystem.setVelocities(newVelocities);
    }

    public static void simulationWithProbeOnce(){
        simulationOnce();
        double[][] newProbeState = EulerSolver.eulersMethodForProbe();
        Probe.setPosition(newProbeState[0]);
        Probe.setVelocity(newProbeState[1]);
    }

    public static void simulationWithLandingModuleOnce(){
        double[][] newModuleState = EulerSolver.eulersMethodForLandingModule();
        LandingModule.setPosition(newModuleState[0]);
        LandingModule.setVelocity(newModuleState[1]);
        System.out.println(Arrays.deepToString(newModuleState));
    }

}