import java.util.Arrays;
import java.util.Random;

class WindDistribution {

    // The main method is here for just testing
    public static void main(String[] args) {
        System.out.println("Just for testing");
        System.out.println("Flat Distribution");
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(flat()));
        }
        System.out.println("Normal Distribution");
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(SDN()));
        }
        System.out.println("Weibull Distribution");
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(weibull()));
        }
        System.out.println("Rayleigh Distribution");
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(rayleigh()));
        }
    }

    public static double[] flat() //flat distribution, returns a value between 0 and 1
    {
        double[] wind = new double[3];
        Random random = new Random();

        // Define the range of the random variable
        double minValue = 0.0;
        double maxValue = 1.0;

        // Generate a random value within the range
        for (int i = 0; i < 3; i++) {
            double randomValue = minValue + (maxValue - minValue) * random.nextDouble();
            wind[i] = randomValue;
        }

        return wind;
    }
    public static double[] SDN() //standard normal distribution
    {
        double[] wind = new double[3];
        Random ran = new Random();

        for (int i = 1; i < 3; i++) {
            wind[i] = ran.nextGaussian();
        }

        return wind;
    }

    public static double generateWeibullRandom(double shape, double scale) {
        Random ran = new Random();
        double min = -1.0;
        double max = 1.0;

        double u = min + (max - min) * ran.nextDouble(); // Uniform random value between -1 and 1

        // Inverse transform sampling using the Weibull distribution formula
        double x = scale * Math.pow(-Math.log(1 - u), 1 / shape);
        return x;
    }

    public static double[] weibull() {
        double shape = 2.5; // Shape parameter
        double scale = 1.0; // Scale parameter
        double[] wind = new double[3];

        for (int i = 0; i < 3; i++) {
            wind[i] = generateWeibullRandom(shape, scale);
        }
        return wind;
    }



    private static double[] rayleigh() {
        Random random = new Random();
        double min = -1.0;
        double max = 1.0;
        double[] wind = new double[3];

        double sigma = (max - min) / Math.sqrt(Math.PI / 2.0);

        for (int i = 0; i < 3; i++) {
            // Generate a random number with Gaussian distribution
            double gaussianValue = random.nextGaussian();

            // Apply Rayleigh distribution formula to transform the Gaussian number
            double rayleighValue = sigma * Math.sqrt(-2.0 * Math.log(gaussianValue));

            // Scale and shift the value to the desired range
            double scaledValue = min + (max - min) * (rayleighValue + 1.0) / 2.0;
            wind[i] = scaledValue;
        }

        return wind;
    }



}