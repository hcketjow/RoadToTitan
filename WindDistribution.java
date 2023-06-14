import java.util.Random;
public class WindDistribution {
    private static double RayleighDistribution(double x, double sigma) {
        return (x / (sigma * sigma)) * Math.exp(-x * x / (2 * sigma * sigma));
    }
    public static void main(String[] args) {
        double sigma = 1.0; // The scale parameter
        Random random = new Random();
        double randomValue, ReyleighDistributionValues;
        int sizeOfRandomGeneratedValues = 10;
        double x[] = new double[sizeOfRandomGeneratedValues];
        double y[] = new double[sizeOfRandomGeneratedValues];
        double z[] = new double[sizeOfRandomGeneratedValues];
        // Generate 10 random values from the Rayleigh distribution
        for (int i = 0; i < sizeOfRandomGeneratedValues; i++) {
            randomValue = Math.sqrt(-2 * sigma * sigma * Math.log(1 - random.nextDouble()));
            ReyleighDistributionValues = RayleighDistribution(randomValue, sigma);
            x[i] = randomValue;
            y[i] = ReyleighDistributionValues;
            z[i] = random.nextDouble();
            System.out.println("Values of x= "+x[i]+ " values of y= "+y[i]+" values of z = "+z[i]);
            System.out.println("Random value: " + randomValue + ", Rayleigh Distribution values: " + ReyleighDistributionValues);
        }
    }
}
