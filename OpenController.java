//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

//Trying to make it work with onStartingTitanLandingWithUserInput with ListOfU(Only -0.01) -> done
//Now trying to make it work with ListOfU(calculated)

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class OpenController {

    //moving the rocket while landing, depending on u, v, θ, where:
    //x is the horizontal position, y the vertical position,
    //θ the angle of rotation,
    //u the acceleration provided by the main thruster,
    //v the total torque provided by the side thrusters;
    //aX is x'', aY is y'', aTheta is θ''

    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Double> currentState = new ArrayList<>();
    public static ArrayList<Double> stateDerivative = new ArrayList<>();
    public static double y = 0, theta = 0, theta_velocity = 0; //Values which we specify

    public static ArrayList<Double> candidates = new ArrayList<Double>();
    public static ArrayList<Double> fitness = new ArrayList<Double>();

    public static ArrayList<double[]> simulationVelocityLog = new ArrayList<double[]>();
    public static ArrayList<double[]> simulationPositionLog = new ArrayList<double[]>();

    public static double duration = 100;// we specify this

    public static double stepSize = 10;// we specify this

    public static ArrayList<ArrayList<Double>> stateLog = new ArrayList<ArrayList<Double>>();

    public static ArrayList<Double> listOfU = new ArrayList<Double>();
    public static ArrayList<Double> listOfV = new ArrayList<Double>();


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

        //initializeState();

        System.out.println("Initializeした！この後1 1が続けばいい I initialized OpenLoop Controller! Hope there will be 1 1 printed out after this");
        System.out.println(simulationVelocityLog.size());
        System.out.println(simulationPositionLog.size());


//        System.out.println("listOfUおわたよ！ Finished listOfU!");
//        System.out.println(listOfU.toString());
//        System.out.println("listOfVおわたよ！ Finished listOfV!");
//        System.out.println(listOfV.toString());

//        for(int i = 0; i < currentState.size(); i++)
//            System.out.print(currentState.get(i) + " ");
//        System.out.println("\n" + stateLog.size());



    }

    public static void initializeState(){ //call this from the constructor
        //Add values to the array
//        currentState.add(takeVariables_ofX());
//        currentState.add(takeVariables_ofY());
//        currentState.add(takeVariables_ofVX());
//        currentState.add(y);
//        currentState.add(theta);
//        currentState.add(theta_velocity);
//        stateLog.add(currentState);
//
//        for (int i = 1; i < 6; i++) {
//            stateDerivative.add(0.0);
//        }

/*後で解放してやっからな I will let you guys awake soon
//        double[] initialPosition = new double[2];
//        initialPosition[0] = LandingModule.getPosition()[0]; //TODO: WHY ARE YOU NULL
//        initialPosition[1] = LandingModule.getPosition()[1];
//
//        double[] initialVelocity = new double[2];
//        initialVelocity[0] = LandingModule.getVelocity()[0];
//        initialVelocity[1] = LandingModule.getVelocity()[1];
//
//        simulationPositionLog.add(initialPosition);
//        simulationVelocityLog.add(initialVelocity);
*/
        setListOfU(100);
        setListOfV(100);
    }

    public static double takeVariableOfX(){
        System.out.print("Put the value of position x: ");
        System.out.println("example 1.3659730286288338E9");

        double stored_x = scanner.nextDouble();
        return stored_x;
    }

    public static double takeVariableOfY(){
        System.out.print("Put the value of position y: ");
        System.out.println("example -4.980193752742708E8");

        double stored_y = scanner.nextDouble();
        return stored_y;
    }

    public static double takeVariableOfXVelocity(){
        System.out.print("Put the value of velocity of x: ");
        System.out.println("example 0.010132918664215396");

        double stored_vx = scanner.nextDouble();
        return stored_vx;
    }

    public static boolean checkFinalState() {
        ArrayList<Double> finalState = currentState;
        if (Math.abs(finalState.get(0)) > MissionData.DELTA_X_LANDING) return false; //check if x_position is within the tolerance
        if (Math.abs(finalState.get(1)) > MissionData.EPSILON_X_LANDING) return false; //check if x_position is within the tolerance
        if (finalState.get(2) != 0) return false; //check if y_position = 0
        if (finalState.get(3) != MissionData.EPSILON_Y_LANDING) return false; //check if y_position = 0
        if (Math.abs(finalState.get(4) % (2 * Math.PI)) > MissionData.DELTA_THETA_LANDING) return false; //check if the angle is within the tolerance
        if (Math.abs(finalState.get(5) % (2 * Math.PI)) > MissionData.EPSILON_THETA_LANDING) return false; //check if the angle is within the tolerance
        return true;
    }
