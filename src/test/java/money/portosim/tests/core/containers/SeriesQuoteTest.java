package money.portosim.tests.core.containers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import money.portosim.Metrics;
import money.portosim.containers.NumericSeries;
import org.testng.Assert;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.SeriesQuote;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class SeriesQuoteTest {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
    @Test
    public void volatilityCalc() throws ParseException {
        var sq = new SeriesQuote();
        sq.put("A", new NumericSeries(Map.of(
                formatter.parse("2010-01-01"), 100.0,
                formatter.parse("2010-02-01"), 120.0,
                formatter.parse("2010-03-01"), 90.0,
                formatter.parse("2010-04-01"), 135.5
        )));
        sq.put("B", new NumericSeries(Map.of(
                formatter.parse("2010-01-01"), 5*100.0,
                formatter.parse("2010-02-01"), 5*120.0,
                formatter.parse("2010-03-01"), 5*90.0,
                formatter.parse("2010-04-01"), 5*135.5
        )));
        
        var actualVolatility = sq.quant().volatility();

        var expAverage = (100.0 + 120.0 + 90.0 + 135.5) / 4.0;
        var expVolatility = Math.sqrt((Math.pow(100.0 - expAverage, 2.0) 
                + Math.pow(120.0 - expAverage, 2.0) + Math.pow(90.0 - expAverage, 2.0) 
                + Math.pow(135.5 - expAverage, 2.0)) / 4.0);
        
        Assert.assertEquals(actualVolatility.get("A"), expVolatility);    
        Assert.assertEquals(actualVolatility.get("B"), 5 * expVolatility);
    }
    
    @Test
    public void rollingWindowAverage() throws ParseException {
        
        var sq = new SeriesQuote();
        sq.put("A", new NumericSeries(Map.of(
                formatter.parse("2010-01-01"), 100.0,
                formatter.parse("2010-02-01"), 120.0,
                formatter.parse("2010-03-01"), 90.0,
                formatter.parse("2010-04-01"), 15.5,
                formatter.parse("2010-05-01"), 32.0,
                formatter.parse("2010-06-01"), 60.0,
                formatter.parse("2010-07-01"), 75.0
        )));
        sq.put("B", new NumericSeries(Map.of(
                formatter.parse("2010-01-01"), 5*100.0,
                formatter.parse("2010-02-01"), 5*120.0,
                formatter.parse("2010-03-01"), 5*90.0,
                formatter.parse("2010-04-01"), 5*15.5,
                formatter.parse("2010-05-01"), 5*32.0,
                formatter.parse("2010-06-01"), 5*60.0,
                formatter.parse("2010-07-01"), 5*75.0
        )));

        var sqAverage = sq.rolling(3, (s) -> new NumericSeries(s).quant().average());
             
        var someSeries = sq.entrySet().iterator().next().getValue();
        var someAvgSeries = sqAverage.entrySet().iterator().next().getValue();

        Assert.assertEquals(someAvgSeries.size(), someSeries.size() - 3 + 1);
        
        var avgVal = (100.0 + 120.0 + 90.0) / 3;
        Assert.assertEquals(sqAverage.get("A").get("2010-03-01"), avgVal, 0.0001);
        Assert.assertEquals(sqAverage.get("B").get("2010-03-01"), 5*avgVal, 0.0001);
        
        avgVal = (32.0 + 60.0 + 75.0) / 3;
        Assert.assertEquals(sqAverage.get("A").get("2010-07-01"), avgVal, 0.0001);
        Assert.assertEquals(sqAverage.get("B").get("2010-07-01"), 5*avgVal, 0.0001);
    }
    
    @Test
    public void combineSumQuotes() throws ParseException {
        var seriesSPY = new NumericSeries(Map.of(
              formatter.parse("2010-01-31"), 100.0,
              formatter.parse("2011-01-31"), 510.0
        ));
        var seriesQQQ = new NumericSeries(Map.of(
              formatter.parse("2010-01-31"), 1500.5,
              formatter.parse("2011-01-31"), 10000.0
        ));
        var series = new SeriesQuote(Map.of("SPY", seriesSPY, "QQQ", seriesQQQ));
        
        var combineSum = series.sum();
        
        Assert.assertEquals(combineSum, seriesSPY.add(seriesQQQ));
    }
    
    @Test
    public void addSeriesQuote() throws ParseException {        
        var quote1T1 = new Quote(Map.of("SPY", 100.0, "QQQ", 230.5));
        var quote1T2 = new Quote(Map.of("SPY", 110.4, "QQQ", 210.0));

        var quoteS1 = new QuoteSeries();

        quoteS1.put("2010-01-31", quote1T1);
        quoteS1.put("2011-01-31", quote1T2);
        
        var seriesQuote1 = quoteS1.transpose();
        
        Assert.assertEquals(seriesQuote1.size(), 2);
        Assert.assertEquals(seriesQuote1.keySet(), Set.of("SPY", "QQQ"));
        
        Assert.assertEquals(seriesQuote1.get("SPY").firstEntry().getKey(), 
                formatter.parse("2010-01-31"));
        Assert.assertEquals(seriesQuote1.get("SPY").firstEntry().getValue(), 100.0);
        
        Assert.assertEquals(seriesQuote1.get("QQQ").lastEntry().getKey(), 
                formatter.parse("2011-01-31"));
        Assert.assertEquals(seriesQuote1.get("QQQ").lastEntry().getValue(), 210.0);
            
        var quote2T1 = new Quote(Map.of("SPY", 50.8, "QQQ", -30.0));
        var quote2T2 = new Quote(Map.of("SPY", 12.0, "QQQ", 45.0));

        var quoteS2 = new QuoteSeries();

        quoteS2.put("2010-01-31", quote2T1);
        quoteS2.put("2011-01-31", quote2T2);

        var seriesQuote2 = quoteS2.transpose();
        var sumS12 = seriesQuote1.add(seriesQuote2);
        
        Assert.assertEquals(sumS12.size(), 2);
        Assert.assertEquals(sumS12.keySet(), Set.of("SPY", "QQQ"));

        Assert.assertEquals(sumS12.get("SPY").firstEntry().getValue(), 100.0 + 50.8);
        Assert.assertEquals(sumS12.get("QQQ").lastEntry().getValue(), 210.0 + 45.0);
     }
}
