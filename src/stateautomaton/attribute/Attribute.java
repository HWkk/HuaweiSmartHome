package stateautomaton.attribute;

import utils.Constants;

import java.util.Arrays;

public class Attribute {

    private double[] attributes;
    private int dimension;

    public Attribute() {
        dimension = Constants.ATTRIBUTE_DIMENSION;
        attributes = new double[Constants.ATTRIBUTE_DIMENSION];
    }

    public Attribute(double[] attributes) {
        this.attributes = attributes;
    }

    public double getAttribute(int index) {
        return index >= attributes.length || index < 0 ? 0.0 : attributes[index];
    }

    public void setAttribute(int index, double val) {
        if(index >= attributes.length || index < 0)
            return;
        attributes[index] = val;
    }

    public int getDimension() {
        return dimension;
    }

    public void mergeWithOther(Attribute other, double decay) {
        for(int i = 0; i < attributes.length; i++)
            attributes[i] = decay * attributes[i] + (1 - decay) * other.getAttribute(i);
    }

    @Override
    public String toString() {
        return Arrays.toString(attributes);
//        StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < attributes.length; i++) {
//            sb.append(attributes[i]);
//            if(i < attributes.length - 1)
//                sb.append(",");
//        }
//        return sb.toString();
    }
}
