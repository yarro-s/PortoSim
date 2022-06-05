/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */

import java.util.Map;
import java.util.Set;
import org.testng.Assert;
import money.portosim.containers.Quote;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class QuoteTest {
    
    @Test
    public void addVal() {
        var quote = new Quote(Map.of("SPY", 100.0, "QQQ", 2000.5));
        var d = 10645.4;
        
        var sum = quote.add(d);
        
        Assert.assertEquals(sum.get("SPY"), 100.0 + d, 10e-6);
        Assert.assertEquals(sum.get("QQQ"), 2000.5 + d, 10e-6);       
    }

    @Test
    public void combineSumQuote() {
        var quote = new Quote(Map.of("SPY", 100.0, "QQQ", 2000.5));

        var combineSum = quote.sum();
        
        Assert.assertEquals(combineSum, 100.0 + 2000.5, 10e-6);
    }
    
    @Test
    public void multSingleQuotes() {
        var quote1 = new Quote(Map.of("SPY", 100.0));
        var quote2 = new Quote(Map.of("SPY", 50.0));

        var mult12 = quote1.mult(quote2).add(quote2);
        
        Assert.assertEquals(mult12.get("SPY"), 5050.0, 10e-6);
    }

    @Test
    public void sumMultipleQuotes() {
        var quote1 = new Quote(Map.of("SPY", 100.0, "QQQ", 230.0));
        var quote2 = new Quote(Map.of("SPY", 50.0, "QQQ", -10.0));

        var sum12 = quote1.add(quote2);

        Assert.assertEquals(sum12.size(), 2);
        Assert.assertEquals(sum12.get("SPY"), 150.0, 10e-6);
        Assert.assertEquals(sum12.get("QQQ"), 220.0, 10e-6);

        var otherQuote = new Quote(Map.of("SPY", 25.0, "GLD", 122.5));
        var sumOther = quote1.add(otherQuote);

        Assert.assertEquals(sumOther.size(), 1);
        Assert.assertEquals(sumOther.get("SPY"), 125.0, 10e-6);
        Assert.assertNull(sumOther.get("GLD"));
    }

    @Test
    public void sumSingleQuotes() {
        var quote1 = new Quote(Map.of("SPY", 100.0));
        var quote2 = new Quote(Map.of("SPY", 50.0));

        var sum12 = quote1.add(quote2);

        Assert.assertEquals(sum12.size(), 1);
        Assert.assertEquals(sum12.keySet(), Set.of("SPY"));
        Assert.assertTrue(sum12.values().contains(150.0));

        var otherQuote = new Quote(Map.of("GLD", 75.2));
        var sumOther = quote1.add(otherQuote);

        Assert.assertTrue(sumOther.isEmpty());
    }
}
