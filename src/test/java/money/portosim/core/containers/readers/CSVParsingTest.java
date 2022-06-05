package money.portosim.core.containers.readers;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import money.portosim.containers.readers.CSVQuoteParser;
import money.portosim.containers.readers.CSVQuoteSeriesParser;
import money.portosim.containers.readers.CSVQuoteSeriesReader;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;

public class CSVParsingTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv";

    @Test
    public void readCSV() throws FileNotFoundException {
        FileReader filereader = new FileReader(csvDataSourcePath);
        CSVQuoteSeriesReader priceReader = new CSVQuoteSeriesReader(filereader);

        var prices = priceReader.readPrices();

        Assert.assertEquals(prices.ordered().firstEntry().getKey(),
                new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime());
        Assert.assertEquals(prices.ordered().firstEntry().getValue(),
                new Quote(Map.of("A", 20.0,"B", 10.5)));

        Assert.assertEquals(prices.ordered().lastEntry().getKey(),
                new GregorianCalendar(2013, Calendar.JANUARY, 1).getTime());
        Assert.assertEquals(prices.ordered().lastEntry().getValue(),
                new Quote(Map.of("A", 30.1,"B", 12.0)));
    }

    @Test
    public void parseQuote() {
        var keyMapping = "A,B,C";
        var values = "1.0,2,30.5";

        var quoteParser = new CSVQuoteParser(keyMapping);
        var parsedQuote = quoteParser.parseAll(values);

        var expQuote = new Quote(Map.of("A", 1.0, "B", 2.0, "C", 30.5));
        Assert.assertEquals(parsedQuote, expQuote);
    }

    @Test
    public void parseQuoteSeries() {
        var keyMapping = "A,B,C";
        var values = "2015-01-01,15.2,120,8.5" + System.lineSeparator() + "2016-01-01,22.0,100.3,10.7"
                + System.lineSeparator() + "2017-01-01,31.5,70.1,25.0";

        var quoteSeriesParser = new CSVQuoteSeriesParser(new CSVQuoteParser(keyMapping));
        var parsedQuoteSeries = quoteSeriesParser.parseAll(values);

        var expQuoteSeries = new QuoteSeries(Map.of(
                new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime(),
                new Quote(Map.of("A", 15.2, "B", 120.0, "C", 8.5)),
                new GregorianCalendar(2016, Calendar.JANUARY, 1).getTime(),
                new Quote(Map.of("A", 22.0, "B", 100.3, "C", 10.7)),
                new GregorianCalendar(2017, Calendar.JANUARY, 1).getTime(),
                new Quote(Map.of("A", 31.5, "B", 70.1, "C", 25.0))));

        Assert.assertEquals(parsedQuoteSeries, expQuoteSeries);
    }
}
