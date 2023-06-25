import java.util.*;

public class GradientDescent {

    public static ArrayList<double[]> neighbors = new ArrayList<double[]>();
    public static ArrayList<Double> distanceForEachNeighbor = new ArrayList<Double>();

    public static double[] velocity = MissionData.PROBE_INIT_VELOCITY;
    public static double[] prevVelocity;

    public static double distance;
    public static double prevDistance = 0;

    public static double step = 0.1;
    public static double prevStep = 0.2;

    public static double derivative = (prevDistance - distance) / (prevStep - step);

    public static double[] calculateVelocity(double[] probePosition, double[] probeVelocity, double[][] solarSystemPositions, double[][] solarSystemVelocities, String destination, int duration) {

        // Reset position of the celestial objects
        SolarSystem.setPositions(solarSystemPositions);
        SolarSystem.setVelocities(solarSystemVelocities);
        Probe.setPosition(probePosition);

        for (int i = 0; i < MissionData.N_OF_STEPS; i++) {
            Simulations.simulationWithProbeOnce();
        }

        if (destination.equals("titan")) {
            distance = Probe.calculateDistanceToTarget("titan");
        } else {
            distance = Probe.calculateDistanceToTarget("earth");
        }

        System.out.println("first distance " + distance);

        // step = AdjustStepSize.adjustStepSize(distance, Math.log10(2)); //chosen manually, could be different value

        // 2. Update the distance and step size 
        // Creating neighbors
        for (int i = 0; i < 4; i++) {
            double[] tmpVector = { MissionData.PROBE_INIT_VELOCITY[0] + step,  MissionData.PROBE_INIT_VELOCITY[1],  MissionData.PROBE_INIT_VELOCITY[2] };
            neighbors.add(tmpVector);
        }
        for (int i = 4; i < 8; i++) {
            double[] tmpVector =  { MissionData.PROBE_INIT_VELOCITY[0], MissionData.PROBE_INIT_VELOCITY[1], MissionData.PROBE_INIT_VELOCITY[2]};
            neighbors.add(tmpVector);
        }
        for (int i = 0; i < 2; i++) {
            neighbors.get(i)[1] += step;
            neighbors.get(i + 4)[1] += step;
        }
        neighbors.get(0)[2] += step;
        neighbors.get(2)[2] += step;
        neighbors.get(4)[2] += step;
        neighbors.get(6)[2] += step;

        // Test each neighbor
        for (double[] neighbor : neighbors) {
            SolarSystem.setPositions(solarSystemPositions);
            SolarSystem.setVelocities(solarSystemVelocities);
            Probe.setPosition(probePosition);
            Probe.setVelocity(neighbor);
            // MissionData.PROBE_INIT_VELOCITY = neighbor;
            for (int i = 0; i < MissionData.N_OF_STEPS; i++) {
                Simulations.simulationWithProbeOnce();
            }
            if (destination.equals("titan")) {
                distanceForEachNeighbor.add(Probe.calculateDistanceToTarget("titan")); //store the distance to compare later
            } else {
                distanceForEachNeighbor.add(Probe.calculateDistanceToTarget("earth")); //store the distance to compare later
            }

        }

        // Update prevDistance (y_0)
        prevDistance = distance;
        distance = Collections.min(distanceForEachNeighbor);
        prevVelocity = velocity;

        // MissionData.PROBE_INIT_VELOCITY = neighbors.get(distanceForEachNeighbor.indexOf(distance));
        velocity = neighbors.get(distanceForEachNeighbor.indexOf(distance));

        derivative = ((distance - prevDistance)) / subtraction(velocity, prevVelocity);

        // prevStep = step; 
        // step = AdjustStepSize.adjustStepSize(distance, derivative/10E6); //error

        System.out.println("second distance " + distance);

        // 3. Now let the program run experiment iteratively...
        double limit;
        if (destination.equals("titan")) {
            limit = 300 + MissionData.TITAN_RADIUS;
        } else {
            limit = MissionData.EARTH_RADIUS;
        }

        while ((distance > limit) && (!Double.isNaN(step))) { // okay up until here
            iteration(probePosition, velocity, solarSystemPositions, solarSystemVelocities, destination);
        }

        // 4. Print the best velocity
        if (destination.equals("titan")) {
            System.out.println("Distance to titan: " + distance);
            // calculate the returning velocity here?
        } else {
            System.out.println("Distance to earth: " + distance);
        }
        // System.out.println(Arrays.toString(Probe.getPosition()));
        // System.out.println(Arrays.toString(Probe.getVelocity()));
        // System.out.println(Arrays.deepToString(SolarSystem.getPositions()));
        // System.out.println(Arrays.deepToString(SolarSystem.getVelocities()));

        // 5. Get the position, velocity
        Probe.setPosition(probePosition);
        SolarSystem.setVelocities(solarSystemVelocities);
        SolarSystem.setPositions(solarSystemPositions);

        distance = 0;
        prevDistance = 0;
        return velocity;
    }

