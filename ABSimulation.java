public class ABSimulation {
    public static void simulationOnce(){
        double[][] newPositions = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocities = new double[SolarSystem.N_OF_OBJECTS][3];

        for(int i = 0; i < SolarSystem.N_OF_OBJECTS; i++){
            double[][] newState = AdamBashforthSolver.ab3ForBody(i);
            newPositions[i] = newState[0];
            newVelocities[i] = newState[1];
        }
        SolarSystem.setPositions(newPositions);
        SolarSystem.setVelocities(newVelocities);
    }

    public static void simulationWithProbeOnce(){
        simulationOnce();
        double[][] newProbeState = AdamBashforthSolver.adamsBashforthForProbe();
        Probe.setPosition(newProbeState[0]);
        Probe.setVelocity(newProbeState[1]);
    }

}