//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class OpenController {

    //moving the rocket while landing, depending on u, v, θ, where:
    //x is the horizontal position, y the vertical position,
    //θ the angle of rotation,
    //u the acceleration provided by the main thruster,
    //v the total torque provided by the side thrusters;
    //aX is x'', aY is y'', aTheta is θ''
    public static final double GRAVITY_FROM_TITAN = 0.001352; //km s^-2, acceleration due to the gravity of titan, g
    private static final double X_POSITION_TOLERANCE = 0.0001; //km
    private static final double ANGLE_TOLERANCE = 0.02; //no unit
    private static final double X_VELOCITY_TOLERANCE = 0.0001; //km s^-1
    private static final double Y_VELOCITY_TOLERANCE = 0.0001; //km s^-1
    private static final double ANGLE_VELOCITY_TOLERANCE = 0.0001; //rad s^-1

    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Double> currentState = new ArrayList<>();
    public static ArrayList<Double> stateDerivative = new ArrayList<>();
    public static double y_values=0, theta_values=0, theta_velocity=0; //Values which we specify

    public static double accelerationFromMainThruster = 10; // u in the manual, we specify this
    public static double accelerationFromSideThruster = 10;// v in the manual, we specify this

    public static double duration = 100;// we specify this

    public static double stepSize = 10;// we specify this

    public static ArrayList<ArrayList<Double>> stateLog = new ArrayList<ArrayList<Double>>();

    public static ArrayList<double[]> ListOfU = new ArrayList<double[]>();
    public static ArrayList<Double> ListOfV = new ArrayList<Double>();


    //moving the rocket while landing, depending on u, v, θ, where:
    //x is the horizontal position, y the vertical position,
    //θ the angle of rotation,
    //u the acceleration provided by the main thruster,
    //v the total torque provided by the side thrusters;
    //aX is x'', aY is y'', aTheta is θ''
    public OpenController() {
        initializeState();
    }

    public static void main(String[] args) { //Just for testing

        initializeState();
        for(int i = 0; i < currentState.size(); i++)
            System.out.print(currentState.get(i) + " ");
        System.out.println("\n" + stateLog.size());

        updateState(0, 10);

        for(int i = 0; i < currentState.size(); i++)
            System.out.print(currentState.get(i) + " ");
        System.out.println("\n" + stateLog.size());

    }

    public static void initializeState(){ //call this from the constructor
        //Add values to the array
        currentState.add(takeVariables_ofX());
        currentState.add(takeVariables_ofY());
        currentState.add(takeVariables_ofVX());
        currentState.add(y_values);
        currentState.add(theta_values);
        currentState.add(theta_velocity);
        stateLog.add(currentState);

        for (int i = 1; i < 6; i++) {
            stateDerivative.add(0.0);
        }

        double[] initialU = new double[] {0.0, 0.0};
        ListOfU.add(initialU);
        ListOfV.add(0.0);
    }

    public static double takeVariables_ofX(){
        System.out.print("Put the value of x: ");
        double stored_x = scanner.nextDouble();
        return stored_x;
    }

    public static double takeVariables_ofY(){
        System.out.print("Put the value of y: ");
        double stored_y = scanner.nextDouble();
        return stored_y;
    }

    public static double takeVariables_ofVX(){
        System.out.print("Put the value of vx: ");
        double stored_vx = scanner.nextDouble();
        return stored_vx;
    }

    public static boolean checkFinalState() {
        ArrayList<Double> finalState = currentState;
        if (Math.abs(finalState.get(0)) > X_POSITION_TOLERANCE) return false; //check if x_position is within the tolerance
        if (Math.abs(finalState.get(1)) > X_VELOCITY_TOLERANCE) return false; //check if x_position is within the tolerance
        if (finalState.get(2) != 0) return false; //check if y_position = 0
        if (finalState.get(3) != Y_VELOCITY_TOLERANCE) return false; //check if y_position = 0
        if (Math.abs(finalState.get(4) % (2 * Math.PI)) > ANGLE_TOLERANCE) return false; //check if the angle is within the tolerance
        if (Math.abs(finalState.get(5) % (2 * Math.PI)) > ANGLE_VELOCITY_TOLERANCE) return false; //check if the angle is within the tolerance
        return true;
    }

    public static void updateState(double t, double stepSize) {

        var RungeKutta = new RungeKutta4Solver();
        ArrayList<Double> newState = RungeKutta.rk4MethodForLandingModule(t, stepSize);

//        stateLog.add(newState);
//        currentState = newState;
    }

    public static void simulation(double duration, double stepSize) {
        for (int time = 0; time <= duration; time += stepSize) {


        }
    }


}
