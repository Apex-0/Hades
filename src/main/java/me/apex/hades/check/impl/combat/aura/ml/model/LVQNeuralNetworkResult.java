package me.apex.hades.check.impl.combat.aura.ml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//Credits to Nova41
public class LVQNeuralNetworkResult {

    private final List<Map.Entry<Double, Integer>> distances;

    LVQNeuralNetworkResult(TreeMap<Double, Integer> distances) {
        this.distances = new ArrayList<>(distances.entrySet());
    }

    public int getCategory() {
        return distances.get(0).getValue();
    }

    public double getDifference() {
        return distances.get(0).getKey();
    }

    public double getLikelihood() {
        return distances.get(0).getKey() / distances.get(1).getKey();
    }

}
