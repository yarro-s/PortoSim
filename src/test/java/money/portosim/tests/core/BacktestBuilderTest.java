/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.tests.core;

import java.io.FileReader;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.BacktestBuilder;
import money.portosim.containers.sources.NumMatrixCSVSource;
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
    public void sp500PlusGoldConvenienceMethods() throws Exception {
        // Load prices from a CSV file
        var prices = new NumMatrixCSVSource(new FileReader(sp500GoldMonthlyCSV));
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        var resStratInitRunPrices = new BacktestBuilder(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .run(prices);
        
        Assert.assertEquals(resStratInitRunPrices.quant().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(resStratInitRunPrices.getPortfolioHistory().size(), 
                prices.rows().size());
        
        var resPricesInitRunStrategy = new BacktestBuilder(prices)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .run(myStrategy);
        
        Assert.assertEquals(resPricesInitRunStrategy.quant().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(resPricesInitRunStrategy.getPortfolioHistory().size(), 
                prices.rows().size());  
        
        var resRebalanceInitRunPrices = new BacktestBuilder(ChronoUnit.YEARS)
                .setStrategy(myStrategy)
                .run(prices);
        
        Assert.assertEquals(resRebalanceInitRunPrices.quant().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(resRebalanceInitRunPrices.getPortfolioHistory().size(), 
                prices.rows().size());
    }
    
    @Test
    public void sp500PlusGoldBuilding() throws Exception {
        // Load prices from the CSV file
        var prices = new NumMatrixCSVSource(new FileReader(sp500GoldMonthlyCSV));
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        var result = new BacktestBuilder()
                .setStrategy(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .setPrices(prices)
                .run();
        
        Assert.assertEquals(result.quant().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.rows().size());
    }
    
}
