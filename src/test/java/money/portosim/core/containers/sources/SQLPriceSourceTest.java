package money.portosim.core.containers.sources;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.testng.annotations.Test;

import money.portosim.containers.core.Series;
import money.portosim.containers.sources.SQLPriceSource;
import org.testng.Assert;

/**
 *
 * @author yarro
 */
public class SQLPriceSourceTest {
    
    final String connectionString = "jdbc:sqlite:src/test/resources/fin_db.db";

    @Test
    public void readQuoteSeriesFromSQL() throws Exception {
        
        var priceSource = new SQLPriceSource(connectionString);
        var quoteSeries = new TreeMap<Date, Map<String, Double>>(priceSource);       
        
        Assert.assertEquals(quoteSeries.size(), 30);      
        Assert.assertEquals(quoteSeries.firstKey(), Series.isoStringToDate("2018-01-01"));
        Assert.assertEquals(quoteSeries.lastKey(), Series.isoStringToDate("2020-06-01"));
        
        var firstEntry = quoteSeries.firstEntry().getValue();
        var lastEntry = quoteSeries.lastEntry().getValue();
        
        Assert.assertEquals(firstEntry.size(), 2);        
        Assert.assertEquals(lastEntry.size(), 2);
        
        Assert.assertEquals(firstEntry.get("SP500TR"), 5511.2100);
        Assert.assertEquals(firstEntry.get("GOLD"), 1343.35);       
        
        Assert.assertEquals(lastEntry.get("SP500TR"), 6351.6699);
        Assert.assertEquals(lastEntry.get("GOLD"), 1770.7000); 
    }

}
