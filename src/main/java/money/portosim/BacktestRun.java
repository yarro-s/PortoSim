package money.portosim;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

class BacktestRun {
    private final Strategy strategy;
    private final Map<Date, Map<String, Double>> priceSeries;
    private final Result result = new Result();

    public BacktestRun(Strategy strategy, Map<Date, Map<String, Double>> priceSeries) {
        this.strategy = strategy;
        this.priceSeries = priceSeries;
    }

    public Result run() {
        new TreeMap<>(priceSeries).entrySet().stream().forEach(currentPrices -> {
            var date = currentPrices.getKey();
            var prices = currentPrices.getValue();

            var portfolio = strategy.makePortfolio(date, prices);
            result.update(date, prices, portfolio);
        });
        return getResult();
    }

    public Result getResult() {
        return result;
    }
}
