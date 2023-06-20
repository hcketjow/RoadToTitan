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
            System.out.println(Arrays.toString(SDN(0.5,0)));
        }
    }

    public static double[] flat() //flat distribution, returns a value between 0 and 1
    {
        double[] wind = new double[3];
        Random random = new Random();

        // Define the range of the random variable
        double minValue = -1.0;
        double maxValue = 1.0;

        // Generate a random value within the range
        for (int i = 0; i < 3; i++) {
            double randomValue = minValue + (maxValue - minValue) * random.nextDouble();
            wind[i] = randomValue;
        }

        return wind;
    }
    public static double[] SDN(double standardDeviation, double mean) //standard normal distribution
    {
        double[] wind = new double[3];
        Random ran = new Random();

        for (int i = 0; i < 3; i++) {
            wind[i] = ran.nextGaussian() * standardDeviation + mean;
            // r.nextGaussian()*desiredStandardDeviation+desiredMean;
        }

        return wind;
    }

}