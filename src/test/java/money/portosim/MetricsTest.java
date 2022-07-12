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
    private Map<String, List<Double>> quoteSeries; 
    
    @BeforeClass
    public void setup() throws FileNotFoundException, IOException {
        var quoteData = new CSVPriceSource(new FileReader(csvDataSourcePath));
        var quoteKeySet = quoteData.entrySet().iterator().next().getValue().keySet();

        quoteSeries = quoteKeySet.stream().collect(Collectors.toMap(Function.identity(),
                key -> new ArrayList<>(new NumericSeries(quoteData.entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(key)))).values())));
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
    public void seriesCummulativeGrowthRate() {
        var cgrA = Metrics.cummulativeGrowthRate(new ArrayList<>(quoteSeries.get("A")));
        
        Assert.assertEquals(cgrA, Math.pow(30.1 / 20.0, 1.0 / 3.0) - 1, 1e-6);
    }
}
