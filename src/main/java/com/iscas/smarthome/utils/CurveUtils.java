package com.iscas.smarthome.utils;

import com.iscas.smarthome.data.CurveStatistics;
import com.iscas.smarthome.data.IQR;

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
        if(data.size() == 0) return new IQR(0.0, 0.0);
        if(data.size() == 1) return new IQR(data.get(0), data.get(0));
        List<Double> temp = new ArrayList<>();
        for(double d : data)
            temp.add(d);
        Collections.sort(temp);

        double Q1 = 0.0, Q3 = 0.0;
        if(temp.size() % 2 == 0) {
            Q1 = calMedian(temp, 0, temp.size() / 2 - 1);
            Q3 = calMedian(temp, temp.size() / 2, temp.size() - 1);
        } else {
            Q1 = calMedian(temp, 0, temp.size() / 2 - 1);
            Q3 = calMedian(temp, temp.size() / 2 + 1, temp.size() - 1);
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
        if(data.size() == 0) return 0.0;
        if(data.size() == 1) return data.get(0);
        List<Double> temp = new ArrayList<>();
        for(double d : data)
            temp.add(d);
        Collections.sort(temp);
        return calMedian(temp, 0, temp.size() - 1);
    }

    public static double calSlopeMedian(List<Double> data) {
        List<Double> slopes = new ArrayList<>();
        for (int i = 1; i < data.size(); i++)
            slopes.add(Math.abs(data.get(i) - data.get(i - 1)));
        return calValueMedian(slopes);
    }

    public static double calValueAvg(List<Double> data) {
        if(data.size() == 0) return 0.0;
        double sum = 0.0;
        for(double d : data)
            sum += d;
        return sum / data.size();
    }

    public static double calSlopeAvg(List<Double> data) {
        List<Double> slopes = new ArrayList<>();
        for (int i = 1; i < data.size(); i++)
            slopes.add(Math.abs(data.get(i) - data.get(i - 1)));
        return calValueAvg(slopes);
    }

    public static double calValueDelta(List<Double> data) {
        double avg = calValueAvg(data);
        double res = 0.0;
        for(double d : data)
            res += Math.pow((d - avg), 2);
        return Math.sqrt(res / data.size());
    }

    public static double calSlopeDelta(List<Double> data) {
        List<Double> slopes = new ArrayList<>();
        for (int i = 1; i < data.size(); i++)
            slopes.add(Math.abs(data.get(i) - data.get(i - 1)));
        return calValueDelta(slopes);
    }

    public static double getValueSimilarity(List<Double> data1, List<Double> data2) {
        IQR valueIQR1 = CurveUtils.calValueIQR(data1);
        double valueAvg1 = CurveUtils.calValueAvg(data1);

        IQR valueIQR2 = CurveUtils.calValueIQR(data2);
        double valueAvg2 = CurveUtils.calValueAvg(data2);

        double valueS1 = getSimilarity(valueIQR1.getQ1(), valueIQR1.getQ3(), valueAvg2);
        double valueS2 = getSimilarity(valueIQR2.getQ1(), valueIQR2.getQ3(), valueAvg1);
        System.out.println("Value Similarity: " + valueS1 + " " + valueS2);

//        double valueS1 = getSimilarity(valueIQR1.getQ1(), valueIQR1.getQ3(), valueMedian1, valueMedian2);
//        double valueS2 = getSimilarity(valueIQR2.getQ1(), valueIQR2.getQ3(), valueMedian2, valueMedian1);
//        System.out.println("Value Similarity: " + valueS1 + " " + valueS2);

        return (valueS1 + valueS2) / 2.0;
    }

    public static double getSlopeSimilarity(List<Double> data1, List<Double> data2) {
        IQR slopeIQR1 = CurveUtils.calSlopeIQR(data1);
        double slopeAvg1 = CurveUtils.calSlopeAvg(data1);

        IQR slopeIQR2 = CurveUtils.calSlopeIQR(data2);
        double slopeAvg2 = CurveUtils.calSlopeAvg(data2);

        double slopeS1 = getSimilarity(slopeIQR1.getQ1(), slopeIQR1.getQ3(), slopeAvg2);
        double slopeS2 = getSimilarity(slopeIQR2.getQ1(), slopeIQR2.getQ3(), slopeAvg1);
        System.out.println("Slope Similarity: " + slopeS1 + " " + slopeS2);

//        double slopeS1 = getSimilarity(slopeIQR1.getQ1(), slopeIQR1.getQ3(), slopeMedian1, slopeMedian2);
//        double slopeS2 = getSimilarity(slopeIQR2.getQ1(), slopeIQR2.getQ3(), slopeMedian2, slopeMedian1);
//        System.out.println("Slope Similarity: " + slopeS1 + " " + slopeS2);

        return (slopeS1 + slopeS2) / 2.0;
    }

    public static double getSimilarity(double q1, double q3, double statistics) {
        if(q3 == q1) return statistics == (q1 + q3) / 2 ? 1.0 : 0.0;
        if(statistics >= q1 && statistics <= q3)
            return 1.0 - Math.abs(statistics - (q1 + q3) / 2) / (q3 - q1);
        if(statistics < q1 && statistics >= q1 - 1.5 * (q3 - q1))
            return 0.5 - (q1 - statistics) / (3.0 * (q3 - q1));
        if(statistics > q3 && statistics <= q3 + 1.5 * (q3 - q1))
            return 0.5 - (statistics - q3) / (3.0 * (q3 - q1));
        return 0.0;
    }
}
