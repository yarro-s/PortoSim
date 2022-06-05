package money.portosim;

import money.portosim.containers.QuoteSeries;

public class Backtest {
    private final AbstractStrategy strategy;
    private final QuoteSeries priceSeries;
    private final Result result = new Result();

    public Backtest(AbstractStrategy strategy, QuoteSeries priceSeries) {
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