    private static void iteration(double[] probePosition, double[] probeVelocity, double[][] solarSystemPositions, double[][] solarSystemVelocities, String destination) {
        // Reset positions of the celestial objects
        // SolarSystem.setPositions(solarSystemPositions);
        // SolarSystem.setVelocities(solarSystemVelocities);
        // Probe.setPosition(probePosition);

        // Updating neighbors
        for (int i = 0; i < 4; i++) {
            double[] tmpVector = {  probeVelocity[0] + step,  probeVelocity[1],  probeVelocity[2] };
            neighbors.set(i, tmpVector);
        }
        for (int i = 4; i < 8; i++) {
            double[] tmpVector =  { probeVelocity[0], probeVelocity[1], probeVelocity[2]};
            neighbors.set(i, tmpVector);
        }
        for (int i = 0; i < 2; i++) {
            neighbors.get(i)[1] += step;
            neighbors.get(i + 4)[1] += step;
        }
        neighbors.get(0)[2] += step;
        neighbors.get(2)[2] += step;
        neighbors.get(4)[2] += step;
        neighbors.get(6)[2] += step;

        // Test each neighbor
        for (double[] neighbor : neighbors) {
            SolarSystem.setPositions(solarSystemPositions);
            SolarSystem.setVelocities(solarSystemVelocities);
            Probe.setPosition(probePosition);
            Probe.setVelocity(neighbor);
            // MissionData.PROBE_INIT_VELOCITY = neighbor;
            for (int i = 0; i < MissionData.N_OF_STEPS; i++) {
                Simulations.simulationWithProbeOnce();
            }
            if (destination.equals("titan")) {
                distanceForEachNeighbor.set(neighbors.indexOf(neighbor), Probe.calculateDistanceToTarget("titan"));
            } else {
                distanceForEachNeighbor.set(neighbors.indexOf(neighbor), Probe.calculateDistanceToTarget("earth"));
            }

        }

        // If the distance is not imporoving, flip the sign of the step
        if (Collections.min(distanceForEachNeighbor) >= distance) {
            step *= -0.7;
            // System.out.println("minimum distance was " + Collections.min(distanceForEachNeighbor));
            // System.out.println("velocity with minimum distance was " + Arrays.toString(neighbors.get(distanceForEachNeighbor.indexOf(Collections.min(distanceForEachNeighbor)))));
            System.out.println("flipping");
            return;
        }

        // Updating the variables
        prevDistance = distance;
        distance = Collections.min(distanceForEachNeighbor);
        prevVelocity = velocity;
        velocity = neighbors.get(distanceForEachNeighbor.indexOf(distance));
        derivative = (prevDistance - distance) / subtraction(velocity, prevVelocity); // or distance - prevDistance
        prevStep = step;
        step = adjustStepSize(distance, derivative/10E6); //
        // step = AdjustStepSize.adjustStepSize(distance, derivative/10E6);


        // Print out the process
        // System.out.println("-------------------" + step + "-------------------");
        System.out.println("distance: " + distance);
        // System.out.print(", velocity: " + Arrays.toString(velocity));
        // System.out.println();
    }


    public static double subtraction(double[] v1, double[] v2) {
        double gap = Math.sqrt(Math.pow((v1[0] - v2[0]), 2) + Math.pow(v1[1] - v2[1], 2) + Math.pow(v1[2] - v2[2], 2));
        return gap;
    }

    public static double adjustStepSize (double distance, double step) {
        double tmpDistanceMeasure = Math.log10(distance);
        System.out.println(tmpDistanceMeasure);

        if (tmpDistanceMeasure < 3) { //3.6
            while (Math.log10(Math.abs(step)) > -4.5) { //-4.1 -> 1000-200ずつ下がる
                step *= 0.5;
            }
        } else if (tmpDistanceMeasure < 3.5) { //4.2
            while (Math.log10(Math.abs(step)) > -4.5) { //-4 -> 6000ずつ下がる
                step *= 0.5;
            }
        } else if (tmpDistanceMeasure < 4) { //4.5
            while (Math.log10(Math.abs(step)) > -4) { //0.001
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 4.5)) { //5
            while (Math.log10(Math.abs(step)) > -3.5) { //0.005
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 5)) { //5.7
            while (Math.log10(Math.abs(step)) > -3) { //0.01 //-2.5
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 5.5)) { //6.3
            while (Math.log10(Math.abs(step)) > -2) { //0.02, 100000ずつ下がっていく
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 6)) { //6.6
            while (Math.log10(Math.abs(step)) > -1.5) { //0.1, 1000000ずつ下がっていく
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 6.5)) { //7
            while (Math.log10(Math.abs(step)) > -1) { //0.3yaaaaaaaa
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 7)) { //7.4
            while (Math.log10(Math.abs(step)) > -0.5) { //
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 7.6)) { //7.8
            while (Math.log10(Math.abs(step)) > 0.3) { //
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 8)) { //8.3
            while (Math.log10(Math.abs(step)) > 0.5) { //6
                step *= 0.5;
            }
        } else if ((tmpDistanceMeasure < 8.5)) { //8.3
            while (Math.log10(Math.abs(step)) > 0.8) { //6
                step *= 0.5;
            }
        } else { // 30000000
            while (Math.log10(Math.abs(step)) < 1) { //30 -1E7
                step *= 1.5;
            }
        }

        // if ((tmpDistanceMeasure < 8.5)) { //8.3
        //     while (Math.log10(Math.abs(step)) > 0.8) { //6
        //         step *= 0.5;
        //     }
        // } else { // 30000000
        //     while (Math.log10(Math.abs(step)) < 1) { //30 -1E7
        //         step *= 1.5;
        //     }
        // }


        return step;
    }
}