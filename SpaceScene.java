import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class SpaceScene extends Scene {

    public SpaceScene(Stage window) {
        super(initialize(window));
    }
    private static Parent initialize(Stage window){

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color:BLACK");

        createDisplayCelestialBodies();

        pane.getChildren().addAll(DisplayCelestialBody.displayCelestialBodyArrayList);

        DisplayProbe probe = new DisplayProbe();
        pane.getChildren().add(probe);

        long startTime = System.currentTimeMillis();

        System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
        System.out.println("probe velocity: " + Arrays.toString(Probe.getVelocity()));

        System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
        System.out.println("probe velocity*: " + Arrays.toString(Probe.getVelocity()));

        AtomicBoolean computed = new AtomicBoolean(false);

        AtomicInteger stepsOnOrbit = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);             //to reset
        AtomicLong currentTime = new AtomicLong(0);
        AtomicReference<LocalDateTime> currentDate = new AtomicReference<>(MissionData.LAUNCH_DATE);
        AtomicReference<Double> fuelToTitan = new AtomicReference<>(0.0);
        AtomicReference<Double> fuelToOrbit = new AtomicReference<>(0.0);
        AtomicReference<Double> fuelToEarth = new AtomicReference<>(0.0);
        AtomicReference<Object> vectorToTitan = new AtomicReference<>();
        AtomicReference<Object> vectorToOrbit = new AtomicReference<>();
        AtomicReference<Object> vectorToEarth = new AtomicReference<>();

        updateLogs();

        Label dateLabel = new Label("current date:\t"+currentDate.get());
        dateLabel.setTextFill(Paint.valueOf("ffffff"));
        if(!MissionData.testingSolvers.get()){
            precompute(computed, stepsOnOrbit, count, fuelToTitan, fuelToOrbit, fuelToEarth, vectorToTitan, vectorToOrbit, vectorToEarth);
            Probe.landingOnTitan.set(false);
            createLabels(pane, fuelToTitan, fuelToOrbit, fuelToEarth, dateLabel);
        }

        AtomicInteger n = new AtomicInteger(0);

        Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(0.5), x -> { //0.1
            if(!Probe.landed.get() && !Probe.landingOnTitan.get()){

                if(MissionData.testingSolvers.get()){
                    Simulations.simulationWithProbeOnce();
                    updateLogs();
                }else {
                    System.out.println();
                    System.out.println("current time: "+currentTime.get());
                    currentDate.set((LocalDateTime)currentDate.get().plusSeconds(MissionData.TIME_STEP_SIZE));
                    dateLabel.setText("current date:\t"+currentDate);
                    setStates(n);

                    n.set(n.get() + 1);
                }

                probe.setLocationOfProbe();
                DisplayCelestialBody.setLocationOfAllCelestialBodies();

                if(currentTime.get() % 10000 == 0)
                    pane.getChildren().add(new Circle(probe.getCenterX(), probe.getCenterY(), 0.1, Paint.valueOf("ffffff")));

                if(!MissionData.testingSolvers.get())
                    checkMissionStages(stepsOnOrbit, count, fuelToTitan, fuelToOrbit, fuelToEarth, vectorToTitan, vectorToOrbit, vectorToEarth, computed);

                currentTime.set(currentTime.get() + MissionData.TIME_STEP_SIZE);
            }
        }));
        animationTimeline.setCycleCount((int) MissionData.N_OF_STEPS * 2 + 1);
        animationTimeline.setDelay(Duration.seconds(3));
        animationTimeline.setOnFinished(e -> {
            printWhenAnimationDone(startTime, stepsOnOrbit, fuelToTitan, fuelToOrbit, fuelToEarth, vectorToEarth);
            if(Probe.landingOnTitan.get())
                window.setScene(new LandingScene(window)); //calling the landing scene
        });

        animationTimeline.play();

        return pane;
    }



    // methods
    private static void createLabels(Pane pane, AtomicReference<Double> fuelToTitan, AtomicReference<Double> fuelToOrbit, AtomicReference<Double> fuelToEarth, Label dateLabel) {
        Label fuel1 = new Label("\tfuel units used to Titan: \t\t"+ fuelToTitan);
        fuel1.setTextFill(Paint.valueOf("ffffff"));
        Label fuel2 = new Label("\tfuel units used to orbit: \t\t"+ fuelToOrbit);
        fuel2.setTextFill(Paint.valueOf("ffffff"));
        Label fuel3 = new Label("\tfuel units used to Earth: \t"+ fuelToEarth);
        fuel3.setTextFill(Paint.valueOf("ffffff"));
        Label fueltotal = new Label("\ttotal fuel units used: \t\t"+ (fuelToTitan.get() + fuelToEarth.get() + fuelToOrbit.get()));
        fueltotal.setTextFill(Paint.valueOf("ffffff"));
        Label separator = new Label("\tspace"+ (fuelToTitan.get() + fuelToEarth.get() + fuelToOrbit.get()));
        separator.setTextFill(Paint.valueOf("000000"));
        VBox labels = new VBox(dateLabel, separator, fuel1, fuel2, fuel3, fueltotal);
        pane.getChildren().add(labels);
    }

    private static void createDisplayCelestialBodies() {
        DisplayCelestialBody sun = new DisplayCelestialBody(MissionData.idMap.get("sun"), null, 10, "ffb813");
        DisplayCelestialBody mercury = new DisplayCelestialBody(MissionData.idMap.get("mercury"), sun, 2, "5c5c5c");
        DisplayCelestialBody venus = new DisplayCelestialBody(MissionData.idMap.get("venus"), sun, 2, "fad5a5");
        DisplayCelestialBody earth = new DisplayCelestialBody(MissionData.idMap.get("earth"), sun, 4, "2f6a69");
        DisplayCelestialBody moon = new DisplayCelestialBody(MissionData.idMap.get("moon"), earth, 1, "ffffff");
        DisplayCelestialBody mars = new DisplayCelestialBody(MissionData.idMap.get("mars"), sun, 4, "993d00");
        DisplayCelestialBody jupiter = new DisplayCelestialBody(MissionData.idMap.get("jupiter"), sun, 7, "db7f53");
        DisplayCelestialBody saturn = new DisplayCelestialBody(MissionData.idMap.get("saturn"), sun, 6, "eedc82");
        DisplayCelestialBody titan = new DisplayCelestialBody(MissionData.idMap.get("titan"), saturn, 2, "0000ff");
        DisplayCelestialBody neptune = new DisplayCelestialBody(MissionData.idMap.get("neptune"), sun, 40, "00ffff");
        DisplayCelestialBody uranus = new DisplayCelestialBody(MissionData.idMap.get("uranus"), sun, 40, "0088ff");
    }

    private static void printWhenAnimationDone(long startTime, AtomicInteger stepsOnOrbit, AtomicReference<Double> fuelToTitan, AtomicReference<Double> fuelToOrbit, AtomicReference<Double> fuelToEarth, AtomicReference<Object> vectorToEarth) {
        System.out.println("animation done");

        System.out.println("probe position: " + Arrays.toString(Probe.getPosition()));
        System.out.println("titan position: " + Arrays.toString((SolarSystem.getPositions()[MissionData.idMap.get("titan")])));
        System.out.println("earth pos: " + Arrays.toString(SolarSystem.getPositions()[3]));

        if(!MissionData.testingSolvers.get()) {
            System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
            System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));

            System.out.println("hc2 vector: " + Arrays.toString((double[]) vectorToEarth.get()));
            System.out.println("steps on orbit: " + stepsOnOrbit.get());
            System.out.println();
            System.out.println("fuel units used to Titan: \t" + fuelToTitan.get());
            System.out.println("fuel units used to orbit: \t" + fuelToOrbit.get());
            System.out.println("fuel units used to Earth: \t" + fuelToEarth.get());
            System.out.println("total fuel units used: \t" + (fuelToTitan.get() + fuelToOrbit.get() + fuelToEarth.get()));
        }

        System.out.println("TOTAL RUNTIME, SEC: "  + (System.currentTimeMillis() - startTime)/1000);
    }

    private static void updateLogs() {
        SolarSystem.solarSystemPositionsLog.add(SolarSystem.getPositions());
        SolarSystem.solarSystemVelocitiesLog.add(SolarSystem.getVelocities());
        Probe.probePositionsLog.add(Probe.getPosition());
        Probe.probeVelocitiesLog.add(Probe.getVelocity());
    }

    private static void setStates(AtomicInteger n) {
        SolarSystem.setPositions(SolarSystem.solarSystemPositionsLog.get(n.get()));
        SolarSystem.setVelocities(SolarSystem.solarSystemVelocitiesLog.get(n.get()));
        Probe.setPosition(Probe.probePositionsLog.get(n.get()));
        Probe.setVelocity(Probe.probeVelocitiesLog.get(n.get()));
    }

    private static void precompute(AtomicBoolean computed, AtomicInteger stepsOnOrbit, AtomicInteger count, AtomicReference<Double> fuelToTitan, AtomicReference<Double> fuelToOrbit, AtomicReference<Double> fuelToEarth, AtomicReference<Object> vectorToTitan, AtomicReference<Object> vectorToOrbit, AtomicReference<Object> vectorToEarth) {
        System.out.println("***** computing *****");

//        //CALL GRADIENT DESCENT (not working)
//        double[] initialVelocity = GradientDescent.calculateVelocity(MissionData.PROBE_INIT_POSITION, MissionData.PROBE_INIT_VELOCITY, MissionData.SOLAR_SYSTEM_INIT_POSITIONS, MissionData.SOLAR_SYSTEM_INIT_VELOCITIES, "titan", 1);
//        ////END

        // CALL HILL CLIMBING 1
        //TODO: uncomment hc call ?
        //double[] initialVelocity = (double[])HillClimbing.computeNewVector(MissionData.SOLAR_SYSTEM_INIT_POSITIONS, MissionData.SOLAR_SYSTEM_INIT_VELOCITIES, MissionData.PROBE_INIT_POSITION, MissionData.PROBE_INIT_VELOCITY,java.time.Duration.between(MissionData.LAUNCH_DATE, MissionData.FINISH_DATE).getSeconds() / MissionData.TIME_STEP_SIZE, "going to titan orbit", "titan").get(0);
        double[] initialVelocity = new double[]{43.989704204263134, -44.0001741313245, -3.079439045462185};
        Probe.setVelocity(initialVelocity);
        vectorToTitan.set(initialVelocity);

        //for Euler
        //43.989704204263134, -44.0001741313245, -3.079439045462185                                     // 5000, actually computed with hc
        //Probe.setVelocity(new double[]{45.8092083845289, -43.45808842840217, -3.2066194551812415});   //  500, actually computed with hc
        //Probe.setVelocity(new double[]{43.20365254355913, -43.27244966306386, -3.1867000696038748});  // 1000, actually computed with hc
        //Probe.setVelocity(new double[]{42.78390972668268, -43.42975489664626, -3.281160892180449});   // 2000, actually computed with hc
        //Probe.setVelocity(new double[]{45.53097522926452, -44.080964736013904, -3.1398957553310787}); // 1500, actually computed with hc
        //Probe.setVelocity(new double[]{48.111258532110234, -44.3761190560857, -3.1546607581783});     // 750, actually computed with hc

        // COUNT FUEL 1
        double fuelTakeoff = Probe.calculateFuelConsumed(MissionData.PROBE_INIT_VELOCITY, Probe.getVelocity());
        fuelToTitan.set(fuelTakeoff);
        System.out.println("fuel for take-off: \t" + fuelToTitan.get());
        System.out.println("within allowed quantity: \t" + (fuelTakeoff/MissionData.TIME_STEP_SIZE < MissionData.PROBE_MAX_FORCE_MAG));

        for(int k = 0; k < MissionData.N_OF_STEPS * 2; k++){
            if(!Probe.landed.get()){
                Simulations.simulationWithProbeOnce();
                checkMissionStages(stepsOnOrbit, count, fuelToTitan, fuelToOrbit, fuelToEarth, vectorToTitan, vectorToOrbit, vectorToEarth, computed);
                updateLogs();
            }
            if(Probe.landingOnTitan.get()){
                break;
            }

        }

        computed.set(true);

        Probe.reachedTitan.set(false);  //reset
        count.set(0);                   //reset
        Probe.goingBack.set(false);     //reset
        Probe.landed.set(false);        //reset

        SolarSystem.resetStates();
        Probe.resetState();
        Probe.setVelocity((double[]) vectorToTitan.get());
    }

    private static void checkMissionStages(AtomicInteger stepsOnOrbit, AtomicInteger count, AtomicReference<Double> fuelToTitan, AtomicReference<Double> fuelToOrbit, AtomicReference<Double> fuelToEarth, AtomicReference<Object> vectorToTitan, AtomicReference<Object> vectorToOrbit, AtomicReference vectorToEarth, AtomicBoolean computed) {
        if(!Probe.reachedTitan.get())
            onFlyingToTitan();
        else if (count.get() == stepsOnOrbit.get()){
            if (!Probe.goingBack.get()){
                if(MissionData.MISSION.equals("phase3")){
                    // onStartingTitanLanding();//why is it called twice?
                    onStartingTitanLandingWithUserInput();
                    return;
                }else{
                    onFinishingOrbiting(Probe.goingBack, fuelToEarth, vectorToEarth, computed);
                }
            }
            if(Probe.calculateDistanceToTarget("earth") <= MissionData.EARTH_RADIUS && !Probe.landed.get()) {
                onLandingOnEarth(Probe.landed, vectorToTitan, vectorToOrbit, vectorToEarth);
            }else if (!Probe.landed.get()) {
                onFlyingBack();
            }
        }else if (Probe.calculateDistanceToTarget("titan") < MissionData.TITAN_RADIUS + 300) {
            onOrbiting(count);
        }else{
            System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
            System.out.println("probe velocity*******: " + Arrays.toString(Probe.getVelocity()));
            System.out.println("unexpected behaviour...");
        }

        if(!Probe.reachedTitan.get() && Probe.calculateDistanceToTarget("titan") < MissionData.TITAN_RADIUS + 300)
            onEnteringTitanOrbit(Probe.reachedTitan, stepsOnOrbit, fuelToOrbit, vectorToOrbit, computed);
    }

    private static void onFlyingBack() {
        System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));
        System.out.println("probe velocity-------: " + Arrays.toString(Probe.getVelocity()));
        System.out.println("going back ");
    }

    private static void onStartingTitanLanding() {
        Probe.landingOnTitan.set(true);

        LandingModule.setPosition(new double[]{Probe.getPosition()[0], Probe.getPosition()[1], 0});
//        LandingModule.setVelocity(new double[]{Probe.getVelocity()[0], Probe.getVelocity()[1], 0});
        LandingModule.setVelocity(new double[]{Probe.getVelocity()[0] - SolarSystem.getVelocities()[MissionData.idMap.get("titan")][0], Probe.getVelocity()[1] - SolarSystem.getVelocities()[MissionData.idMap.get("titan")][1], 0});

        System.out.println("!!!!!!!!These values are the example values for the user input!!!!!!!!");
        double[] just4me = new double[]{Probe.getPosition()[0], Probe.getPosition()[1], 0};
        System.out.println(Arrays.toString(just4me));
        double[] just44me = new double[]{Probe.getVelocity()[0] - SolarSystem.getVelocities()[MissionData.idMap.get("titan")][0], Probe.getVelocity()[1] - SolarSystem.getVelocities()[MissionData.idMap.get("titan")][1], 0};
        System.out.println(Arrays.toString(just44me));
        /*
[1.3659730286288338E9, -4.980193752742708E8, 0.0]
[0.010132918664215396, 0.019170080934351574, 0.0]

[1.3659730286288338E9, -4.980193752742708E8, 0.0]
[0.010132918664215396, 0.019170080934351574, 0.0]
         */


        LandingModule.setU(-0.01);
        LandingModule.setV(0);

        System.out.println("trying to land");
    }

    private static void onStartingTitanLandingWithUserInput() {
        Probe.landingOnTitan.set(true);

        double[] landingModulePosition = new double[3];
        landingModulePosition[0] = OpenController.takeVariableOfX();
        landingModulePosition[1] = OpenController.takeVariableOfY();
        landingModulePosition[2] = 0; //customizable
        double[] landingModuleVelocity = new double[3];
        landingModuleVelocity[0] = OpenController.takeVariableOfXVelocity();
        landingModuleVelocity[1] = 0; //customizable
        landingModuleVelocity[2] = 0; //customizable

        LandingModule.setPosition(landingModulePosition);
        LandingModule.setVelocity(landingModuleVelocity);

        LandingModule.setU(-0.01);
        LandingModule.setV(0);

        System.out.println("trying to land");
    }

    private static void onOrbiting(AtomicInteger count) {
        System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
        System.out.println("probe velocity$$$$$$$: " + Arrays.toString(Probe.getVelocity()));
        System.out.println("probe position$$$$$$$: " + Arrays.toString(Probe.getPosition()));
        System.out.println("ORBITING...");
        count.set(count.get() + 1);
    }

    private static void onLandingOnEarth(AtomicBoolean landed, AtomicReference<Object> vectorToTitan, AtomicReference<Object> vectorToOrbit, AtomicReference vectorToEarth) {
        System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));
        System.out.println("!!! LANDED !!! IT'S CRAZY, IT'S PARTY");
        Probe.setVelocity(SolarSystem.getVelocities()[MissionData.idMap.get("earth")]);
        System.out.println("probe velocity-------: " + Arrays.toString(Probe.getVelocity()));
        System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));
        System.out.println("v1 \t" + Arrays.toString((double[]) vectorToTitan.get()));
        System.out.println("v2 \t" + Arrays.toString((double[]) vectorToOrbit.get()));
        System.out.println("v3 \t" + Arrays.toString((double[]) vectorToEarth.get()));
        landed.set(true);
    }

    private static void onFinishingOrbiting(AtomicBoolean goingBack, AtomicReference<Double> fuelToEarth, AtomicReference vectorToEarth, AtomicBoolean computed) {
        double[] oldVelocity = Probe.getVelocity(); // for fuel

        double[] unitVector = computeUnitVector();
        System.out.println(Arrays.toString(unitVector)+" unit vector");
        Probe.setVelocity(new double[]{unitVector[0] * MissionData.PROBE_MAX_VELOCITY_MAG/2, unitVector[1] * MissionData.PROBE_MAX_VELOCITY_MAG/2, unitVector[2] * MissionData.PROBE_MAX_VELOCITY_MAG/2});
        System.out.println(Arrays.toString(Probe.getVelocity())+" times 30");
        if(!computed.get()){
            System.out.println("***** computing *****");

            // CALL HILL CLIMBING 3
            ArrayList<Object> vectorAndCount = HillClimbing.computeNewVector(Arrays.stream(SolarSystem.getPositions()).map(double[]::clone).toArray(s -> SolarSystem.getPositions().clone()), Arrays.stream(SolarSystem.getVelocities()).map(double[]::clone).toArray(s -> SolarSystem.getVelocities().clone()), Arrays.copyOf(Probe.getPosition(), 3), Arrays.copyOf(Probe.getVelocity(), 3), MissionData.N_OF_STEPS, "going back to earth", "earth");
            Probe.setVelocity((double[]) vectorAndCount.get(0));
            vectorToEarth.set(vectorAndCount.get(0));

            //CALL GRADIENT DESCENT (not working)
            //callGradientDescent(vectorToEarth);

            double[] newVelocity = Probe.getVelocity(); //for fuel
            double fuelToTurnBack = Probe.calculateFuelConsumed(oldVelocity, newVelocity);
            System.out.println("fuel to turn back: \t" + fuelToTurnBack);
            System.out.println("within allowed quantity: \t" + (fuelToTurnBack/MissionData.TIME_STEP_SIZE < MissionData.PROBE_MAX_FORCE_MAG));

            // COUNT FUEL 3
            fuelToEarth.set(fuelToTurnBack);
        }

        //Probe.setVelocity(new double[]{-48.49947853159396, 11.374414048655058, 2.9183211784251775});    // precomputed for return with 1000 for Euler
        goingBack.set(true);
    }

    private static void callGradientDescent(AtomicReference vectorToEarth) {
        double[] velocity = GradientDescent.calculateVelocity(Arrays.copyOf(Probe.getPosition(), 3), Arrays.copyOf(Probe.getVelocity(), 3), Arrays.stream(SolarSystem.getPositions()).map(double[]::clone).toArray(s -> SolarSystem.getPositions().clone()), Arrays.stream(SolarSystem.getVelocities()).map(double[]::clone).toArray(s -> SolarSystem.getVelocities().clone()), "earth", 1);
        Probe.setVelocity(velocity);
        vectorToEarth.set(velocity);
    }

    private static double[] computeUnitVector() {
        double[] earthPosition = SolarSystem.getPositions()[MissionData.idMap.get("earth")];
        double[] vectorDirection = new double[]{earthPosition[0] - Probe.getPosition()[0], earthPosition[1] - Probe.getPosition()[1], earthPosition[2] - Probe.getPosition()[2]};
        double norm = Math.sqrt(Math.pow(vectorDirection[0], 2) + Math.pow(vectorDirection[1], 2) + Math.pow(vectorDirection[2], 2));
        double[] unitVector =  new double[]{vectorDirection[0]/norm, vectorDirection[1]/norm, vectorDirection[2]/norm};
        return unitVector;
    }

    private static void  onEnteringTitanOrbit(AtomicBoolean reachedTitan, AtomicInteger stepsOnOrbit, AtomicReference<Double> fuelToOrbit, AtomicReference<Object> vectorToOrbit, AtomicBoolean computed) {
        System.out.println("**************************** TITAN'S VELOCITY MAGNITUDE: " + Math.sqrt(Math.pow(SolarSystem.getVelocities()[MissionData.idMap.get("titan")][0], 2) + Math.pow(SolarSystem.getVelocities()[MissionData.idMap.get("titan")][1], 2) + Math.pow(SolarSystem.getVelocities()[MissionData.idMap.get("titan")][2], 2)));
        System.out.println("**************************** TITAN'S VELOCITY: " + Arrays.toString(SolarSystem.getVelocities()[MissionData.idMap.get("titan")]));
        System.out.println("****************************");
        System.out.println("****************************");

        double periodT = Math.sqrt(4*Math.pow(Math.PI, 2)*Math.pow(Probe.calculateDistanceToTarget("titan"), 3) / MissionData.GRAVITY_CONSTANT / MissionData.MASSES[MissionData.idMap.get("titan")]);
        System.out.println("period T " + periodT);
        if(!computed.get()){
            System.out.println("***** computing *****");

            // CALL HILL CLIMBING 2
            ArrayList<Object> vectorAndCount = HillClimbing.computeNewVector(Arrays.stream(SolarSystem.getPositions()).map(double[]::clone).toArray(s -> SolarSystem.getPositions().clone()), Arrays.stream(SolarSystem.getVelocities()).map(double[]::clone).toArray(s -> SolarSystem.getVelocities().clone()), Arrays.copyOf(Probe.getPosition(), 3), Arrays.copyOf(Probe.getVelocity(), 3), (long)periodT / MissionData.TIME_STEP_SIZE + 1, "staying on titan orbit", "titan");
            double[] v = (double[]) vectorAndCount.get(0);
            stepsOnOrbit.set((Integer) vectorAndCount.get(1));
            ////END

//                    double[] v = new double[]{4.216684733284191, 12.773035209626444, -2.320559764305828}; //precomputed for orbiting with 1000
//                    stepsOnOrbit.set(9); //precomputed for orbiting with 1000

            System.out.println(Arrays.toString(v) + "     v");
            System.out.println("step h: " + MissionData.TIME_STEP_SIZE);
            System.out.println("steps on orbit: " + stepsOnOrbit.get());

            double[] oldVelocity = Probe.getVelocity();
            double[] newVelocity = v;
            // COUNT FUEL 2
            double fuelForOrbit = Probe.calculateFuelConsumed(oldVelocity, newVelocity);
            fuelToOrbit.set(fuelForOrbit);
            System.out.println("fuel for orbit: \t" + fuelForOrbit);
            System.out.println("within allowed quantity: \t" + (fuelForOrbit/MissionData.TIME_STEP_SIZE < MissionData.PROBE_MAX_FORCE_MAG));
            Probe.setVelocity(v);
            vectorToOrbit.set(v);
        }

//        //Andrej stuff
//        double[] titanVelocity = SolarSystem.getVelocities()[MissionData.idMap.get("titan")];
//        Probe.setVelocity(new double[]{titanVelocity[0]*1.0112, titanVelocity[1]*1.0112, titanVelocity[2]*1.0112});
//        stepsOnOrbit.set(1000000000);
//        //end

        reachedTitan.set(true);
    }

    private static void onFlyingToTitan() {
        System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
        System.out.println("probe velocity**: " + Arrays.toString(Probe.getVelocity()));
    }

}
