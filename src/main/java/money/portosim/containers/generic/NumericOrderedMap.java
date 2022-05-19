package money.portosim.containers.generic;

import money.portosim.QuantifiableSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NumericOrderedMap<K> extends TaggedOrderedMap<K, Double>
        implements AlgebraicComposableMap<K, Double>, QuantifiableSeries {

    public NumericOrderedMap(Map<K, Double> m) {
        super(m);
    }

    public NumericOrderedMap() {
        super();
    }

    @Override
    public AlgebraicComposableMap<K, Double> add(AlgebraicComposableMap<K, Double> tm) {
        return new NumericOrderedMap<>(compose(tm, Double::sum));
    }

    @Override
    public AlgebraicComposableMap<K, Double> add(Double value) {
        var dummyMap = new NumericOrderedMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.add(dummyMap);
    }

    @Override
    public AlgebraicComposableMap<K, Double> multiply(AlgebraicComposableMap<K, Double> ntm) {
        return new NumericOrderedMap<>(compose(ntm, (x, y) -> x * y));
    }

    @Override
    public AlgebraicComposableMap<K, Double> multiply(Double value) {
        var dummyMap = new NumericOrderedMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.multiply(dummyMap);
    }

    @Override
    public AlgebraicComposableMap<K, Double> divide(AlgebraicComposableMap<K, Double> ntm) {
        return new NumericOrderedMap<>(compose(ntm, (x, y) -> x / y));
    }

    @Override
    public AlgebraicComposableMap<K, Double> divide(Double value) {
        var dummyMap = new NumericOrderedMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.divide(dummyMap);
    }

    @Override
    public AlgebraicComposableMap<K, Double> subtract(AlgebraicComposableMap<K, Double> ntm) {
        return new NumericOrderedMap<>(compose(ntm, (x, y) -> x - y));
    }

    @Override
    public AlgebraicComposableMap<K, Double> subtract(Double value) {
        var dummyMap = new NumericOrderedMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.subtract(dummyMap);
    }

    @Override
    public Double sum() {
        return this.values().stream().reduce(Double::sum).orElse(0.0);
    }

    @Override
    public List<Double> numSeries() {
        return new ArrayList<>(ordered().values());
    }
}
