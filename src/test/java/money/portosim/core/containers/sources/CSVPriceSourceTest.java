package money.portosim.core.containers.sources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import money.portosim.containers.core.Series;
import money.portosim.containers.sources.CSVPriceSource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class CSVPriceSourceTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv";
    
    @Test
    public void readQuoteSeriesFromCSV() throws FileNotFoundException, IOException {
        var quoteSeriesSource = new CSVPriceSource(new FileReader(csvDataSourcePath));
        var quoteSeries = new TreeMap<Date, Map<String, Double>>(quoteSeriesSource);
        
        Assert.assertEquals(quoteSeries.size(), 4);      
        Assert.assertEquals(quoteSeries.firstKey(), Series.isoStringToDate("2010-01-01"));
        Assert.assertEquals(quoteSeries.lastKey(), Series.isoStringToDate("2013-01-01"));
        
        Assert.assertEquals(quoteSeries.firstEntry().getValue().size(), 2);        
        Assert.assertEquals(quoteSeries.lastEntry().getValue().size(), 2);
        
        Assert.assertEquals(quoteSeries.firstEntry().getValue().get("A"), 20.0);        
        Assert.assertEquals(quoteSeries.lastEntry().getValue().get("B"), 12.0);
    }
}
