package money.portosim;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;

public interface QuantifiableSeries {
    List<Double> numSeries();

    default OptionalDouble volatility() {
        try {
            return OptionalDouble.of(Metrics.volatility(numSeries()));
        } catch (NoSuchElementException e) {
            return OptionalDouble.empty();
        }
    }
}
