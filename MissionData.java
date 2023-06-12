import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Map.entry;

//Mission parameters specification
public class MissionData {

    //Differential equation solver
    static final String SOLVER = "euler"; //options: "euler", "rk4", "ab3", "am2";
    static final AtomicBoolean testingSolvers = new AtomicBoolean(false); //set to true if "ab3" or "am2" is chosen as SOLVER

    //Time step size in seconds
    public static final long TIME_STEP_SIZE = 5000;

    static final double GRAVITY_CONSTANT = 6.6743 * Math.pow(10, -20);

    //Celestial objects' states at launch
    //order: sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus
    public static final double[][] SOLAR_SYSTEM_INIT_POSITIONS = { { 0, 0, 0 }, { 7833268.43923962, 44885949.3703908, 2867693.20054382 }, { -28216773.9426889, 103994008.541512, 3012326.64296788 },
            { -148186906.893642, -27823158.5715694, 33746.8987977113 }, { -148458048.395164, -27524868.1841142, 70233.6499287411 }, { -159116303.422552,	189235671.561057, 7870476.08522969 },
            { 692722875.928222,	258560760.813524, -16570817.7105996 }, { 1253801723.95465, -760453007.810989, -36697431.1565206 }, { 1254501624.95946, -761340299.067828,	-36309613.8378104 },
            { 4454487339.09447, -397895128.763904, -94464151.3421107 }, { 1958732435.99338, 2191808553.21893, -17235283.8321992 } };

    public static final double[][] SOLAR_SYSTEM_INIT_VELOCITIES = { { 0, 0, 0 }, { -57.4967480139828, 11.52095127176, 6.21695374334136 }, { -34.0236737066136, -8.96521274688838, 1.84061735279188 },
            { 5.05251577575409, -29.3926687625899, 0.00170974277401292 }, { 4.34032634654904, -30.0480834180741, -0.0116103535014229 }, { -17.6954469224752, -13.4635253412947, 0.152331928200531 },
            { -4.71443059866156, 12.8555096964427, 0.0522118126939208 }, { 4.46781341335014, 8.23989540475628, -0.320745376969732 }, { 8.99593229549645, 11.1085713608453, -2.25130986174761 },
            { 0.447991656952326, 5.44610697514907, -0.122638125365954 }, { -5.12766216337626, 4.22055347264457, 0.0821190336403063 } };

    public static final double[] MASSES = {  1.99e30 ,  3.30e23 ,  4.87e24 ,  5.97e24 ,  7.35e22 ,  6.42e23 , 1.90e27 ,  5.68e26 ,  1.35e23 ,  1.02e26 ,  8.68e25 };

    public static final int EARTH_RADIUS = 6370;
    public static final int TITAN_RADIUS = 2575;

    //order: sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus
    public static final Map<String, Integer> idMap = Map.ofEntries(
            entry("sun", 0),
            entry("mercury", 1),
            entry("venus", 2),
            entry("earth", 3),
            entry("moon", 4),
            entry("mars", 5),
            entry("jupiter", 6),
            entry("saturn", 7),
            entry("titan", 8),
            entry("neptune", 9),
            entry("uranus", 10)
            );

    //Number of seconds between start and finish of the mission (from phase 1, so one year)
    static final LocalDateTime LAUNCH_DATE = LocalDateTime.of(2023, 4, 1, 0,0);
    static final LocalDateTime FINISH_DATE = LocalDateTime.of(2024, 4, 1, 0,0);
    static final long AIRTIME = Duration.between(LAUNCH_DATE, FINISH_DATE).getSeconds();

    //Number of steps in the simulation for one year
    public static final long N_OF_STEPS = AIRTIME / TIME_STEP_SIZE;

    //Spacecraft-related fields
    public static final double PROBE_MASS = 50000;
    public static final int PROBE_MAX_VELOCITY_MAG = 60;   // relative to Earth
    public static final int PROBE_MAX_FORCE_MAG =  3*10000000;   // N
    //default (position: Earth's centre offset by Earth's radius on x, velocity: hand-picked vector from phase 1
    public static final double[] PROBE_INIT_POSITION = {SOLAR_SYSTEM_INIT_POSITIONS[idMap.get("earth")][0] + EARTH_RADIUS, SOLAR_SYSTEM_INIT_POSITIONS[idMap.get("earth")][1], SOLAR_SYSTEM_INIT_POSITIONS[idMap.get("earth")][2]};
    public static final double[] PROBE_INIT_VELOCITY = {42, -42, -11};//for step 1000: , {43.053436279296875, -42.77777099609375, -3.1736793518066406};//{42.7210693359375, -43.122314453125, -3.2548828125};//{43.053436279296875, -42.77777099609375, -3.1736793518066406}; //{42.7210693359375, -43.122314453125, -3.2548828125};
    //[42.7210693359375, -43.122314453125, -3.2548828125]
    //3402.5140307034
    //[42.7210693359375, -43.12237548828125, -3.25482177734375]
    //740.4863898979625
    //velocity from phase 1: {42, -42, -11};
    //exam input (airtime: 10000)
    //{SolarSystem.positions[3][0], SolarSystem.positions[3][1] + SolarSystem.EARTH_RADIUS, SolarSystem.positions[3][2]};
    //{SolarSystem.velocities[3][0] + 3, SolarSystem.velocities[3][1] + 4, SolarSystem.velocities[3][2]};
}


/*
    //stuff we'll probably never need

    //"The state y(t) comprises both the position vector and the velocity vector v(t)"
    static double[][] currentState = {};

    //"The result can be presented as a list of pairs of times and states,
    //{(t0, y0), (t1, y1), (t2, y2), . . . , (tn, yn)}"
    static double[][] stateLog = {};
*/