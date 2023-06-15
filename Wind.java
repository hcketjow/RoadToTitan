import java.io.*;
import java.util.*;

class Wind {
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

    public double[] weibull(double eta, double beta)
    {
        double[] wind = new double[3];
        for (int i = 1; i < 3; i++) {
            wind[i] = new WeibullDistribution(getRandomGenerator(), shape, scale,
                    WeibullDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
        }

        return wind;
    }


}
