/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.sources;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import money.portosim.containers.core.Series;
import money.portosim.containers.sources.CSVHeaderRowColumn;

/**
 *
 * @author yarro
 */
public class QuoteSeriesCSVSource extends CSVHeaderRowColumn<Date, Map<String, Double>> {

    public QuoteSeriesCSVSource(FileReader fileReader) throws IOException {
        super(fileReader);
    }
 
    @Override
    protected Date toKey(String rawKey) {
        return Series.isoStringToDate(rawKey);
    }

    @Override
    protected Map<String, Double> toValue(Map<String, String> mappedRecord) {
        return mappedRecord.keySet().stream().collect(Collectors
                .toMap(Function.identity(), k -> 
                        Double.parseDouble((String) mappedRecord.get(k))));
    }
}
