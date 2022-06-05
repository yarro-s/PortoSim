package money.portosim.containers.readers;

import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CSVQuoteSeriesParser extends AbstractMapParser<String, Date, Quote> {

    private List<String> rawRecords;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final CSVQuoteParser cSVQuoteParser;

    public CSVQuoteSeriesParser(CSVQuoteParser csvQuoteParser) {
        this.cSVQuoteParser = csvQuoteParser;
    }

    public QuoteSeries parseAll(String nlsRecords) {
        rawRecords = Arrays.asList(nlsRecords.split(System.lineSeparator()));
        return parseAll(rawRecords);
    }

    @Override
    public QuoteSeries parseAll(List<String> records) {
        rawRecords = records;
        return new QuoteSeries(super.parseAll(records));
    }

    @Override
    protected Date recordToKey(int recIdx) {
        var strDate = splitRawRecord(rawRecords.get(recIdx))[0];

        Date date = new Date();
        try {
            date = simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
        }

        return date;
    }

    @Override
    protected Quote recordToVal(String rawRecord) {
        var listRecord = Arrays.asList(splitRawRecord(rawRecord));
        var vals = listRecord.subList(1, listRecord.size());
        return cSVQuoteParser.parseAll(vals);
    }

    private String[] splitRawRecord(String rawRecord) {
        return rawRecord.split(",");
    }
}
