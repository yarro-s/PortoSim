package money.portosim.core;

import money.portosim.Backtest;
import money.portosim.containers.PriceMap;
import money.portosim.containers.PriceSeries;
import org.testng.Assert;
import org.testng.annotations.Test;
import money.portosim.containers.generic.NumericMap;
import money.portosim.strategies.ConstantAllocation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class BacktestTest {
    private String csvDataSourcePath = "src/test/resources/simple.csv";

    @Test
    public void constantAllocSingleAssetTest() {

        Date[] timePoints = new Date[]{
                new GregorianCalendar(2010, Calendar.DECEMBER, 31).getTime(),
                new GregorianCalendar(2011, Calendar.DECEMBER, 31).getTime(),
                new GregorianCalendar(2012, Calendar.DECEMBER, 31).getTime()
        };
        PriceMap[] priceMaps = new PriceMap[]{
                new PriceMap(Map.of("A", 125.0)),
                new PriceMap(Map.of("A", 80.0)),
                new PriceMap(Map.of("A", 260.0))
        };
        PriceSeries prices = new PriceSeries(Map.of(
                timePoints[0], priceMaps[0],
                timePoints[1], priceMaps[1],
                timePoints[2], priceMaps[2])
        );

        var assetAmounts = new NumericMap<>(Map.of("A", 24.0));

        var constAlloc = new ConstantAllocation(assetAmounts);
        var backtest = new Backtest(constAlloc, prices);

        backtest.run();

        var result = backtest.getResult();

        var expValue0 = assetAmounts.multiply(priceMaps[0]).sum();
        var expValueN = assetAmounts.multiply(priceMaps[priceMaps.length-1]).sum();
        var expTotalReturn = expValueN / expValue0;

        Assert.assertEquals(result.totalReturn().orElse(0.0), expTotalReturn);
    }

    @Test
    public void constantAllocMultiAssetTest() {
        Date[] timePoints = new Date[]{
                new GregorianCalendar(2005, Calendar.DECEMBER, 31).getTime(),
                new GregorianCalendar(2006, Calendar.DECEMBER, 31).getTime(),
                new GregorianCalendar(2007, Calendar.DECEMBER, 31).getTime()
        };
        PriceMap[] priceMaps = new PriceMap[]{
                new PriceMap(Map.of("A", 50.0, "B", 120.0)),
                new PriceMap(Map.of("A", 20.0, "B", 200.0)),
                new PriceMap(Map.of("A", 80.0, "B", 150.0))
        };
        PriceSeries prices = new PriceSeries(Map.of(
                timePoints[0], priceMaps[0],
                timePoints[1], priceMaps[1],
                timePoints[2], priceMaps[2])
        );

        var assetAmounts = new NumericMap<>(Map.of("A", 10.0, "B", 25.0));

        var constAlloc = new ConstantAllocation(assetAmounts);
        var backtest = new Backtest(constAlloc, prices);

        backtest.run();

        var result = backtest.getResult();

        var expValue0 = assetAmounts.multiply(priceMaps[0]).sum();
        var expValueN = assetAmounts.multiply(priceMaps[priceMaps.length-1]).sum();
        var expTotalReturn = expValueN / expValue0;

        Assert.assertEquals(result.totalReturn().orElse(0.0), expTotalReturn);
    }
}
