package me.apex.hades.check.impl.combat.ml.model;

import lombok.Getter;

//Credits to Nova41
public class LVQNeuralNetworkSummary {

    @Getter
    private final double stepSize;
    @Getter
    private final int epoch;
    @Getter
    private final int inputs;
    @Getter
    private final int outputs;

    LVQNeuralNetworkSummary(int epoch, double stepSize, int inputs, int outputs) {
        this.epoch = epoch;
        this.stepSize = stepSize;
        this.inputs = inputs;
        this.outputs = outputs;
    }

}
