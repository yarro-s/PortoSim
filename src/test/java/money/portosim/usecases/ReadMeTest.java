/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.usecases;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import money.portosim.Backtest;
import money.portosim.containers.PriceSeries;
import money.portosim.containers.generic.NumericMap;
import money.portosim.containers.readers.CSVPriceSeriesReader;
import money.portosim.strategies.ConstantAllocation;
import money.portosim.strategies.FixedAllocation;
import money.portosim.strategies.TimedStrategy;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class ReadMeTest {
    private String sp500GoldMonthlyCSV = "src/test/resources/sp500_gold_3yr_monthly.csv";

    @Test
    public void sp500PlusGoldAlloc() throws FileNotFoundException {
        var priceReader = new CSVPriceSeriesReader(new FileReader(sp500GoldMonthlyCSV));
        PriceSeries prices = priceReader.readPrices();
        
        var myStrategy = new TimedStrategy(ChronoUnit.YEARS);
        myStrategy.setNextStrategy(new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3))); 
        
        var backtest = new Backtest(myStrategy, prices);

        var result = backtest.run();
        
        Assert.assertEquals(result.totalReturn().orElse(0.0), 1.2026, 0.0001);
        Assert.assertEquals(result.getPortfolioHistory().size(), prices.size());
    }
}
