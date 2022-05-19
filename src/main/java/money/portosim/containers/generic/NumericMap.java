package money.portosim.containers.generic;

import java.util.Map;

public class NumericMap<K> extends TaggedMap<K, Double> implements AlgebraicComposableMap<K, Double> {

    public NumericMap(Map<K, Double> m) {
        super(m);
    }

    public NumericMap() {
        super();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new NumericMap<>(asMap());
    }

    @Override
    public AlgebraicComposableMap<K, Double> add(AlgebraicComposableMap<K, Double> tm) {
        return new NumericMap<>(compose(tm, Double::sum));
    }

    @Override
    public AlgebraicComposableMap<K, Double> add(Double value) {
        var dummyMap = new NumericMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.add(dummyMap);
    }

    @Override
    public AlgebraicComposableMap<K, Double> multiply(AlgebraicComposableMap<K, Double> ntm) {
        return new NumericMap<>(compose(ntm, (x, y) -> x * y));
    }

    @Override
    public AlgebraicComposableMap<K, Double> multiply(Double value) {
        var dummyMap = new NumericMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.multiply(dummyMap);
    }

    @Override
    public AlgebraicComposableMap<K, Double> divide(AlgebraicComposableMap<K, Double> ntm) {
        return new NumericMap<>(compose(ntm, (x, y) -> x / y));
    }

    @Override
    public AlgebraicComposableMap<K, Double> divide(Double value) {
        var dummyMap = new NumericMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.divide(dummyMap);
    }

    @Override
    public AlgebraicComposableMap<K, Double> subtract(AlgebraicComposableMap<K, Double> ntm) {
        return new NumericMap<>(compose(ntm, (x, y) -> x - y));
    }

    @Override
    public AlgebraicComposableMap<K, Double> subtract(Double value) {
        var dummyMap = new NumericMap<>(this);
        dummyMap.replaceAll((k, v) -> value);
        return this.subtract(dummyMap);
    }

    @Override
    public Double sum() {
        return this.values().stream().reduce(Double::sum).orElse(0.0);
    }
}
