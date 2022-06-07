/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.core.containers.sources;

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.sources.QuoteSeriesSQLSource;
import money.portosim.containers.sources.SQLKeyCategoryData;
import org.testng.Assert;

/**
 *
 * @author yarro
 */
public class SQLSourceTest {
    
    final String connectionString = "jdbc:sqlite:src/test/resources/fin_db.db";

    @Test
    public void readQuoteSeriesFromSQL() throws Exception {
        
        var priceSource = new QuoteSeriesSQLSource(connectionString);
        
        var quoteSeries = new QuoteSeries(priceSource);       
        
        Assert.assertEquals(quoteSeries.size(), 30);      
        Assert.assertEquals(quoteSeries.firstKey(), QuoteSeries.isoStringToDate("2018-01-01"));
        Assert.assertEquals(quoteSeries.lastKey(), QuoteSeries.isoStringToDate("2020-06-01"));
        
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
