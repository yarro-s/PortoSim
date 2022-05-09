package money.portosim.core;

import java.time.temporal.ChronoUnit;
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
import java.util.List;
import java.util.Map;
import money.portosim.helpers.SpecifiedAllocation;
import money.portosim.helpers.SpecifiedMultiAllocation;
import money.portosim.strategies.FixedAllocation;
import money.portosim.strategies.TimedStrategy;

public class BacktestTest {
    private String csvDataSourcePath = "src/test/resources/simple.csv";
    
    @Test
    public void sp500GoldScaledTimedAllocTest() {       
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2018, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2019, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2020, Calendar.JANUARY, 31).getTime()
        };
        var prices = new PriceSeries(Map.of(
            timePoints[0], new PriceMap(Map.of("SP500", 1000.0, "GOLD", 1000.0)),
            timePoints[1], new PriceMap(Map.of("SP500", 9575.0, "GOLD", 9772.0)),
            timePoints[2], new PriceMap(Map.of("SP500", 1141.0, "GOLD", 1169.0))
        ));
        
        var strategy = new TimedStrategy(ChronoUnit.YEARS);
        strategy.setNextStrategy(new FixedAllocation(Map.of("SP500", 0.7, "GOLD", 0.3))); 
        
        var backtest = new Backtest(strategy, prices);

        backtest.run();

        var result = backtest.getResult();
        
        Assert.assertEquals(result.totalReturn().orElse(0), 1149.86 / 1000.0, 0.001);
    }
    
    @Test
    public void sp500GoldScaledAllocTest() {
        var strategy = new FixedAllocation(Map.of("SP500", 0.6, "GOLD", 0.4)); 
        
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2018, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2019, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2020, Calendar.JANUARY, 31).getTime()
        };
        var prices = new PriceSeries(Map.of(
            timePoints[0], new PriceMap(Map.of("SP500", 1000.0, "GOLD", 1000.0)),
            timePoints[1], new PriceMap(Map.of("SP500", 957.538, "GOLD", 977.282)),
            timePoints[2], new PriceMap(Map.of("SP500", 1141.291, "GOLD", 1169.839))
        ));
        
        var backtest = new Backtest(strategy, prices);

        backtest.run();

        var result = backtest.getResult();
        
        Assert.assertEquals(result.totalReturn().orElse(0), 1152.710 / 1000.0, 0.001);
    }
    
    @Test
    public void sp500ScaledAllocTest() {
        var strategy = new FixedAllocation(Map.of("SP500", 1.0)); 
        
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2018, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2019, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2020, Calendar.JANUARY, 31).getTime()
        };
        var prices = new PriceSeries(Map.of(
            timePoints[0], new PriceMap(Map.of("SP500", 1000.0)),
            timePoints[1], new PriceMap(Map.of("SP500", 957.538)),
            timePoints[2], new PriceMap(Map.of("SP500", 1141.291))
        ));
        
        var backtest = new Backtest(strategy, prices);

        backtest.run();

        var result = backtest.getResult();
        
        Assert.assertEquals(result.totalReturn().orElse(0), 1141.291 / 1000.0);
    }
    
    @Test
    public void fixedAllocMultiAssetTest() {
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2004, Calendar.JUNE, 31).getTime(),
            new GregorianCalendar(2004, Calendar.DECEMBER, 30).getTime(),
            new GregorianCalendar(2005, Calendar.JUNE, 31).getTime(),
            new GregorianCalendar(2005, Calendar.DECEMBER, 30).getTime(),
            new GregorianCalendar(2006, Calendar.JUNE, 31).getTime(),
            new GregorianCalendar(2006, Calendar.DECEMBER, 30).getTime()
        };
        final PriceMap[] priceMaps = new PriceMap[]{
            new PriceMap(Map.of("A", 125.0, "B", 50.0)),
            new PriceMap(Map.of("A", 500.0, "B", 10.0)),
            new PriceMap(Map.of("A", 200.0, "B", 80.0)),
            new PriceMap(Map.of("A", 20.0, "B", 150.0)),
            new PriceMap(Map.of("A", 350.0, "B", 250.0)),
            new PriceMap(Map.of("A", 5.0, "B", 125.0))
        };
        final PriceSeries prices = new PriceSeries(Map.of(
            timePoints[0], priceMaps[0],
            timePoints[1], priceMaps[1],
            timePoints[2], priceMaps[2],
            timePoints[3], priceMaps[3],
            timePoints[4], priceMaps[4],
            timePoints[5], priceMaps[5])
        );
        final List<Double> weightsA = List.of(0.5, 0.9, 0.4);
        final List<Double> weightsB = List.of(0.5, 0.1, 0.6);
              
        var multiAlloc = new SpecifiedMultiAllocation(Map.of("A", weightsA, "B", weightsB));
        
        var timedAlloc = new TimedStrategy(ChronoUnit.YEARS);
        timedAlloc.setNextStrategy(multiAlloc); 
        
        var backtest = new Backtest(timedAlloc, prices);

        backtest.run();
        
        var valueHist = backtest.getResult().getValueHistory();
        
        Assert.assertEquals(valueHist.get(timePoints[0]), weightsA.get(0) * priceMaps[0].get("A") + 
                weightsB.get(0) * priceMaps[0].get("B"));
        Assert.assertEquals(valueHist.get(timePoints[1]), weightsA.get(0) * priceMaps[1].get("A") + 
                weightsB.get(0) * priceMaps[1].get("B"));
        Assert.assertEquals(valueHist.get(timePoints[2]), weightsA.get(1) * priceMaps[2].get("A") + 
                weightsB.get(1) * priceMaps[2].get("B"));
        Assert.assertEquals(valueHist.get(timePoints[3]), weightsA.get(1) * priceMaps[3].get("A") + 
                weightsB.get(1) * priceMaps[3].get("B"));
        Assert.assertEquals(valueHist.get(timePoints[4]), weightsA.get(2) * priceMaps[4].get("A") + 
                weightsB.get(2) * priceMaps[4].get("B"));
        Assert.assertEquals(valueHist.get(timePoints[5]), weightsA.get(2) * priceMaps[5].get("A") + 
                weightsB.get(2) * priceMaps[5].get("B"));
    }

    
    @Test
    public void fixedAllocSingleAssetTest() {
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2010, Calendar.JUNE, 31).getTime(),
            new GregorianCalendar(2010, Calendar.DECEMBER, 30).getTime(),
            new GregorianCalendar(2011, Calendar.JUNE, 31).getTime(),
            new GregorianCalendar(2011, Calendar.DECEMBER, 30).getTime(),
            new GregorianCalendar(2012, Calendar.JUNE, 31).getTime(),
            new GregorianCalendar(2012, Calendar.DECEMBER, 30).getTime()
        };
        final PriceMap[] priceMaps = new PriceMap[]{
            new PriceMap(Map.of("A", 125.0)),
            new PriceMap(Map.of("A", 500.0)),
            new PriceMap(Map.of("A", 200.0)),
            new PriceMap(Map.of("A", 20.0)),
            new PriceMap(Map.of("A", 350.0)),
            new PriceMap(Map.of("A", 5.0))
        };
        final PriceSeries prices = new PriceSeries(Map.of(
            timePoints[0], priceMaps[0],
            timePoints[1], priceMaps[1],
            timePoints[2], priceMaps[2],
            timePoints[3], priceMaps[3],
            timePoints[4], priceMaps[4],
            timePoints[5], priceMaps[5])
        );
        final double[] weights = new double[] {0.5, 0.9, 0.4};
        
        var s = new SpecifiedAllocation("A", weights);
        var timedAlloc = new TimedStrategy(ChronoUnit.YEARS);
        timedAlloc.setNextStrategy(s); 
        
        var backtest = new Backtest(timedAlloc, prices);

        backtest.run();
        
        var portfolioHist = backtest.getResult().getPortfolioHistory();
        
        Assert.assertEquals(portfolioHist.get(timePoints[0]).positions().get("A"), weights[0]);
        Assert.assertEquals(portfolioHist.get(timePoints[1]).positions().get("A"), weights[0]);
        Assert.assertEquals(portfolioHist.get(timePoints[2]).positions().get("A"), weights[1]);
        Assert.assertEquals(portfolioHist.get(timePoints[3]).positions().get("A"), weights[1]);
        Assert.assertEquals(portfolioHist.get(timePoints[4]).positions().get("A"), weights[2]);
        Assert.assertEquals(portfolioHist.get(timePoints[5]).positions().get("A"), weights[2]);
        
        var valueHist = backtest.getResult().getValueHistory();
        
        Assert.assertEquals(valueHist.get(timePoints[0]), weights[0] * priceMaps[0].get("A"));
        Assert.assertEquals(valueHist.get(timePoints[1]), weights[0] * priceMaps[1].get("A"));
        Assert.assertEquals(valueHist.get(timePoints[2]), weights[1] * priceMaps[2].get("A"));
        Assert.assertEquals(valueHist.get(timePoints[3]), weights[1] * priceMaps[3].get("A"));
        Assert.assertEquals(valueHist.get(timePoints[4]), weights[2] * priceMaps[4].get("A"));
        Assert.assertEquals(valueHist.get(timePoints[5]), weights[2] * priceMaps[5].get("A"));
    }

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
