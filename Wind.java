import java.util.Random;

class Wind {

    public static void main(String[] args) {

    }

    public static double[] SDN() //standard normal distribution
    {
        double[] wind = new double[3];
        Random ran = new Random();
        double min = -1;
        double max = 1;

        for (int i = 1; i < 3; i++) {
            wind[i] = min + (max - min) * ran.nextGaussian();
        }

        return wind;
    }

    public static double generateWeibullRandom(double shape, double scale) {
        Random random = new Random();
        double u = random.nextDouble(); // Uniform random value between 0 and 1

        // Inverse transform sampling using the Weibull distribution formula
        double x = scale * Math.pow(-Math.log(1 - u), 1 / shape);
        return x;
    }

    public static double WeibullRandomGenerator() {
        double shape = 2.5; // Shape parameter
        double scale = 1.0; // Scale parameter
        double randomValue = generateWeibullRandom(shape, scale);

        return randomValue;
    }


}
