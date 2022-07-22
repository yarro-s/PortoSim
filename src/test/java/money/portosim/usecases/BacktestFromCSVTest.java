package money.portosim.usecases;

import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.NavigableMap;
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
    private final String sp500DailyCSV_10yr = "src/test/resources/spy_gld_2010_2020.csv";

    @Test
    public void periodStartEndSpecified() throws Exception {    
        var prices = new CSVPriceSource(new FileReader(sp500DailyCSV_10yr));
        var startDate = new GregorianCalendar(2012, Calendar.JANUARY, 31).getTime();
        var endDate = new GregorianCalendar(2017, Calendar.JANUARY, 31).getTime();
        var ticker = "SPY";
        
        var backtestStartSet = Backtest.withStrategy(new ConstantAllocation(Map.of(ticker, 1.0)))
                .setPrices(prices)
                .setStart(startDate)
                .setEnd(endDate);
        backtestStartSet.run();

        var result = backtestStartSet.getResult();
        var pfHist = backtestStartSet.getResult().getPortfolioHistory();
        
        var priceSlice = new TreeMap<>(prices).subMap(startDate, true, endDate, true);
        
        var expTotalReturn = NumericMap.of(priceSlice.lastEntry().getValue())
                .div(NumericMap.of(priceSlice.firstEntry().getValue()))
                .getOrDefault(ticker, 0.0);

        Assert.assertEquals(result.apply(Metrics::totalReturn), expTotalReturn, 10e-6);
        Assert.assertEquals(pfHist.size(), priceSlice.size());
    }
    
    @Test
    public void periodStartSpecified() throws Exception {    
        var prices = new CSVPriceSource(new FileReader(sp500DailyCSV_10yr));
        var startDate = new GregorianCalendar(2012, Calendar.JANUARY, 31).getTime();
        var ticker = "SPY";
        
        var backtestStartSet = Backtest.withStrategy(new ConstantAllocation(Map.of(ticker, 1.0)))
                .setPrices(prices)
                .setStart(startDate);
        backtestStartSet.run();

        var result = backtestStartSet.getResult();
        var pfHist = backtestStartSet.getResult().getPortfolioHistory();
        
        NavigableMap<Date, Map<String, Double>> priceSlice = new TreeMap<>(prices);
        var endDate = priceSlice.lastKey();
        priceSlice = priceSlice.subMap(startDate, true, endDate, true);
        
        var expTotalReturn = NumericMap.of(priceSlice.lastEntry().getValue())
                .div(NumericMap.of(priceSlice.firstEntry().getValue()))
                .getOrDefault(ticker, 0.0);

        Assert.assertEquals(result.apply(Metrics::totalReturn), expTotalReturn, 10e-6);
        Assert.assertEquals(pfHist.size(), priceSlice.size());
    }
    
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

        Assert.assertEquals(result.apply(Metrics::totalReturn), expTotalReturn, 0.0001);
        Assert.assertEquals(pfHist.size(), prices.size());
    }
}
