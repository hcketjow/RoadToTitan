import java.util.ArrayList;
import java.util.Arrays;

public class Simulations {
    public static void simulationOnce(){
        switch (MissionData.SOLVER){
            case "euler":
                EulerSimulation.simulationOnce();
                break;
            case "rk4":
                RK4Simulation.simulationOnce();
                break;
            case "ab":
                if(SolarSystem.solarSystemPositionsLog.size() < 3)
                    EulerSimulation.simulationOnce();
                else
                    ABSimulation.simulationOnce();
                break;
            case "am2":
                AdamsMoultonSimulations.simulationOnce();
                break;
        }
    }

    public static void simulation(){
        for(int k = 0; k < MissionData.N_OF_STEPS; k++)
            simulationOnce();
    }

    public static void simulationWithProbe(){
        for(int k = 0; k < MissionData.N_OF_STEPS; k++)
            simulationWithProbeOnce();
    }

    public static double simulationWithProbeForHC(long n, String target, int orbitHeight){
        double bestDist = Double.MAX_VALUE; //local infinity
        for(int k = 0; k < n; k++){
            simulationWithProbeOnce();
            bestDist = Math.min(bestDist, Math.abs(Probe.calculateDistanceToTarget(target) - orbitHeight));
        }
        return bestDist;
    }

    public static int simulationWithProbeForHCtoOrbitTitan(long n){
        int bestCount = 0; //local -infinity
        for(int k = 0; k < n; k++){
            simulationWithProbeOnce();
            double dist = Probe.calculateDistanceToTarget("titan");
            if (dist <= MissionData.TITAN_RADIUS)
                return 0;
            if (dist <= MissionData.TITAN_RADIUS + 300 && dist >= MissionData.TITAN_RADIUS + 100)
                bestCount++;
        }
        return bestCount;
    }

    public static void simulationWithProbeOnce() {
        switch (MissionData.SOLVER) {
            case "euler":
                EulerSimulation.simulationWithProbeOnce();
                break;
            case "rk4":
                RK4Simulation.simulationWithProbeOnce();
                break;
            case "ab3":
                if(SolarSystem.solarSystemPositionsLog.size() < 3)
                    EulerSimulation.simulationWithProbeOnce();
                else
                    ABSimulation.simulationWithProbeOnce();
                break;
            case "am2":
                AdamsMoultonSimulations.simulationWithProbeOnce();
                break;
        }
    }
}
