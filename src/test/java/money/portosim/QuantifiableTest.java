/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    public void rollingAverage() {      
        var rollAvgVal_A = quoteSeries.get("A").rolling(2).apply(Metrics::average);
        
        Assert.assertEquals(rollAvgVal_A, List.of((20.0 + 25.5) / 2, (25.5 + 23.0) / 2,
                (23.0 + 30.1) / 2));
    }
    
    @Test
    public void rollingCummulativeGrowthRate() {
        var rollCGR_A = quoteSeries.get("A").rolling(2).apply(Metrics::cummulativeGrowthRate);
        
        var expRollCGR_A = List.of(25.5 / 20.0 - 1, 23.0 / 25.5 - 1, 30.1 / 23.0 - 1);
        Assert.assertEquals(rollCGR_A, expRollCGR_A);
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
