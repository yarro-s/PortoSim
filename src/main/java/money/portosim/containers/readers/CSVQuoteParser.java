package money.portosim.containers.readers;

import money.portosim.containers.Quote;

import java.util.Arrays;
import java.util.List;

public class CSVQuoteParser extends AbstractMapParser<String, String, Double> {

    private List<String> keys;

    public CSVQuoteParser(String csKeys) {
        keys = Arrays.asList(csKeys.split(","));
    }

    public CSVQuoteParser(String csKeys, boolean ignoreFirst) {
        this(csKeys);
        if (ignoreFirst) keys = keys.subList(1, keys.size());
    }

    public Quote parseAll(String csRecords) {
        final List<String> records = Arrays.asList(csRecords.split(","));
        return parseAll(records);
    }

    @Override
    public Quote parseAll(List<String> records) {
        return new Quote(super.parseAll(records));
    }

    @Override
    protected String recordToKey(int recIdx) {
        return keys.get(recIdx);
    }

    @Override
    protected Double recordToVal(String strVal) {
        return Double.parseDouble(strVal);
    }
}
