package money.portosim;

import java.util.Date;
import money.portosim.containers.numeric.NumMatrix;

public class Backtest {
    private final AbstractStrategy strategy;
    private final NumMatrix<Date, String> priceSeries;
    private final Result result = new Result();

    public Backtest(AbstractStrategy strategy, NumMatrix<Date, String> priceSeries) {
        this.strategy = strategy;
        this.priceSeries = priceSeries;
    }

    public Result run() {
        priceSeries.rows().entrySet().stream().forEach(currentPrices -> {
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
