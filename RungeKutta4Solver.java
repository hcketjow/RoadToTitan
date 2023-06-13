//k1, k2, k3, and k4: Intermediate variables used in the RK4 method to calculate the new position. They store the velocities.
//v1, v2, v3, and v4: Intermediate variables used in the RK4 method to calculate the new velocity. They store the accelerations.

public class RungeKutta4Solver {
    public static double[][] rungeKutta4ForBody(int i) {
        double[][] initPositions = SolarSystem.getPositions();
        double[][] initVelocities = SolarSystem.getVelocities();
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[] prevPos = SolarSystem.getPositions()[i];
        double[] prevVel = SolarSystem.getVelocities()[i];
        double[] k1, k2, k3, k4, a1, a2, a3, a4;

        // Calculate k1 and a1
        k1 = computekn(prevVel.clone());
        a1 = computekn(SolarSystem.calculateAccelerationOfBody(i));

        //Modified Euler step for k2 (step/2, update with k1)
        double[][] newVelocitiesFork2 = getVelocitiesFork(k1, a1, 0.5);
        k2 = computekn(newVelocitiesFork2[i]);
        a2 = computekn(SolarSystem.calculateAccelerationOfBody(i));
        resetStates(initPositions, initVelocities);

        //Modified Euler step for k3 (step/2, update with k2)
        double[][] newVelocitiesFork3 = getVelocitiesFork(k2, a2, 0.5);
        k3 = computekn(newVelocitiesFork3[i]);
        a3 = computekn(SolarSystem.calculateAccelerationOfBody(i));
        resetStates(initPositions, initVelocities);


        //Modified Euler step for k4 (step, update with k3)
        double[][] newVelocitiesFork4 = getVelocitiesFork(k3, a3, 1);
        k4 = computekn(newVelocitiesFork4[i]);
        a4 = computekn(SolarSystem.calculateAccelerationOfBody(i));
        resetStates(initPositions, initVelocities);

        // Calculate newPos and newVel using k1, k2, k3, k4, l1, l2, l3, l4
        for (int entry = 0; entry < 3; entry++) {
            newPos[entry] = prevPos[entry] + (k1[entry] + 2 * k2[entry] + 2 * k3[entry] + k4[entry]) / 6.0;
            newVel[entry] = prevVel[entry] + (a1[entry] + 2 * a2[entry] + 2 * a3[entry] + a4[entry]) / 6.0;
        }

        newState[0] = newPos;
        newState[1] = newVel;

        return newState;
    }

    private static void resetStates(double[][] initPositions, double[][] initVelocities) {
        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);
    }

    private static double[] computekn(double[] newVelocitiesFork2) {
        double[] k = newVelocitiesFork2;
        k = new double[]{MissionData.TIME_STEP_SIZE*k[0], MissionData.TIME_STEP_SIZE*k[1], MissionData.TIME_STEP_SIZE*k[2]};
        return k;
    }

    private static double[][] getVelocitiesFork(double[] kn, double[] an, double c) {
        double[][] newPositionsFork = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork[j][entry] = SolarSystem.getPositions()[j][entry] + c * kn[entry];
                newVelocitiesFork[j][entry] = SolarSystem.getVelocities()[j][entry] + c * an[entry];
            }
        }
        resetStates(newPositionsFork, newVelocitiesFork);
        return newVelocitiesFork;
    }

    public static double[][] rk4MethodForProbe() {
        double[][] initPositions = SolarSystem.getPositions();
        double[][] initVelocities = SolarSystem.getVelocities();
        double[] initPosition = Probe.getPosition();
        double[] initVelocity = Probe.getVelocity();

        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[] prevPos = Probe.getPosition();
        double[] prevVel = Probe.getVelocity();
        double[] k1, k2, k3, k4, a1, a2, a3, a4;

        // Calculate k1 and a1
        k1 = computekn(prevVel.clone());
        a1 = computekn(Probe.calculateAccelerationOfProbe());

        //Modified Euler step for k2 (step/2, update with k1)
        double[] probeVelocityFork2 = getProbeVelocityFork2(prevPos, prevVel, k1, a1, 0.5);
        k2 = computekn(probeVelocityFork2);
        a2 = computekn(Probe.calculateAccelerationOfProbe());

        resetStates(initPositions, initVelocities);
        resetState(initPosition, initVelocity);

        //Modified Euler step for k3 (step/2, update with k2)
        double[] probeVelocityFork3 = getProbeVelocityFork2(prevPos, prevVel, k2, a2, 0.5);
        k3 = computekn(probeVelocityFork3);
        a3 = computekn(Probe.calculateAccelerationOfProbe());

        resetStates(initPositions, initVelocities);
        resetState(initPosition, initVelocity);

        //Modified Euler step for k4 (step, update with k3)
        double[] probeVelocityFork4 = getProbeVelocityFork2(prevPos, prevVel, k3, a3, 1);
        k4 = computekn(probeVelocityFork4);
        a4 = computekn(Probe.calculateAccelerationOfProbe());

        resetStates(initPositions, initVelocities);

        // Calculate newPos and newVel using k1, k2, k3, k4, l1, l2, l3, l4
        for (int entry = 0; entry < 3; entry++) {
            newPos[entry] = prevPos[entry] + (k1[entry] + 2 * k2[entry] + 2 * k3[entry] + k4[entry]) / 6.0;
            newVel[entry] = prevVel[entry] + (a1[entry] + 2 * a2[entry] + 2 * a3[entry] + a4[entry]) / 6.0;
        }

        newState[0] = newPos;
        newState[1] = newVel;

        return newState;
    }

    private static double[] getProbeVelocityFork2(double[] prevPos, double[] prevVel, double[] k1, double[] a1, double c) {
        double[][] newPositionsFork2 = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork2 = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork2[j][entry] = SolarSystem.getPositions()[j][entry] + c * k1[entry];
                newVelocitiesFork2[j][entry] = SolarSystem.getVelocities()[j][entry] + c * a1[entry];
            }
        }
        double[] probePositionFork2 = new double[3];
        double[] probeVelocityFork2 = new double[3];
        for(int entry = 0; entry < 3; entry++){
            probePositionFork2[entry] = prevPos[entry] + c * k1[entry];
            probeVelocityFork2[entry] = prevVel[entry] + c * a1[entry];
        }
        resetStates(newPositionsFork2, newVelocitiesFork2);
        resetState(probePositionFork2, probeVelocityFork2);
        return probeVelocityFork2;
    }

    private static void resetState(double[] probePositionFork2, double[] probeVelocityFork2) {
        Probe.setPosition(probePositionFork2);
        Probe.setVelocity(probeVelocityFork2);
    }
}