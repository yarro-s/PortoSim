package money.portosim.containers.readers;

import money.portosim.containers.PriceMap;
import money.portosim.containers.PriceSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CSVPriceSeriesParser extends AbstractMapParser<String, Date, PriceMap> {

    private List<String> rawRecords;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private CSVPriceMapParser csvPriceMapParser;

    public CSVPriceSeriesParser(CSVPriceMapParser csvPriceMapParser) {
        this.csvPriceMapParser = csvPriceMapParser;
    }

    public PriceSeries parseAll(String nlsRecords) {
        rawRecords = Arrays.asList(nlsRecords.split(System.lineSeparator()));
        return parseAll(rawRecords);
    }

    @Override
    public PriceSeries parseAll(List<String> records) {
        rawRecords = records;
        return new PriceSeries(super.parseAll(records));
    }

    @Override
    protected Date recordToTag(int recIdx) {
        var strDate = splitRawRecord(rawRecords.get(recIdx))[0];

        Date date = new Date();
        try {
            date = simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    @Override
    protected PriceMap recordToVal(String rawRecord) {
        var listRecord = Arrays.asList(splitRawRecord(rawRecord));
        var vals = listRecord.subList(1, listRecord.size());
        return csvPriceMapParser.parseAll(vals);
    }

    private String[] splitRawRecord(String rawRecord) {
        return rawRecord.split(",");
    }
}
