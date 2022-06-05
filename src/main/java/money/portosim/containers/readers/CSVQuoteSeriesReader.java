package money.portosim.containers.readers;

import money.portosim.containers.QuoteSeries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVQuoteSeriesReader {
    BufferedReader reader;
    CSVQuoteSeriesParser priceQuoteSeriesParser;

    public CSVQuoteSeriesReader(FileReader reader) {
        this.reader = new BufferedReader(reader);
        try {
            var csKeyMapping = this.reader.readLine();
            priceQuoteSeriesParser = new CSVQuoteSeriesParser(new CSVQuoteParser(csKeyMapping, true));
        } catch (IOException e) {
        }
    }

    public QuoteSeries readPrices() {
        List<String> recordList = new ArrayList<>();
        while (true) {
            try {
                String record;
                if ((record = reader.readLine()) == null) break;
                else recordList.add(record);
            } catch (IOException e) {
            }
        }
        return priceQuoteSeriesParser.parseAll(recordList);
    }
}
