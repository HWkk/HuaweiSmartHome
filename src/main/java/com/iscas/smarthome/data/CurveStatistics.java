package com.iscas.smarthome.data;

/**
 * 曲线统计值
 */
public class CurveStatistics {

    private IQR slopeIQR;
    private IQR valueIQR;

    public CurveStatistics() {}

    public CurveStatistics(IQR slopeIQR, IQR valueIQR) {
        this.slopeIQR = slopeIQR;
        this.valueIQR = valueIQR;
    }

    public IQR getSlopeIQR() {
        return slopeIQR;
    }

    public void setSlopeIQR(IQR slopeIQR) {
        this.slopeIQR = slopeIQR;
    }

    public IQR getValueIQR() {
        return valueIQR;
    }

    public void setValueIQR(IQR valueIQR) {
        this.valueIQR = valueIQR;
    }
}
