//k1, k2, k3, and k4: Intermediate variables used in the RK4 method to calculate the new position. They store the velocities.
//v1, v2, v3, and v4: Intermediate variables used in the RK4 method to calculate the new velocity. They store the accelerations.

public class RK3Solver {
    public static double[][] rungeKutta3ForBody(int i) {
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[] prevPos = SolarSystem.getPositions()[i];
        double[] prevVel = SolarSystem.getVelocities()[i];
        double[] k1, k2, k3, v1, v2, v3;
    
        // Calculate k1 and v1
        k1 = prevVel.clone();
        v1 = SolarSystem.calculateAccelerationOfBody(i);
    
        // Calculate k2 and v2
        double[] tempPos1 = new double[3];
        double[] tempVel1 = new double[3];
        for (int j = 0; j < 3; j++) {
            tempPos1[j] = prevPos[j] + 0.5 * MissionData.TIME_STEP_SIZE * k1[j];
            tempVel1[j] = prevVel[j] + 0.5 * MissionData.TIME_STEP_SIZE * v1[j];
        }
        k2 = tempVel1.clone();
        v2 = rk4AccelerationForBody(i, tempPos1);
    
        // Calculate k3 and v3
        double[] tempPos2 = new double[3];
        double[] tempVel2 = new double[3];
        for (int j = 0; j < 3; j++) {
            tempPos2[j] = prevPos[j] + MissionData.TIME_STEP_SIZE;
            tempVel2[j] = prevVel[j] - v1[j] + 2 * v2[j];
        }
        k3 = tempVel2.clone();
        v3 = rk4AccelerationForBody(i, tempPos2);
    
        // Calculate newPos and newVel using k1, k2, k3, l1, l2, l3
        for (int j = 0; j < 3; j++) {
            newPos[j] = prevPos[j] + (MissionData.TIME_STEP_SIZE / 6.0) * (k1[j] + 4 * k2[j] + k3[j]);
            newVel[j] = prevVel[j] + (MissionData.TIME_STEP_SIZE / 6.0) * (v1[j] + 4 * v2[j] + v3[j]);
        }
    
        newState[0] = newPos;
        newState[1] = newVel;
    
        return newState;
    }
    
    public static double[][] rungeKutta3ForProbe() {
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[] prevPos = Probe.getPosition();
        double[] prevVel = Probe.getVelocity();
        double[] k1, k2, k3, v1, v2, v3;
    
        // Calculate k1 and v1
        k1 = prevVel.clone();
        v1 = Probe.calculateAccelerationOfProbe();
    
        // Calculate k2 and v2
        double[] tempPos1 = new double[3];
        double[] tempVel1 = new double[3];
        for (int i = 0; i < 3; i++) {
            tempPos1[i] = prevPos[i] + 0.5 * MissionData.TIME_STEP_SIZE * k1[i];
            tempVel1[i] = prevVel[i] + 0.5 * MissionData.TIME_STEP_SIZE * v1[i];
        }
        Probe.setPosition(tempPos1);
        Probe.setVelocity(tempVel1);
        k2 = tempVel1.clone();
        v2 = Probe.calculateAccelerationOfProbe();
    
        // Calculate k3 and l3
        double[] tempPos2 = new double[3];
        double[] tempVel2 = new double[3];
        for (int i = 0; i < 3; i++) {
            tempPos2[i] = prevPos[i] + MissionData.TIME_STEP_SIZE;
            tempVel2[i] = prevVel[i] - v1[i] + 2 * v2[i];
        }
        Probe.setPosition(tempPos2);
        Probe.setVelocity(tempVel2);
        k3 = tempVel2.clone();
        v3 = Probe.calculateAccelerationOfProbe();
    
        // Calculate newPos and newVel using k1, k2, k3, l1, l2, l3
        for (int i = 0; i < 3; i++) {
            newPos[i] = prevPos[i] + (MissionData.TIME_STEP_SIZE / 6.0) * (k1[i] + 4 * k2[i] + k3[i]);
            newVel[i] = prevVel[i] + (MissionData.TIME_STEP_SIZE / 6.0) * (v1[i] + 4 * v2[i] + v3[i]);
        }
    
    
        // Reset the probe's position and velocity to their original values
        Probe.setPosition(prevPos);
        Probe.setVelocity(prevVel);
    
        newState[0] = newPos;
        newState[1] = newVel;
    
        return newState;
    }

    private static double[] rk4ForceForBody(int i, double[] position){
        double[] f = new double[3];

        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
            if(j != i){
                double m_i = MissionData.MASSES[i];
                double m_j = MissionData.MASSES[j];
                double[] pos_j = SolarSystem.getPositions()[j];
                double dist = Math.sqrt(Math.pow((position[0] - pos_j[0]), 2) + Math.pow(position[1] - pos_j[1], 2) + Math.pow(position[2] - pos_j[2], 2));

                for(int entry = 0; entry < 3; entry++) {
                    double dif = position[entry] - pos_j[entry];
                    f[entry] -= (MissionData.GRAVITY_CONSTANT * m_i * m_j * dif / Math.pow(dist, 3));
                }
            }
        }

        return f;
    }

    public static double[] rk4AccelerationForBody(int i, double[] position){
        double[] f = rk4ForceForBody(i, position);
        double[] a = new double[3];
        double m = MissionData.MASSES[i];

        for(int entry = 0; entry < 3; entry++) {
            a[entry] = f[entry]/m;
        }

        return a;
    }
}