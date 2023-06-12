//k1, k2, k3, and k4: Intermediate variables used in the RK4 method to calculate the new position. They store the velocities.
//v1, v2, v3, and v4: Intermediate variables used in the RK4 method to calculate the new velocity. They store the accelerations.

import java.util.Arrays;

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
        k1 = prevVel.clone();
        k1 = new double[]{MissionData.TIME_STEP_SIZE*k1[0], MissionData.TIME_STEP_SIZE*k1[1], MissionData.TIME_STEP_SIZE*k1[2]};
        a1 = SolarSystem.calculateAccelerationOfBody(i);
        a1 = new double[]{MissionData.TIME_STEP_SIZE*a1[0], MissionData.TIME_STEP_SIZE*a1[1], MissionData.TIME_STEP_SIZE*a1[2]};

        //Modified Euler step for k2 (step/2, update with k1)
        double[][] newPositionsFork2 = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork2 = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork2[j][entry] = SolarSystem.getPositions()[j][entry] + 0.5 * k1[entry];
                newVelocitiesFork2[j][entry] = SolarSystem.getVelocities()[j][entry] + 0.5 * a1[entry];
            }
        }
        SolarSystem.setPositions(newPositionsFork2);
        SolarSystem.setVelocities(newVelocitiesFork2);
        k2 = newVelocitiesFork2[i];
        k2 = new double[]{MissionData.TIME_STEP_SIZE*k2[0], MissionData.TIME_STEP_SIZE*k2[1], MissionData.TIME_STEP_SIZE*k2[2]};
        a2 = SolarSystem.calculateAccelerationOfBody(i);
        a2 = new double[]{MissionData.TIME_STEP_SIZE*a2[0], MissionData.TIME_STEP_SIZE*a2[1], MissionData.TIME_STEP_SIZE*a2[2]};

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);

        //Modified Euler step for k3 (step/2, update with k2)
        double[][] newPositionsFork3 = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork3 = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork3[j][entry] = SolarSystem.getPositions()[j][entry] + 0.5 * k2[entry];
                newVelocitiesFork3[j][entry] = SolarSystem.getVelocities()[j][entry] + 0.5 * a2[entry];
            }
        }
        SolarSystem.setPositions(newPositionsFork3);
        SolarSystem.setVelocities(newVelocitiesFork3);
        k3 = newVelocitiesFork3[i];
        k3 = new double[]{MissionData.TIME_STEP_SIZE*k3[0], MissionData.TIME_STEP_SIZE*k3[1], MissionData.TIME_STEP_SIZE*k3[2]};
        a3 = SolarSystem.calculateAccelerationOfBody(i);
        a3 = new double[]{MissionData.TIME_STEP_SIZE*a3[0], MissionData.TIME_STEP_SIZE*a3[1], MissionData.TIME_STEP_SIZE*a3[2]};

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);

        //Modified Euler step for k4 (step, update with k3)
        double[][] newPositionsFork4 = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork4 = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork4[j][entry] = SolarSystem.getPositions()[j][entry] + k3[entry];
                newVelocitiesFork4[j][entry] = SolarSystem.getVelocities()[j][entry] + a3[entry];
            }
        }
        SolarSystem.setPositions(newPositionsFork4);
        SolarSystem.setVelocities(newVelocitiesFork4);
        k4 = newVelocitiesFork4[i];
        k4 = new double[]{MissionData.TIME_STEP_SIZE*k4[0], MissionData.TIME_STEP_SIZE*k4[1], MissionData.TIME_STEP_SIZE*k4[2]};
        a4 = SolarSystem.calculateAccelerationOfBody(i);
        a4 = new double[]{MissionData.TIME_STEP_SIZE*a4[0], MissionData.TIME_STEP_SIZE*a4[1], MissionData.TIME_STEP_SIZE*a4[2]};

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);

        // Calculate newPos and newVel using k1, k2, k3, k4, l1, l2, l3, l4
        for (int entry = 0; entry < 3; entry++) {
            newPos[entry] = prevPos[entry] + (k1[entry] + 2 * k2[entry] + 2 * k3[entry] + k4[entry]) / 6.0;
            newVel[entry] = prevVel[entry] + (a1[entry] + 2 * a2[entry] + 2 * a3[entry] + a4[entry]) / 6.0;
        }

        newState[0] = newPos;
        newState[1] = newVel;

        return newState;
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
        k1 = prevVel.clone();
        k1 = new double[]{MissionData.TIME_STEP_SIZE*k1[0], MissionData.TIME_STEP_SIZE*k1[1], MissionData.TIME_STEP_SIZE*k1[2]};
        a1 = Probe.calculateAccelerationOfProbe();
        a1 = new double[]{MissionData.TIME_STEP_SIZE*a1[0], MissionData.TIME_STEP_SIZE*a1[1], MissionData.TIME_STEP_SIZE*a1[2]};

        //Modified Euler step for k2 (step/2, update with k1)
        double[][] newPositionsFork2 = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork2 = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork2[j][entry] = SolarSystem.getPositions()[j][entry] + 0.5 * k1[entry];
                newVelocitiesFork2[j][entry] = SolarSystem.getVelocities()[j][entry] + 0.5 * a1[entry];
            }
        }
        double[] probePositionFork2 = new double[3];
        double[] probeVelocityFork2 = new double[3];
        for(int entry = 0; entry < 3; entry++){
            probePositionFork2[entry] = prevPos[entry] + 0.5 * k1[entry];
            probeVelocityFork2[entry] = prevVel[entry] + 0.5 * a1[entry];
        }
        SolarSystem.setPositions(newPositionsFork2);
        SolarSystem.setVelocities(newVelocitiesFork2);
        Probe.setPosition(probePositionFork2);
        Probe.setVelocity(probeVelocityFork2);
        k2 = probeVelocityFork2;
        k2 = new double[]{MissionData.TIME_STEP_SIZE*k2[0], MissionData.TIME_STEP_SIZE*k2[1], MissionData.TIME_STEP_SIZE*k2[2]};
        a2 = Probe.calculateAccelerationOfProbe();
        a2 = new double[]{MissionData.TIME_STEP_SIZE*a2[0], MissionData.TIME_STEP_SIZE*a2[1], MissionData.TIME_STEP_SIZE*a2[2]};

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);
        Probe.setPosition(initPosition);
        Probe.setVelocity(initVelocity);

        //Modified Euler step for k3 (step/2, update with k2)
        double[][] newPositionsFork3 = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork3 = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork3[j][entry] = SolarSystem.getPositions()[j][entry] + 0.5 * k2[entry];
                newVelocitiesFork3[j][entry] = SolarSystem.getVelocities()[j][entry] + 0.5 * a2[entry];
            }
        }
        double[] probePositionFork3 = new double[3];
        double[] probeVelocityFork3 = new double[3];
        for(int entry = 0; entry < 3; entry++){
            probePositionFork3[entry] = prevPos[entry] + 0.5 * k2[entry];
            probeVelocityFork3[entry] = prevVel[entry] + 0.5 * a2[entry];
        }
        SolarSystem.setPositions(newPositionsFork3);
        SolarSystem.setVelocities(newVelocitiesFork3);
        Probe.setPosition(probePositionFork3);
        Probe.setVelocity(probeVelocityFork3);
        k3 = probeVelocityFork3;
        k3 = new double[]{MissionData.TIME_STEP_SIZE*k3[0], MissionData.TIME_STEP_SIZE*k3[1], MissionData.TIME_STEP_SIZE*k3[2]};
        a3 = Probe.calculateAccelerationOfProbe();
        a3 = new double[]{MissionData.TIME_STEP_SIZE*a3[0], MissionData.TIME_STEP_SIZE*a3[1], MissionData.TIME_STEP_SIZE*a3[2]};

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);
        Probe.setPosition(initPosition);
        Probe.setVelocity(initVelocity);

        //Modified Euler step for k4 (step, update with k3)
        double[][] newPositionsFork4 = new double[SolarSystem.N_OF_OBJECTS][3];
        double[][] newVelocitiesFork4 = new double[SolarSystem.N_OF_OBJECTS][3];
        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            for(int entry = 0; entry < 3; entry++){
                newPositionsFork4[j][entry] = SolarSystem.getPositions()[j][entry] + k3[entry];
                newVelocitiesFork4[j][entry] = SolarSystem.getVelocities()[j][entry] + a3[entry];
            }
        }
        double[] probePositionFork4 = new double[3];
        double[] probeVelocityFork4 = new double[3];
        for(int entry = 0; entry < 3; entry++){
            probePositionFork4[entry] = prevPos[entry] + 0.5 * k3[entry];
            probeVelocityFork4[entry] = prevVel[entry] + 0.5 * a3[entry];
        }
        SolarSystem.setPositions(newPositionsFork4);
        SolarSystem.setVelocities(newVelocitiesFork4);
        Probe.setPosition(probePositionFork4);
        Probe.setVelocity(probeVelocityFork4);
        k4 = probeVelocityFork4;
        k4 = new double[]{MissionData.TIME_STEP_SIZE*k4[0], MissionData.TIME_STEP_SIZE*k4[1], MissionData.TIME_STEP_SIZE*k4[2]};
        a4 = Probe.calculateAccelerationOfProbe();
        a4 = new double[]{MissionData.TIME_STEP_SIZE*a4[0], MissionData.TIME_STEP_SIZE*a4[1], MissionData.TIME_STEP_SIZE*a4[2]};

        SolarSystem.setPositions(initPositions);
        SolarSystem.setVelocities(initVelocities);

        // Calculate newPos and newVel using k1, k2, k3, k4, l1, l2, l3, l4
        for (int entry = 0; entry < 3; entry++) {
            newPos[entry] = prevPos[entry] + (k1[entry] + 2 * k2[entry] + 2 * k3[entry] + k4[entry]) / 6.0;
            newVel[entry] = prevVel[entry] + (a1[entry] + 2 * a2[entry] + 2 * a3[entry] + a4[entry]) / 6.0;
        }

        newState[0] = newPos;
        newState[1] = newVel;

        return newState;
    }
}