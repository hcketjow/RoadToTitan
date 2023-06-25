import java.util.ArrayList;

public class SolarSystem {

    public static ArrayList<double[][]> solarSystemPositionsLog = new ArrayList<>();
    public static ArrayList<double[][]> solarSystemVelocitiesLog = new ArrayList<>();
    private static double[][] positions = copy2DArray(MissionData.SOLAR_SYSTEM_INIT_POSITIONS);
    private static double[][] velocities = copy2DArray(MissionData.SOLAR_SYSTEM_INIT_VELOCITIES);

    static final int N_OF_OBJECTS = getPositions().length;


    private static double[] calculateTotalForceOnBody(int i){
        double[] f = new double[3];

        //final formula:
        //f = -sum( g * m_i * m_j * dif(pos_i, pos_j) / (euclidean_distance(pos_i, pos_j))^3 ) ..where i!=j, pos_i/j vectors
        for(int j = 0; j < N_OF_OBJECTS; j++){
            if(j != i){
                double m_i = MissionData.MASSES[i];
                double m_j = MissionData.MASSES[j];
                double[] pos_i = getPositions()[i];
                double[] pos_j = getPositions()[j];
                double dist = Math.sqrt(Math.pow((pos_i[0] - pos_j[0]), 2) + Math.pow(pos_i[1] - pos_j[1], 2) + Math.pow(pos_i[2] - pos_j[2], 2));

                for(int entry = 0; entry < 3; entry++) {
                    double dif = pos_i[entry] - pos_j[entry];
                    f[entry] -= (MissionData.GRAVITY_CONSTANT * m_i * m_j * dif / Math.pow(dist, 3));
                }
            }
        }

        return f;
    }

    public static double[] calculateAccelerationOfBody(int i){
        double[] f = calculateTotalForceOnBody(i);
        double[] a = new double[3];
        double m = MissionData.MASSES[i];

        for(int entry = 0; entry < 3; entry++)
            a[entry] = f[entry]/m;


        return a;
    }

    //helper-method to copy positions and velocities
    private static double[][] copy2DArray(double[][] arr){
        double[][] result = new double[arr.length][];
        for(int i = 0; i < arr.length; i++)
            result[i] = arr[i];
        return result;
    }

    public static double[][] calculateAccelerations(){
        double[][] accelerations = new double[SolarSystem.N_OF_OBJECTS][3];
        for (int i = 0; i < SolarSystem.N_OF_OBJECTS; i++)
            accelerations[i] = SolarSystem.calculateAccelerationOfBody(i);
        return accelerations;
    }

    public static double[][] getPositions() {
        return positions;
    }

    public static void setPositions(double[][] positions) {
        SolarSystem.positions = positions;
    }

    public static double[][] getVelocities() {
        return velocities;
    }

    public static void setVelocities(double[][] velocities) {
        SolarSystem.velocities = velocities;
    }

    public static void resetStates() {
        setPositions(MissionData.SOLAR_SYSTEM_INIT_POSITIONS);
        setVelocities(MissionData.SOLAR_SYSTEM_INIT_VELOCITIES);
    }
}