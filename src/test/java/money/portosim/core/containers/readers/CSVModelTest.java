package money.portosim.core.containers.readers;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import money.portosim.containers.readers.CSVQuoteParser;
import money.portosim.containers.readers.CSVQuoteSeriesParser;
import money.portosim.containers.readers.CSVQuoteSeriesReader;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.readers.CSVHeaderRowColumn;

public class CSVModelTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv";

    @Test
    public void readCSV() throws Exception {
        
        CSVHeaderRowColumn<Date, Quote> priceSource = 
                new CSVHeaderRowColumn(new FileReader(csvDataSourcePath)) {
                    
            @Override
            protected Date toKey(String rawKey) {
                return QuoteSeries.isoStringToDate(rawKey);
            }

            @Override
            protected Object toValue(Map mappedRecord) {
                var v = ((Map<String, String>) mappedRecord).keySet().stream()
                        .collect(Collectors.toMap(Function.identity(), k -> 
                                Double.parseDouble((String) mappedRecord.get(k))));
                return new Quote(v);
            }            
        };
        
        var quoteSeries = new QuoteSeries(priceSource);
        
        Assert.assertEquals(quoteSeries.size(), 4);      
        Assert.assertEquals(quoteSeries.firstKey(), QuoteSeries.isoStringToDate("2010-01-01"));
        Assert.assertEquals(quoteSeries.lastKey(), QuoteSeries.isoStringToDate("2013-01-01"));
        
        Assert.assertEquals(quoteSeries.firstEntry().getValue().size(), 2);        
        Assert.assertEquals(quoteSeries.lastEntry().getValue().size(), 2);
        
        Assert.assertEquals(quoteSeries.firstEntry().getValue().get("A"), 20.0);        
        Assert.assertEquals(quoteSeries.lastEntry().getValue().get("B"), 12.0);
    }
}
