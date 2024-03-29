package money.portosim.usecases;

import java.io.FileReader;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.Backtest;
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
    private final String spyPlusGold_10yr = "src/test/resources/spy_gld_2010_2020.csv";
    
    @Test
    public void sp500PlusGoldExtendedUseCases() throws Exception {
        // Load prices from a CSV file
        var prices = new CSVPriceSource(new FileReader(spyPlusGold_10yr));
        
        // Define a constant allocation portfolio
        var fixedAlloc = new FixedAllocation(Map.of("SPY", 0.7, "GLD", 0.3));
        
        // Build the backtest
        var result = Backtest.withStrategy(fixedAlloc)
                .setRebalancePeriod(ChronoUnit.YEARS)   // rebalance every year
                .run(prices);   // test on the historic prices
        
        double totalReturn = result.full().totalReturn();
        double cagr = result.setValuesPerRefPeriod(365).full().cummulativeGrowthRate();
        
        var volatility90Day = result.rolling(90).volatility();
        double max3MVolatility = volatility90Day.values().stream().mapToDouble(a->a).max().orElse(0.0);
        
        var sharpeYearly = result.setMeanRiskFreeRate(0.5 / 100)
                .setValuesPerRefPeriod(2).rolling(365).sharpeRatio();
        var minSharpeYearly = sharpeYearly.values().stream().mapToDouble(a->a).min().orElse(0.0);
        
        Assert.assertEquals(totalReturn, 2.326, 0.001);
        Assert.assertEquals(cagr, 0.1303, 0.0001);
        Assert.assertEquals(max3MVolatility, 75.3545, 0.001);
        Assert.assertEquals(minSharpeYearly, -1.777, 0.01);
    }
    
    @Test
    public void sp500PlusGoldSimpleBuild() throws Exception {
        // Load prices from a CSV file
        var prices = new CSVPriceSource(new FileReader(sp500GoldMonthlyCSV));
        
        // Then define a fixed allocation 70% stocks / 30% gold portfolio
        var fixedAlloc = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
        
        // Build a backtest with a rebalancing period of one year
        var result = Backtest.withStrategy(fixedAlloc)
                .setRebalancePeriod(ChronoUnit.YEARS)   // rebalance every year
                .run(prices);   // test on the historic prices
        
        Assert.assertEquals(result.full().totalReturn(), 1.2026, 0.00001);
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
        
        // Create a new backtest forPrices the strategy and the price data
        var backtest = Backtest.withStrategy(rebalancedStrategy).setPrices(prices);

        // Run the backtest
        var result = backtest.run();
        
        Assert.assertEquals(result.full().totalReturn(), 1.2026, 0.00001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }    
}
