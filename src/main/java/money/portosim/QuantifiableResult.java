package money.portosim;

import java.util.OptionalDouble;

public interface QuantifiableResult extends QuantifiableSeries {

    default OptionalDouble cummulativeGrowthRate() {
        if (numSeries().size() > 1) {
            return OptionalDouble.of(Metrics.cummulativeGrowthRate(numSeries()));
        } else {
            return OptionalDouble.empty();
        }
    }

    default OptionalDouble totalReturn() {
        if (numSeries().size() > 1) {
            return OptionalDouble.of(Metrics.totalReturn(numSeries()));
        } else {
            return OptionalDouble.empty();
        }
    }
}
