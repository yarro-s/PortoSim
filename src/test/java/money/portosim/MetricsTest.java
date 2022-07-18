/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    public void sharpeRatio() {
        var spy10Yr = spyGldSeries.get("SPY");
        
        var cgr = Metrics.cummulativeGrowthRate(spy10Yr, 365);
        var actualSharpe = Metrics.sharpeRatio(spy10Yr, cgr);
        
//        Assert.assertEquals(actualSharpe, 0.0);
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
    public void toReturns() {
        var returnsA = quoteSeries.get("A");
        
        var expReturns = List.of(25.5 / 20.0 - 1, 23.0 / 25.5 - 1, 30.1 / 23.0 - 1);
        
        Assert.assertEquals(Metrics.toReturns(returnsA), expReturns);
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
