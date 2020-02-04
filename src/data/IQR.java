package data;

import utils.Constants;

import java.io.Serializable;

public class IQR implements Serializable {

    private double Q1;
    private double Q3;
    private double IQR;

    public IQR(double q1, double q3) {
        Q1 = q1;
        Q3 = q3;
        this.IQR = Q3 - Q1;
    }

    public IQR() {}

    public double getQ1() {
        return Q1;
    }

    public void setQ1(double q1) {
        Q1 = q1;
    }

    public double getQ3() {
        return Q3;
    }

    public void setQ3(double q3) {
        Q3 = q3;
    }

    public double getIQR() {
        return IQR;
    }

    public void setIQR(double IQR) {
        this.IQR = IQR;
    }

    public boolean isOutlier(double value) {
        double min = Q1 - Constants.IQR_THRESHOLD * IQR;
        double max = Q3 + Constants.IQR_THRESHOLD * IQR;
        return value < min || value > max;
    }
}