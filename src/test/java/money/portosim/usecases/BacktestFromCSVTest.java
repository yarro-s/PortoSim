/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.usecases;

import java.io.FileReader;
import money.portosim.Backtest;
import money.portosim.containers.NumericMap;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.readers.QuoteSeriesCSVSource;
import money.portosim.strategies.ConstantAllocation;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class BacktestFromCSVTest {
    
    private final String sp500DailyCSV = "src/test/resources/sp500_3yr_daily.csv";

    @Test
    public void constantAllocSP500() throws Exception {    
        var priceSource = new QuoteSeriesCSVSource(new FileReader(sp500DailyCSV));
        var prices = new QuoteSeries(priceSource);

        var asset = new NumericMap<String>();
        asset.put("SP500TR", 1.0);

        var backtest = new Backtest(new ConstantAllocation(asset), prices);

        backtest.run();

        var result = backtest.getResult();
        var pfHist = backtest.getResult().getPortfolioHistory();
        
        var expTotalReturn = prices.ordered().lastEntry()
                .getValue().div(prices.ordered().firstEntry().getValue())
                .getOrDefault("SP500TR", 0.0);

        Assert.assertEquals(result.totalReturn().orElse(0.0), expTotalReturn, 0.001);
        Assert.assertEquals(pfHist.size(), prices.size());
    }
}
