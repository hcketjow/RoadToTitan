import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        //order: sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus
        //for the good-looking solar system
        /*
        DisplayCelestialBody sun = new DisplayCelestialBody(MissionData.idMap.get("sun"), null, 40, "ffb813");
        DisplayCelestialBody mercury = new DisplayCelestialBody(MissionData.idMap.get("mercury"), sun, 8, "5c5c5c");
        DisplayCelestialBody venus = new DisplayCelestialBody(MissionData.idMap.get("venus"), sun, 12, "fad5a5");
        DisplayCelestialBody earth = new DisplayCelestialBody(MissionData.idMap.get("earth"), sun, 13, "2f6a69");
        DisplayCelestialBody moon = new DisplayCelestialBody(MissionData.idMap.get("moon"), earth, 3, "ffffff");
        DisplayCelestialBody mars = new DisplayCelestialBody(MissionData.idMap.get("mars"), sun, 10, "993d00");
        DisplayCelestialBody jupiter = new DisplayCelestialBody(MissionData.idMap.get("jupiter"), sun, 30, "db7f53");
        DisplayCelestialBody saturn = new DisplayCelestialBody(MissionData.idMap.get("saturn"), sun, 23, "eedc82");
        DisplayCelestialBody titan = new DisplayCelestialBody(MissionData.idMap.get("titan"), saturn, 4, "ffffff");
        */

        DisplayCelestialBody sun = new DisplayCelestialBody(MissionData.idMap.get("sun"), null, 10, "ffb813");
        DisplayCelestialBody mercury = new DisplayCelestialBody(MissionData.idMap.get("mercury"), sun, 2, "5c5c5c");
        DisplayCelestialBody venus = new DisplayCelestialBody(MissionData.idMap.get("venus"), sun, 2, "fad5a5");
        DisplayCelestialBody earth = new DisplayCelestialBody(MissionData.idMap.get("earth"), sun, 4, "2f6a69");
        DisplayCelestialBody moon = new DisplayCelestialBody(MissionData.idMap.get("moon"), earth, 1, "ffffff");
        DisplayCelestialBody mars = new DisplayCelestialBody(MissionData.idMap.get("mars"), sun, 4, "993d00");
        DisplayCelestialBody jupiter = new DisplayCelestialBody(MissionData.idMap.get("jupiter"), sun, 7, "db7f53");
        DisplayCelestialBody saturn = new DisplayCelestialBody(MissionData.idMap.get("saturn"), sun, 6, "eedc82");
        DisplayCelestialBody titan = new DisplayCelestialBody(MissionData.idMap.get("titan"), saturn, 2, "0000ff"); //ffffff
        DisplayCelestialBody neptune = new DisplayCelestialBody(MissionData.idMap.get("neptune"), sun, 40, "00ffff");
        DisplayCelestialBody uranus = new DisplayCelestialBody(MissionData.idMap.get("uranus"), sun, 40, "0088ff"); //ffffff

        pane.getChildren().addAll(DisplayCelestialBody.displayCelestialBodyArrayList);

        DisplayProbe probe = new DisplayProbe();
        pane.getChildren().add(probe);

        long startTime = System.currentTimeMillis();

        System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
        System.out.println("probe velocity: " + Arrays.toString(Probe.getVelocity()));

        System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
        System.out.println("probe velocity*: " + Arrays.toString(Probe.getVelocity()));

        AtomicBoolean showGUI = new AtomicBoolean(true);
        AtomicBoolean testingSolvers = new AtomicBoolean(false); //set to true if "ab3" or "am2" is chosen as SOLVER
        AtomicBoolean computed = new AtomicBoolean(false);

        AtomicBoolean reachedTitan = new AtomicBoolean(false);  //to reset
        AtomicInteger stepsOnOrbit = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);             //to reset
        AtomicBoolean goingBack = new AtomicBoolean(false);     //to reset
        AtomicLong currentTime = new AtomicLong(0);
        AtomicBoolean landed = new AtomicBoolean(false);        //to reset
        AtomicReference<Double> fuelUnitsUsed = new AtomicReference<>(0.0);
        AtomicReference<Object> vectorToTitan = new AtomicReference<>();
        AtomicReference<Object> vectorToOrbit = new AtomicReference<>();
        AtomicReference<Object> vectorToEarth = new AtomicReference<>();

        SolarSystem.solarSystemPositionsLog.add(MissionData.SOLAR_SYSTEM_INIT_POSITIONS);
        SolarSystem.solarSystemVelocitiesLog.add(MissionData.SOLAR_SYSTEM_INIT_VELOCITIES);
        Probe.probePositionsLog.add(MissionData.PROBE_INIT_POSITION);
        Probe.probeVelocitiesLog.add(MissionData.PROBE_INIT_VELOCITY);

        if(!testingSolvers.get()){
            precomputeProbeVelocities(computed, reachedTitan, stepsOnOrbit, count, goingBack, landed, fuelUnitsUsed, vectorToTitan, vectorToOrbit, vectorToEarth);
        }

        AtomicInteger n = new AtomicInteger(0);

        Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(0.1), x -> { //0.1
            if(!landed.get() && showGUI.get()){

                if(testingSolvers.get()){
                    Simulations.simulationWithProbeOnce();
                    SolarSystem.solarSystemPositionsLog.add(SolarSystem.getPositions());
                    SolarSystem.solarSystemVelocitiesLog.add(SolarSystem.getVelocities());
                    Probe.probePositionsLog.add(Probe.getPosition());
                    Probe.probeVelocitiesLog.add(Probe.getVelocity());
                }else {
                    System.out.println();
                    System.out.println("current time: "+currentTime.get());
                    SolarSystem.setPositions(SolarSystem.solarSystemPositionsLog.get(n.get()));
                    SolarSystem.setVelocities(SolarSystem.solarSystemVelocitiesLog.get(n.get()));
                    Probe.setPosition(Probe.probePositionsLog.get(n.get()));
                    Probe.setVelocity(Probe.probeVelocitiesLog.get(n.get()));

                    n.set(n.get() + 1);
                }

                probe.setLocationOfProbe();
                DisplayCelestialBody.setLocationOfAllCelestialBodies();

                if(currentTime.get() % 10000 == 0){
                    pane.getChildren().add(new Circle(probe.getCenterX(), probe.getCenterY(), 0.1, Paint.valueOf("ffffff")));
                }

                if(!testingSolvers.get()){
                    checkStates(reachedTitan, stepsOnOrbit, count, goingBack, landed, fuelUnitsUsed, vectorToTitan, vectorToOrbit, vectorToEarth, computed);
                }

                currentTime.set(currentTime.get() + MissionData.TIME_STEP_SIZE);
                }
        }
        ));
        animationTimeline.setCycleCount((int) MissionData.N_OF_STEPS * 2 + 1);
        animationTimeline.setDelay(Duration.seconds(3));
        animationTimeline.setOnFinished(e -> {
            System.out.println("animation done");

            System.out.println("probe position: " + Arrays.toString(Probe.getPosition()));
            System.out.println("titan position: " + Arrays.toString((SolarSystem.getPositions()[MissionData.idMap.get("titan")])));
            System.out.println("earth pos: " + Arrays.toString(SolarSystem.getPositions()[3]));

            if(!testingSolvers.get()) {
                System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
                System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));

                System.out.println("hc2 vector: " + Arrays.toString((double[]) vectorToEarth.get()));
                System.out.println("steps on orbit: " + stepsOnOrbit.get());
                System.out.println();
                System.out.println("total fuel units used: \t" + fuelUnitsUsed.get());
            }

            System.out.println("TOTAL RUNTIME, SEC: "  + (System.currentTimeMillis() - startTime)/1000);
        });

        animationTimeline.play();

        return pane;
    }

    private static void precomputeProbeVelocities(AtomicBoolean computed, AtomicBoolean reachedTitan, AtomicInteger stepsOnOrbit, AtomicInteger count, AtomicBoolean goingBack, AtomicBoolean landed, AtomicReference<Double> fuelUnitsUsed, AtomicReference<Object> vectorToTitan, AtomicReference<Object> vectorToOrbit, AtomicReference<Object> vectorToEarth) {
        System.out.println("***** computing *****");

//        //CALL GRADIENT DESCENT (not working)
//        double[] initialVelocity = GradientDescent.calculateVelocity(MissionData.PROBE_INIT_POSITION, MissionData.PROBE_INIT_VELOCITY, MissionData.SOLAR_SYSTEM_INIT_POSITIONS, MissionData.SOLAR_SYSTEM_INIT_VELOCITIES, "titan", 1);
//        ////END

        // CALL HILL CLIMBING 1
        double[] initialVelocity = (double[])HillClimbing.computeNewVector(MissionData.SOLAR_SYSTEM_INIT_POSITIONS, MissionData.SOLAR_SYSTEM_INIT_VELOCITIES, MissionData.PROBE_INIT_POSITION, MissionData.PROBE_INIT_VELOCITY,java.time.Duration.between(MissionData.LAUNCH_DATE, MissionData.FINISH_DATE).getSeconds() / MissionData.TIME_STEP_SIZE, "going to titan orbit", "titan").get(0);
        Probe.setVelocity(initialVelocity);
        vectorToTitan.set(initialVelocity);
        ////END

        //for Euler
        //Probe.setVelocity(new double[]{45.8092083845289, -43.45808842840217, -3.2066194551812415});   //  500, actually computed with hc
        //Probe.setVelocity(new double[]{43.20365254355913, -43.27244966306386, -3.1867000696038748});  // 1000, actually computed with hc
        //Probe.setVelocity(new double[]{42.78390972668268, -43.42975489664626, -3.281160892180449});   // 2000, actually computed with hc
        //Probe.setVelocity(new double[]{45.53097522926452, -44.080964736013904, -3.1398957553310787}); // 1500, actually computed with hc
        //Probe.setVelocity(new double[]{48.111258532110234, -44.3761190560857, -3.1546607581783});     // 750, actually computed with hc

        // COUNT FUEL 1
        double fuelTakeoff = Probe.calculateFuelConsumed(MissionData.PROBE_INIT_VELOCITY, Probe.getVelocity());
        fuelUnitsUsed.set((double) fuelTakeoff);
        System.out.println("fuel for take-off: \t" + fuelUnitsUsed.get());
        System.out.println("within allowed quantity: \t" + (fuelTakeoff/MissionData.TIME_STEP_SIZE < MissionData.PROBE_MAX_FORCE_MAG));

        for(int k = 0; k < MissionData.N_OF_STEPS * 2; k++){
            if(!landed.get()){
                Simulations.simulationWithProbeOnce();
                checkStates(reachedTitan, stepsOnOrbit, count, goingBack, landed, fuelUnitsUsed, vectorToTitan, vectorToOrbit, vectorToEarth, computed);

                SolarSystem.solarSystemPositionsLog.add(SolarSystem.getPositions());
                SolarSystem.solarSystemVelocitiesLog.add(SolarSystem.getVelocities());
                Probe.probePositionsLog.add(Probe.getPosition());
                Probe.probeVelocitiesLog.add(Probe.getVelocity());
            }
        }

        computed.set(true);

        reachedTitan.set(false);  //reset
        count.set(0);             //reset
        goingBack.set(false);     //reset
        landed.set(false);        //reset

        SolarSystem.resetStates();
        Probe.resetState();
        Probe.setVelocity((double[]) vectorToTitan.get());
    }

    private static void checkStates(AtomicBoolean reachedTitan, AtomicInteger stepsOnOrbit, AtomicInteger count, AtomicBoolean goingBack, AtomicBoolean landed, AtomicReference<Double> fuelUnitsUsed, AtomicReference<Object> vectorToTitan, AtomicReference<Object> vectorToOrbit, AtomicReference vectorToEarth, AtomicBoolean computed) {
        if(!reachedTitan.get()){
            System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
            System.out.println("probe velocity**: " + Arrays.toString(Probe.getVelocity()));
        }else if (count.get() == stepsOnOrbit.get()){
            if (!goingBack.get()){
                double[] oldVelocity = Probe.getVelocity(); // for fuel

                // ----good stuff?
                double[] earthPosition = SolarSystem.getPositions()[MissionData.idMap.get("earth")];
                double[] vectorDirection = new double[]{earthPosition[0] - Probe.getPosition()[0], earthPosition[1] - Probe.getPosition()[1], earthPosition[2] - Probe.getPosition()[2]};
                double norm = Math.sqrt(Math.pow(vectorDirection[0], 2) + Math.pow(vectorDirection[1], 2) + Math.pow(vectorDirection[2], 2));
                double[] unitVector =  new double[]{vectorDirection[0]/norm, vectorDirection[1]/norm, vectorDirection[2]/norm};
                System.out.println(Arrays.toString(unitVector)+" unit vector");
                Probe.setVelocity(new double[]{unitVector[0] * MissionData.PROBE_MAX_VELOCITY_MAG/2, unitVector[1] * MissionData.PROBE_MAX_VELOCITY_MAG/2, unitVector[2] * MissionData.PROBE_MAX_VELOCITY_MAG/2});
                System.out.println(Arrays.toString(Probe.getVelocity())+" times 30");
                if(!computed.get()){
                    System.out.println("***** computing *****");
                    // CALL HILL CLIMBING 3
                    ArrayList<Object> vectorAndCount = HillClimbing.computeNewVector(Arrays.stream(SolarSystem.getPositions()).map(double[]::clone).toArray(s -> SolarSystem.getPositions().clone()), Arrays.stream(SolarSystem.getVelocities()).map(double[]::clone).toArray(s -> SolarSystem.getVelocities().clone()), Arrays.copyOf(Probe.getPosition(), 3), Arrays.copyOf(Probe.getVelocity(), 3), MissionData.N_OF_STEPS, "going back to earth", "earth");
                    Probe.setVelocity((double[]) vectorAndCount.get(0));
                    vectorToEarth.set(vectorAndCount.get(0));
                    ////END

//                    //CALL GRADIENT DESCENT (not working)
//                    double[] velocity = GradientDescent.calculateVelocity(Arrays.copyOf(Probe.getPosition(), 3), Arrays.copyOf(Probe.getVelocity(), 3), Arrays.stream(SolarSystem.getPositions()).map(double[]::clone).toArray(s -> SolarSystem.getPositions().clone()), Arrays.stream(SolarSystem.getVelocities()).map(double[]::clone).toArray(s -> SolarSystem.getVelocities().clone()), "earth", 1);
//                    Probe.setVelocity(velocity);
//                    vectorToEarth.set(velocity);
//                    ////END

                    double[] newVelocity = Probe.getVelocity(); //for fuel
                    double fuelToTurnBack = Probe.calculateFuelConsumed(oldVelocity, newVelocity);
                    System.out.println("fuel to turn back: \t" + fuelToTurnBack);
                    System.out.println("within allowed quantity: \t" + (fuelToTurnBack/MissionData.TIME_STEP_SIZE < MissionData.PROBE_MAX_FORCE_MAG));

                    // COUNT FUEL 3
                    fuelUnitsUsed.set(fuelUnitsUsed.get() + fuelToTurnBack);
                }

                //Probe.setVelocity(new double[]{-48.49947853159396, 11.374414048655058, 2.9183211784251775});    // precomputed for return with 1000 for Euler
                goingBack.set(true);
            }
            if(Probe.calculateDistanceToTarget("earth") <= MissionData.EARTH_RADIUS && !landed.get()){
                System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));
                System.out.println("!!! LANDED !!! IT'S CRAZY, IT'S PARTY");
                Probe.setVelocity(SolarSystem.getVelocities()[MissionData.idMap.get("earth")]);
                System.out.println("probe velocity-------: " + Arrays.toString(Probe.getVelocity()));
                System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));
                System.out.println("v1 \t" + Arrays.toString((double[]) vectorToTitan.get()));
                System.out.println("v2 \t" + Arrays.toString((double[]) vectorToOrbit.get()));
                System.out.println("v3 \t" + Arrays.toString((double[]) vectorToEarth.get()));
                landed.set(true);
            }else if(Probe.calculateDistanceToTarget("earth") <= MissionData.EARTH_RADIUS && landed.get()){
                System.out.println("probe velocity=======: " + Arrays.toString(Probe.getVelocity()));
                System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));
                System.out.println("*chilling*");
            }else if (!landed.get()){
                System.out.println("distance to Earth: " + Probe.calculateDistanceToTarget("earth"));
                System.out.println("probe velocity-------: " + Arrays.toString(Probe.getVelocity()));
                System.out.println("going back "); //System.out.println("bye b!tch ;* ");
            }
        }else if (Probe.calculateDistanceToTarget("titan") < MissionData.TITAN_RADIUS + 300){
            System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
            System.out.println("probe velocity$$$$$$$: " + Arrays.toString(Probe.getVelocity()));
            System.out.println("probe position$$$$$$$: " + Arrays.toString(Probe.getPosition()));
            System.out.println("ORBITING... ХОБА");
            count.set(count.get() + 1);
            if (Probe.calculateDistanceToTarget("titan") < MissionData.TITAN_RADIUS + 100){
                System.out.println("! TOO CLOSE   altitude: "+(Probe.calculateDistanceToTarget("titan")-MissionData.TITAN_RADIUS));
            }

            if (Probe.calculateDistanceToTarget("titan") <= MissionData.TITAN_RADIUS ){
                System.out.println("!!!!! WAY TOO CLOSE");
            }

        }else{
            System.out.println("distance to Titan: " + Probe.calculateDistanceToTarget("titan"));
            System.out.println("probe velocity*******: " + Arrays.toString(Probe.getVelocity()));
            System.out.println("zoomies");
            //System.out.println("trying to orbit");
        }

        double[] tv = SolarSystem.getVelocities()[MissionData.idMap.get("titan")];

        if(!reachedTitan.get() && Probe.calculateDistanceToTarget("titan") < MissionData.TITAN_RADIUS + 300){
            System.out.println("**************************** TITAN'S VELOCITY MAGNITUDE: " + Math.sqrt(Math.pow(SolarSystem.getVelocities()[MissionData.idMap.get("titan")][0], 2) + Math.pow(SolarSystem.getVelocities()[MissionData.idMap.get("titan")][1], 2) + Math.pow(SolarSystem.getVelocities()[MissionData.idMap.get("titan")][2], 2)));
            System.out.println("**************************** TITAN'S VELOCITY: " + Arrays.toString(tv));
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
                System.out.println("fuel for orbit: \t" + fuelForOrbit);
                System.out.println("within allowed quantity: \t" + (fuelForOrbit/MissionData.TIME_STEP_SIZE < MissionData.PROBE_MAX_FORCE_MAG));
                fuelUnitsUsed.set(fuelUnitsUsed.get() + fuelForOrbit);
                Probe.setVelocity(v);
                vectorToOrbit.set(v);
            }


            reachedTitan.set(true);
        }
    }
}
