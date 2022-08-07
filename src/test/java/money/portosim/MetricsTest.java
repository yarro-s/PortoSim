package money.portosim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.sources.CSVPriceSource;
import money.portosim.metrics.Quant;
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
                
        var actualSharpe = Quant.of(listToMap(assetPrice))
                .setMeanRiskFreeRate(2.0 / 100).setValuesPerRefPeriod(4).full()
                .sharpeRatio();
        
        Assert.assertEquals(actualSharpe, 1.7998, 0.0001);
    }
    
    @Test
    public void sharpeRatioReal() {
        var spy10Yr = spyGldSeries.get("SPY");
        
        var meanRiskFreeRate = 16.44 / 100;
        var quant = Quant.of(listToMap(spy10Yr)).setValuesPerRefPeriod(365);
        
        var actualSharpe = quant.setMeanRiskFreeRate(meanRiskFreeRate).full()
                .sharpeRatio();
        
        Assert.assertEquals(actualSharpe, 0.0, 0.01);        
        
        meanRiskFreeRate = 0.5 / 100;
        
        actualSharpe = quant.setMeanRiskFreeRate(meanRiskFreeRate).full()
                .sharpeRatio();
        
        Assert.assertEquals(actualSharpe, 29.63, 0.01);
    }
    
    @Test
    public void noDrawdown() {
        var noDrawdown = listToMap(List.of(10.5, 17.7, 17.7, 25.2));
        
        var actualDD = Quant.of(noDrawdown).full().maxDrawdown();
        
        Assert.assertEquals(actualDD, 0.0);
    }
    
    @Test
    public void someMaxDrawdown() {
        var someDrawdownMiddle = listToMap(List.of(17.5, 20.3, 5.2, 15.7));
        var someDrawdownEdges = listToMap(List.of(20.5, 10.0, 15.1, 2.5));
        
        var actualMaxDDMiddle = Quant.of(someDrawdownMiddle).full().maxDrawdown();
        var actualMaxDDEdges = Quant.of(someDrawdownEdges).full().maxDrawdown();
        
        Assert.assertEquals(actualMaxDDMiddle, (5.2 - 20.3) / 20.3);
        Assert.assertEquals(actualMaxDDEdges, (2.5 - 20.5) / 20.5);
    }

    
    @Test
    public void stdDeviation() {
        var quant = Quant.of(listToMap(quoteSeries.get("A"))).full();
        var stdDevA = quant.stdDeviation();
        
        var expDev = Math.sqrt(quant.variance());
        
        Assert.assertEquals(stdDevA, expDev, 1e-6);
    }
    
    @Test
    public void seriesVariance() {
        var quant = Quant.of(listToMap(quoteSeries.get("A"))).full();
        var varianceA = quant.variance();
        
        var averageA = quant.average();
        var expVariance = (Math.pow(20.0 - averageA, 2.0) + Math.pow(25.5 - averageA, 2.0) + 
                Math.pow(23.0 - averageA, 2.0) + Math.pow(30.1 - averageA, 2.0)) / (4 - 1);
        
        Assert.assertEquals(varianceA, expVariance, 1e-6);
    }
    
    @Test
    public void seriesVolatility() {
        var volA = Quant.of(listToMap(quoteSeries.get("A"))).full().volatility();
        var volB = Quant.of(listToMap(quoteSeries.get("B"))).full().volatility();
        
        Assert.assertEquals(volA, 3.7003, 1e-3);
        Assert.assertEquals(volB, 0.9014, 1e-3);
    }
    
    @Test
    public void seriesAverage() {      
        var avgValA = Quant.of(listToMap(quoteSeries.get("A"))).full().average();
        var avgValB = Quant.of(listToMap(quoteSeries.get("B"))).full().average();
        
        Assert.assertEquals(avgValA, 24.65, 1e-3);
        Assert.assertEquals(avgValB, 10.75, 1e-3);
    }
    
    @Test
    public void seriesCummulativeGrowthRateAnnual() {
        var spySeries = listToMap(spyGldSeries.get("SPY"));
        var valuesPerYear = spySeries.size() / 10.0;
        
        var cagrActual = Quant.of(spySeries).setValuesPerRefPeriod((int) valuesPerYear)
                .full().cummulativeGrowthRate();
        var cagrExpected = Math.pow(321.9 / 113.3, valuesPerYear / spySeries.size()) - 1;
        
        Assert.assertEquals(cagrActual, cagrExpected, 1e-3);
    }
    
    @Test
    public void seriesCummulativeGrowthRateMultiperiod() {
        var biAnnualCGRA = Quant.of(listToMap(quoteSeries.get("A")))
                .setValuesPerRefPeriod(2).full()
                .cummulativeGrowthRate();
        
        Assert.assertEquals(biAnnualCGRA, Math.pow(30.1 / 20.0, 2 / 3.0) - 1, 1e-6);
    }
    
    @Test
    public void seriesCummulativeGrowthRate() {
        var cgrA = Quant.of(listToMap(quoteSeries.get("A")))
                .setValuesPerRefPeriod(1).full()
                .cummulativeGrowthRate();
        
        Assert.assertEquals(cgrA, Math.pow(30.1 / 20.0, 1.0 / 3.0) - 1, 1e-6);
    }
    
    private NavigableMap<Integer, Double> listToMap(List<Double> list) {
        return new TreeMap<>(IntStream.range(0, list.size()).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> list.get(i))));
    }
}