/*



 */

    public static void simulation(double duration, double stepSize) {
        for (int time = 0; time <= duration; time += stepSize) {
        }
    }

    public static double getU(int entry) {
        return listOfU.get(entry + 1);
    }

    public static void setListOfU(int numOfEntry) {
        for (int i = 0; i < numOfEntry; i++) {
            listOfU.add(-0.01);
        }
        /*
        for (int i = 0; i < numOfEntry; i++) {
            double optimumValue = decideOptimumValue(0.02, 10);
            listOfU.add(optimumValue);

            double[] simNewPos = new double[3];

            simNewPos[0] = simulationPositionLog.get(simulationVelocityLog.size() - 1)[0] + MissionData.LANDING_STEP_SIZE * simulationVelocityLog.get(simulationVelocityLog.size() - 1)[0]; //一抹の不安
            simNewPos[1] = simulationPositionLog.get(simulationVelocityLog.size() - 1)[1] + MissionData.LANDING_STEP_SIZE * simulationVelocityLog.get(simulationVelocityLog.size() - 1)[0];
            simNewPos[2] = 0;
            simulationPositionLog.add(simNewPos);

            double[] simNewVel = new double[3];
            simNewVel[0] = simulationVelocityLog.get(simulationVelocityLog.size() - 1)[0] + MissionData.LANDING_STEP_SIZE * optimumValue * Math.sin(simulationPositionLog.get(simulationPositionLog.size() - 1)[2]);
            simNewVel[1] = simulationVelocityLog.get(simulationVelocityLog.size() - 1)[1] + MissionData.LANDING_STEP_SIZE * optimumValue * Math.cos(simulationPositionLog.get(simulationPositionLog.size() - 1)[2]) - MissionData.GRAVITY_TITAN;
            simNewVel[2] = 0;
            simulationVelocityLog.add(simNewVel);
        }

         */
    }

    public static double getV(int entry) {
        return listOfV.get(entry + 1);
    }

    public static void setListOfV(int numOfEntry) {
        for (int i = 0; i < numOfEntry; i++) {
            listOfV.add(0.0);
        }
    }

    public static double generateRandomValue(double absoluteValue) {
        return (-absoluteValue) + (double)(Math.random() * ((absoluteValue - (-absoluteValue)) + 1));
    }

    public static double decideOptimumValue(double max, int numOfCandidates) {

        for (int i = 0; i < numOfCandidates; i++) {
            candidates.add(generateRandomValue(max));
        }
        for (int i = 0; i < numOfCandidates; i++) {
            fitness.add(calculateFitnessUsingEuler(candidates.get(i)));
        }

        int positionOfTheFittestCandidate = fitness.indexOf(Collections.min(fitness));

        return candidates.get(positionOfTheFittestCandidate);
    }


    public static double calculateFitnessUsingEuler(double tmpU) {
        double tmpNewVelX = simulationVelocityLog.get(simulationVelocityLog.size() - 1)[0] + MissionData.LANDING_STEP_SIZE * tmpU * Math.sin(simulationPositionLog.get(simulationPositionLog.size() - 1)[2]);
        double tmpNewVelY = simulationVelocityLog.get(simulationVelocityLog.size() - 1)[1] + MissionData.LANDING_STEP_SIZE * tmpU * Math.cos(simulationPositionLog.get(simulationPositionLog.size() - 1)[2]) - MissionData.GRAVITY_TITAN;
        return tmpNewVelX + tmpNewVelY;
    }
}

/*
Strategy

Ignore setOfV for now
I need a method to return setOfU

number of elements in setOfU = duration / stepsize
How do I calculate the value of each element？

max value of u is not provided, let's go with 0.02 in the meantime
Generate a random value between -maxValueOfU and maxValueOfU
Repeat this 10 times (I can change this) to produce candidate

Simulate each candidate, take the one that leads to velocity[0] and velocity[1] near 0

Repeat this ListOfU.length() times


ListOfU will be the result of simulation
Using this ListOfU, implement GUI
 */

