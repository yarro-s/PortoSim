package money.portosim.tests.core.strategies;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import money.portosim.Portfolio;
import money.portosim.strategies.FixedAllocation;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import money.portosim.containers.core.Pair;
import money.portosim.containers.numeric.NumFrame;
import money.portosim.containers.numeric.NumMatrix;

public class FixedAllocationTest {

    private NumFrame<String> targetAlloc;
    private NumFrame<String> currentPrices;
    private NumFrame<String> updatedPrices;

    @BeforeMethod
    public void setUp() {
        targetAlloc = NumFrame.of(Map.of("A", 0.4, "B", 0.6));
        currentPrices = NumFrame.of(Map.of("A", 10.0, "B", 60.0));
        updatedPrices = NumFrame.of(Map.of("A", 200.0, "B", 350.0));
    }
    
    @Test
    public void sp500GoldAllocTest() {
        var strategy = new FixedAllocation(Map.of("SP500", 0.6, "GOLD", 0.4)); 
        
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2018, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2019, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2020, Calendar.JANUARY, 31).getTime()
        };
        var prices = NumMatrix.of(Map.of(
            Pair.of(timePoints[0], "SP500"), 5511.21, Pair.of(timePoints[0], "GOLD"), 1343.35,
            Pair.of(timePoints[1], "SP500"), 5383.6299, Pair.of(timePoints[1], "GOLD"), 1322.5000,
            Pair.of(timePoints[2], "SP500"), 6551.0000, Pair.of(timePoints[2], "GOLD"), 1580.8500));
       
        var currPrice = prices.rows().get(timePoints[0]);
        var pf = strategy.makePortfolio(timePoints[0], currPrice);
        
        var sp500ExpAmount = 0.6 * 1000 / 5511.21; 
        var goldExpAmount = 0.4 * 1000 / 1343.35; 
        
        Assert.assertEquals(pf.positions().get("SP500"), sp500ExpAmount);
        Assert.assertEquals(pf.positions().get("GOLD"), goldExpAmount);
        Assert.assertEquals(pf.valueAtPrice(currPrice).orElse(0.0), 
                sp500ExpAmount * 5511.21 + goldExpAmount * 1343.35);
        
        currPrice = prices.rows().get(timePoints[1]);
        pf = strategy.makePortfolio(timePoints[1], currPrice);
        
        var pfValue = pf.valueAtPrice(currPrice).orElse(0.0);
        sp500ExpAmount = 0.6 * pfValue / 5383.6299; 
        goldExpAmount = 0.4 * pfValue / 1322.5000; 
        
        Assert.assertEquals(pf.positions().get("SP500"), sp500ExpAmount);
        Assert.assertEquals(pf.positions().get("GOLD"), goldExpAmount);
        Assert.assertEquals(pfValue, sp500ExpAmount * 5383.6299 + goldExpAmount * 1322.5000);
        
        currPrice = prices.rows().get(timePoints[2]);
        pf = strategy.makePortfolio(timePoints[2], currPrice);
        
        pfValue = pf.valueAtPrice(currPrice).orElse(0.0);
        sp500ExpAmount = 0.6 * pfValue / 6551.0000; 
        goldExpAmount = 0.4 * pfValue / 1580.8500; 
        
        Assert.assertEquals(pf.positions().get("SP500"), sp500ExpAmount);
        Assert.assertEquals(pf.positions().get("GOLD"), goldExpAmount);
        Assert.assertEquals(pfValue, sp500ExpAmount * 6551.0000 + goldExpAmount * 1580.8500);
    }

    @Test
    public void multiAssetUpdateTest() throws CloneNotSupportedException {
        var fixedAlloc = new FixedAllocation(targetAlloc);

        var pf0 = (Portfolio) fixedAlloc.makePortfolio(null, currentPrices).clone();
        var pf1 = fixedAlloc.makePortfolio(null, updatedPrices);

        var pf0Value = pf0.valueAtPrice(currentPrices).orElse(0.0);
        var pf1Value = pf1.valueAtPrice(updatedPrices).orElse(0.0);

        Assert.assertTrue(pf1Value > pf0Value);

        var updValuesBeforeRebalance = NumFrame.of(pf0.positions().mult(updatedPrices));
        Assert.assertEquals(pf1Value, updValuesBeforeRebalance.sum());

        var expUpdatedAmounts = Map.of("A", 0.4 * pf1Value / 200.0,
                "B", 0.6 * pf1Value / 350.0);
        Assert.assertEquals(pf1.positions(), expUpdatedAmounts);
    }

    @Test
    public void multiAssetInvariantUpdateTest() throws CloneNotSupportedException {
        var fixedAlloc = new FixedAllocation(targetAlloc);

        var pf0 = (Portfolio) fixedAlloc.makePortfolio(null, currentPrices).clone();
        var pf1 = fixedAlloc.makePortfolio(null, currentPrices);

        var pf0Value = pf0.valueAtPrice(currentPrices).orElse(0.0);
        var pf1Value = pf1.valueAtPrice(currentPrices).orElse(0.0);

        Assert.assertEquals(pf1Value, pf0Value);
    }

    @Test
    public void multiAssetInitTest() {
        var fixedAlloc = new FixedAllocation(targetAlloc);

        var pf = fixedAlloc.makePortfolio(null, currentPrices);

        var pfValue = pf.valueAtPrice(currentPrices).orElse(0.0);
        Assert.assertTrue(pfValue > 0.0);

        var expAmounts = Map.of("A", 0.4 * pfValue / 10.0,
                "B", 0.6 * pfValue / 60.0);
        Assert.assertEquals(pf.positions(), expAmounts);
    }
}
