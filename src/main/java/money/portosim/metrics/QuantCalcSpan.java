package money.portosim.metrics;

import java.util.List;

/**
 *
 * @author yarro
 */
class QuantCalcSpan implements QuantSpan {
    
    private final QuantSpanContext context;

    QuantCalcSpan(QuantSpanContext context) {
        this.context = context;
    }

    @Override
    public int getValuesPerRefPeriod() {
        return context.getValuesPerRefPeriod();
    }

    @Override
    public double getMeanRiskFreeRate() {
        return context.getMeanRiskFreeRate();
    }

    @Override
    public List<Double> values() {
        return context.values();
    }
}
