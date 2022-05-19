package money.portosim.core.containers.readers;

import money.portosim.containers.PriceMap;
import money.portosim.containers.PriceSeries;
import money.portosim.containers.readers.CSVPriceMapParser;
import money.portosim.containers.readers.CSVPriceSeriesParser;
import money.portosim.containers.readers.CSVPriceSeriesReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class CSVParsingTest {
    private final String csvDataSourcePath = "src/test/resources/simple.csv";

    @Test
    public void readCSV() throws FileNotFoundException {
        FileReader filereader = new FileReader(csvDataSourcePath);
        CSVPriceSeriesReader priceReader = new CSVPriceSeriesReader(filereader);

        var prices = priceReader.readPrices();

        Assert.assertEquals(prices.ordered().firstEntry().getKey(),
                new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime());
        Assert.assertEquals(prices.ordered().firstEntry().getValue(),
                new PriceMap(Map.of("A", 20.0,"B", 10.5)));

        Assert.assertEquals(prices.ordered().lastEntry().getKey(),
                new GregorianCalendar(2013, Calendar.JANUARY, 1).getTime());
        Assert.assertEquals(prices.ordered().lastEntry().getValue(),
                new PriceMap(Map.of("A", 30.1,"B", 12.0)));
    }

    @Test
    public void parsePriceMap() {
        var keyMapping = "A,B,C";
        var values = "1.0,2,30.5";

        var priceMapParser = new CSVPriceMapParser(keyMapping);
        var parsedPriceMap = priceMapParser.parseAll(values);

        var expPriceMap = new PriceMap(Map.of("A", 1.0, "B", 2.0, "C", 30.5));
        Assert.assertEquals(parsedPriceMap, expPriceMap);
    }

    @Test
    public void parsePriceSeries() {
        var keyMapping = "A,B,C";
        var values = "2015-01-01,15.2,120,8.5" + System.lineSeparator() + "2016-01-01,22.0,100.3,10.7"
                + System.lineSeparator() + "2017-01-01,31.5,70.1,25.0";

        var priceSeriesParser = new CSVPriceSeriesParser(new CSVPriceMapParser(keyMapping));
        var parsedPriceSeries = priceSeriesParser.parseAll(values);

        var expPriceSeries = new PriceSeries(Map.of(
                new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime(),
                new PriceMap(Map.of("A", 15.2, "B", 120.0, "C", 8.5)),
                new GregorianCalendar(2016, Calendar.JANUARY, 1).getTime(),
                new PriceMap(Map.of("A", 22.0, "B", 100.3, "C", 10.7)),
                new GregorianCalendar(2017, Calendar.JANUARY, 1).getTime(),
                new PriceMap(Map.of("A", 31.5, "B", 70.1, "C", 25.0))));

        Assert.assertEquals(parsedPriceSeries, expPriceSeries);
    }
}
