/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.BacktestBuilder;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.readers.CSVQuoteSeriesReader;
import money.portosim.strategies.FixedAllocation;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class BacktestBuilderTest {
    /**
     * Test of run method, of class BacktestBuilder.
     */
    private final String sp500GoldMonthlyCSV = "src/test/resources/sp500_gold_3yr_monthly.csv";
    
    @Test
    public void sp500PlusGoldConvenienceMethods() throws FileNotFoundException {
        // Load prices from the CSV file
        var priceReader = new CSVQuoteSeriesReader(new FileReader(sp500GoldMonthlyCSV));
        QuoteSeries prices = new QuoteSeries(priceReader.readPrices());
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        var resultStrategyInitRunPrices = new BacktestBuilder(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .run(prices);
        
        Assert.assertEquals(resultStrategyInitRunPrices.totalReturn().orElse(0.0), 1.2026, 0.0001);
        Assert.assertEquals(resultStrategyInitRunPrices.getPortfolioHistory().size(), prices.size());
        
        var resultPricesInitRunStrategy = new BacktestBuilder(prices)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .run(myStrategy);
        
        Assert.assertEquals(resultPricesInitRunStrategy.totalReturn().orElse(0.0), 1.2026, 0.0001);
        Assert.assertEquals(resultPricesInitRunStrategy.getPortfolioHistory().size(), prices.size());  
        
        var resultRebalanceInitRunPrices = new BacktestBuilder(ChronoUnit.YEARS)
                .setStrategy(myStrategy)
                .run(prices);
        
        Assert.assertEquals(resultRebalanceInitRunPrices.totalReturn().orElse(0.0), 1.2026, 0.0001);
        Assert.assertEquals(resultRebalanceInitRunPrices.getPortfolioHistory().size(), prices.size());
    }
    
    @Test
    public void sp500PlusGoldBuilding() throws FileNotFoundException {
        // Load prices from the CSV file
        var priceReader = new CSVQuoteSeriesReader(new FileReader(sp500GoldMonthlyCSV));
        QuoteSeries prices = priceReader.readPrices();
        
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
    
}
