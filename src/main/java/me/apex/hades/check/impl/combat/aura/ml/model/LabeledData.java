package me.apex.hades.check.impl.combat.aura.ml.model;

//Credits to Nova41
public class LabeledData implements Cloneable {

    private final int category;
    private double[] data;

    public LabeledData(int category, double[] data) {
        this.category = category;
        this.data = data;
    }

    public int getCategory() {
        return this.category;
    }

    public double[] getData() {
        return this.data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public void setData(int row, double data) {
        this.data[row] = data;
    }

    public LabeledData clone() {
        try {
            return (LabeledData) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
