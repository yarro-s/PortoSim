package money.portosim;

import money.portosim.containers.NumericMap;
import java.util.Map;
import java.util.OptionalDouble;

public class Portfolio {
    NumericMap<String> positions;

    public NumericMap<String> positions() {
        return positions;
    }

    public OptionalDouble valueAtPrice(Map<String, Double> assetPrices) {
        var quote = NumericMap.of(assetPrices);
        return this.positions().mult(quote).values().stream().reduce(Double::sum)
                .map(OptionalDouble::of).orElseGet(OptionalDouble::empty);
    }

    public Portfolio() {
        positions = new NumericMap<>();
    }

    public Portfolio(Map<String, Double> m) {
        positions = new NumericMap<>(m);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Portfolio(positions());
    }

    @Override
    public String toString() {
        return positions().toString();
    }
}
