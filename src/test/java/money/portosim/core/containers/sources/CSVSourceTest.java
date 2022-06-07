/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.core.containers.sources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.sources.QuoteSeriesCSVSource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class CSVSourceTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv";
    
    @Test
    public void readQuoteSeriesFromCSV() throws FileNotFoundException, IOException {
        var quoteSeriesSource = new QuoteSeriesCSVSource(new FileReader(csvDataSourcePath));
        var quoteSeries = new QuoteSeries(quoteSeriesSource);
        
        Assert.assertEquals(quoteSeries.size(), 4);      
        Assert.assertEquals(quoteSeries.firstKey(), QuoteSeries.isoStringToDate("2010-01-01"));
        Assert.assertEquals(quoteSeries.lastKey(), QuoteSeries.isoStringToDate("2013-01-01"));
        
        Assert.assertEquals(quoteSeries.firstEntry().getValue().size(), 2);        
        Assert.assertEquals(quoteSeries.lastEntry().getValue().size(), 2);
        
        Assert.assertEquals(quoteSeries.firstEntry().getValue().get("A"), 20.0);        
        Assert.assertEquals(quoteSeries.lastEntry().getValue().get("B"), 12.0);
    }
}
