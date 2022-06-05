/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.usecases;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.Backtest;
import money.portosim.BacktestBuilder;
import money.portosim.containers.readers.CSVQuoteSeriesReader;
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
    private final String spyGoldDailyCSV = "src/test/resources/spy_gld_2010_2020.csv";
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    @Test
    public void containerFeatures() throws FileNotFoundException, ParseException { 
        // Load prices from a CSV file
        var priceReader = new CSVQuoteSeriesReader(new FileReader(spyGoldDailyCSV));
        var prices = priceReader.readPrices();
        
        var priceSlice = prices.from("2015-01-02").to("2018-11-30");   // also accepts Date
       
        Assert.assertTrue(priceSlice.size() < prices.size());
        Assert.assertEquals(priceSlice.firstEntry().getKey(), formatter.parse("2015-01-02"));
        Assert.assertEquals(priceSlice.lastEntry().getKey(), formatter.parse("2018-11-30"));
    }
    
    @Test
    public void sp500PlusGoldSimpleBuild() throws FileNotFoundException {
        // Load prices from a CSV file
        var priceReader = new CSVQuoteSeriesReader(new FileReader(sp500GoldMonthlyCSV));
        var prices = priceReader.readPrices();
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        // Build a backtest
        var result = new BacktestBuilder(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)   // rebalance every year
                .run(prices);    // test on the historic prices
        
        Assert.assertEquals(result.totalReturn().orElse(0.0), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }
    
    @Test
    public void sp500PlusGoldAlloc() throws FileNotFoundException {
        // Load prices from the CSV file
        var priceReader = new CSVQuoteSeriesReader(new FileReader(sp500GoldMonthlyCSV));
        var prices = priceReader.readPrices();
        
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
