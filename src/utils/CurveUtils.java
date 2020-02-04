package utils;

import data.CurveStatistics;
import data.IQR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurveUtils {

    public static CurveStatistics calStatistics(List<Double> data) {
        return new CurveStatistics(calSlopeIQR(data), calValueIQR(data));
    }

    public static double calMedian(List<Double> data, int start, int end) {
        int len = end - start + 1;
        if(len % 2 == 1) return data.get(start + len / 2);
        return (data.get(start + len / 2 - 1) + data.get(start + len / 2)) / 2.0;
    }

    public static IQR calValueIQR(List<Double> data) {
        List<Double> temp = new ArrayList<>();
        for(double d : data)
            temp.add(d);
        Collections.sort(temp);

        double Q1 = 0.0, Q3 = 0.0;
        if(temp.size() % 2 == 0) {
            Q1 = calMedian(data, 0, temp.size() / 2 - 1);
            Q3 = calMedian(data, temp.size() / 2, temp.size() - 1);
        } else {
            Q1 = calMedian(data, 0, temp.size() / 2 - 1);
            Q3 = calMedian(data, temp.size() / 2 + 1, temp.size() - 1);
        }
        return new IQR(Q1, Q3);
    }

    public static IQR calSlopeIQR(List<Double> data) {
        List<Double> slopes = new ArrayList<>();
        for(int i = 1; i < data.size(); i++)
            slopes.add(Math.abs(data.get(i) - data.get(i - 1)));
        return calValueIQR(slopes);
    }

    public static double calValueMedian(List<Double> data) {
        List<Double> temp = new ArrayList<>();
        for(double d : data)
            temp.add(d);
        Collections.sort(temp);
        return calMedian(temp, 0, temp.size() - 1);
    }

    public static double calSlopeMedian(List<Double> data) {
        List<Double> slopes = new ArrayList<>();
        for(int i = 1; i < data.size(); i++)
            slopes.add(Math.abs(data.get(i) - data.get(i - 1)));
        return calValueMedian(slopes);
    }
}
