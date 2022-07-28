package money.portosim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.sources.CSVPriceSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class MetricsTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv"; 
    private final String spyPlusGold_10yr = "src/test/resources/spy_gld_2010_2020.csv";
    
    private Map<String, List<Double>> quoteSeries; 
    private Map<String, List<Double>> spyGldSeries; 
    
    @BeforeClass
    public void setup() throws FileNotFoundException, IOException {
        var simpleData = new CSVPriceSource(new FileReader(csvDataSourcePath)).entrySet();
        var simpleKeySet = simpleData.iterator().next().getValue().keySet();

        quoteSeries = simpleKeySet.stream().collect(Collectors.toMap(Function.identity(),
                key -> new ArrayList<>(new NumericSeries(simpleData.stream().collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(key)))).values())));
                
        var spyGldData = new CSVPriceSource(new FileReader(spyPlusGold_10yr)).entrySet();
        var spyGldSet = spyGldData.iterator().next().getValue().keySet();

        spyGldSeries = spyGldSet.stream().collect(Collectors.toMap(Function.identity(),
                key -> new ArrayList<>(new NumericSeries(spyGldData.stream().collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(key)))).values())));
    }
    
    @Test
    public void sharpeRatioSynthetic() {
        var assetPrice = List.of(90.0, 100.0, 120.0, 110.0, 90.0, 150.0, 130.0, 
                128.0, 180.0, 270.0, 220.0, 210.0, 300.0);
                
        var meanRiskFreeRate = 2.0 / 100;
        var actualSharpe = Metrics.sharpeRatio(assetPrice, meanRiskFreeRate, 4);
        
        Assert.assertEquals(actualSharpe, 1.7998, 0.0001);
    }
    
    @Test
    public void sharpeRatioReal() {
        var spy10Yr = spyGldSeries.get("SPY");
        
        var meanRiskFreeRate = Metrics.toReturns(spy10Yr, 365).stream()
                .mapToDouble(a->a).average().getAsDouble();
        var actualSharpe = Metrics.sharpeRatio(spy10Yr, meanRiskFreeRate, 365);
        
        Assert.assertEquals(actualSharpe, 0.0, 0.001);        
        
        meanRiskFreeRate = 0.5 / 100;
        actualSharpe = Metrics.sharpeRatio(spy10Yr, meanRiskFreeRate, 365);
        
        Assert.assertEquals(actualSharpe, 29.63, 0.01);
    }
    
    @Test
    public void noDrawdown() {
        var noDrawdown = List.of(10.5, 17.7, 17.7, 25.2);
        
        var actualDD = Metrics.maxDrawdown(noDrawdown);
        
        Assert.assertEquals(actualDD, 0.0);
    }
    
    @Test
    public void someMaxDrawdown() {
        var someDrawdownMiddle = List.of(17.5, 20.3, 5.2, 15.7);
        var someDrawdownEdges = List.of(20.5, 10.0, 15.1, 2.5);
        
        var actualMaxDDMiddle = Metrics.maxDrawdown(someDrawdownMiddle);
        var actualMaxDDEdges = Metrics.maxDrawdown(someDrawdownEdges);
        
        Assert.assertEquals(actualMaxDDMiddle, (5.2 - 20.3) / 20.3);
        Assert.assertEquals(actualMaxDDEdges, (2.5 - 20.5) / 20.5);
    }
    
    @Test
    public void excessReturns() {
        var valsA = quoteSeries.get("A");
        var baseRate = 0.15;
        
        var expExcessReturns = List.of(25.5 / 20.0 - 1, 23.0 / 25.5 - 1, 30.1 / 23.0 - 1)
                .stream().map(v -> v - baseRate).toList();
        
        Assert.assertEquals(Metrics.excessReturns(valsA, baseRate), expExcessReturns);
    }
    
    @Test
    public void toReturnsOverPeriod() {
        var seriesNOdd = List.of(25.0, 12.5, 30.2, 32.4, 45.6);
        
        var refPeriod = 2;      
        var expReturns = List.of(12.5 / 25.0 - 1, 30.2 / 12.5 - 1, 
                32.4 / 30.2 - 1, 45.6 / 32.4 - 1);
        
        Assert.assertEquals(Metrics.toReturns(seriesNOdd, refPeriod), expReturns);
        
        refPeriod = 3;
        expReturns = List.of(30.2 / 25.0 - 1, 32.4 / 12.5 - 1, 45.6 / 30.2 - 1);
        
        Assert.assertEquals(Metrics.toReturns(seriesNOdd, refPeriod), expReturns);    
        
        var seriesNEven = List.of(25.0, 12.5, 30.2, 32.4, 45.6, 23.5);
        refPeriod = 2;      
        expReturns = List.of(12.5 / 25.0 - 1, 30.2 / 12.5 - 1, 
                32.4 / 30.2 - 1, 45.6 / 32.4 - 1, 23.5 / 45.6 - 1);
        
        Assert.assertEquals(Metrics.toReturns(seriesNEven, refPeriod), expReturns);
        
        refPeriod = 3;
        expReturns = List.of(30.2 / 25.0 - 1, 32.4 / 12.5 - 1, 45.6 / 30.2 - 1, 
                23.5 / 32.4 - 1);
        
        Assert.assertEquals(Metrics.toReturns(seriesNEven, refPeriod), expReturns);
    }
    
    @Test
    public void toReturns() {
        var returnsA = Metrics.toReturns(quoteSeries.get("A"));
        
        var expReturns = List.of(25.5 / 20.0 - 1, 23.0 / 25.5 - 1, 30.1 / 23.0 - 1);
        
        Assert.assertEquals(returnsA, expReturns);
    }
    
    @Test
    public void stdDeviation() {
        var stdDevA = Metrics.stdDeviation(quoteSeries.get("A"));
        
        var expDev = Math.sqrt(Metrics.variance(quoteSeries.get("A")));
        
        Assert.assertEquals(stdDevA, expDev, 1e-6);
    }
    
    @Test
    public void seriesVariance() {
        var varianceA = Metrics.variance(quoteSeries.get("A"));
        
        var averageA = Metrics.average(quoteSeries.get("A"));
        var expVariance = (Math.pow(20.0 - averageA, 2.0) + Math.pow(25.5 - averageA, 2.0) + 
                Math.pow(23.0 - averageA, 2.0) + Math.pow(30.1 - averageA, 2.0)) / (4 - 1);
        
        Assert.assertEquals(varianceA, expVariance, 1e-6);
    }
    
    @Test
    public void seriesVolatility() {
        var volA = Metrics.volatility(quoteSeries.get("A"));
        var volB = Metrics.volatility(quoteSeries.get("B"));
        
        Assert.assertEquals(volA, 3.7003, 1e-3);
        Assert.assertEquals(volB, 0.9014, 1e-3);
    }
    
    @Test
    public void seriesAverage() {      
        var avgValA = Metrics.average(quoteSeries.get("A"));
        var avgValB = Metrics.average(quoteSeries.get("B"));
        
        Assert.assertEquals(avgValA, 24.65, 1e-3);
        Assert.assertEquals(avgValB, 10.75, 1e-3);
    }
    
    @Test
    public void seriesCummulativeGrowthRateAnnual() {
        var spySeries = spyGldSeries.get("SPY");
        var valuesPerYear = spySeries.size() / 10.0;
        
        var cagrActual = Metrics.cummulativeGrowthRate(spySeries, valuesPerYear);
        var cagrExpected = Math.pow(321.9 / 113.3, valuesPerYear / spySeries.size()) - 1;
        
        Assert.assertEquals(cagrActual, cagrExpected, 1e-4);
    }
    
    @Test
    public void seriesCummulativeGrowthRateMultiperiod() {
        var biAnnualCGRA = Metrics.cummulativeGrowthRate(quoteSeries.get("A"), 2);
        
        Assert.assertEquals(biAnnualCGRA, Math.pow(30.1 / 20.0, 2 / 3.0) - 1, 1e-6);
    }
    
    @Test
    public void seriesCummulativeGrowthRate() {
        var cgrA = Metrics.cummulativeGrowthRate(quoteSeries.get("A"));
        
        Assert.assertEquals(cgrA, Math.pow(30.1 / 20.0, 1.0 / 3.0) - 1, 1e-6);
    }
}
