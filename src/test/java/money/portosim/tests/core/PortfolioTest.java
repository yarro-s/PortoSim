package money.portosim.tests.core;

import money.portosim.Portfolio;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import money.portosim.containers.numeric.NumFrame;

public class PortfolioTest {

    @Test
    public void portfolioValueTest() {
        var p1 = new Portfolio();

        p1.positions().put("AAPL", 25.3);   // put shares
        p1.positions().put("T", 500.1);

        var currentPrices = NumFrame.of(Map.of("AAPL", 515.5, "T", 650.2));

        var currentValue = p1.valueAtPrice(currentPrices);

        Assert.assertTrue(currentValue.isPresent());
        Assert.assertEquals(currentValue.getAsDouble(), 25.3 * 515.5 + 500.1 * 650.2, .01);
    }

    @Test
    public void basicPortfolioTest() {
        var p1 = new Portfolio();

        p1.positions().put("IBM", 1000.0);   // put shares of IBM
        p1.positions().put("XOM", 50.5);   // put shares of XOM

        var d1 = new Portfolio();

        d1.positions().put("IBM", -300.0);   // remove shares of IBM
        d1.positions().put("XOM", 10.5);   // add some more shares of XOM
        d1.positions().put("T", 200.0);   // add share of T

        var pSum = p1.positions().add(d1.positions());

        Assert.assertEquals(pSum.get("IBM"), 1000.0 - 300);
        Assert.assertEquals(pSum.get("XOM"), 50.5 + 10.5);

        p1.positions().put("T", 0.0);   // make the asset known to the portfolio

        pSum = p1.positions().add(d1.positions());
        
        Assert.assertEquals(pSum.get("IBM"), 1000.0 - 300);
        Assert.assertEquals(pSum.get("XOM"), 50.5 + 10.5);
        Assert.assertEquals(pSum.get("T"), 200.0);
    }
}
