/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class MetricsTest {
//    private final String csvDataSourcePath = "src/test/resources/simple.csv"; 
//    private QuoteSeries quoteSeries; 
    
    @BeforeClass
    public void setup() throws FileNotFoundException, IOException {
//        var quoteSeriesSource = new QuoteSeriesCSVSource(new FileReader(csvDataSourcePath));
//        quoteSeries = new QuoteSeries(quoteSeriesSource);
    }
    
    @Test
    public void seriesVolatility() {
           
//        var vol = quoteSeries.quant().volatility();
//        
//        Assert.assertEquals(vol.get("A"), 3.7003, 0.0001);
//        Assert.assertEquals(vol.get("B"), 0.9014, 0.0001);
    }
    
    @Test
    public void seriesAverage() {
        
//        var avgVal = quoteSeries.quant().average();
//        
//        final Quote exp = new Quote(Map.of("A", 24.65, "B", 10.75));
//        Assert.assertEquals(avgVal, exp);
    }
}
