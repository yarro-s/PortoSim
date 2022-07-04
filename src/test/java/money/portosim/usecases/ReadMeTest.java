/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.usecases;

import java.io.FileReader;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.Backtest;
import money.portosim.BacktestBuilder;
import money.portosim.containers.sources.CSVPriceSource;
import money.portosim.strategies.FixedAllocation;
import money.portosim.strategies.TimedStrategy;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class ReadMeTest {
    
    private final String sp500GoldMonthlyCSV = "src/test/resources/sp500_gold_3yr_monthly.csv";
    
    @Test
    public void sp500PlusGoldSimpleBuild() throws Exception {
        // Load prices from a CSV file
        var prices = new CSVPriceSource(new FileReader(sp500GoldMonthlyCSV));
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        // Build a backtest
        var result = new BacktestBuilder(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)   // rebalance every year
                .run(prices);    // test on the historic prices
        
        Assert.assertEquals(result.quant().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }
    
    @Test
    public void sp500PlusGoldAlloc() throws Exception {
        // Load prices from the CSV file   
        var prices = new CSVPriceSource(new FileReader(sp500GoldMonthlyCSV));
        
        // Define a constant allocation portfolio
        var fixedAllocation = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        // Chain the strategies so that the fixed allocation is rebalanced every year
        var rebalancedStrategy = new TimedStrategy(ChronoUnit.YEARS).chainTo(fixedAllocation); 
        
        // Create a new backtest with the strategy and the price data
        var backtest = new Backtest(rebalancedStrategy, prices);

        // Run the backtest
        var result = backtest.run();
        
        Assert.assertEquals(result.quant().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }    
}
