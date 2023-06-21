public class EulerSolver {

    public static double[][] eulersMethodForBody(int i){
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[] prevPos = SolarSystem.getPositions()[i];
        double[] prevVel = SolarSystem.getVelocities()[i];
        double[] prevAcc = SolarSystem.calculateAccelerationOfBody(i);

        //final formula:
        //{new_pos, new_vel} = {prev_pos, prev_vel} + step * {prev_vel, prev_acc} ..where lhs is written into newState in code, {x1, x2} if a vector with entries x1, x2, pos/vel/acc 3d vectors
        createNewState(newState, newPos, newVel, prevPos, prevVel, prevAcc, MissionData.TIME_STEP_SIZE);

        return newState;
    }

    public static double[][] eulersMethodForProbe(){
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[] prevPos = Probe.getPosition();
        double[] prevVel = Probe.getVelocity();
        double[] prevAcc = Probe.calculateAccelerationOfProbe();

        //final formula:
        //{new_pos, new_vel} = {prev_pos, prev_vel} + step * {prev_vel, prev_acc} ..where lhs is written into newState in code, {x1, x2} if a vector with entries x1, x2, pos/vel/acc 3d vectors
        createNewState(newState, newPos, newVel, prevPos, prevVel, prevAcc, MissionData.TIME_STEP_SIZE);

        return newState;
    }

    private static void createNewState(double[][] newState, double[] newPos, double[] newVel, double[] prevPos, double[] prevVel, double[] prevAcc, long timeStepSize) {
        for(int entry = 0; entry < 3; entry++){
            newPos[entry] = prevPos[entry] + timeStepSize * prevVel[entry];
            newVel[entry] = prevVel[entry] + timeStepSize * prevAcc[entry];
        }
        newState[0] = newPos;
        newState[1] = newVel;
    }

    public static double[][] eulersMethodForLandingModule(){
        double[][] newState = new double[2][3];
        double[] newPos = new double[3];
        double[] newVel = new double[3];
        double[] prevPos = LandingModule.getPosition();
        double[] prevVel = LandingModule.getVelocity();
        double[] prevAcc = LandingModule.calculateAccelerationOfModule();

        createNewState(newState, newPos, newVel, prevPos, prevVel, prevAcc, MissionData.LANDING_STEP_SIZE);

        return newState;
    }
}
