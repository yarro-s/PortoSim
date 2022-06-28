package money.portosim;

import java.util.Map;
import java.util.OptionalDouble;
import money.portosim.containers.numeric.NumFrame;

public class Portfolio {
    NumFrame<String> positions;

    public NumFrame<String> positions() {
        return positions;
    }

    public OptionalDouble valueAtPrice(NumFrame<String> assetPrices) {
        return this.positions().mult(assetPrices).values().stream().reduce(Double::sum)
                .map(OptionalDouble::of).orElseGet(OptionalDouble::empty);
    }

    public Portfolio() {
        positions = NumFrame.empty();
    }

    public Portfolio(Map<String, Double> m) {
        positions = NumFrame.of(m);
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
