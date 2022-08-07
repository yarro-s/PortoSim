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
    public QuantSpanContext setValuesPerRefPeriod(int valuesPerRefPeriod) {
        context.setValuesPerRefPeriod(valuesPerRefPeriod);
        return context;
    }

    @Override
    public double getMeanRiskFreeRate() {
        return context.getMeanRiskFreeRate();
    }
    
    @Override
    public QuantSpanContext setMeanRiskFreeRate(double meanRiskFreeRate) {
        context.setMeanRiskFreeRate(meanRiskFreeRate);
        return context;
    }

    @Override
    public List<Double> values() {
        return context.values();
    }
}
