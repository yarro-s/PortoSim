package money.portosim;

import money.portosim.containers.PriceSeries;

public class Backtest {
    private final AbstractStrategy strategy;
    private final PriceSeries priceSeries;
    private final Result result = new Result();

    public Backtest(AbstractStrategy strategy, PriceSeries priceSeries) {
        this.strategy = strategy;
        this.priceSeries = priceSeries;
    }

    public Result run() {
        priceSeries.ordered().entrySet().stream().forEach(currentPrices -> {
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
