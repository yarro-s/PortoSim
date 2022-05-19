package money.portosim.containers.readers;

import money.portosim.containers.PriceSeries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVPriceSeriesReader {
    BufferedReader reader;
    CSVPriceSeriesParser priceSeriesParser;

    public CSVPriceSeriesReader(FileReader reader) {
        this.reader = new BufferedReader(reader);
        try {
            var csKeyMapping = this.reader.readLine();
            priceSeriesParser = new CSVPriceSeriesParser(new CSVPriceMapParser(csKeyMapping, true));
        } catch (IOException e) {
        }
    }

    public PriceSeries readPrices() {
        List<String> recordList = new ArrayList<>();
        while (true) {
            try {
                String record;
                if ((record = reader.readLine()) == null) break;
                else recordList.add(record);
            } catch (IOException e) {
            }
        }
        return priceSeriesParser.parseAll(recordList);
    }
}
