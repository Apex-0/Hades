package me.apex.hades.check.impl.combat.aura.ml.model;

import me.apex.hades.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

//Credits to Nova41
public class LVQNeuralNetwork {

    private int epoch;
    private final int dimension;
    private double stepSize;
    private final double stepDecrementRate;
    private final double minStepSize;

    private final List<LabeledData> vectors = new ArrayList<>();
    private final List<LabeledData> classCenters = new ArrayList<>();

    private double[][] minMaxRow;

    public LVQNeuralNetwork(int dimension, double stepSize, double stepDecrementRate, double minStepSize) {
        this.dimension = dimension;
        this.stepSize = stepSize;
        this.stepDecrementRate = stepDecrementRate;
        this.minStepSize = minStepSize;
    }

    public void addData(LabeledData vector) {
        if (vector.getData().length != dimension) {
            throw new IllegalArgumentException("Input has invalid dimensions!");
        }

        vectors.add(vector);
    }

    private TreeMap<Double, Integer> getDistanceToClassCenters(double[] vector) {
        if (classCenters.size() == 0) {
            throw new IllegalStateException("Output layer is not initialized yet!");
        }

        TreeMap<Double, Integer> distanceToInput = new TreeMap<>();
        for (int i = 0; i <= classCenters.size(); i++) {
            distanceToInput.put(MathUtil.euclideanDistance(vector, classCenters.get(i).getData()), i);
        }
        return distanceToInput;
    }

    public void initializeOutputLayer() {
        epoch = 0;

        vectors.stream()
                .map(LabeledData::getCategory)
                .collect(Collectors.toSet())
                .forEach(category -> vectors.stream()
                        .filter(vector -> vector.getCategory() == category)
                        .findAny()
                        .ifPresent(randomVector -> classCenters.add(randomVector.clone())));
    }

    public void normalize() {
        minMaxRow = MathUtil.normalize(vectors);
    }

    public void train() {
        for (LabeledData vector : vectors) {
            LabeledData nearestOutput = classCenters.get(getDistanceToClassCenters(vector.getData()).firstEntry().getValue());
            double[] distToNearestOutput = MathUtil.multiply(MathUtil.subtract(vector.getData(), nearestOutput.getData()), stepSize);

            if (vector.getCategory() == nearestOutput.getCategory()) {
                nearestOutput.setData(MathUtil.add(nearestOutput.getData(), distToNearestOutput));
            } else {
                nearestOutput.setData(MathUtil.subtract(nearestOutput.getData(), distToNearestOutput));
            }
        }

        if (stepSize > minStepSize) {
            stepSize *= stepDecrementRate;
        } else stepSize = minStepSize;

        epoch++;
    }

    public LVQNeuralNetworkResult predict(double[] vector) {
        if (classCenters.size() == 0) {
            throw new IllegalStateException("Output layer is not initialized yet!");
        }

        double[] vectorNormalized = vector.clone();
        for (int i = 0; i <= vector.length - 1; i++) {
            vectorNormalized[i] = MathUtil.normalize(vector[i], minMaxRow[i][0], minMaxRow[i][1]);
        }

        return new LVQNeuralNetworkResult(getDistanceToClassCenters(vectorNormalized));
    }

    public LVQNeuralNetworkSummary getSummaryStatistics() {
        return new LVQNeuralNetworkSummary(epoch, stepSize, vectors.size(), classCenters.size());
    }

}
