package money.portosim.core;

import java.io.FileReader;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.Backtest;
import money.portosim.containers.sources.CSVPriceSource;
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
        // Load prices from the CSV file
        var prices = new CSVPriceSource(new FileReader(sp500GoldMonthlyCSV));
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        var resStratInitRunPrices = Backtest.withStrategy(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .run(prices);
        
        Assert.assertEquals(resStratInitRunPrices.full().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(resStratInitRunPrices.getPortfolioHistory().size(), prices.size());
        
        var resPricesInitRunStrategy = Backtest.forPrices(prices)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .run(myStrategy);
        
        Assert.assertEquals(resPricesInitRunStrategy.full().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(resPricesInitRunStrategy.getPortfolioHistory().size(), prices.size());  
        
        var resRebalanceInitRunPrices = Backtest.rebalanceEvery(ChronoUnit.YEARS)
                .setStrategy(myStrategy)
                .run(prices);
        
        Assert.assertEquals(resRebalanceInitRunPrices.full().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(resRebalanceInitRunPrices.getPortfolioHistory().size(), prices.size());
    }
    
    @Test
    public void sp500PlusGoldBuilding() throws Exception {
        // Load prices from the CSV file
        var prices = new CSVPriceSource(new FileReader(sp500GoldMonthlyCSV));
        
        // Define a constant allocation portfolio
        var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        var result = Backtest.create()
                .setStrategy(myStrategy)
                .setRebalancePeriod(ChronoUnit.YEARS)
                .setPrices(prices)
                .run();
        
        Assert.assertEquals(result.full().totalReturn(), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }
    
}
