package money.portosim.core.containers;

import java.util.Map;
import java.util.Set;
import money.portosim.containers.NumericMap;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class NumericMapTest {
    
    @Test
    public void addQuoteToNumericMaps() {
        var nm1 = new NumericMap<String>(Map.of("A", 100.5, "B", 800.0));
        var quote2 = NumericMap.of(Map.of("A", 5.0, "B", 100.0));       

        var sum12 = nm1.add(quote2);

        Assert.assertEquals(sum12.keySet(), Set.of("A", "B"));
        Assert.assertEquals(sum12.get("A"), 100.5 + 5.0);
        Assert.assertEquals(sum12.get("B"), 800.0 + 100.0);
        
        
        var nm3 = new NumericMap<String>(Map.of("A", 5.0, "B", 2.5));
        
        var res123 = nm1.add(quote2).mult(nm3);

        Assert.assertEquals(res123.get("A"), (100.5 + 5.0) * 5.0);
        Assert.assertEquals(res123.get("B"), (800.0 + 100.0) * 2.5);
    }
    
    @Test
    public void chainOpNumericMaps() {
        var nm1 = new NumericMap<String>(Map.of("A", 10.0, "B", 9000.0));
        var nm2 = new NumericMap<String>(Map.of("A", -100.0, "B", 50000.0));
        var nm3 = new NumericMap<String>(Map.of("A", 7.0, "B", 30.0));       

        var res123 = nm1.add(nm2).mult(nm3);

        Assert.assertEquals(res123.keySet(), Set.of("A", "B"));
        Assert.assertEquals(res123.get("A"), (10.0 + -100.0) * 7.0);
        Assert.assertEquals(res123.get("B"), (9000.0 + 50000.0) * 30.0);
    }
    
    @Test
    public void sumNumericMaps() {
        var nm1 = new NumericMap<String>(Map.of("A", 10.0, "B", 9000.0));
        var nm2 = new NumericMap<String>(Map.of("A", -100.0, "B", 50000.0));

        var sum12 = nm1.add(nm2);

        Assert.assertEquals(sum12.keySet(), Set.of("A", "B"));
        Assert.assertEquals(sum12.get("A"), 10.0 + -100.0);
        Assert.assertEquals(sum12.get("B"), 9000.0 + 50000.0);
    }
    
    @Test
    public void addVal() {
        var quote = NumericMap.of(Map.of("SPY", 100.0, "QQQ", 2000.5));
        var d = 10645.4;
        
        var sum = quote.add(d);
        
        Assert.assertEquals(sum.get("SPY"), 100.0 + d, 10e-6);
        Assert.assertEquals(sum.get("QQQ"), 2000.5 + d, 10e-6);       
    }

    @Test
    public void combineSumQuote() {
        var quote = NumericMap.of(Map.of("SPY", 100.0, "QQQ", 2000.5));

        var combineSum = quote.sum();
        
        Assert.assertEquals(combineSum, 100.0 + 2000.5, 10e-6);
    }
    
    @Test
    public void multSingleQuotes() {
        var quote1 = NumericMap.of(Map.of("SPY", 100.0));
        var quote2 = NumericMap.of(Map.of("SPY", 50.0));

        var mult12 = quote1.mult(quote2).add(quote2);
        
        Assert.assertEquals(mult12.get("SPY"), 5050.0, 10e-6);
    }

    @Test
    public void sumMultipleQuotes() {
        var quote1 = NumericMap.of(Map.of("SPY", 100.0, "QQQ", 230.0));
        var quote2 = NumericMap.of(Map.of("SPY", 50.0, "QQQ", -10.0));

        var sum12 = quote1.add(quote2);

        Assert.assertEquals(sum12.size(), 2);
        Assert.assertEquals(sum12.get("SPY"), 150.0, 10e-6);
        Assert.assertEquals(sum12.get("QQQ"), 220.0, 10e-6);

        var otherQuote = NumericMap.of(Map.of("SPY", 25.0, "GLD", 122.5));
        var sumOther = quote1.add(otherQuote);

        Assert.assertEquals(sumOther.size(), 1);
        Assert.assertEquals(sumOther.get("SPY"), 125.0, 10e-6);
        Assert.assertNull(sumOther.get("GLD"));
    }

    @Test
    public void sumSingleQuotes() {
        var quote1 = NumericMap.of(Map.of("SPY", 100.0));
        var quote2 = NumericMap.of(Map.of("SPY", 50.0));

        var sum12 = quote1.add(quote2);

        Assert.assertEquals(sum12.size(), 1);
        Assert.assertEquals(sum12.keySet(), Set.of("SPY"));
        Assert.assertTrue(sum12.values().contains(150.0));

        var otherQuote = NumericMap.of(Map.of("GLD", 75.2));
        var sumOther = quote1.add(otherQuote);

        Assert.assertTrue(sumOther.isEmpty());
    }
}
