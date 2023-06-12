import java.util.ArrayList;
import java.util.Arrays;

public class AdamsMoulton {

        public static ArrayList<double[][]> adamsMoulton_solverForBody() {
                ArrayList<double[][]> newState = new ArrayList<double[][]>();

                double[][] newPos = new double[SolarSystem.N_OF_OBJECTS][3];
                double[][] newVel = new double[SolarSystem.N_OF_OBJECTS][3];
                double[][] newAcc = new double[SolarSystem.N_OF_OBJECTS][3];

                double[][] prevPos = AdamsMoultonSimulations.prevState.get(0);
                double[][] prevVel = AdamsMoultonSimulations.prevState.get(1);
                double[][] prevAcc = AdamsMoultonSimulations.prevState.get(2);

                double[][] prevPrevVel = AdamsMoultonSimulations.prevPrevState.get(1);
                double[][] prevPrevAcc = AdamsMoultonSimulations.prevPrevState.get(2);

                long step_size = MissionData.TIME_STEP_SIZE;

                // Estimate future position and velocity using RK3 method
                for (int i = 0; i < SolarSystem.N_OF_OBJECTS; i++) {
                        newPos[i] = RK3Solver.rungeKutta3ForBody(i)[0];
                        newVel[i] = RK3Solver.rungeKutta3ForBody(i)[1];
                }
                newAcc = calculateAccelerationForBody(newPos);

                // Calculate new position and velocity using Adams-Moulton method
                for (int i = 0; i < SolarSystem.N_OF_OBJECTS; i++) {
                        for (int entry = 0; entry < 3; entry++) {
                                newPos[i][entry] = prevPos[i][entry] + ((step_size) / 12) * (5 * newVel[i][entry] + 8 * prevVel[i][entry] - prevPrevVel[i][entry]);

                                newVel[i][entry] = prevVel[i][entry] + ((step_size) / 12) * (5 * newAcc[i][entry] + 8 * prevAcc[i][entry] - prevPrevAcc[i][entry]);
                        }
                }

                newState.add(newPos);
                newState.add(newVel);

                return newState;
        }

        public static double[][] adamsMoulton_solverForProbe() {

                //positions and velocites of celestial bodies already updated
                double[][] newStateProbe = new double[2][3];
                double[] newPosProbe = new double[3];
                double[] newVelProbe = new double[3];
                double[] newAccProbe = new double[3];

                long step_size = MissionData.TIME_STEP_SIZE;

                // Estimate future position and velocity using RK3 method
                newPosProbe = RK3Solver.rungeKutta3ForProbe()[0];
                newVelProbe = RK3Solver.rungeKutta3ForProbe()[1];

                //TODO: calculate newAcc
                newAccProbe = calculateAccelerationForProbe(newPosProbe);

                // Calculate new position and velocity using Adams-Moulton method
                for (int entry = 0; entry < 3; entry++) {
                        newPosProbe[entry] = AdamsMoultonSimulations.prevStateProbe.get(0)[entry] + ((step_size) / 12) * (5 * newVelProbe[entry] + 8 * AdamsMoultonSimulations.prevStateProbe.get(1)[entry] - AdamsMoultonSimulations.prevPrevStateProbe.get(1)[entry]); 
                        newVelProbe[entry] = AdamsMoultonSimulations.prevStateProbe.get(1)[entry] + ((step_size) / 12) * (5 * newAccProbe[entry] + 8 * AdamsMoultonSimulations.prevStateProbe.get(2)[entry] - AdamsMoultonSimulations.prevPrevStateProbe.get(2)[entry]); 
                }

                newStateProbe[0] = newPosProbe;
                newStateProbe[1] = newVelProbe;

                return newStateProbe;
        }

        public static double[] calculateAccelerationForProbe(double[] newPos) {
                double[] newAcc = new double[3];
                double[] f = new double[3];

                double m_i = MissionData.PROBE_MASS;
                double[] pos_i = newPos;
        
                for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
                    double m_j = MissionData.MASSES[j];
                    double[] pos_j = SolarSystem.getPositions()[j];
                    double dist = Math.sqrt(Math.pow((pos_i[0] - pos_j[0]), 2) + Math.pow(pos_i[1] - pos_j[1], 2) + Math.pow(pos_i[2] - pos_j[2], 2));
        
                    for(int entry = 0; entry < 3; entry++) {
                        double dif = pos_i[entry] - pos_j[entry];
                        f[entry] -= (MissionData.GRAVITY_CONSTANT * m_i * m_j * dif / Math.pow(dist, 3));
                    }
                }

                for (int i = 0; i < 3; i++) {
                        newAcc[i] = f[i]/m_i;
                }
                return newAcc;
        }

        public static double[][] calculateAccelerationForBody(double[][] newPos) {
                double[][] newAcc = new double[SolarSystem.N_OF_OBJECTS][3];
                double[][] f = new double[SolarSystem.N_OF_OBJECTS][3];
                for (int i = 0; i < SolarSystem.N_OF_OBJECTS; i++) {
                        double m_i = MissionData.MASSES[i];
                        for(int j = 0; j < SolarSystem.N_OF_OBJECTS; j++){
                                if(j != i){
                                    double m_j = MissionData.MASSES[j];
                                    double[] pos_i = newPos[i];
                                    double[] pos_j = newPos[j];
                                    double dist = Math.sqrt(Math.pow((pos_i[0] - pos_j[0]), 2) + Math.pow(pos_i[1] - pos_j[1], 2) + Math.pow(pos_i[2] - pos_j[2], 2));
                    
                                    for(int entry = 0; entry < 3; entry++) {
                                        double dif = pos_i[entry] - pos_j[entry];
                                        f[i][entry] -= (MissionData.GRAVITY_CONSTANT * m_i * m_j * dif / Math.pow(dist, 3));
                                    }
                                }
                        }
                        for (int j = 0; j < 3; j++) {
                                newAcc[i][j] = f[i][j]/m_i;
                        }
                }
                return newAcc;
        }
}