package money.portosim.containers.readers;

import money.portosim.containers.PriceMap;

import java.util.Arrays;
import java.util.List;

public class CSVPriceMapParser extends AbstractMapParser<String, String, Double> {

    private List<String> tags;

    public CSVPriceMapParser(String csTags) {
        tags = Arrays.asList(csTags.split(","));
    }

    public CSVPriceMapParser(String csTags, boolean ignoreFirst) {
        this(csTags);
        if (ignoreFirst) tags = tags.subList(1, tags.size());
    }

    public PriceMap parseAll(String csRecords) {
        final List<String> records = Arrays.asList(csRecords.split(","));
        return parseAll(records);
    }

    @Override
    public PriceMap parseAll(List<String> records) {
        return new PriceMap(super.parseAll(records));
    }

    @Override
    protected String recordToTag(int recIdx) {
        return tags.get(recIdx);
    }

    @Override
    protected Double recordToVal(String strVal) {
        return Double.parseDouble(strVal);
    }
}
