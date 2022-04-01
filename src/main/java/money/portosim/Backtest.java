package money.portosim;

import money.portosim.containers.PriceSeries;

public class Backtest {
    private final Strategy strategy;
    private final PriceSeries priceSeries;
    private final Result result = new Result();

    public Backtest(Strategy strategy, PriceSeries priceSeries) {
        this.strategy = strategy;
        this.priceSeries = priceSeries;

        this.strategy.setResult(result);
    }

    public void run() {
        priceSeries.ordered().entrySet().stream().forEach(currentPrices -> {
            var date = currentPrices.getKey();
            var prices = currentPrices.getValue();

            var portfolio = strategy.apply(date, prices);
            result.update(date, prices, portfolio);
        });
    }

    public Result getResult() {
        return result;
    }
}
