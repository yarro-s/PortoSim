package money.portosim.tests.core;

import java.time.temporal.ChronoUnit;
import money.portosim.Backtest;
import org.testng.Assert;
import org.testng.annotations.Test;
import money.portosim.strategies.ConstantAllocation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import money.portosim.containers.core.Pair;
import money.portosim.containers.numeric.NumMatrix;
import money.portosim.containers.numeric.NumFrame;
import money.portosim.tests.helpers.SpecifiedAllocation;
import money.portosim.tests.helpers.SpecifiedMultiAllocation;
import money.portosim.strategies.FixedAllocation;
import money.portosim.strategies.TimedStrategy;

public class BacktestTest {
    
    @Test
    public void sp500GoldScaledTimedAllocTest() {       
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2018, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2019, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2020, Calendar.JANUARY, 31).getTime()
        };
        var prices = NumMatrix.of(Map.of(
            Pair.of(timePoints[0], "SP500"), 1000.0, Pair.of(timePoints[0], "GOLD"), 1000.0,
            Pair.of(timePoints[1], "SP500"), 9575.0, Pair.of(timePoints[1], "GOLD"), 9772.0,
            Pair.of(timePoints[2], "SP500"), 1141.0, Pair.of(timePoints[2], "GOLD"), 1169.0
        ));
        
        var strategy = new TimedStrategy(ChronoUnit.YEARS);
        strategy.chainTo(new FixedAllocation(Map.of("SP500", 0.7, "GOLD", 0.3))); 
        
        var backtest = new Backtest(strategy, prices);

        backtest.run();

        var result = backtest.getResult();
        
        Assert.assertEquals(result.quant().totalReturn(), 1149.86 / 1000.0, 0.001);
    }
    
    @Test
    public void sp500GoldScaledAllocTest() {
        var strategy = new FixedAllocation(Map.of("SP500", 0.6, "GOLD", 0.4)); 
        
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2018, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2019, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2020, Calendar.JANUARY, 31).getTime()
        };
        var prices = NumMatrix.of(Map.of(
            Pair.of(timePoints[0], "SP500"), 1000.0, Pair.of(timePoints[0], "GOLD"), 1000.0,
            Pair.of(timePoints[1], "SP500"), 957.538, Pair.of(timePoints[1], "GOLD"), 977.282,
            Pair.of(timePoints[2], "SP500"), 1141.291, Pair.of(timePoints[2], "GOLD"), 1169.839
        ));
        
        var backtest = new Backtest(strategy, prices);

        backtest.run();

        var result = backtest.getResult();
        
        Assert.assertEquals(result.quant().totalReturn(), 1152.710 / 1000.0, 0.001);
    }
    
    @Test
    public void sp500ScaledAllocTest() {
        var strategy = new FixedAllocation(Map.of("SP500", 1.0)); 
        
        final Date[] timePoints = new Date[]{
            new GregorianCalendar(2018, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2019, Calendar.JANUARY, 31).getTime(),
            new GregorianCalendar(2020, Calendar.JANUARY, 31).getTime()
        };
        var prices = NumMatrix.of(Map.of(
            Pair.of(timePoints[0], "SP500"), 1000.0,
            Pair.of(timePoints[1], "SP500"), 957.538, 
            Pair.of(timePoints[2], "SP500"), 1141.291 
        ));
        
        var backtest = new Backtest(strategy, prices);

        backtest.run();

        var result = backtest.getResult();
        
        Assert.assertEquals(result.quant().totalReturn(), 1141.291 / 1000.0);
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
        final Map<String, Double>[] priceMaps = new Map[]{
            Map.of("A", 125.0, "B", 50.0),
            Map.of("A", 500.0, "B", 10.0),
            Map.of("A", 200.0, "B", 80.0),
            Map.of("A", 20.0, "B", 150.0),
            Map.of("A", 350.0, "B", 250.0),
            Map.of("A", 5.0, "B", 125.0)
        };
        var prices = NumMatrix.of(Map.of(
            Pair.of(timePoints[0], "A"), 125.0, Pair.of(timePoints[0], "B"), 50.0,
            Pair.of(timePoints[1], "A"), 500.0, Pair.of(timePoints[1], "B"), 10.0,
            Pair.of(timePoints[2], "A"), 200.0, Pair.of(timePoints[2], "B"), 80.0,
            Pair.of(timePoints[3], "A"), 20.0, Pair.of(timePoints[3], "B"), 150.0,
            Pair.of(timePoints[4], "A"), 350.0, Pair.of(timePoints[4], "B"), 250.0));
        
        final List<Double> weightsA = List.of(0.5, 0.9, 0.4);
        final List<Double> weightsB = List.of(0.5, 0.1, 0.6);
              
        var multiAlloc = new SpecifiedMultiAllocation(Map.of("A", weightsA, "B", weightsB));
        
        var timedAlloc = new TimedStrategy(ChronoUnit.YEARS);
        timedAlloc.chainTo(multiAlloc); 
        
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
        final NumFrame<String>[] priceMaps = new NumFrame[]{
                NumFrame.of(Map.of("A", 125.0)),
                NumFrame.of(Map.of("A", 500.0)),
                NumFrame.of(Map.of("A", 200.0)),
                NumFrame.of(Map.of("A", 20.0)),
                NumFrame.of(Map.of("A", 350.0)),
                NumFrame.of(Map.of("A", 5.0))
        };
        var prices = NumMatrix.of(Map.of(
                Pair.of(timePoints[0], "A"), 125.0,
                Pair.of(timePoints[1], "A"), 500.0,
                Pair.of(timePoints[2], "A"), 200.0,
                Pair.of(timePoints[3], "A"), 20.0,
                Pair.of(timePoints[4], "A"), 350.0,
                Pair.of(timePoints[5], "A"), 5.0));
        
        final double[] weights = new double[] {0.5, 0.9, 0.4};
        
        var s = new SpecifiedAllocation("A", weights);
        var timedAlloc = new TimedStrategy(ChronoUnit.YEARS);
        timedAlloc.chainTo(s); 
        
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
        var priceMaps = new NumFrame[]{
                NumFrame.of(Map.of("A", 125.0)),
                NumFrame.of(Map.of("A", 80.0)),
                NumFrame.of(Map.of("A", 260.0))
        };
        var prices = NumMatrix.of(Map.of(
                Pair.of(timePoints[0], "A"), 125.0,
                Pair.of(timePoints[1], "A"), 80.0,
                Pair.of(timePoints[2], "A"), 260.0));

        var assetAmounts = NumFrame.of(Map.of("A", 24.0));

        var constAlloc = new ConstantAllocation(assetAmounts);
        var backtest = new Backtest(constAlloc, prices);

        backtest.run();

        var result = backtest.getResult();

        var expValue0 = assetAmounts.mult(priceMaps[0]).sum();
        var expValueN = assetAmounts.mult(priceMaps[priceMaps.length-1]).sum();
        var expTotalReturn = expValueN / expValue0;

        Assert.assertEquals(result.quant().totalReturn(), expTotalReturn);
    }

    @Test
    public void constantAllocMultiAssetTest() {
        Date[] timePoints = new Date[]{
                new GregorianCalendar(2005, Calendar.DECEMBER, 31).getTime(),
                new GregorianCalendar(2006, Calendar.DECEMBER, 31).getTime(),
                new GregorianCalendar(2007, Calendar.DECEMBER, 31).getTime()
        };
        var priceMaps = new NumFrame[]{
                NumFrame.of(Map.of("A", 50.0, "B", 120.0)),
                NumFrame.of(Map.of("A", 20.0, "B", 200.0)),
                NumFrame.of(Map.of("A", 80.0, "B", 150.0))
        };
        var prices = NumMatrix.of(Map.of(
                Pair.of(timePoints[0], "A"), 50.0, Pair.of(timePoints[0], "B"), 120.0,
                Pair.of(timePoints[1], "A"), 20.0, Pair.of(timePoints[1], "B"), 200.0,
                Pair.of(timePoints[2], "A"), 80.0, Pair.of(timePoints[2], "B"), 150.0
        ));

        NumFrame<String> assetAmounts = NumFrame.of(Map.of("A", 10.0, "B", 25.0));

        var constAlloc = new ConstantAllocation(assetAmounts);
        var backtest = new Backtest(constAlloc, prices);

        backtest.run();

        var result = backtest.getResult();

        var expValue0 = NumFrame.of(assetAmounts.mult(priceMaps[0])).sum();
        var expValueN = NumFrame.of(assetAmounts.mult(priceMaps[priceMaps.length-1])).sum();
        var expTotalReturn = expValueN / expValue0;

        Assert.assertEquals(result.quant().totalReturn(), expTotalReturn);
    }
}
