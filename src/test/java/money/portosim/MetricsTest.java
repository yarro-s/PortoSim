/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.NumericMatrix;
import money.portosim.containers.sources.QuoteSeriesCSVSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class MetricsTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv"; 
    private QuoteSeries quoteSeries; 
    private NumericMatrix seriesQuote;
    
    @BeforeClass
    public void setup() throws FileNotFoundException, IOException {
        var quoteSeriesSource = new QuoteSeriesCSVSource(new FileReader(csvDataSourcePath));
        quoteSeries = new QuoteSeries(quoteSeriesSource);
        seriesQuote = quoteSeries.transpose();
    }
       
    @Test
    public void seriesQuoteVolatility() {
           
        var vol = seriesQuote.quant().volatility();
        
        Assert.assertEquals(vol.get("A"), 3.7003, 0.0001);
        Assert.assertEquals(vol.get("B"), 0.9014, 0.0001);
    }
    
    @Test
    public void seriesQuoteAverage() {
        
        var avgVal = seriesQuote.quant().average();
        
        final Quote exp = new Quote(Map.of("A", 24.65, "B", 10.75));
        Assert.assertEquals(avgVal, exp);
    }
    
    @Test
    public void seriesVolatility() {
           
        var vol = quoteSeries.quant().volatility();
        
        Assert.assertEquals(vol.get("A"), 3.7003, 0.0001);
        Assert.assertEquals(vol.get("B"), 0.9014, 0.0001);
    }
    
    @Test
    public void seriesAverage() {
        
        var avgVal = quoteSeries.quant().average();
        
        final Quote exp = new Quote(Map.of("A", 24.65, "B", 10.75));
        Assert.assertEquals(avgVal, exp);
    }
}
