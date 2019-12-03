package data;

import stateautomaton.attribute.Attribute;

import java.io.Serializable;

public class Data implements Serializable{

    private String mode;
    private Attribute attribute;

    public Data(String mode, Attribute attribute) {
        this.mode = mode;
        this.attribute = attribute;
    }

    public Data() {}

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return mode + " " + attribute.toString();
    }
}
