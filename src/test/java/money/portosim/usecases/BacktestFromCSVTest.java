/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.usecases;

import java.io.FileReader;
import java.util.TreeMap;
import money.portosim.Backtest;
import money.portosim.Metrics;
import money.portosim.containers.NumericMap;
import money.portosim.containers.sources.CSVPriceSource;
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
        var prices = new CSVPriceSource(new FileReader(sp500DailyCSV));

        var asset = new NumericMap<String>();
        asset.put("SP500TR", 1.0);
        
        var backtest = Backtest.withStrategy(new ConstantAllocation(asset)).setPrices(prices);

        backtest.run();

        var result = backtest.getResult();
        var pfHist = backtest.getResult().getPortfolioHistory();
        
        var expTotalReturn = NumericMap.of(new TreeMap<>(prices).lastEntry()
                .getValue()).div(NumericMap.of(new TreeMap<>(prices).firstEntry().getValue()))
                .getOrDefault("SP500TR", 0.0);

        Assert.assertEquals(result.apply(Metrics::totalReturn), expTotalReturn, 0.001);
        Assert.assertEquals(pfHist.size(), prices.size());
    }
}
