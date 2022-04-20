package money.portosim.core.strategies;

import money.portosim.Portfolio;
import money.portosim.containers.PriceMap;
import money.portosim.strategies.FixedAllocation;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import money.portosim.containers.generic.NumericMap;

import java.util.Map;

public class FixedAllocationTest {

    private NumericMap<String> targetAlloc;
    private PriceMap currentPrices;
    private PriceMap updatedPrices;

    @BeforeMethod
    public void setUp() {
        targetAlloc = new NumericMap<>(Map.of("A", 0.4, "B", 0.6));
        currentPrices = new PriceMap(Map.of("A", 10.0, "B", 60.0));
        updatedPrices = new PriceMap(Map.of("A", 200.0, "B", 350.0));
    }

    @Test
    public void multiAssetUpdateTest() throws CloneNotSupportedException {
        var fixedAlloc = new FixedAllocation(targetAlloc);

        var pf0 = (Portfolio) fixedAlloc.makePortfolio(null, currentPrices).clone();
        var pf1 = fixedAlloc.makePortfolio(null, updatedPrices);

        var pf0Value = pf0.valueAtPrice(currentPrices).orElse(0.0);
        var pf1Value = pf1.valueAtPrice(updatedPrices).orElse(0.0);

        Assert.assertTrue(pf1Value > pf0Value);

        var updValuesBeforeRebalance = pf0.positions().multiply(updatedPrices);
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
