package pl.parser.nbp;

import java.util.List;

public class Statistics {
    static double calculateMean(List<Double> rates) {
        double sum = 0;
        int length = rates.size();

        for (Double r : rates) {
            sum += r;
        }

        return sum / length;
    }

    static double calculateStdDev(List<Double> rates) {
        double variance = 0;
        int length = rates.size();
        double mean = calculateMean(rates);

        for (Double r : rates) {
            variance += Math.pow((r - mean), 2);
        }
        variance /= length;

        return Math.sqrt(variance);
    }
}
