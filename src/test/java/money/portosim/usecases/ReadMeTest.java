/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.usecases;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.Backtest;
import money.portosim.BacktestBuilder;
import money.portosim.containers.PriceSeries;
import money.portosim.containers.readers.CSVPriceSeriesReader;
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
    public void sp500PlusGoldSimpleBuild() throws FileNotFoundException {
        // Load prices from the CSV file
        var priceReader = new CSVPriceSeriesReader(new FileReader(sp500GoldMonthlyCSV));
        PriceSeries prices = priceReader.readPrices();
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        var result = new BacktestBuilder()
                .setStrategy(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .setPrices(prices)
                .run();
        
        Assert.assertEquals(result.totalReturn().orElse(0.0), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }
    
    @Test
    public void sp500PlusGoldAlloc() throws FileNotFoundException {
        // Load prices from the CSV file
        var priceReader = new CSVPriceSeriesReader(new FileReader(sp500GoldMonthlyCSV));
        PriceSeries prices = priceReader.readPrices();
        
        // Define a constant allocation portfolio
        var fixedAllocation = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        // Chain the strategies so that the fixed allocation is rebalanced every year
        var rebalancedStrategy = new TimedStrategy(ChronoUnit.YEARS).chainTo(fixedAllocation); 
        
        // Create a new backtest with the strategy and the price data
        var backtest = new Backtest(rebalancedStrategy, prices);

        // Run the backtest
        var result = backtest.run();
        
        Assert.assertEquals(result.totalReturn().orElse(0.0), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }    
}
