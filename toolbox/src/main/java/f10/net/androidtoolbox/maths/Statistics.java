package f10.net.androidtoolbox.maths;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by fl0 on 16/08/2017.
 */

public class Statistics {

    public final double mean;
    public final double minimum;
    public final double maximum;
    public final double standardDeviation;

    private Statistics(double mean, double minimum, double maximum, double standardDeviation) {
        this.mean = mean;
        this.minimum = minimum;
        this.maximum = maximum;
        this.standardDeviation = standardDeviation;
    }

    public static Statistics from(Set<Double> set) {
        ArrayList<Double> list = new ArrayList();
        list.addAll(set);
        return from(list);
    }

    public static Statistics from(List<Double> list) {
        double[] array = new double[list.size()];
        for(int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return from(array);
    }

    public static Statistics from(double[] list) {

        double sum = 0.0;
        double mean = 0.0;
        double num=0.0;
        double numi = 0.0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double i : list) {
            sum+=i;
            min = i < min ? i : min;
            max = i > max ? i : max;
        }
        mean = sum/list.length;

        for (double i : list) {
            numi = Math.pow(i - mean, 2);
            num+=numi;
        }

        double stdev = Math.sqrt(num / list.length);

        return new Statistics(mean, min, max, stdev);
    }
}
