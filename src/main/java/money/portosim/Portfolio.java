package money.portosim;

import money.portosim.containers.generic.NumericMap;
import money.portosim.containers.PriceMap;

import java.util.Map;
import java.util.OptionalDouble;

public class Portfolio {
    NumericMap<String> positions;

    public NumericMap<String> positions() {
        return positions;
    }

    public OptionalDouble valueAtPrice(PriceMap assetPrices) {
        return this.positions().multiply(assetPrices).values().stream().reduce(Double::sum)
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
