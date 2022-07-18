/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class QuantifiableTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv"; 
    private Map<String, NumericSeries> quoteSeries; 
    
    @BeforeClass
    public void setup() throws FileNotFoundException, IOException {
        var quoteData = new CSVPriceSource(new FileReader(csvDataSourcePath));
        var quoteKeySet = quoteData.entrySet().iterator().next().getValue().keySet();

        quoteSeries = quoteKeySet.stream().collect(Collectors.toMap(Function.identity(),
                key -> new NumericSeries(quoteData.entrySet().stream().collect(Collectors
                        .toMap(Map.Entry::getKey, e -> e.getValue().get(key))))));
    }
    
    
    @Test
    public void rollingWindowAverage() {
        var ns = new NumericSeries();
        
        ns.put("2010-01-01", 100.0);
        ns.put("2010-02-01", 120.0);
        ns.put("2010-03-01", 90.0);
        ns.put("2010-04-01", 15.5);
        ns.put("2010-05-01", 32.0);
        ns.put("2010-06-01", 60.0);
        ns.put("2010-07-01", 75.0);

        var nsAverage = ns.<Double>rolling(3).apply(Metrics::average);

        var expAverage = new NumericSeries();
        
        expAverage.put("2010-03-01", (100.0 + 120.0 + 90.0) / 3);
        expAverage.put("2010-04-01", (120.0 + 90.0 + 15.5) / 3);
        expAverage.put("2010-05-01", (90.0 + 15.5 + 32.0) / 3);
        expAverage.put("2010-06-01", (15.5 + 32.0 + 60.0) / 3);
        expAverage.put("2010-07-01", (32.0 + 60.0 + 75.0) / 3);
        
        Assert.assertEquals(nsAverage, expAverage);
    }
    
    @Test
    public void seriesAverage() {      
        var seriesAvgVal_A = quoteSeries.get("A").apply(Metrics::average);
        
        Assert.assertEquals(seriesAvgVal_A, 24.65, 1e-3);
    }
    
    @Test
    public void seriesCummulativeGrowthRate() {
        var seriesCGR_A = quoteSeries.get("A").apply(Metrics::cummulativeGrowthRate);
        
        Assert.assertEquals(seriesCGR_A, Math.pow(30.1 / 20.0, 1.0 / 3.0) - 1, 1e-6);
    }
}
