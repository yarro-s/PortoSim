package money.portosim.core.containers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import money.portosim.Metrics;
import org.testng.Assert;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class QuoteSeriesTest {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
    @Test
    public void volatilityCalc() {
        var qs = new QuoteSeries();
        
        qs.put("2010-01-01", new Quote(Map.of("A", 100.0, "B", 5*100.0)));
        qs.put("2010-02-01", new Quote(Map.of("A", 120.0, "B", 5*120.0)));
        qs.put("2010-03-01", new Quote(Map.of("A", 90.0, "B", 5*90.0)));
        qs.put("2010-04-01", new Quote(Map.of("A", 135.5, "B", 5*135.5)));

        var actualVolatility = qs.quant().volatility();

        var expAverage = (100.0 + 120.0 + 90.0 + 135.5) / 4.0;
        var expVolatility = Math.sqrt((Math.pow(100.0 - expAverage, 2.0) 
                + Math.pow(120.0 - expAverage, 2.0) + Math.pow(90.0 - expAverage, 2.0) 
                + Math.pow(135.5 - expAverage, 2.0)) / 4.0);
        
        Assert.assertEquals(actualVolatility.get("A"), expVolatility);    
        Assert.assertEquals(actualVolatility.get("B"), 5 * expVolatility);
    }
    
    @Test
    public void rollingWindowAverage() {
        var qs = new QuoteSeries();
        
        qs.put("2010-01-01", new Quote(Map.of("A", 100.0, "B", 5*100.0)));
        qs.put("2010-02-01", new Quote(Map.of("A", 120.0, "B", 5*120.0)));
        qs.put("2010-03-01", new Quote(Map.of("A", 90.0, "B", 5*90.0)));
        qs.put("2010-04-01", new Quote(Map.of("A", 15.5, "B", 5*15.5)));
        qs.put("2010-05-01", new Quote(Map.of("A", 32.0, "B", 5*32.0)));
        qs.put("2010-06-01", new Quote(Map.of("A", 60.0, "B", 5*60.0)));
        qs.put("2010-07-01", new Quote(Map.of("A", 75.0, "B", 5*75.0)));

        var qsAverage = qs.rolling(3, (s) -> new QuoteSeries(s).quant().average());

        Assert.assertEquals(qsAverage.size(), qs.size() - 3 + 1);
        
        var avgVal = (100.0 + 120.0 + 90.0) / 3;
        Assert.assertEquals(qsAverage.get("2010-03-01").get("A"), avgVal, 0.001);
        Assert.assertEquals(qsAverage.get("2010-03-01").get("B"), 5*avgVal, 0.001);
        
        avgVal = (32.0 + 60.0 + 75.0) / 3;
        Assert.assertEquals(qsAverage.get("2010-07-01").get("A"), avgVal, 0.001);
        Assert.assertEquals(qsAverage.get("2010-07-01").get("B"), 5*avgVal, 0.001);
    }
    
    @Test
    public void combineSumQuotes() throws ParseException {
        var quoteT1 = new Quote(Map.of("SPY", 100.0, "QQQ", 1500.5));
        var quoteT2 = new Quote(Map.of("SPY", 510.0, "QQQ", 10000.0));   
        var quoteS = new QuoteSeries(Map.of(
                formatter.parse("2010-01-31"), quoteT1,
                formatter.parse("2011-01-31"), quoteT2
        ));

        var combineSum = quoteS.sum();
        
        Assert.assertEquals(combineSum, quoteT1.add(quoteT2));
    }
    
    @Test
    public void multQuoteSeries() throws ParseException {
        var quote1T1 = new Quote(Map.of("SPY", 100.0, "QQQ", 230.5));
        var quote1T2 = new Quote(Map.of("SPY", 110.4, "QQQ", 210.0));       
        var quoteS1 = new QuoteSeries(Map.of(
                formatter.parse("2010-01-31"), quote1T1,
                formatter.parse("2011-01-31"), quote1T2
        ));
        
        var quote2T1 = new Quote(Map.of("SPY", 10.8, "QQQ", 50.0));
        var quote2T2 = new Quote(Map.of("SPY", 4.2, "QQQ", 2.5));     
        var quoteS2 = new QuoteSeries(Map.of(
                formatter.parse("2010-01-31"), quote2T1,
                formatter.parse("2011-01-31"), quote2T2
        ));

        var multS12 = quoteS1.mult(quoteS2).add(quoteS2);
        
        Assert.assertEquals(multS12.size(), 2);
        Assert.assertEquals(multS12.get("2010-01-31"), quote1T1.mult(quote2T1).add(quote2T1));
        Assert.assertEquals(multS12.get("2011-01-31"), quote1T2.mult(quote2T2).add(quote2T2));
    }
    
    @Test
    public void addQuoteSeries() throws ParseException {        
        var quote1T1 = new Quote(Map.of("SPY", 100.0, "QQQ", 230.5));
        var quote1T2 = new Quote(Map.of("SPY", 110.4, "QQQ", 210.0));

        var quoteS1 = new QuoteSeries();

        quoteS1.put("2010-01-31", quote1T1);
        quoteS1.put("2011-01-31", quote1T2);

        Assert.assertEquals(quoteS1.size(), 2);
        Assert.assertEquals(quoteS1.keySet(), Set.of(formatter.parse("2010-01-31"),
                formatter.parse("2011-01-31")));
        Assert.assertEquals(quoteS1.firstEntry().getValue(), quote1T1);
        Assert.assertEquals(quoteS1.lastEntry().getValue(), quote1T2);
            
        var quote2T1 = new Quote(Map.of("SPY", 50.8, "QQQ", -30.0));
        var quote2T2 = new Quote(Map.of("SPY", 12.0, "QQQ", 45.0));

        var quoteS2 = new QuoteSeries();

        quoteS2.put("2010-01-31", quote2T1);
        quoteS2.put("2011-01-31", quote2T2);

        var sumS12 = quoteS1.add(quoteS2);
        
        Assert.assertEquals(sumS12.size(), 2);
        Assert.assertEquals(sumS12.keySet(), Set.of(formatter.parse("2010-01-31"),
                formatter.parse("2011-01-31")));
        Assert.assertEquals(sumS12.firstEntry().getValue(), quote1T1.add(quote2T1));
        Assert.assertEquals(sumS12.lastEntry().getValue(), quote1T2.add(quote2T2));
     }
}
