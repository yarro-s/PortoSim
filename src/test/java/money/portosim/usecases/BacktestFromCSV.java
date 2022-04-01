package money.portosim.usecases;

import money.portosim.Backtest;
import money.portosim.containers.PriceSeries;
import money.portosim.containers.readers.CSVPriceSeriesReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import money.portosim.containers.generic.NumericMap;
import money.portosim.strategies.ConstantAllocation;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class BacktestFromCSV {
    private String sp500DailyCSV = "src/test/resources/sp500_3yr_daily.csv";

    @Test
    public void constantAllocSP500() throws FileNotFoundException {
        var priceReader = new CSVPriceSeriesReader(new FileReader(sp500DailyCSV));
        PriceSeries prices = priceReader.readPrices();

        var asset = new NumericMap<String>();
        asset.put("SP500TR", 1.0);

        var backtest = new Backtest(new ConstantAllocation(asset), prices);

        backtest.run();

        var result = backtest.getResult();
        var expTotalReturn = prices.ordered().lastEntry()
                .getValue().divide(prices.ordered().firstEntry().getValue())
                .getOrDefault("SP500TR", 0.0);

        Assert.assertEquals(result.totalReturn().orElse(0.0), expTotalReturn, 0.001);
    }
}
