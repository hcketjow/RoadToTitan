import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HillClimbing {
    private static ArrayList<double[]> usedVectors = new ArrayList<>();
    private static double step = 4;
    private static double correction = 0.97;

    private static ArrayList<double[]> generateNeighbours(double[] startVector){

        ArrayList<double[]> neighbours = new ArrayList<double[]>();
        double[] vector = new double[3];

        double[] diff = {-step, 0, step};
        //combinations for neighbours
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    vector = new double[]{startVector[0] + diff[i], startVector[1] + diff[j], startVector[2] + diff[k]};
                    if(!(Arrays.equals(vector, startVector)) && !isUsed(vector)) neighbours.add(vector);
                }
            }
        }
        return neighbours;
    }

    // scheduled step size variation (or not)
    private static ArrayList<Object> hillClimbing(double[] currentBestVector, double currentBestDist, double[][] solarSystemInitPositions, double[][] solarSystemInitVelocities, double[] probeInitPosition, double[] probeInitVelocity, long duration, String target, int distanceFromCentre, int offset){
        if(currentBestDist < offset){
            ArrayList<Object> newBest = new ArrayList<>();
            newBest.add(currentBestVector);
            newBest.add(currentBestDist);
            return newBest;
        }

        //create a population of neighbours
        ArrayList<double[]> neighbours = generateNeighbours(currentBestVector);
        usedVectors.add(currentBestVector);
        usedVectors.addAll(neighbours);

        //get distances for neighbours ///best distances = closest to mid-orbit throughout the flight
        ArrayList<Double> distances = new ArrayList<>();
        for(int i = 0; i < neighbours.size(); i++){
            Probe.setVelocity(neighbours.get(i));
            distances.add(Simulations.simulationWithProbeForHC(duration, target, distanceFromCentre));
            resetAllStates(solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity);
        }

        //get index of the smallest distance /// to mid-orbit
        int index = distances.indexOf(Collections.min(distances));

        //create output list
        ArrayList<Object> newBest = new ArrayList<>();

        if(distances.get(index) <= currentBestDist){
            System.out.println(Arrays.toString(neighbours.get(index)));
            System.out.println(distances.get(index));
            step = step * distances.get(index)/currentBestDist;
            System.out.println("------step: " + step + "--------");
            return hillClimbing(neighbours.get(index), distances.get(index), solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration, target, distanceFromCentre, offset);//step * distances.get(index)/currentBestDist
        }else{
            newBest.add(currentBestVector);
            newBest.add(currentBestDist);
            return newBest;
        }
    }

    public static ArrayList<Object> computeNewVector(double[][] solarSystemInitPositions, double[][] solarSystemInitVelocities, double[] probeInitPosition, double[] probeInitVelocity, long duration, String missionStage, String target){
        switch (missionStage){
            case "going to titan orbit":
                return executeHillClimbing(solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration, target, MissionData.TITAN_RADIUS + (300 + 100) / 2, 100);
            case "staying on titan orbit":
                return hillClimbingToOrbitTitan(probeInitVelocity, -1, solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration);
            case "going back to earth":
                return executeHillClimbing(solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration, target, 0, MissionData.EARTH_RADIUS);
            default:
                return new ArrayList<>();
        }
    }

    private static ArrayList<Object> executeHillClimbing(double[][] solarSystemInitPositions, double[][] solarSystemInitVelocities, double[] probeInitPosition, double[] probeInitVelocity, long duration, String target, int distanceFromCentre, int offset) {
        //first run with old velocity vector
        Simulations.simulationWithProbeForHC(duration, target, distanceFromCentre);
        System.out.println("ENTERS HC");
        double[] currentBest = probeInitVelocity;
        double bestDist = Probe.calculateDistanceToTarget(target); //local infinity
        System.out.println(bestDist);

        resetAllStates(solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity);

        System.out.println("------step: " + step + "--------");
        ArrayList<Object> newBest = hillClimbing(currentBest, bestDist, solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration, target, distanceFromCentre, offset);
        step /= 2;

        while(step/4 > Double.MIN_VALUE && (Double) newBest.get(1) > offset) {//
            System.out.println("------step: " + step + "--------");
            newBest = hillClimbing((double[]) newBest.get(0), (double) newBest.get(1), solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration, target, distanceFromCentre, offset);
            step = step/2;
        }

        //an optimisation trick based on coarse observations - 750: 20; //1000: 20; //2000: 20; //500: 18; (solver step: hill climbing step)
        //why? not sure, but this way it is actually fast
        if(MissionData.TIME_STEP_SIZE <= 500)
            step = 18;
        else
            step = 20;

        usedVectors = new ArrayList<>();

        System.out.println("done");
        System.out.println("vector: " + Arrays.toString((double[]) newBest.get(0)));
        System.out.println("mag: " + Math.sqrt(Math.pow(((double[]) newBest.get(0))[0], 2) + Math.pow(((double[]) newBest.get(0))[1], 2) + Math.pow(((double[]) newBest.get(0))[2], 2)));
        System.out.println("distance to orbit: " + newBest.get(1));
        System.out.println(Arrays.toString(currentBest) + "\t -init");
        System.out.println(bestDist + "\t \t -init");

        return newBest;
    }

    private static void resetAllStates(double[][] solarSystemInitPositions, double[][] solarSystemInitVelocities, double[] probeInitPosition, double[] probeInitVelocity) {
        SolarSystem.setPositions(solarSystemInitPositions);
        SolarSystem.setVelocities(solarSystemInitVelocities);
        Probe.setPosition(probeInitPosition);
        Probe.setVelocity(probeInitVelocity);
    }

    private static ArrayList<Object> hillClimbingToOrbitTitan(double[] currentBestVector, int currentBestCount, double[][] solarSystemInitPositions, double[][] solarSystemInitVelocities, double[] probeInitPosition, double[] probeInitVelocity, long duration) {

        if(currentBestCount == duration){
            ArrayList<Object> newBest = new ArrayList<>();
            newBest.add(currentBestVector);
            newBest.add(currentBestCount);
            return newBest;
        }

        System.out.println("------correction: " + correction + "--------");
        double k = (1 + 1/(Probe.calculateDistanceToTarget("titan"))) * correction;
        double[] tv = SolarSystem.getVelocities()[MissionData.idMap.get("titan")];

        double[] v = new double[]{k * tv[0], k * tv[1], k * tv[2]};
        Probe.setVelocity(v);
        int count = Simulations.simulationWithProbeForHCtoOrbitTitan(duration);

        resetAllStates(solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity);

        System.out.println(count);

        if(correction <= 1){
            System.out.println(Arrays.toString(currentBestVector));
            System.out.println(currentBestCount);
            if(count >= currentBestCount){
                correction += 0.001/4/2/2/5/3;
                return hillClimbingToOrbitTitan(v, count, solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration);
            }else{
                correction *= 1.000001;
                return hillClimbingToOrbitTitan(currentBestVector, currentBestCount, solarSystemInitPositions, solarSystemInitVelocities, probeInitPosition, probeInitVelocity, duration);
            }
        }else{
            ArrayList<Object> newBest = new ArrayList<Object>();
            newBest.add(currentBestVector);
            newBest.add(currentBestCount);
            return newBest;
        }
    }
    private static boolean isUsed(double[] vector){
        for(double[] v : usedVectors){
            if(v[0] == vector[0] && v[1] == vector[1] && v[2] == vector[2]){
                return true;
            }
        }
        return false;
    }

}